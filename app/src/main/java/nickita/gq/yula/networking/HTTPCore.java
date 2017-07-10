package nickita.gq.yula.networking;

import android.util.Log;

import java.io.IOException;

import nickita.gq.yula.callbacks.OnReadyCallback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 10/7/17.
 */
public class HTTPCore {
    public static void GET(final String url, final OnReadyCallback c) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String output = "EMPTY";
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    output = response.body().string();
                } catch (Exception e) {
                    Log.e("HTTPCore.GET:", e.getMessage());
                }
                c.onReady(output);
            }
        }).start();
    }
}
