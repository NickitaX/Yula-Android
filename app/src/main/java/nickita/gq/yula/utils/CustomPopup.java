package nickita.gq.yula.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import nickita.gq.yula.MainActivity;
import nickita.gq.yula.R;
import nickita.gq.yula.callbacks.OnReadyCallback;
import nickita.gq.yula.fragments.MapFragment;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.model.User;
import nickita.gq.yula.networking.HTTPCore;

/**
 * Created by admin on 12/7/17.
 */
public class CustomPopup {

    public static void showRegisterAccountPopup(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_register_account_layout);
        final Button register = (Button) dialog.findViewById(R.id.register_account_button);
        final EditText firstNameEdit = (EditText) dialog.findViewById(R.id.add_user_first_name);
        final EditText lastNameEdit = (EditText) dialog.findViewById(R.id.add_user_last_name);
        final EditText emailEdit = (EditText) dialog.findViewById(R.id.add_user_email);
        final EditText passwordEdit = (EditText) dialog.findViewById(R.id.add_user_password);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.register_account_progress);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    final User newUser = new User(emailEdit.getText().toString(), firstNameEdit.getText().toString(),
                            lastNameEdit.getText().toString(), UUID.randomUUID().toString(),
                            "newbie", passwordEdit.getText().toString());
                    HTTPCore.GET(APIFactory.assembleAddUserRequest(newUser), new OnReadyCallback() {
                        @Override
                        public void onReady(final String response) {
                            dialog.dismiss();
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.equals("0")) {
                                        AlertBuilder.showSimpleAlert(context, "Account created!");
                                        Storage.saveUserToMemory(context, newUser);
                                        context.startActivity(new Intent(context, MainActivity.class));
                                    } else {
                                        AlertBuilder.showSimpleAlert(context, "Error. Try again!");
                                    }
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();
    }

    public static void showAddTagPopup(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_add_tag_layout);
        Button submit = (Button) dialog.findViewById(R.id.submit_tag_button);
        final EditText descriptionEditText = (EditText) dialog.findViewById(R.id.description_edit_text);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.add_tag_progress);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view_) {
                ((Activity) context).getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                progressBar.setVisibility(View.VISIBLE);
                try {
                    MapFragment mapFragment = (MapFragment) FragmentBroker.getFragmentByTag(context, R.id.main_container);
                    String tagId = UUID.randomUUID().toString();
                    String tagDescription = descriptionEditText.getText().toString();
                    Location userLocation = mapFragment.getLastLocation();
                    double lat = userLocation.getLatitude();
                    double lng = userLocation.getLongitude();
                    Date datePosted = new Date();
                    HTTPCore.GET(APIFactory.assembleAddGeoTagRequest(new GeoTag("id123", lat, lng, tagId, tagDescription, "active", datePosted.toString())), new OnReadyCallback() {
                        @Override
                        public void onReady(String response) {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            dialog.dismiss();
                            if (response.equals("0")) {
                                AlertBuilder.showSimpleAlert(context, "Tag created!");
                            } else {
                                AlertBuilder.showSimpleAlert(context, "Error. Try again!");
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
