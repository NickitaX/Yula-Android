package nickita.gq.yula.networking;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import nickita.gq.yula.callbacks.OnReadyCallback;
import nickita.gq.yula.callbacks.OnTagsPulledCallback;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.networking.HTTPCore;
import nickita.gq.yula.values.APIValues;

/**
 * Created by admin on 13/7/17.
 */
public class TagsManager {
    public static void pullGeoTagsFromServer(final OnTagsPulledCallback c){
        try {
            HTTPCore.GET(APIValues.GET_ALL_TAGS, new OnReadyCallback() {
                @Override
                public void onReady(String response) {
                    Moshi moshi = new Moshi.Builder().build();
                    Type type = Types.newParameterizedType(List.class, GeoTag.class);
                    JsonAdapter<List<GeoTag>> adapter = moshi.adapter(type);
                    try {
                        c.pulled(adapter.fromJson(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
