
package intelre.cpm.com.intelre.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RspDetailGetterSetter {

    @SerializedName("Store_Category_Master")
    @Expose
    private List<StoreCategoryMaster> storeCategoryMaster = null;

    public List<StoreCategoryMaster> getStoreCategoryMaster() {
        return storeCategoryMaster;
    }

    public void setStoreCategoryMaster(List<StoreCategoryMaster> storeCategoryMaster) {
        this.storeCategoryMaster = storeCategoryMaster;
    }

}
