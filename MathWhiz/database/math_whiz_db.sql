-- =====================================================================
-- Database Script: math_whiz_db.sql
-- Application: BAPPI – MATH WHIZ (Java Servlet & JDBC Mathematical Game)
-- Practical Lab Assignment: Live Lab Test #2
-- Author: Md. Bappi (mbstu.ac.bd)
-- =====================================================================

-- Step 1: Create Database
CREATE DATABASE IF NOT EXISTS math_whiz_db;
USE math_whiz_db;

-- Step 2: Create game_scores Table
DROP TABLE IF EXISTS game_scores;
CREATE TABLE game_scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100) NOT NULL,
    score INT NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Step 3: Insert Initial Demonstration Data for Leaderboard
INSERT INTO game_scores (player_name, score, difficulty, played_at) VALUES
('Md. Bappi', 10, 'NORMAL', '2026-11-09 10:15:00'),
('Sarah Rahman', 10, 'NORMAL', '2026-11-09 11:30:20'),
('Tanvir Ahmed', 9, 'NORMAL', '2026-11-09 12:45:10'),
('Ayesha Siddiqa', 8, 'NORMAL', '2026-11-09 13:10:05'),
('Zubair Hossain', 8, 'NORMAL', '2026-11-09 14:02:45'),
('Nusrat Jahan', 7, 'NORMAL', '2026-11-09 15:20:30'),
('Fahim Chowdhury', 7, 'NORMAL', '2026-11-09 16:05:15'),
('Rifat Hasan', 5, 'NORMAL', '2026-11-09 17:40:00'),
('Mehedi Hasan', 5, 'NORMAL', '2026-11-09 18:15:22'),
('Anika Tabassum', 4, 'NORMAL', '2026-11-09 19:00:10');

-- Step 4: Verify Leaderboard Query (Top 10 sorted by score DESC, played_at ASC)
SELECT id, player_name, score, difficulty, played_at
FROM game_scores
ORDER BY score DESC, played_at ASC
LIMIT 10;
