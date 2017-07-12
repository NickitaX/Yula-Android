package nickita.gq.yula.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

/**
 * Created by admin on 12/7/17.
 */
public class FragmentBroker {
    public static Fragment getFragmentByTag(Context context, int container){
        return ((Activity)context).getFragmentManager().findFragmentById(container);
    }
}
