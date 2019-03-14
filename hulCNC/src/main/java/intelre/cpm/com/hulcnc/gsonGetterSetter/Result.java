
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("Customer_Id")
    @Expose
    private Integer customerId;
    @SerializedName("Customer_Name")
    @Expose
    private String customerName;
    @SerializedName("Retailer_Store_Name")
    @Expose
    private String retailerStoreName;
    @SerializedName("Card_No")
    @Expose
    private String cardNo;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Last_Purchase_Date")
    @Expose
    private String lastPurchaseDate;
    @SerializedName("Last_Purchase_Detail")
    @Expose
    private String lastPurchaseDetail;
    @SerializedName("Sku_Id")
    @Expose
    private Integer skuId;
    @SerializedName("Qty")
    @Expose
    private Integer qty;
    @SerializedName("Amount")
    @Expose
    private Integer amount;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRetailerStoreName() {
        return retailerStoreName;
    }

    public void setRetailerStoreName(String retailerStoreName) {
        this.retailerStoreName = retailerStoreName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(String lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public String getLastPurchaseDetail() {
        return lastPurchaseDetail;
    }

    public void setLastPurchaseDetail(String lastPurchaseDetail) {
        this.lastPurchaseDetail = lastPurchaseDetail;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
