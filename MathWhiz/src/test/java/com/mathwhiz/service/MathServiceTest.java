package com.mathwhiz.service;

import com.mathwhiz.model.QuestionItem;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive JUnit Test Suite verifying all mathematical functions
 * and game scoring logic specified in the Lab Practice rubric (TASK 03, 04, 05, 06, 09).
 */
public class MathServiceTest {

    private static final double DELTA = 1e-4;

    // =========================================================================
    // TASK 03 & 09: Prime Number Verification
    // =========================================================================
    @Test
    public void testIsPrime_Seven() {
        assertEquals("7 is prime and must return 1", 1, MathService.isPrime(7));
    }

    @Test
    public void testIsPrime_Eight() {
        assertEquals("8 is composite and must return 0", 0, MathService.isPrime(8));
    }

    @Test
    public void testIsPrime_One() {
        assertEquals("1 is not prime (< 2 rule) and must return 0", 0, MathService.isPrime(1));
    }

    @Test
    public void testIsPrime_NegativeAndZero() {
        assertEquals("0 is not prime and must return 0", 0, MathService.isPrime(0));
        assertEquals("-5 is not prime and must return 0", 0, MathService.isPrime(-5));
        assertEquals("-1 is not prime and must return 0", 0, MathService.isPrime(-1));
    }

    @Test
    public void testIsPrime_TwoAndThree() {
        assertEquals("2 is prime and must return 1", 1, MathService.isPrime(2));
        assertEquals("3 is prime and must return 1", 1, MathService.isPrime(3));
    }

    @Test
    public void testIsPrime_LargerNumbers() {
        assertEquals("29 is prime", 1, MathService.isPrime(29));
        assertEquals("31 is prime", 1, MathService.isPrime(31));
        assertEquals("49 is composite (7*7)", 0, MathService.isPrime(49));
    }

    // =========================================================================
    // TASK 04 & 09: Modulus Operation Verification
    // =========================================================================
    @Test
    public void testCalculateMod_SeventeenModFive() {
        assertEquals("17 % 5 should equal 2", 2, MathService.calculateMod(17, 5));
    }

    @Test
    public void testCalculateMod_ExactDivision() {
        assertEquals("20 % 5 should equal 0", 0, MathService.calculateMod(20, 5));
    }

    @Test
    public void testCalculateMod_SmallerDividend() {
        assertEquals("3 % 7 should equal 3", 3, MathService.calculateMod(3, 7));
    }

    @Test(expected = ArithmeticException.class)
    public void testCalculateMod_DivisorZeroThrowsException() {
        MathService.calculateMod(10, 0);
    }

    // =========================================================================
    // TASK 05 & 09: Trigonometric Functions Verification
    // =========================================================================
    @Test
    public void testCalculateSin_ThirtyDegrees() {
        double result = MathService.calculateSin(30.0);
        assertEquals("sin(30 deg) should be 0.5", 0.5, result, DELTA);
    }

    @Test
    public void testCalculateSin_ZeroAndNinetyDegrees() {
        assertEquals("sin(0 deg) should be 0.0", 0.0, MathService.calculateSin(0.0), DELTA);
        assertEquals("sin(90 deg) should be 1.0", 1.0, MathService.calculateSin(90.0), DELTA);
    }

    @Test
    public void testCalculateCos_SixtyDegrees() {
        double result = MathService.calculateCos(60.0);
        assertEquals("cos(60 deg) should be 0.5", 0.5, result, DELTA);
    }

    @Test
    public void testCalculateCos_ZeroAndNinetyDegrees() {
        assertEquals("cos(0 deg) should be 1.0", 1.0, MathService.calculateCos(0.0), DELTA);
        assertEquals("cos(90 deg) should be 0.0", 0.0, MathService.calculateCos(90.0), DELTA);
    }

    @Test
    public void testCalculateTan_FortyFiveDegrees() {
        double result = MathService.calculateTan(45.0);
        assertEquals("tan(45 deg) should be 1.0", 1.0, result, DELTA);
    }

    @Test
    public void testCalculateTan_ZeroDegrees() {
        assertEquals("tan(0 deg) should be 0.0", 0.0, MathService.calculateTan(0.0), DELTA);
    }

    @Test(expected = ArithmeticException.class)
    public void testCalculateTan_NinetyDegreesThrowsException() {
        MathService.calculateTan(90.0);
    }

    @Test(expected = ArithmeticException.class)
    public void testCalculateTan_TwoHundredSeventyDegreesThrowsException() {
        MathService.calculateTan(270.0);
    }

    @Test
    public void testPythagoreanIdentity() {
        // Reference: x^2 + y^2 = 1 (cos^2 + sin^2 = 1)
        double[] testAngles = {15.0, 30.0, 45.0, 60.0, 75.0, 120.0};
        for (double angle : testAngles) {
            double sinVal = MathService.calculateSin(angle);
            double cosVal = MathService.calculateCos(angle);
            double identity = (sinVal * sinVal) + (cosVal * cosVal);
            assertEquals("sin^2 + cos^2 must equal 1 for " + angle + " deg", 1.0, identity, DELTA);
        }
    }

    // =========================================================================
    // TASK 06: Scoring Logic Integration Verification
    // =========================================================================
    @Test
    public void testScoring_AllCorrectYieldsTenMarks() {
        QuestionItem q1 = new QuestionItem();
        q1.setQuestionNumber(1);
        q1.setQuestionType("PRIME");
        q1.setMarks(2);
        q1.setPrimeCandidate(7);
        q1.setUserAnswerRaw("1");
        MathService.evaluateQuestion(q1);

        QuestionItem q2 = new QuestionItem();
        q2.setQuestionNumber(2);
        q2.setQuestionType("MOD");
        q2.setMarks(2);
        q2.setModA(17);
        q2.setModB(5);
        q2.setUserAnswerRaw("2");
        MathService.evaluateQuestion(q2);

        QuestionItem q3 = new QuestionItem();
        q3.setQuestionNumber(3);
        q3.setQuestionType("TRIG_SIN");
        q3.setMarks(3);
        q3.setTrigDegree(30.0);
        q3.setUserAnswerRaw("0.5");
        MathService.evaluateQuestion(q3);

        QuestionItem q4 = new QuestionItem();
        q4.setQuestionNumber(4);
        q4.setQuestionType("TRIG_COS");
        q4.setMarks(3);
        q4.setTrigDegree(60.0);
        q4.setUserAnswerRaw("0.50");
        MathService.evaluateQuestion(q4);

        int totalScore = q1.getMarksAwarded() + q2.getMarksAwarded() + q3.getMarksAwarded() + q4.getMarksAwarded();
        assertEquals("Perfect answers must award full 10 marks", 10, totalScore);
    }

    @Test
    public void testScoring_IncorrectAnswersZeroMarks() {
        QuestionItem q1 = new QuestionItem();
        q1.setQuestionNumber(1);
        q1.setQuestionType("PRIME");
        q1.setMarks(2);
        q1.setPrimeCandidate(7);
        q1.setUserAnswerRaw("0"); // Wrong (7 is prime)
        MathService.evaluateQuestion(q1);

        assertEquals("Incorrect answer must yield 0 marks", 0, q1.getMarksAwarded());
        assertFalse(q1.isCorrect());
    }

    @Test
    public void testGenerateQuestions_ProducesFourItems() {
        List<QuestionItem> questions = MathService.generateQuestions();
        assertNotNull("Generated questions list must not be null", questions);
        assertEquals("Must generate exactly 4 questions", 4, questions.size());
        assertEquals("Q1 must be Prime (2 marks)", 2, questions.get(0).getMarks());
        assertEquals("Q2 must be Modulus (2 marks)", 2, questions.get(1).getMarks());
        assertEquals("Q3 must be Trig (3 marks)", 3, questions.get(2).getMarks());
        assertEquals("Q4 must be Trig (3 marks)", 3, questions.get(3).getMarks());
    }
}
