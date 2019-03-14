
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingStock {

    @SerializedName("Region_Id")
    @Expose
    private Integer regionId;
    @SerializedName("Distributor_Id")
    @Expose
    private Integer distributorId;
    @SerializedName("Sku_Id")
    @Expose
    private Integer skuId;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

}
