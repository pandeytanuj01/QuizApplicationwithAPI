import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class QuestionBank {
    private String url = "https://opentdb.com/api.php?amount=25&category=18";
    private String questionJSON = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
    private JSONObject jsonObject = new JSONObject(questionJSON);
    private JSONArray questionArray = (JSONArray) jsonObject.get("results");

    QuestionBank() throws IOException {
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
