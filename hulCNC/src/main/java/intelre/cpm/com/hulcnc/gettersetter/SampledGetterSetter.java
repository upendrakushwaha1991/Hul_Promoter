package intelre.cpm.com.hulcnc.gettersetter;

import java.util.ArrayList;

import intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMaster;

/**
 * Created by upendra on 20-02-2019.
 */

public class SampledGetterSetter {
    String customerData="";
    String customerData_cd="";
    String mobile="";
    String name="";
    String customerSales="";
    String customerSales_cd="";
    String key_id;
    boolean isExists;

    public boolean isExists() {
        return isExists;
    }

    public void setExists(boolean exists) {
        isExists = exists;
    }

    public String getCustomerData() {
        return customerData;
    }

    public void setCustomerData(String customerData) {
        this.customerData = customerData;
    }

    public String getCustomerData_cd() {
        return customerData_cd;
    }

    public void setCustomerData_cd(String customerData_cd) {
        this.customerData_cd = customerData_cd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerSales() {
        return customerSales;
    }

    public void setCustomerSales(String customerSales) {
        this.customerSales = customerSales;
    }

    public String getCustomerSales_cd() {
        return customerSales_cd;
    }

    public void setCustomerSales_cd(String customerSales_cd) {
        this.customerSales_cd = customerSales_cd;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public ArrayList<BrandMaster> getSamplingChecklistData() {
        return samplingChecklistData;
    }

    public void setSamplingChecklistData(ArrayList<BrandMaster> samplingChecklistData) {
        this.samplingChecklistData = samplingChecklistData;
    }

    ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();


}
