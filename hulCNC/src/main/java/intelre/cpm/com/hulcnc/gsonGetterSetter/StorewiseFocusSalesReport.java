
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorewiseFocusSalesReport {

    @SerializedName("Srno")
    @Expose
    private Integer srno;
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Time_Period")
    @Expose
    private String timePeriod;
    @SerializedName("Target")
    @Expose
    private Double target;
    @SerializedName("Achivement")
    @Expose
    private Integer achivement;
    @SerializedName("Ach_Per")
    @Expose
    private Double achPer;

    public Integer getSrno() {
        return srno;
    }

    public void setSrno(Integer srno) {
        this.srno = srno;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Integer getAchivement() {
        return achivement;
    }

    public void setAchivement(Integer achivement) {
        this.achivement = achivement;
    }

    public Double getAchPer() {
        return achPer;
    }

    public void setAchPer(Double achPer) {
        this.achPer = achPer;
    }

}
