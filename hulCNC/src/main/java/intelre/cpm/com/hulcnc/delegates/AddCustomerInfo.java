package intelre.cpm.com.hulcnc.delegates;

public class AddCustomerInfo {
    private String visitDate;
    private String store_id;
    private  String store_cd;

    public String getStore_cd() {
        return store_cd;
    }

    public void setStore_cd(String store_cd) {
        this.store_cd = store_cd;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    private String customerNmae;
    private String retailerStoreNmae;
    private String cardNumber;
    private String mobileNumber;

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }


    public String getCustomerNmae() {
        return customerNmae;
    }

    public void setCustomerNmae(String customerNmae) {
        this.customerNmae = customerNmae;
    }

    public String getRetailerStoreNmae() {
        return retailerStoreNmae;
    }

    public void setRetailerStoreNmae(String retailerStoreNmae) {
        this.retailerStoreNmae = retailerStoreNmae;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
