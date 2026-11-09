<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BAPPI – MATH WHIZ | Java Servlet & JDBC Game</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <div class="glass-card">
        <header class="app-header">
            <span class="app-badge">Live Lab Test #2 &bull; Java Web Application</span>
            <h1 class="app-title">BAPPI – MATH WHIZ</h1>
            <p class="app-subtitle">A Server-Side Mathematical Challenge with Java Servlets, JDBC &amp; MySQL</p>
        </header>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                &#9888; <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <section style="margin-bottom: 25px;">
            <h3 style="font-size: 1.15rem; color: #1e293b; margin-bottom: 8px;">Game Rules &amp; Mark Distribution</h3>
            <p style="font-size: 0.92rem; color: #64748b; line-height: 1.5;">
                Welcome to Math Whiz! You will be presented with four server-calculated mathematical challenges.
                All calculations are performed dynamically by Java methods rather than hardcoded answers.
            </p>

            <table class="rules-table">
                <thead>
                    <tr>
                        <th>Question</th>
                        <th>Mathematical Area</th>
                        <th>Rule / Format</th>
                        <th>Marks</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><strong>Q1</strong></td>
                        <td>Prime Number</td>
                        <td>1 = Prime, 0 = Not Prime (numbers &lt; 2 are not prime)</td>
                        <td><span class="mark-badge">2 Marks</span></td>
                    </tr>
                    <tr>
                        <td><strong>Q2</strong></td>
                        <td>Modulus Operation</td>
                        <td>Integer remainder of division (a % b, divisor &ne; 0)</td>
                        <td><span class="mark-badge">2 Marks</span></td>
                    </tr>
                    <tr>
                        <td><strong>Q3</strong></td>
                        <td>Trigonometric Value</td>
                        <td>Sin / Cos / Tan in degrees (converted to radians)</td>
                        <td><span class="mark-badge">3 Marks</span></td>
                    </tr>
                    <tr>
                        <td><strong>Q4</strong></td>
                        <td>Trigonometric Value</td>
                        <td>Sin / Cos / Tan in degrees (converted to radians)</td>
                        <td><span class="mark-badge">3 Marks</span></td>
                    </tr>
                    <tr>
                        <td colspan="3"><strong>Total Maximum Score</strong></td>
                        <td><strong style="color: #047857; font-size: 1.05rem;">10 Marks</strong></td>
                    </tr>
                </tbody>
            </table>
        </section>

        <!-- Player Entry Form -->
        <form action="${pageContext.request.contextPath}/start" method="POST">
            <div class="form-group">
                <label for="playerName" class="form-label">Player Name:</label>
                <input type="text"
                       id="playerName"
                       name="playerName"
                       class="form-input"
                       placeholder="Enter your name (e.g., Md. Bappi)"
                       required
                       autofocus>
            </div>

            <div class="form-group">
                <label for="difficulty" class="form-label">Difficulty Level:</label>
                <select id="difficulty" name="difficulty" class="form-select">
                    <option value="NORMAL" selected>NORMAL (Standard Mathematical Challenges)</option>
                </select>
                <small style="color: #64748b; font-size: 0.85rem; display: block; margin-top: 5px;">
                    * All questions currently calibrated at NORMAL difficulty; saved in database as required.
                </small>
            </div>

            <div class="btn-group">
                <button type="submit" class="btn btn-primary" style="flex: 2;">
                    &#9658; Start Game
                </button>
                <a href="${pageContext.request.contextPath}/leaderboard" class="btn btn-secondary" style="flex: 1;">
                    &#127942; Top 10 Leaderboard
                </a>
            </div>
        </form>
    </div>

    <footer class="footer">
        <p>&copy; 2026 <strong>BAPPI – MATH WHIZ</strong> | Developed by Md. Bappi (mbstu.ac.bd) | Live Lab Test #2</p>
    </footer>
</div>
</body>
</html>
