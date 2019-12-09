import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class QuestionBank {
    public String url ;
    private String questionJSON ;
    private JSONObject jsonObject ;
    private JSONArray questionArray;

    QuestionBank(String url) throws IOException {
        questionJSON = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        jsonObject = new JSONObject(questionJSON);
        questionArray = (JSONArray) jsonObject.get("results");
    }

    Question[] getData() {
        Question[] questions = new Question[questionArray.length()];
        for (int i = 0; i < questionArray.length(); i++) {
            String[] incorrectAnswers = new String[questionArray.getJSONObject(i).getJSONArray("incorrect_answers").length()];
            for (int j = 0; j < questionArray.getJSONObject(i).getJSONArray("incorrect_answers").length(); j++)
                incorrectAnswers[j] = questionArray.getJSONObject(i).getJSONArray("incorrect_answers").getString(j);
            questions[i] = new Question(questionArray.getJSONObject(i).getString("question"),
                    incorrectAnswers,
                    questionArray.getJSONObject(i).getString("correct_answer"));
        }
        System.out.println(StringEscapeUtils.unescapeHtml4(questions[0].getQuestion() + " " + questions[0].getCorrectAnswer() + " " + Arrays.toString(questions[0].getAnswers())));
        return questions;
    }

}
