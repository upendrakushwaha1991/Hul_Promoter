package intelre.cpm.com.hulcnc.gettersetter;

public class QuizGetterSetter {
    private  String brand_id,brand,category,region_id,visit_date;
    private  String question,question_id;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    private String CurrectanswerCd="",Currectanswer;
    private String Answer;
    private Integer answerId;
    private Integer Right_Answer;

    public Integer getRight_Answer() {
        return Right_Answer;
    }

    public void setRight_Answer(Integer right_Answer) {
        Right_Answer = right_Answer;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public String getCurrectanswerCd() {
        return CurrectanswerCd;
    }

    public void setCurrectanswerCd(String currectanswerCd) {
        CurrectanswerCd = currectanswerCd;
    }

    public String getCurrectanswer() {
        return Currectanswer;
    }

    public void setCurrectanswer(String currectanswer) {
        Currectanswer = currectanswer;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }
}
