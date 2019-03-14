
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesReportGetterSetter {

    @SerializedName("Sales_Report")
    @Expose
    private List<SalesReport> salesReport = null;

    public List<SalesReport> getSalesReport() {
        return salesReport;
    }

    public void setSalesReport(List<SalesReport> salesReport) {
        this.salesReport = salesReport;
    }

}
