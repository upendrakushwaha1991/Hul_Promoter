package intelre.cpm.com.hulcnc.gettersetter;

/**
 * Created by upendra on 31-7-2018.
 */

public class SearchSalesGetterSetter {
    String customerName="";
    String card_no="";
    String price="";
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
