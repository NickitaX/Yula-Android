package nickita.gq.yula.networking;

import android.content.Intent;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import nickita.gq.yula.MainActivity;
import nickita.gq.yula.callbacks.OnReadyCallback;
import nickita.gq.yula.callbacks.OnUserExistsCallback;
import nickita.gq.yula.model.Login;
import nickita.gq.yula.model.User;
import nickita.gq.yula.utils.APIFactory;
import nickita.gq.yula.utils.Storage;

/**
 * Created by admin on 16/7/17.
 */
public class UserManager {

    public static void doesUserExist(Login login, final OnUserExistsCallback c){
        try {
            HTTPCore.GET(APIFactory.assembleLoginRequest(login), new OnReadyCallback() {
                @Override
                public void onReady(String response) {
                    Moshi moshi = new Moshi.Builder().build();
                    Type type = Types.newParameterizedType(List.class, User.class);
                    JsonAdapter<List<User>> adapter = moshi.adapter(type);
                    try {
                        if(adapter.fromJson(response)!=null&&adapter.fromJson(response).size()>0){
                            c.checked(true);
                        }else{
                            c.checked(false);
                        }
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
