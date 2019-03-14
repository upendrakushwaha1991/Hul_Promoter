
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainingTopicGetterSetter {

    @SerializedName("Window_Checklist")
    @Expose
    private List<WindowChecklist> windowChecklist = null;

    public List<WindowChecklist> getWindowChecklist() {
        return windowChecklist;
    }

    public void setWindowChecklist(List<WindowChecklist> windowChecklist) {
        this.windowChecklist = windowChecklist;
    }

}
