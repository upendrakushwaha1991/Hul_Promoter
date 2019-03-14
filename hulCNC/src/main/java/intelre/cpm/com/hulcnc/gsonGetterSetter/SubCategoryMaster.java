
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryMaster {

    @SerializedName("Sub_Category_Id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("Sub_Category")
    @Expose
    private String subCategory;
    @SerializedName("Category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("Sub_Category_Sequence")
    @Expose
    private Integer subCategorySequence;

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategorySequence() {
        return subCategorySequence;
    }

    public void setSubCategorySequence(Integer subCategorySequence) {
        this.subCategorySequence = subCategorySequence;
    }

}
