<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String playerName = (String) session.getAttribute("playerName");
    String difficulty = (String) session.getAttribute("difficulty");
    if (playerName == null || playerName.trim().isEmpty()) {
        playerName = "Md. Bappi";
    }
    if (difficulty == null || difficulty.trim().isEmpty()) {
        difficulty = "NORMAL";
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mathematical Challenges | BAPPI – MATH WHIZ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .grid-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        .preset-bar {
            display: flex;
            gap: 10px;
            margin-bottom: 25px;
            flex-wrap: wrap;
            justify-content: center;
        }
        .preset-btn {
            background: #ffffff;
            border: 1px solid #cbd5e1;
            padding: 8px 14px;
            border-radius: 20px;
            font-size: 0.85rem;
            cursor: pointer;
            font-weight: 600;
            color: #334155;
            transition: all 0.2s ease;
        }
        .preset-btn:hover {
            background: #e0e7ff;
            border-color: #6366f1;
            color: #3730a3;
        }
        @media (max-width: 600px) {
            .grid-2 {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="glass-card">
        <header class="app-header" style="margin-bottom: 15px;">
            <span class="app-badge">Mathematical Challenges</span>
            <h1 class="app-title">BAPPI – MATH WHIZ</h1>
            <div style="margin-top: 10px; display: flex; justify-content: center; gap: 12px; flex-wrap: wrap;">
                <span style="background: #e0e7ff; color: #3730a3; padding: 5px 14px; border-radius: 20px; font-weight: 700; font-size: 0.9rem;">
                    &#128100; Player: <%= playerName %>
                </span>
                <span style="background: #fef3c7; color: #92400e; padding: 5px 14px; border-radius: 20px; font-weight: 700; font-size: 0.9rem;">
                    &#9874; Difficulty: <%= difficulty %>
                </span>
                <span style="background: #ecfdf5; color: #065f46; padding: 5px 14px; border-radius: 20px; font-weight: 700; font-size: 0.9rem;">
                    &#128175; Total Marks: 10
                </span>
            </div>
        </header>

        <p style="color: #64748b; font-size: 0.95rem; text-align: center; margin-bottom: 15px;">
            Enter your custom numbers (operands) and your calculated answers for each challenge.
            Server-side Java methods will calculate and verify the exact results dynamically!
        </p>

        <!-- Quick Test Presets -->
        <div class="preset-bar">
            <button type="button" class="preset-btn" onclick="applyTask09TestCase()">
                &#128203; Standard Test Cases (Task 09)
            </button>
            <button type="button" class="preset-btn" onclick="applyAlternativeTest()">
                &#9889; Alt Test (Prime: 8, Tan: 45&deg;)
            </button>
            <button type="button" class="preset-btn" onclick="applyZeroDivisorTest()">
                &#9888; Test Divisor: 0 (Validation Error)
            </button>
            <button type="button" class="preset-btn" onclick="applyRandomValues()">
                &#127922; Randomize Numbers
            </button>
        </div>

        <form action="${pageContext.request.contextPath}/submit" method="POST" id="gameForm">
            <input type="hidden" name="playerName" value="<%= playerName %>">
            <input type="hidden" name="difficulty" value="<%= difficulty %>">

            <!-- Question 1: Prime Number -->
            <div class="question-card">
                <div class="question-header">
                    <span class="question-number">
                        <span>Question 1</span>
                        <span style="font-size: 0.82rem; color: #64748b; font-weight: 500;">&bull; Prime Number</span>
                    </span>
                    <span class="mark-badge">2 Marks</span>
                </div>
                <div class="question-prompt">
                    Determine whether a given integer is Prime
                </div>
                <div class="question-hint">
                    <em>Rule: Return 1 if prime; otherwise 0. Numbers less than 2 are not prime.</em>
                </div>

                <div class="grid-2">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="prime_number" class="form-label">Input Integer (n):</label>
                        <input type="number"
                               id="prime_number"
                               name="prime_number"
                               class="form-input"
                               value="7"
                               placeholder="e.g., 7, 8, 1, 29"
                               required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="prime_answer" class="form-label">Your Answer (1 = Prime, 0 = Not Prime):</label>
                        <input type="number"
                               id="prime_answer"
                               name="prime_answer"
                               class="form-input"
                               value="1"
                               min="0"
                               max="1"
                               placeholder="1 or 0"
                               required>
                    </div>
                </div>
            </div>

            <!-- Question 2: Modulus Operation -->
            <div class="question-card">
                <div class="question-header">
                    <span class="question-number">
                        <span>Question 2</span>
                        <span style="font-size: 0.82rem; color: #64748b; font-weight: 500;">&bull; Modulus Operation</span>
                    </span>
                    <span class="mark-badge">2 Marks</span>
                </div>
                <div class="question-prompt">
                    Calculate remainder of integer division: <span id="mod_preview">17 % 5</span>
                </div>
                <div class="question-hint">
                    <em>Rule: Accept integers only. The divisor must not be zero (Divisor 0 triggers validation error).</em>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="mod_a" class="form-label">Dividend (a):</label>
                        <input type="number"
                               id="mod_a"
                               name="mod_a"
                               class="form-input"
                               value="17"
                               oninput="updateModPreview()"
                               required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="mod_b" class="form-label">Divisor (b):</label>
                        <input type="number"
                               id="mod_b"
                               name="mod_b"
                               class="form-input"
                               value="5"
                               oninput="updateModPreview()"
                               required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="mod_answer" class="form-label">Your Remainder:</label>
                        <input type="number"
                               id="mod_answer"
                               name="mod_answer"
                               class="form-input"
                               value="2"
                               placeholder="e.g., 2"
                               required>
                    </div>
                </div>
            </div>

            <!-- Question 3: Trigonometry 1 -->
            <div class="question-card">
                <div class="question-header">
                    <span class="question-number">
                        <span>Question 3</span>
                        <span style="font-size: 0.82rem; color: #64748b; font-weight: 500;">&bull; Trigonometric Value</span>
                    </span>
                    <span class="mark-badge">3 Marks</span>
                </div>
                <div class="question-prompt">
                    Calculate Trigonometric Value: <span id="trig1_preview">sin(30&deg;)</span>
                </div>
                <div class="question-hint">
                    <em>Rule: Angles in degrees are converted to radians before calculation. Tolerance: &plusmn;0.02.</em>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr 1.2fr; gap: 12px;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig1_func" class="form-label">Function:</label>
                        <select id="trig1_func" name="trig1_func" class="form-select" onchange="updateTrig1Preview()">
                            <option value="sin" selected>sin(&theta;)</option>
                            <option value="cos">cos(&theta;)</option>
                            <option value="tan">tan(&theta;)</option>
                        </select>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig1_degree" class="form-label">Angle (Degrees):</label>
                        <input type="number"
                               step="any"
                               id="trig1_degree"
                               name="trig1_degree"
                               class="form-input"
                               value="30"
                               oninput="updateTrig1Preview()"
                               required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig1_answer" class="form-label">Your Answer (Decimal):</label>
                        <input type="number"
                               step="any"
                               id="trig1_answer"
                               name="trig1_answer"
                               class="form-input"
                               value="0.5"
                               placeholder="e.g., 0.5"
                               required>
                    </div>
                </div>
            </div>

            <!-- Question 4: Trigonometry 2 -->
            <div class="question-card">
                <div class="question-header">
                    <span class="question-number">
                        <span>Question 4</span>
                        <span style="font-size: 0.82rem; color: #64748b; font-weight: 500;">&bull; Trigonometric Value</span>
                    </span>
                    <span class="mark-badge">3 Marks</span>
                </div>
                <div class="question-prompt">
                    Calculate Trigonometric Value: <span id="trig2_preview">cos(60&deg;)</span>
                </div>
                <div class="question-hint">
                    <em>Rule: Angles in degrees are converted to radians. Tangent at 90&deg;/270&deg; is undefined.</em>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr 1.2fr; gap: 12px;">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig2_func" class="form-label">Function:</label>
                        <select id="trig2_func" name="trig2_func" class="form-select" onchange="updateTrig2Preview()">
                            <option value="sin">sin(&theta;)</option>
                            <option value="cos" selected>cos(&theta;)</option>
                            <option value="tan">tan(&theta;)</option>
                        </select>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig2_degree" class="form-label">Angle (Degrees):</label>
                        <input type="number"
                               step="any"
                               id="trig2_degree"
                               name="trig2_degree"
                               class="form-input"
                               value="60"
                               oninput="updateTrig2Preview()"
                               required>
                    </div>
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="trig2_answer" class="form-label">Your Answer (Decimal):</label>
                        <input type="number"
                               step="any"
                               id="trig2_answer"
                               name="trig2_answer"
                               class="form-input"
                               value="0.5"
                               placeholder="e.g., 0.5 or 1.0"
                               required>
                    </div>
                </div>
            </div>

            <!-- Buttons -->
            <div class="btn-group">
                <button type="submit" class="btn btn-primary" style="flex: 2;">
                    &#10004; Submit Answers &amp; View Score
                </button>
                <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary" style="flex: 1;">
                    &#8634; Restart
                </a>
            </div>
        </form>
    </div>

    <footer class="footer">
        <p>&copy; 2026 <strong>BAPPI – MATH WHIZ</strong> | Developed by Md. Bappi (mbstu.ac.bd) | Live Lab Test #2</p>
    </footer>
</div>

<script>
    function updateModPreview() {
        const a = document.getElementById('mod_a').value || '?';
        const b = document.getElementById('mod_b').value || '?';
        document.getElementById('mod_preview').innerText = a + " % " + b;
    }

    function updateTrig1Preview() {
        const f = document.getElementById('trig1_func').value;
        const deg = document.getElementById('trig1_degree').value || '?';
        document.getElementById('trig1_preview').innerHTML = f + "(" + deg + "&deg;)";
    }

    function updateTrig2Preview() {
        const f = document.getElementById('trig2_func').value;
        const deg = document.getElementById('trig2_degree').value || '?';
        document.getElementById('trig2_preview').innerHTML = f + "(" + deg + "&deg;)";
    }

    function applyTask09TestCase() {
        document.getElementById('prime_number').value = 7;
        document.getElementById('prime_answer').value = 1;

        document.getElementById('mod_a').value = 17;
        document.getElementById('mod_b').value = 5;
        document.getElementById('mod_answer').value = 2;

        document.getElementById('trig1_func').value = 'sin';
        document.getElementById('trig1_degree').value = 30;
        document.getElementById('trig1_answer').value = 0.5;

        document.getElementById('trig2_func').value = 'cos';
        document.getElementById('trig2_degree').value = 60;
        document.getElementById('trig2_answer').value = 0.5;

        updateModPreview();
        updateTrig1Preview();
        updateTrig2Preview();
    }

    function applyAlternativeTest() {
        document.getElementById('prime_number').value = 8;
        document.getElementById('prime_answer').value = 0;

        document.getElementById('mod_a').value = 25;
        document.getElementById('mod_b').value = 4;
        document.getElementById('mod_answer').value = 1;

        document.getElementById('trig1_func').value = 'tan';
        document.getElementById('trig1_degree').value = 45;
        document.getElementById('trig1_answer').value = 1.0;

        document.getElementById('trig2_func').value = 'sin';
        document.getElementById('trig2_degree').value = 90;
        document.getElementById('trig2_answer').value = 1.0;

        updateModPreview();
        updateTrig1Preview();
        updateTrig2Preview();
    }

    function applyZeroDivisorTest() {
        document.getElementById('prime_number').value = 13;
        document.getElementById('prime_answer').value = 1;

        document.getElementById('mod_a').value = 17;
        document.getElementById('mod_b').value = 0; // Trigger divisor 0 validation error
        document.getElementById('mod_answer').value = 0;

        document.getElementById('trig1_func').value = 'sin';
        document.getElementById('trig1_degree').value = 30;
        document.getElementById('trig1_answer').value = 0.5;

        document.getElementById('trig2_func').value = 'cos';
        document.getElementById('trig2_degree').value = 60;
        document.getElementById('trig2_answer').value = 0.5;

        updateModPreview();
        updateTrig1Preview();
        updateTrig2Preview();
    }

    function applyRandomValues() {
        const primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31];
        const composites = [4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20];
        const isP = Math.random() > 0.5;
        const pNum = isP ? primes[Math.floor(Math.random() * primes.length)] : composites[Math.floor(Math.random() * composites.length)];
        document.getElementById('prime_number').value = pNum;
        document.getElementById('prime_answer').value = isP ? 1 : 0;

        const a = Math.floor(Math.random() * 80) + 15;
        const b = Math.floor(Math.random() * 8) + 3;
        document.getElementById('mod_a').value = a;
        document.getElementById('mod_b').value = b;
        document.getElementById('mod_answer').value = (a % b);

        document.getElementById('trig1_func').value = 'sin';
        document.getElementById('trig1_degree').value = 30;
        document.getElementById('trig1_answer').value = 0.5;

        document.getElementById('trig2_func').value = 'tan';
        document.getElementById('trig2_degree').value = 45;
        document.getElementById('trig2_answer').value = 1.0;

        updateModPreview();
        updateTrig1Preview();
        updateTrig2Preview();
    }
</script>
</body>
</html>
