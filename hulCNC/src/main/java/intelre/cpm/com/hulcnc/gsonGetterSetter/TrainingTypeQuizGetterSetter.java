
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainingTypeQuizGetterSetter {

    @SerializedName("Training_Type")
    @Expose
    private List<TrainingType> trainingType = null;

    public List<TrainingType> getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(List<TrainingType> trainingType) {
        this.trainingType = trainingType;
    }

}
