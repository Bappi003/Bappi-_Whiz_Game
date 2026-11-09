package com.mathwhiz.service;

import com.mathwhiz.model.QuestionItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service class implementing reusable mathematical methods and game logic.
 *
 * Requirements:
 * - TASK 03: public static int isPrime(int number)
 * - TASK 04: public static int calculateMod(int a, int b)
 * - TASK 05: public static double calculateSin(double degree)
 *            public static double calculateCos(double degree)
 *            public static double calculateTan(double degree)
 * - TASK 06: Game scoring and evaluation
 */
public class MathService {

    /** Default tolerance for comparing floating-point trigonometric answers */
    public static final double TRIG_TOLERANCE = 0.02;

    private static final Random RANDOM = new Random();

    // =========================================================================
    // TASK 03: Prime Number Function
    // =========================================================================
    /**
     * Determines whether a given integer is prime.
     * Rule: Return 1 if prime; otherwise return 0. Numbers less than 2 are not prime.
     *
     * @param number the integer to check
     * @return 1 if prime, 0 otherwise
     */
    public static int isPrime(int number) {
        if (number < 2) {
            return 0;
        }
        if (number == 2) {
            return 1;
        }
        if (number % 2 == 0) {
            return 0;
        }
        int limit = (int) Math.sqrt(number);
        for (int i = 3; i <= limit; i += 2) {
            if (number % i == 0) {
                return 0;
            }
        }
        return 1;
    }

    // =========================================================================
    // TASK 04: Modulus Function
    // =========================================================================
    /**
     * Calculates the remainder of integer division using the % operator.
     * Rule: Divisor must not be zero.
     *
     * @param a dividend (integer)
     * @param b divisor (integer, must not be zero)
     * @return remainder of integer division a % b
     * @throws ArithmeticException if divisor b is zero
     */
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is undefined for modulus operation.");
        }
        return a % b;
    }

    // =========================================================================
    // TASK 05: Trigonometric Functions
    // =========================================================================
    /**
     * Calculates sine of an angle given in degrees.
     * Converts degrees to radians before calling Math.sin().
     *
     * @param degree angle in degrees
     * @return sine value
     */
    public static double calculateSin(double degree) {
        double radians = Math.toRadians(degree);
        return Math.sin(radians);
    }

    /**
     * Calculates cosine of an angle given in degrees.
     * Converts degrees to radians before calling Math.cos().
     *
     * @param degree angle in degrees
     * @return cosine value
     */
    public static double calculateCos(double degree) {
        double radians = Math.toRadians(degree);
        return Math.cos(radians);
    }

    /**
     * Calculates tangent of an angle given in degrees.
     * Converts degrees to radians before calling Math.tan().
     * Handles undefined tangent values (e.g., 90 deg, 270 deg, where cos = 0).
     *
     * @param degree angle in degrees
     * @return tangent value
     * @throws ArithmeticException if tangent is mathematically undefined
     */
    public static double calculateTan(double degree) {
        // Normalize angle to [0, 360)
        double normalized = Math.IEEEremainder(degree, 360.0);
        if (normalized < 0) {
            normalized += 360.0;
        }
        // Tangent is undefined at 90 deg and 270 deg (or whenever cos(theta) is 0)
        if (Math.abs(normalized - 90.0) < 1e-6 || Math.abs(normalized - 270.0) < 1e-6) {
            throw new ArithmeticException("Tangent is undefined for " + degree + " deg (division by zero: cos = 0).");
        }
        double radians = Math.toRadians(degree);
        return Math.tan(radians);
    }

    /**
     * Compares user answer with expected double answer using tolerance.
     */
    public static boolean isTrigAnswerClose(double userAnswer, double expectedAnswer, double tolerance) {
        return Math.abs(userAnswer - expectedAnswer) <= tolerance;
    }

    // =========================================================================
    // Question Generation
    // =========================================================================
    /**
     * Dynamically generates four mathematical challenges:
     * Q1: Prime Number (2 Marks)
     * Q2: Modulus Operation (2 Marks)
     * Q3: Trigonometry (3 Marks)
     * Q4: Trigonometry (3 Marks)
     *
     * @return List of 4 QuestionItem objects
     */
    public static List<QuestionItem> generateQuestions() {
        List<QuestionItem> list = new ArrayList<>();

        // Question 1: Prime Number (2 Marks)
        // Pool of interesting test numbers (mix of primes, composites, and edge cases like 1, 2)
        int[] primeCandidates = {1, 2, 3, 4, 7, 8, 9, 11, 13, 15, 17, 19, 21, 23, 29, 31, 37, 41, 49, 53};
        int primeNum = primeCandidates[RANDOM.nextInt(primeCandidates.length)];

        QuestionItem q1 = new QuestionItem();
        q1.setQuestionNumber(1);
        q1.setCategory("Prime Number");
        q1.setQuestionType("PRIME");
        q1.setMarks(2);
        q1.setPrimeCandidate(primeNum);
        q1.setQuestionText("Is " + primeNum + " a prime number?");
        q1.setInstructions("Enter 1 if Prime, or 0 if Not Prime (Numbers < 2 are not prime).");
        list.add(q1);

        // Question 2: Modulus Operation (2 Marks)
        // Ensure divisor != 0
        int[] divisors = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int modB = divisors[RANDOM.nextInt(divisors.length)];
        int modA = 10 + RANDOM.nextInt(85); // 10 to 94

        QuestionItem q2 = new QuestionItem();
        q2.setQuestionNumber(2);
        q2.setCategory("Modulus Operation");
        q2.setQuestionType("MOD");
        q2.setMarks(2);
        q2.setModA(modA);
        q2.setModB(modB);
        q2.setQuestionText("Calculate the remainder of integer division: " + modA + " % " + modB);
        q2.setInstructions("Enter integer remainder only. Divisor is " + modB + ".");
        list.add(q2);

        // Standard angles known in trigonometry: 0, 30, 45, 60, 90, 180, 270, 360
        double[] trigAngles = {0.0, 30.0, 45.0, 60.0, 90.0, 180.0, 360.0};
        double[] tanAngles = {0.0, 30.0, 45.0, 60.0, 180.0, 360.0}; // Avoid 90, 270 for playable tan questions

        // Question 3: Trigonometry (3 Marks)
        // Choose between Sin or Cos
        boolean isSinQ3 = RANDOM.nextBoolean();
        String func3 = isSinQ3 ? "sin" : "cos";
        double angle3 = trigAngles[RANDOM.nextInt(trigAngles.length)];

        QuestionItem q3 = new QuestionItem();
        q3.setQuestionNumber(3);
        q3.setCategory("Trigonometry");
        q3.setQuestionType(isSinQ3 ? "TRIG_SIN" : "TRIG_COS");
        q3.setMarks(3);
        q3.setTrigFunction(func3);
        q3.setTrigDegree(angle3);
        q3.setQuestionText("Calculate the value of " + func3 + "(" + (int) angle3 + "&deg;)");
        q3.setInstructions("Enter decimal answer (e.g., 0.5, 1.0, 0.707). Degrees will be converted to radians.");
        list.add(q3);

        // Question 4: Trigonometry (3 Marks)
        // Select Tan or the other trig function
        int pick = RANDOM.nextInt(3);
        String func4;
        double angle4;
        if (pick == 0) {
            func4 = "tan";
            angle4 = tanAngles[RANDOM.nextInt(tanAngles.length)];
        } else if (pick == 1) {
            func4 = "cos";
            angle4 = trigAngles[RANDOM.nextInt(trigAngles.length)];
        } else {
            func4 = "sin";
            angle4 = trigAngles[RANDOM.nextInt(trigAngles.length)];
        }

        QuestionItem q4 = new QuestionItem();
        q4.setQuestionNumber(4);
        q4.setCategory("Trigonometry");
        q4.setQuestionType(func4.equals("tan") ? "TRIG_TAN" : (func4.equals("cos") ? "TRIG_COS" : "TRIG_SIN"));
        q4.setMarks(3);
        q4.setTrigFunction(func4);
        q4.setTrigDegree(angle4);
        q4.setQuestionText("Calculate the value of " + func4 + "(" + (int) angle4 + "&deg;)");
        q4.setInstructions("Enter decimal answer (e.g., 1.0, 0.5, 0.577). Degrees will be converted to radians.");
        list.add(q4);

        return list;
    }

    // =========================================================================
    // TASK 06: Implement Game Logic & Evaluation
    // =========================================================================
    /**
     * Evaluates a single question item using the appropriate Java mathematical function.
     * Populates calculation details, expected answer, correct flag, and marks awarded.
     *
     * @param item QuestionItem containing operands and user raw answer
     */
    public static void evaluateQuestion(QuestionItem item) {
        String userAns = item.getUserAnswerRaw();
        if (userAns == null) {
            userAns = "";
        }
        userAns = userAns.trim();

        boolean correct = false;
        String expectedText = "";
        String details = "";

        try {
            switch (item.getQuestionType()) {
                case "PRIME": {
                    int expected = isPrime(item.getPrimeCandidate());
                    expectedText = String.valueOf(expected);
                    int userInt = Integer.parseInt(userAns);
                    correct = (userInt == expected);
                    details = "Evaluated via MathService.isPrime(" + item.getPrimeCandidate() + ") = " + expected +
                            (expected == 1 ? " (Prime)" : " (Not Prime)");
                    break;
                }
                case "MOD": {
                    int expected = calculateMod(item.getModA(), item.getModB());
                    expectedText = String.valueOf(expected);
                    int userInt = Integer.parseInt(userAns);
                    correct = (userInt == expected);
                    details = "Evaluated via MathService.calculateMod(" + item.getModA() + ", " + item.getModB() +
                            ") = " + item.getModA() + " % " + item.getModB() + " = " + expected;
                    break;
                }
                case "TRIG_SIN": {
                    double expected = calculateSin(item.getTrigDegree());
                    expectedText = String.format("%.3f", expected);
                    double userDouble = Double.parseDouble(userAns);
                    correct = isTrigAnswerClose(userDouble, expected, TRIG_TOLERANCE);
                    details = String.format("Evaluated via MathService.calculateSin(%.1f&deg;) -&gt; rad: %.4f -&gt; sin: %.4f (Expected ~ %s)",
                            item.getTrigDegree(), Math.toRadians(item.getTrigDegree()), expected, expectedText);
                    break;
                }
                case "TRIG_COS": {
                    double expected = calculateCos(item.getTrigDegree());
                    expectedText = String.format("%.3f", expected);
                    double userDouble = Double.parseDouble(userAns);
                    correct = isTrigAnswerClose(userDouble, expected, TRIG_TOLERANCE);
                    details = String.format("Evaluated via MathService.calculateCos(%.1f&deg;) -&gt; rad: %.4f -&gt; cos: %.4f (Expected ~ %s)",
                            item.getTrigDegree(), Math.toRadians(item.getTrigDegree()), expected, expectedText);
                    break;
                }
                case "TRIG_TAN": {
                    double expected = calculateTan(item.getTrigDegree());
                    expectedText = String.format("%.3f", expected);
                    double userDouble = Double.parseDouble(userAns);
                    correct = isTrigAnswerClose(userDouble, expected, TRIG_TOLERANCE);
                    details = String.format("Evaluated via MathService.calculateTan(%.1f&deg;) -&gt; rad: %.4f -&gt; tan: %.4f (Expected ~ %s)",
                            item.getTrigDegree(), Math.toRadians(item.getTrigDegree()), expected, expectedText);
                    break;
                }
                default:
                    details = "Unknown question type.";
            }
        } catch (NumberFormatException e) {
            correct = false;
            details = "Invalid numerical input: '" + userAns + "'";
        } catch (ArithmeticException ae) {
            correct = false;
            details = "Arithmetic error during evaluation: " + ae.getMessage();
        }

        item.setCorrect(correct);
        item.setExpectedAnswerText(expectedText);
        item.setCalculationDetails(details);
        item.setMarksAwarded(correct ? item.getMarks() : 0);
    }
}
