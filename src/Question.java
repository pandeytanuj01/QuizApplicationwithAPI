class Question {
    private String question;
    private String[] answer;
    private String correctAnswer;

    Question(String question, String[] answer, String correctAnswer) {
        this.question = question;
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }

    String getQuestion() {
        return question;
    }

    String[] getAnswers() {
        return answer;
    }

    String getCorrectAnswer() {
        return correctAnswer;
    }

}

