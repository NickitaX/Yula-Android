package nickita.gq.yula.utils;

import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.model.User;
import nickita.gq.yula.values.APIValues;

/**
 * Created by admin on 10/7/17.
 */
public class APIFactory {

    public static String assembleAddUserRequest(User user){
        return APIValues.ADD_USER+"?"
                +"user_email="+user.getEmail()+"&"
                +"first_name="+user.getFirstName()+"&"
                +"last_name="+user.getLastName()+"&"
                +"user_id="+user.getUserID()+"&"
                +"rating="+user.getRating()+"&"
                +"user_password="+user.getPassword();
    }

    public static String assembleAddGeoTagRequest(GeoTag tag){
    return APIValues.ADD_GEO_TAG+"?"
            +"user_id="+tag.getUserid()+"&"
            +"lat="+tag.getLat()+"&"
            +"lng="+tag.getLng()+"&"
            +"tag_id="+tag.getTagid()+"&"
            +"tag_description="+tag.getDescription()+"&"
            +"tag_status="+tag.getStatus()+"&"
            +"tag_date="+tag.getDate();
    }
}
