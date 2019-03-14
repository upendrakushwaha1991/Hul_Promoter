
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingStockSetterGetter {

    @SerializedName("Mapping_Stock")
    @Expose
    private List<MappingStock> mappingStock = null;

    public List<MappingStock> getMappingStock() {
        return mappingStock;
    }

    public void setMappingStock(List<MappingStock> mappingStock) {
        this.mappingStock = mappingStock;
    }

}
