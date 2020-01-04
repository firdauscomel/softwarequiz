package com.daus.softwarequiz;

public class Quiz {
    private int questionId;
    private char answer;
    private int quiz_answer_a;
    private int quiz_answer_b;
    private int quiz_answer_c;

    //Copy constructor
    public Quiz(Quiz quizCopy) {
        this.questionId = quizCopy.questionId;
        this.answer = quizCopy.answer;
        this.quiz_answer_a = quizCopy.quiz_answer_a;
        this.quiz_answer_b = quizCopy.quiz_answer_b;
        this.quiz_answer_c = quizCopy.quiz_answer_c;
    }

    public Quiz(int questionId, char answer, int quiz_answer_a, int quiz_answer_b, int quiz_answer_c) {
        this.questionId = questionId;
        this.answer = answer;
        this.quiz_answer_a = quiz_answer_a;
        this.quiz_answer_b = quiz_answer_b;
        this.quiz_answer_c = quiz_answer_c;
    }

    public int getQuiz_answer_a() {
        return quiz_answer_a;
    }

    public void setQuiz_answer_a(int quiz_answer_a) {
        this.quiz_answer_a = quiz_answer_a;
    }

    public int getQuiz_answer_b() {
        return quiz_answer_b;
    }

    public void setQuiz_answer_b(int quiz_answer_b) {
        this.quiz_answer_b = quiz_answer_b;
    }

    public int getQuiz_answer_c() {
        return quiz_answer_c;
    }

    public void setQuiz_answer_c(int quiz_answer_c) {
        this.quiz_answer_c = quiz_answer_c;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public char getAnswer() {
        return answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }
}
