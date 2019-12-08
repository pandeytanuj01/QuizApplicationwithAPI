public class DisplayQuestion {
    private Question question;
    private String[] answer;

    DisplayQuestion(Question question, String[] answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion() {
        return question;
    }

    String[] getAnswers() {
        return answer;
    }
}
