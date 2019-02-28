package intelre.cpm.com.hulcnc.gettersetter;

import java.io.Serializable;

/**
 * Created by upendra on 04-10-2018.
 */
public class SearchStoreDataGetterSetter implements Serializable {

    String  mobile = "";
    String key_id;
    String visit_date;
    String card_no="";
    String user_id="";
    String SAVE_DATA_STATUS="";
    String customerName="";
    String sale_qty="";
    String ammount="";

    public String getSale_qty() {
        return sale_qty;
    }

    public void setSale_qty(String sale_qty) {
        this.sale_qty = sale_qty;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }


    public String getSAVE_DATA_STATUS() {
        return SAVE_DATA_STATUS;
    }

    public void setSAVE_DATA_STATUS(String SAVE_DATA_STATUS) {
        this.SAVE_DATA_STATUS = SAVE_DATA_STATUS;
    }

    String store__type="";

    public String getStore__type() {
        return store__type;
    }

    public void setStore__type(String store__type) {
        this.store__type = store__type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

}
