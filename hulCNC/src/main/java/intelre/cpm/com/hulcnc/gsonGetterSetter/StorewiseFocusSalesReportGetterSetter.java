
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StorewiseFocusSalesReportGetterSetter {

    @SerializedName("Storewise_Focus_Sales_Report")
    @Expose
    private List<StorewiseFocusSalesReport> storewiseFocusSalesReport = null;

    public List<StorewiseFocusSalesReport> getStorewiseFocusSalesReport() {
        return storewiseFocusSalesReport;
    }

    public void setStorewiseFocusSalesReport(List<StorewiseFocusSalesReport> storewiseFocusSalesReport) {
        this.storewiseFocusSalesReport = storewiseFocusSalesReport;
    }

}
