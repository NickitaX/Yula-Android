package nickita.gq.yula.utils;

import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.values.APIValues;

/**
 * Created by admin on 10/7/17.
 */
public class APIFactory {
    public static String assembleAddGeoTagRequest(GeoTag tag){
    return APIValues.ADD_GEO_TAG+"?"
            +"userid="+tag.getUserid()+"&"
            +"lat="+tag.getLat()+"&"
            +"lng="+tag.getLng()+"&"
            +"tagid="+tag.getTagid()+"&"
            +"description="+tag.getDescription()+"&"
            +"status="+tag.getStatus()+"&"
            +"date="+tag.getDate();
    }
}
