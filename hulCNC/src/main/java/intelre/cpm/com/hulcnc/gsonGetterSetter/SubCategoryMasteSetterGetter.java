
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryMasteSetterGetter {

    @SerializedName("Sub_Category_Master")
    @Expose
    private List<SubCategoryMaster> subCategoryMaster = null;

    public List<SubCategoryMaster> getSubCategoryMaster() {
        return subCategoryMaster;
    }

    public void setSubCategoryMaster(List<SubCategoryMaster> subCategoryMaster) {
        this.subCategoryMaster = subCategoryMaster;
    }

}
