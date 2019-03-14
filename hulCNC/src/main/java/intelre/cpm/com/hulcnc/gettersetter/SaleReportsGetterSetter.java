package intelre.cpm.com.hulcnc.gettersetter;

public class SaleReportsGetterSetter {
    String sku,value,ftd,total,mtd;
    String sku_id;

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFtd() {
        return ftd;
    }

    public void setFtd(String ftd) {
        this.ftd = ftd;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMtd() {
        return mtd;
    }

    public void setMtd(String mtd) {
        this.mtd = mtd;
    }
}
