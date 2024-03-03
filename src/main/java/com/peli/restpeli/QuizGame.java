package com.peli.restpeli;

import java.util.List;

public class QuizGame {
    private List<QuizQuestion> questions;
    private int currentQuestionIndex;

    public QuizGame(List<QuizQuestion> questions) {
        this.questions = questions;
        this.currentQuestionIndex = 0;
    }

    public QuizQuestion getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        } else {
            return null;
        }
    }

    public void setCurrentQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            currentQuestionIndex = index;
        }
    }

    public boolean moveToNextQuestion() {
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }

    public void resetGame() {
        currentQuestionIndex = 0;
    }

}
