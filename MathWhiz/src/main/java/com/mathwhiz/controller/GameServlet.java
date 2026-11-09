package com.mathwhiz.controller;

import com.mathwhiz.dao.ScoreDAO;
import com.mathwhiz.model.GameScore;
import com.mathwhiz.model.QuestionItem;
import com.mathwhiz.service.MathService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller handling user navigation, game session lifecycle,
 * answer evaluation, scoring, and leaderboard routing.
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/game", "/start", "/submit", "/leaderboard"})
public class GameServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ScoreDAO scoreDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.scoreDAO = new ScoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/leaderboard".equals(path)) {
            handleLeaderboard(request, response);
        } else if ("/game".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("playerName") == null) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            request.getRequestDispatcher("/game.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/start".equals(path)) {
            handleStartGame(request, response);
        } else if ("/submit".equals(path)) {
            handleSubmitAnswers(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    /**
     * Handles starting a new game session:
     * - Validates player name
     * - Prepares initial question templates with editable inputs
     * - Saves session state and routes to game.jsp
     */
    private void handleStartGame(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String playerName = request.getParameter("playerName");
        String difficulty = request.getParameter("difficulty");

        if (playerName == null || playerName.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your name to start the game.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        playerName = playerName.trim();
        if (difficulty == null || difficulty.trim().isEmpty()) {
            difficulty = "NORMAL";
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("playerName", playerName);
        session.setAttribute("difficulty", difficulty);

        // Forward to game page
        request.getRequestDispatcher("/game.jsp").forward(request, response);
    }

    /**
     * Evaluates submitted user-provided numbers and answers.
     * Calculates answers using server-side MathService methods dynamically.
     * Computes score out of 10, saves to database, and forwards to result.jsp.
     */
    private void handleSubmitAnswers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String playerName = (session != null) ? (String) session.getAttribute("playerName") : null;
        String difficulty = (session != null) ? (String) session.getAttribute("difficulty") : "NORMAL";

        if (playerName == null) {
            playerName = request.getParameter("playerName");
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Guest Player";
            }
        }

        List<QuestionItem> evaluatedQuestions = new java.util.ArrayList<>();
        int totalScore = 0;
        boolean hasAnyValidationError = false;

        // -------------------------------------------------------------
        // Question 1: Prime Number (2 Marks)
        // -------------------------------------------------------------
        QuestionItem q1 = new QuestionItem();
        q1.setQuestionNumber(1);
        q1.setCategory("Prime Number");
        q1.setQuestionType("PRIME");
        q1.setMarks(2);
        q1.setInstructions("1 = Prime, 0 = Not Prime (numbers < 2 are not prime).");

        int primeInput = 7;
        try {
            primeInput = Integer.parseInt(request.getParameter("prime_number"));
        } catch (Exception e) {
            primeInput = 7;
        }
        q1.setPrimeCandidate(primeInput);
        q1.setQuestionText("Is " + primeInput + " a prime number?");

        String primeAnsStr = request.getParameter("prime_answer");
        q1.setUserAnswerRaw(primeAnsStr != null ? primeAnsStr.trim() : "");

        int expectedPrime = MathService.isPrime(primeInput);
        q1.setExpectedAnswerText(String.valueOf(expectedPrime));

        try {
            int userPrimeAns = Integer.parseInt(q1.getUserAnswerRaw());
            boolean isPrimeCorrect = (userPrimeAns == expectedPrime);
            q1.setCorrect(isPrimeCorrect);
            q1.setMarksAwarded(isPrimeCorrect ? 2 : 0);
            q1.setCalculationDetails("MathService.isPrime(" + primeInput + ") = " + expectedPrime +
                    (expectedPrime == 1 ? " (Prime)" : " (Not Prime)"));
            if (isPrimeCorrect) totalScore += 2;
        } catch (Exception e) {
            q1.setCorrect(false);
            q1.setMarksAwarded(0);
            q1.setCalculationDetails("Invalid integer input for answer: '" + q1.getUserAnswerRaw() + "'");
        }
        evaluatedQuestions.add(q1);

        // -------------------------------------------------------------
        // Question 2: Modulus Operation (2 Marks)
        // -------------------------------------------------------------
        QuestionItem q2 = new QuestionItem();
        q2.setQuestionNumber(2);
        q2.setCategory("Modulus Operation");
        q2.setQuestionType("MOD");
        q2.setMarks(2);
        q2.setInstructions("Integer remainder of a % b. Divisor must not be zero.");

        int modA = 17;
        int modB = 5;
        try {
            modA = Integer.parseInt(request.getParameter("mod_a"));
            modB = Integer.parseInt(request.getParameter("mod_b"));
        } catch (Exception e) {
            modA = 17;
            modB = 5;
        }
        q2.setModA(modA);
        q2.setModB(modB);
        q2.setQuestionText("Calculate the remainder of integer division: " + modA + " % " + modB);

        String modAnsStr = request.getParameter("mod_answer");
        q2.setUserAnswerRaw(modAnsStr != null ? modAnsStr.trim() : "");

        if (modB == 0) {
            // Task 09: Divisor: 0 -> Validation error
            hasAnyValidationError = true;
            q2.setHasValidationError(true);
            q2.setValidationErrorMessage("Validation error: Divisor cannot be zero (Division by zero is undefined)!");
            q2.setCorrect(false);
            q2.setMarksAwarded(0);
            q2.setExpectedAnswerText("Undefined");
            q2.setCalculationDetails("Validation error: Divisor (b) = 0 is not allowed. ArithmeticException handled.");
        } else {
            int expectedMod = MathService.calculateMod(modA, modB);
            q2.setExpectedAnswerText(String.valueOf(expectedMod));

            try {
                int userModAns = Integer.parseInt(q2.getUserAnswerRaw());
                boolean isModCorrect = (userModAns == expectedMod);
                q2.setCorrect(isModCorrect);
                q2.setMarksAwarded(isModCorrect ? 2 : 0);
                q2.setCalculationDetails("MathService.calculateMod(" + modA + ", " + modB + ") = " +
                        modA + " % " + modB + " = " + expectedMod);
                if (isModCorrect) totalScore += 2;
            } catch (Exception e) {
                q2.setCorrect(false);
                q2.setMarksAwarded(0);
                q2.setCalculationDetails("Invalid integer input for answer: '" + q2.getUserAnswerRaw() + "'");
            }
        }
        evaluatedQuestions.add(q2);

        // -------------------------------------------------------------
        // Question 3: Trigonometry 1 (3 Marks)
        // -------------------------------------------------------------
        QuestionItem q3 = new QuestionItem();
        q3.setQuestionNumber(3);
        q3.setCategory("Trigonometry");
        q3.setMarks(3);
        q3.setInstructions("Degrees converted to radians before calculation using Java Math class.");

        String trig1Func = request.getParameter("trig1_func");
        if (trig1Func == null || (!trig1Func.equals("sin") && !trig1Func.equals("cos") && !trig1Func.equals("tan"))) {
            trig1Func = "sin";
        }
        q3.setTrigFunction(trig1Func);
        q3.setQuestionType("TRIG_" + trig1Func.toUpperCase());

        double trig1Deg = 30.0;
        try {
            trig1Deg = Double.parseDouble(request.getParameter("trig1_degree"));
        } catch (Exception e) {
            trig1Deg = 30.0;
        }
        q3.setTrigDegree(trig1Deg);
        q3.setQuestionText("Calculate the value of " + trig1Func + "(" + (int) trig1Deg + "&deg;)");

        String trig1AnsStr = request.getParameter("trig1_answer");
        q3.setUserAnswerRaw(trig1AnsStr != null ? trig1AnsStr.trim() : "");

        try {
            double expectedTrig1;
            if ("cos".equals(trig1Func)) {
                expectedTrig1 = MathService.calculateCos(trig1Deg);
            } else if ("tan".equals(trig1Func)) {
                expectedTrig1 = MathService.calculateTan(trig1Deg);
            } else {
                expectedTrig1 = MathService.calculateSin(trig1Deg);
            }

            q3.setExpectedAnswerText(String.format("%.3f", expectedTrig1));
            double userTrig1Ans = Double.parseDouble(q3.getUserAnswerRaw());
            boolean isTrig1Correct = MathService.isTrigAnswerClose(userTrig1Ans, expectedTrig1, MathService.TRIG_TOLERANCE);
            q3.setCorrect(isTrig1Correct);
            q3.setMarksAwarded(isTrig1Correct ? 3 : 0);
            q3.setCalculationDetails(String.format("MathService.calculate%s(%.1f&deg;) -> rad: %.4f -> value: %.4f (Expected ~ %.3f)",
                    trig1Func.substring(0, 1).toUpperCase() + trig1Func.substring(1), trig1Deg,
                    Math.toRadians(trig1Deg), expectedTrig1, expectedTrig1));
            if (isTrig1Correct) totalScore += 3;
        } catch (ArithmeticException ae) {
            q3.setCorrect(false);
            q3.setMarksAwarded(0);
            q3.setExpectedAnswerText("Undefined");
            q3.setCalculationDetails("Arithmetic notice: " + ae.getMessage());
        } catch (Exception e) {
            q3.setCorrect(false);
            q3.setMarksAwarded(0);
            q3.setCalculationDetails("Invalid decimal input for answer: '" + q3.getUserAnswerRaw() + "'");
        }
        evaluatedQuestions.add(q3);

        // -------------------------------------------------------------
        // Question 4: Trigonometry 2 (3 Marks)
        // -------------------------------------------------------------
        QuestionItem q4 = new QuestionItem();
        q4.setQuestionNumber(4);
        q4.setCategory("Trigonometry");
        q4.setMarks(3);
        q4.setInstructions("Degrees converted to radians before calculation using Java Math class.");

        String trig2Func = request.getParameter("trig2_func");
        if (trig2Func == null || (!trig2Func.equals("sin") && !trig2Func.equals("cos") && !trig2Func.equals("tan"))) {
            trig2Func = "cos";
        }
        q4.setTrigFunction(trig2Func);
        q4.setQuestionType("TRIG_" + trig2Func.toUpperCase());

        double trig2Deg = 60.0;
        try {
            trig2Deg = Double.parseDouble(request.getParameter("trig2_degree"));
        } catch (Exception e) {
            trig2Deg = 60.0;
        }
        q4.setTrigDegree(trig2Deg);
        q4.setQuestionText("Calculate the value of " + trig2Func + "(" + (int) trig2Deg + "&deg;)");

        String trig2AnsStr = request.getParameter("trig2_answer");
        q4.setUserAnswerRaw(trig2AnsStr != null ? trig2AnsStr.trim() : "");

        try {
            double expectedTrig2;
            if ("sin".equals(trig2Func)) {
                expectedTrig2 = MathService.calculateSin(trig2Deg);
            } else if ("tan".equals(trig2Func)) {
                expectedTrig2 = MathService.calculateTan(trig2Deg);
            } else {
                expectedTrig2 = MathService.calculateCos(trig2Deg);
            }

            q4.setExpectedAnswerText(String.format("%.3f", expectedTrig2));
            double userTrig2Ans = Double.parseDouble(q4.getUserAnswerRaw());
            boolean isTrig2Correct = MathService.isTrigAnswerClose(userTrig2Ans, expectedTrig2, MathService.TRIG_TOLERANCE);
            q4.setCorrect(isTrig2Correct);
            q4.setMarksAwarded(isTrig2Correct ? 3 : 0);
            q4.setCalculationDetails(String.format("MathService.calculate%s(%.1f&deg;) -> rad: %.4f -> value: %.4f (Expected ~ %.3f)",
                    trig2Func.substring(0, 1).toUpperCase() + trig2Func.substring(1), trig2Deg,
                    Math.toRadians(trig2Deg), expectedTrig2, expectedTrig2));
            if (isTrig2Correct) totalScore += 3;
        } catch (ArithmeticException ae) {
            q4.setCorrect(false);
            q4.setMarksAwarded(0);
            q4.setExpectedAnswerText("Undefined");
            q4.setCalculationDetails("Arithmetic notice: " + ae.getMessage());
        } catch (Exception e) {
            q4.setCorrect(false);
            q4.setMarksAwarded(0);
            q4.setCalculationDetails("Invalid decimal input for answer: '" + q4.getUserAnswerRaw() + "'");
        }
        evaluatedQuestions.add(q4);

        // Persist score to MySQL database via JDBC PreparedStatement
        GameScore gameScore = new GameScore(playerName, totalScore, difficulty);
        boolean saved = scoreDAO.saveScore(gameScore);

        request.setAttribute("playerName", playerName);
        request.setAttribute("difficulty", difficulty);
        request.setAttribute("totalScore", totalScore);
        request.setAttribute("evaluatedQuestions", evaluatedQuestions);
        request.setAttribute("dbSaved", saved);
        request.setAttribute("gameScore", gameScore);
        request.setAttribute("hasValidationError", hasAnyValidationError);

        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    /**
     * Fetches top 10 scores and forwards to leaderboard.jsp.
     */
    private void handleLeaderboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<GameScore> topScores = scoreDAO.getTopScores(10);
        request.setAttribute("topScores", topScores);
        request.getRequestDispatcher("/leaderboard.jsp").forward(request, response);
    }
}
