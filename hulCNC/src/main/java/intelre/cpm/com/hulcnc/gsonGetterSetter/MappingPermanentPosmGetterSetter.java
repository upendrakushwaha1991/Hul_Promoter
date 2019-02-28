
package intelre.cpm.com.hulcnc.gsonGetterSetter;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MappingPermanentPosmGetterSetter {

    @SerializedName("Mapping_Permanent_Posm")
    @Expose
    private List<MappingPermanentPosm> mappingPermanentPosm = null;

    public List<MappingPermanentPosm> getMappingPermanentPosm() {
        return mappingPermanentPosm;
    }

    public void setMappingPermanentPosm(List<MappingPermanentPosm> mappingPermanentPosm) {
        this.mappingPermanentPosm = mappingPermanentPosm;
    }

}
