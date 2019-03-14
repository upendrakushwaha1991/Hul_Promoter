
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JCPGetterSetter {

    @SerializedName("Journey_Plan")
    @Expose
    private List<JourneyPlan> journeyPlan = null;

    public List<JourneyPlan> getJourneyPlan() {
        return journeyPlan;
    }

    public void setJourneyPlan(List<JourneyPlan> journeyPlan) {
        this.journeyPlan = journeyPlan;
    }

}
