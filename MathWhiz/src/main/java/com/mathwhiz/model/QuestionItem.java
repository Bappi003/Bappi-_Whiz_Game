package com.mathwhiz.model;

import java.io.Serializable;

/**
 * Model representing a single question presented to the player during the game.
 */
public class QuestionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int questionNumber;     // 1, 2, 3, or 4
    private String category;         // "Prime Number", "Modulus Operation", "Trigonometry"
    private String questionType;     // "PRIME", "MOD", "TRIG_SIN", "TRIG_COS", "TRIG_TAN"
    private int marks;               // 2 for Q1 & Q2; 3 for Q3 & Q4
    private String questionText;
    private String instructions;

    // Operands
    private int primeCandidate;
    private int modA;
    private int modB;
    private String trigFunction;     // "sin", "cos", "tan"
    private double trigDegree;

    // Result evaluation
    private String userAnswerRaw;
    private boolean correct;
    private int marksAwarded;
    private String expectedAnswerText;
    private String calculationDetails;
    private boolean hasValidationError;
    private String validationErrorMessage;

    public QuestionItem() {
    }

    public boolean isHasValidationError() {
        return hasValidationError;
    }

    public void setHasValidationError(boolean hasValidationError) {
        this.hasValidationError = hasValidationError;
    }

    public String getValidationErrorMessage() {
        return validationErrorMessage;
    }

    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public int getPrimeCandidate() {
        return primeCandidate;
    }

    public void setPrimeCandidate(int primeCandidate) {
        this.primeCandidate = primeCandidate;
    }

    public int getModA() {
        return modA;
    }

    public void setModA(int modA) {
        this.modA = modA;
    }

    public int getModB() {
        return modB;
    }

    public void setModB(int modB) {
        this.modB = modB;
    }

    public String getTrigFunction() {
        return trigFunction;
    }

    public void setTrigFunction(String trigFunction) {
        this.trigFunction = trigFunction;
    }

    public double getTrigDegree() {
        return trigDegree;
    }

    public void setTrigDegree(double trigDegree) {
        this.trigDegree = trigDegree;
    }

    public String getUserAnswerRaw() {
        return userAnswerRaw;
    }

    public void setUserAnswerRaw(String userAnswerRaw) {
        this.userAnswerRaw = userAnswerRaw;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getMarksAwarded() {
        return marksAwarded;
    }

    public void setMarksAwarded(int marksAwarded) {
        this.marksAwarded = marksAwarded;
    }

    public String getExpectedAnswerText() {
        return expectedAnswerText;
    }

    public void setExpectedAnswerText(String expectedAnswerText) {
        this.expectedAnswerText = expectedAnswerText;
    }

    public String getCalculationDetails() {
        return calculationDetails;
    }

    public void setCalculationDetails(String calculationDetails) {
        this.calculationDetails = calculationDetails;
    }
}
