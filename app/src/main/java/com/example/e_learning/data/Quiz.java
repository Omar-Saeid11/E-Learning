package com.example.e_learning.data;

public class Quiz {
    String question;
    String chooseOne;
    String chooseTwo;

    public Quiz() {
    }

    public Quiz(String question, String chooseOne, String chooseTwo, String chooseThree, String chooseFour, int rightAnswer) {
        this.question = question;
        this.chooseOne = chooseOne;
        this.chooseTwo = chooseTwo;
        this.chooseThree = chooseThree;
        this.chooseFour = chooseFour;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getChooseOne() {
        return chooseOne;
    }

    public String getChooseTwo() {
        return chooseTwo;
    }

    public String getChooseThree() {
        return chooseThree;
    }

    public String getChooseFour() {
        return chooseFour;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    String chooseThree;
    String chooseFour;
    int rightAnswer;
}
