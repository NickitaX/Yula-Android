package nickita.gq.yula.utils;

import android.app.Activity;
import android.app.Dialog;
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

import nickita.gq.yula.R;
import nickita.gq.yula.callbacks.OnReadyCallback;
import nickita.gq.yula.fragments.MapFragment;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.networking.HTTPCore;

/**
 * Created by admin on 12/7/17.
 */
public class CustomPopup {
    public static void showAddTagPopup(final View view){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_tag_layout);
        Button submit = (Button) dialog.findViewById(R.id.submit_tag_button);
        final EditText descriptionEditText = (EditText) dialog.findViewById(R.id.description_edit_text);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.add_tag_progress);
        dialog.show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view_) {
                ((Activity)view.getContext()).getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                progressBar.setVisibility(View.VISIBLE);
                try {
                    MapFragment mapFragment = (MapFragment) FragmentBroker.getFragmentByTag(view.getContext(),R.id.main_container);
                    String tagId = UUID.randomUUID().toString();
                    String tagDescription = descriptionEditText.getText().toString();
                    Location userLocation = mapFragment.getLastLocation();
                    double lat = userLocation.getLatitude();
                    double lng = userLocation.getLongitude();
                    Date datePosted = new Date();
                    HTTPCore.GET(APIFactory.assembleAddGeoTagRequest(new GeoTag("id123", lat, lng, tagId, tagDescription, "active", datePosted.toString())), new OnReadyCallback() {
                        @Override
                        public void onReady(String response) {
                            ((Activity)view.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                            dialog.dismiss();
                            Snackbar.make(view, response, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
