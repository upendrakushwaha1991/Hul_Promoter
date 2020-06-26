
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorewiseSalesReportGetterSetter {

    @SerializedName("Storewise_Sales_Report")
    @Expose
    private List<StorewiseSalesReport> storewiseSalesReport = null;

    public List<StorewiseSalesReport> getStorewiseSalesReport() {
        return storewiseSalesReport;
    }

    public void setStorewiseSalesReport(List<StorewiseSalesReport> storewiseSalesReport) {
        this.storewiseSalesReport = storewiseSalesReport;
    }

}
