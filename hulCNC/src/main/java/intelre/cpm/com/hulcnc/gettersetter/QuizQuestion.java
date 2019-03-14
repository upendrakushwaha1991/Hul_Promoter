
package intelre.cpm.com.hulcnc.gettersetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizQuestion {

    @SerializedName("Question_Category_Id")
    @Expose
    private Integer questionCategoryId;
    @SerializedName("Question_Category")
    @Expose
    private String questionCategory;
    @SerializedName("Question_Id")
    @Expose
    private Integer questionId;
    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Brand_Id")
    @Expose
    private Integer brandId;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Answer_Id")
    @Expose
    private Integer answerId;
    @SerializedName("Answer")
    @Expose
    private String answer;
    @SerializedName("Right_Answer")
    @Expose
    private Integer rightAnswer;

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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Integer getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Integer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

}
