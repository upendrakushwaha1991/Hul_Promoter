
package intelre.cpm.com.hulcnc.gettersetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizQuestionGetterSetter {

    @SerializedName("Quiz_Question")
    @Expose
    private List<QuizQuestion> quizQuestion = null;

    public List<QuizQuestion> getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(List<QuizQuestion> quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

}
