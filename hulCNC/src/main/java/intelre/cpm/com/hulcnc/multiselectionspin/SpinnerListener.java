package intelre.cpm.com.hulcnc.multiselectionspin;

import java.util.ArrayList;

import intelre.cpm.com.hulcnc.gsonGetterSetter.StoreCategoryMaster;

/**
 * Created by jeevanp on 2/2/2018.
 */

public interface SpinnerListener {
    void onItemsSelected(ArrayList<StoreCategoryMaster> items);
}