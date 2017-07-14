package nickita.gq.yula.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by admin on 14/7/17.
 */
public class AlertBuilder {
    public static void showSimpleAlert(Context context, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(true);
        alert.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }
}
