package com.mathwhiz.dao;

import com.mathwhiz.model.GameScore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Data Access Object (DAO) for game score persistence and leaderboard retrieval.
 * Uses JDBC and PreparedStatement for all operations.
 */
public class ScoreDAO {

    // Thread-safe in-memory cache / fallback if database server is temporarily offline
    private static final List<GameScore> MEMORY_BACKUP = Collections.synchronizedList(new ArrayList<>());
    private static boolean tableVerified = false;

    public ScoreDAO() {
        ensureTableExists();
    }

    /**
     * Automatically ensures the game_scores table exists in math_whiz_db.
     */
    public synchronized void ensureTableExists() {
        if (tableVerified) {
            return;
        }
        String sql = "CREATE TABLE IF NOT EXISTS game_scores (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player_name VARCHAR(100) NOT NULL, " +
                "score INT NOT NULL, " +
                "difficulty VARCHAR(20) NOT NULL DEFAULT 'NORMAL', " +
                "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            tableVerified = true;
        } catch (Exception e) {
            System.err.println("[ScoreDAO] Database table verification notice: " + e.getMessage());
        }
    }

    /**
     * Saves a player's score to the database using PreparedStatement.
     *
     * @param gameScore the score record to persist
     * @return true if successfully saved to database, false if saved to memory fallback
     */
    public boolean saveScore(GameScore gameScore) {
        if (gameScore == null) {
            return false;
        }

        Timestamp currentTime = gameScore.getPlayedAt() != null
                ? gameScore.getPlayedAt()
                : new Timestamp(System.currentTimeMillis());
        gameScore.setPlayedAt(currentTime);

        String sql = "INSERT INTO game_scores (player_name, score, difficulty, played_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, gameScore.getPlayerName());
            ps.setInt(2, gameScore.getScore());
            ps.setString(3, gameScore.getDifficulty() != null ? gameScore.getDifficulty() : "NORMAL");
            ps.setTimestamp(4, currentTime);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        gameScore.setId(rs.getInt(1));
                    }
                }
                // Also mirror to memory
                MEMORY_BACKUP.add(gameScore);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[ScoreDAO] SQL Exception in saveScore: " + e.getMessage());
        }

        // Memory fallback ensures user experience is never broken
        gameScore.setId(MEMORY_BACKUP.size() + 1);
        MEMORY_BACKUP.add(gameScore);
        return false;
    }

    /**
     * Retrieves the top scores sorted by score descending, then played_at ascending.
     * Suggested SQL:
     * SELECT * FROM game_scores ORDER BY score DESC, played_at ASC LIMIT ?;
     *
     * @param limit number of top records to return (e.g. 10)
     * @return list of top GameScore objects
     */
    public List<GameScore> getTopScores(int limit) {
        List<GameScore> list = new ArrayList<>();
        String sql = "SELECT id, player_name, score, difficulty, played_at " +
                "FROM game_scores " +
                "ORDER BY score DESC, played_at ASC " +
                "LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GameScore s = new GameScore();
                    s.setId(rs.getInt("id"));
                    s.setPlayerName(rs.getString("player_name"));
                    s.setScore(rs.getInt("score"));
                    s.setDifficulty(rs.getString("difficulty"));
                    s.setPlayedAt(rs.getTimestamp("played_at"));
                    list.add(s);
                }
            }

            if (!list.isEmpty()) {
                return list;
            }
        } catch (SQLException e) {
            System.err.println("[ScoreDAO] SQL Exception in getTopScores: " + e.getMessage());
        }

        // Fallback to in-memory scores if database is unreachable or empty
        List<GameScore> sortedBackup = new ArrayList<>(MEMORY_BACKUP);
        sortedBackup.sort(Comparator.comparingInt(GameScore::getScore).reversed()
                .thenComparing(GameScore::getPlayedAt));

        if (sortedBackup.size() > limit) {
            return new ArrayList<>(sortedBackup.subList(0, limit));
        }
        return sortedBackup;
    }
}
