<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mathwhiz.model.GameScore" %>
<%
    @SuppressWarnings("unchecked")
    List<GameScore> topScores = (List<GameScore>) request.getAttribute("topScores");
    if (topScores == null) {
        // If accessed directly without servlet routing, redirect through servlet
        response.sendRedirect(request.getContextPath() + "/leaderboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Top 10 Leaderboard | BAPPI – MATH WHIZ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="glass-card">
        <header class="app-header">
            <span class="app-badge">&#127942; Top 10 High Scores</span>
            <h1 class="app-title">BAPPI – MATH WHIZ</h1>
            <p class="app-subtitle">Hall of Fame powered by MySQL &amp; JDBC PreparedStatement</p>
            <div style="margin-top: 8px;">
                <code style="background: #f1f5f9; padding: 4px 10px; border-radius: 6px; font-size: 0.82rem; color: #475569;">
                    SQL: ORDER BY score DESC, played_at ASC LIMIT 10
                </code>
            </div>
        </header>

        <% if (topScores.isEmpty()) { %>
            <div class="alert alert-info" style="text-align: center;">
                No game scores recorded yet. Be the first player to top the leaderboard!
            </div>
        <% } else { %>
            <div style="overflow-x: auto;">
                <table class="leaderboard-table">
                    <thead>
                        <tr>
                            <th style="width: 70px; text-align: center;">Rank</th>
                            <th>Player Name</th>
                            <th style="text-align: center;">Score</th>
                            <th>Difficulty</th>
                            <th>Date &amp; Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (int i = 0; i < topScores.size(); i++) {
                            GameScore s = topScores.get(i);
                            int rank = i + 1;
                            String rankClass = (rank == 1) ? "rank-1" : ((rank == 2) ? "rank-2" : ((rank == 3) ? "rank-3" : "rank-other"));
                            String medal = (rank == 1) ? "&#129351;" : ((rank == 2) ? "&#129352;" : ((rank == 3) ? "&#129353;" : "#" + rank));
                        %>
                        <tr>
                            <td style="text-align: center;">
                                <span class="rank-pill <%= rankClass %>"><%= medal %></span>
                            </td>
                            <td>
                                <strong><%= s.getPlayerName() %></strong>
                                <% if (rank == 1) { %>
                                    <span style="font-size: 0.75rem; background: #fef08a; color: #854d0e; padding: 2px 6px; border-radius: 4px; margin-left: 6px; font-weight: 700;">CHAMPION</span>
                                <% } %>
                            </td>
                            <td style="text-align: center;">
                                <span class="mark-badge" style="font-size: 0.95rem; font-weight: 800;">
                                    <%= s.getScore() %> / 10
                                </span>
                            </td>
                            <td>
                                <span style="font-size: 0.85rem; color: #475569; background: #f1f5f9; padding: 3px 8px; border-radius: 4px;">
                                    <%= s.getDifficulty() %>
                                </span>
                            </td>
                            <td style="color: #64748b; font-size: 0.88rem;">
                                <%= s.getFormattedDate() %>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <% } %>

        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary" style="flex: 2;">
                &#9658; Play New Game
            </a>
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary" style="flex: 1;">
                &#8962; Home
            </a>
        </div>
    </div>

    <footer class="footer">
        <p>&copy; 2026 <strong>BAPPI – MATH WHIZ</strong> | Developed by Md. Bappi (mbstu.ac.bd) | Live Lab Test #2</p>
    </footer>
</div>
</body>
</html>
