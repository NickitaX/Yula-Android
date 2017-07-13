package nickita.gq.yula.callbacks;

import java.util.List;

import nickita.gq.yula.model.GeoTag;

/**
 * Created by admin on 13/7/17.
 */
public interface OnTagsPulledCallback {
    void pulled(List<GeoTag> tags);
}
