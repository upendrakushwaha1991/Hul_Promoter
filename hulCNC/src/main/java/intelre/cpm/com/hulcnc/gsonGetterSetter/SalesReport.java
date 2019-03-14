
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReport {

    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Sale_Type")
    @Expose
    private String saleType;
    @SerializedName("Sku_Id")
    @Expose
    private Integer skuId;
    @SerializedName("MTD")
    @Expose
    private Integer mTD;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getMTD() {
        return mTD;
    }

    public void setMTD(Integer mTD) {
        this.mTD = mTD;
    }

}
