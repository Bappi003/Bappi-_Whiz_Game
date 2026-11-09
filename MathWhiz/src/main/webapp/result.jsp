<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mathwhiz.model.QuestionItem" %>
<%@ page import="com.mathwhiz.model.GameScore" %>
<%
    String playerName = (String) request.getAttribute("playerName");
    Integer totalScoreObj = (Integer) request.getAttribute("totalScore");
    int totalScore = (totalScoreObj != null) ? totalScoreObj : 0;
    String difficulty = (String) request.getAttribute("difficulty");
    Boolean dbSaved = (Boolean) request.getAttribute("dbSaved");
    Boolean hasValidationError = (Boolean) request.getAttribute("hasValidationError");
    @SuppressWarnings("unchecked")
    List<QuestionItem> evaluated = (List<QuestionItem>) request.getAttribute("evaluatedQuestions");

    if (playerName == null || evaluated == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Game Result | BAPPI – MATH WHIZ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="glass-card">
        <header class="app-header" style="margin-bottom: 20px;">
            <span class="app-badge">Official Results</span>
            <h1 class="app-title">BAPPI – MATH WHIZ</h1>
            <p class="app-subtitle">Performance Evaluation for <strong><%= playerName %></strong></p>
        </header>

        <!-- Score Hero -->
        <div class="score-hero">
            <div style="font-size: 1.05rem; font-weight: 700; color: #475569; text-transform: uppercase; letter-spacing: 1px;">
                Final Score
            </div>
            <div class="score-number">
                <%= totalScore %> <span class="score-out-of">/ 10</span>
            </div>
            <div>
                <% if (totalScore == 10) { %>
                    <span class="score-badge" style="background: #dcfce7; color: #166534;">
                        &#127881; Outstanding! Perfect Score (100%)
                    </span>
                <% } else if (totalScore >= 7) { %>
                    <span class="score-badge" style="background: #e0e7ff; color: #3730a3;">
                        &#128079; Great Job! Passed with High Distinction
                    </span>
                <% } else if (totalScore >= 5) { %>
                    <span class="score-badge" style="background: #fef3c7; color: #92400e;">
                        &#9889; Good Effort! Keep practicing to reach 10/10
                    </span>
                <% } else { %>
                    <span class="score-badge" style="background: #fee2e2; color: #991b1b;">
                        &#9888; Review Mathematical Concepts and Try Again!
                    </span>
                <% } %>
            </div>
        </div>

        <% if (Boolean.TRUE.equals(hasValidationError)) { %>
            <div class="alert alert-danger">
                &#9888; <strong>Validation Notice (Task 09):</strong> Divisor was 0 (Division by zero is undefined). Validation error handled successfully. Corresponding marks not awarded.
            </div>
        <% } %>

        <!-- Database Save Notification -->
        <% if (Boolean.TRUE.equals(dbSaved)) { %>
            <div class="alert alert-success">
                &#10004; <strong>Database Status:</strong> Score successfully recorded into MySQL table <code>game_scores</code> (Difficulty: <%= difficulty %>).
            </div>
        <% } else { %>
            <div class="alert alert-info">
                &#8505; <strong>Session Status:</strong> Score recorded into system session &amp; in-memory leaderboard (Difficulty: <%= difficulty %>).
            </div>
        <% } %>

        <!-- Question Breakdown Section -->
        <section style="margin-top: 25px;">
            <h3 style="font-size: 1.15rem; color: #1e293b; margin-bottom: 15px;">Detailed Question Breakdown</h3>

            <div style="overflow-x: auto;">
                <table class="eval-table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Category</th>
                            <th>Question</th>
                            <th>Your Answer</th>
                            <th>Expected</th>
                            <th>Status</th>
                            <th>Marks</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (int i = 0; i < evaluated.size(); i++) {
                            QuestionItem q = evaluated.get(i);
                        %>
                        <tr>
                            <td><strong>Q<%= q.getQuestionNumber() %></strong></td>
                            <td><span style="font-size: 0.85rem; color: #475569;"><%= q.getCategory() %></span></td>
                            <td>
                                <div><strong><%= q.getQuestionText() %></strong></div>
                                <div style="font-size: 0.8rem; color: #64748b; margin-top: 3px;">
                                    <em><%= q.getCalculationDetails() %></em>
                                </div>
                            </td>
                            <td>
                                <code><%= (q.getUserAnswerRaw() != null && !q.getUserAnswerRaw().isEmpty()) ? q.getUserAnswerRaw() : "(Blank)" %></code>
                            </td>
                            <td>
                                <strong style="color: #0f172a;"><%= q.getExpectedAnswerText() %></strong>
                            </td>
                            <td>
                                <% if (q.isHasValidationError()) { %>
                                    <span class="status-badge status-incorrect" style="background: #fef3c7; color: #92400e;">
                                        &#9888; Validation Error
                                    </span>
                                <% } else if (q.isCorrect()) { %>
                                    <span class="status-badge status-correct">&#10004; Correct</span>
                                <% } else { %>
                                    <span class="status-badge status-incorrect">&#10008; Incorrect</span>
                                <% } %>
                            </td>
                            <td>
                                <strong><%= q.getMarksAwarded() %> / <%= q.getMarks() %></strong>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Navigation Buttons -->
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary" style="flex: 1;">
                &#9658; Play Again
            </a>
            <a href="${pageContext.request.contextPath}/leaderboard" class="btn btn-secondary" style="flex: 1;">
                &#127942; View Top 10 Leaderboard
            </a>
        </div>
    </div>

    <footer class="footer">
        <p>&copy; 2026 <strong>BAPPI – MATH WHIZ</strong> | Developed by Md. Bappi (mbstu.ac.bd) | Live Lab Test #2</p>
    </footer>
</div>
</body>
</html>
