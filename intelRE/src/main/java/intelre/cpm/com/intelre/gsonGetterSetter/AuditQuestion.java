
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditQuestion {

    @SerializedName("Question_Id")
    @Expose
    private Integer questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Answer_Id")
    @Expose
    private Integer answerId;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("Question_Category_Id")
    @Expose
    private Integer questionCategoryId;
    @SerializedName("Question_Category")
    @Expose
    private String questionCategory;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(Integer questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

}
