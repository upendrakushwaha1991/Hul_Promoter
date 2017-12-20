
package intelre.cpm.com.intelre.gsonGetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreCategoryMaster implements Serializable {

    @SerializedName("Rsp_Id")
    @Expose
    private Integer rspId;
    @SerializedName("Rsp_Name")
    @Expose
    private String rspName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("Store_Id")
    @Expose
    private Integer storeId;
    @SerializedName("Brand_Id")
    @Expose
    private Integer brandId;
    @SerializedName("IREP_Status")
    @Expose
    private Boolean iREPStatus;

    public Integer getRspId() {
        return rspId;
    }



    public void setRspId(Integer rspId) {
        this.rspId = rspId;
    }

    public String getRspName() {
        return rspName;
    }

    public void setRspName(String rspName) {
        this.rspName = rspName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Boolean getIREPStatus() {
        return iREPStatus;
    }

    public void setIREPStatus(Boolean iREPStatus) {
        this.iREPStatus = iREPStatus;
    }

    private String key_id="0";

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    private String flag="";

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
/* private String rspname;
    private String emailid;
    private String phoneno;
    private String brand;
    private String irepregistered;
    private String rsp_id;

    public String getRsp_id() {
        return rsp_id;
    }

    public void setRsp_id(String rsp_id) {
        this.rsp_id = rsp_id;
    }

    public String getRspname() {
        return rspname;
    }

    public void setRspname(String rspname) {
        this.rspname = rspname;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getIrepregistered() {
        return irepregistered;
    }

    public void setIrepregistered(String irepregistered) {
        this.irepregistered = irepregistered;
    }*/
}
