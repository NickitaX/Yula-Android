package nickita.gq.yula.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import nickita.gq.yula.R;
import nickita.gq.yula.callbacks.OnReadyCallback;
import nickita.gq.yula.callbacks.OnTagsPulledCallback;
import nickita.gq.yula.model.GeoTag;
import nickita.gq.yula.networking.HTTPCore;
import nickita.gq.yula.utils.TagsManager;
import nickita.gq.yula.values.APIValues;

/**
 * Created by admin on 10/7/17.
 */
public class MapFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    private View mView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private List<GeoTag> mTagList;
    private LocationRequest locationRequest;

    public Location getLastLocation() {
        return mLastLocation;
    }

    public void loadGeotags() {
        mGoogleMap.clear();
        mTagList = new LinkedList<>();
        TagsManager.pullGeoTagsFromServer(new OnTagsPulledCallback() {
            @Override
            public void pulled(List<GeoTag> tags) {
                mTagList = tags;
                placeTagsOnMap();
            }
        });
    }

    private GeoTag getTagById(String id) {
        for (GeoTag tag : mTagList) {
            if (tag.getTagid().equals(id)) {
                return tag;
            }
        }
        return null;
    }

    public void navigateToTagWithId(String id) {
        GeoTag found = getTagById(id);
        if (found != null) {
            navigateSmoothOnMap(new LatLng(found.getLat(), found.getLng()));
        }
    }

    private void placeTagsOnMap() {
        for (final GeoTag tag : mTagList) {
            final LatLng latLng = new LatLng(tag.getLat(), tag.getLng());
            ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(tag.getDescription()));
                }
            });
        }
    }

    private void navigateSmoothOnMap(LatLng target) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(target).zoom(35).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment_layout, container, false);
        initialize(savedInstanceState);
        prepareMap();
        setUpLocationListener();
        return mView;
    }

    private void initialize(Bundle savedInstanceState) {
        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
    }

    private void prepareMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                mGoogleMap = mMap;
                if (ContextCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    setUpGMaps();
                } else {
                    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
            }
        });
    }

    private void setUpGMaps() {
        if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        askGps();
        updateLocation();
    }

    public void askGps() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(getClass().toString(), "TEMP:ERROR");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (mGoogleApiClient == null) {
            return;
        }
        setFusedLocationListener();
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        loadGeotags();
    }

    private void setUpLocationListener() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mView.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void setFusedLocationListener(){
        if(!mGoogleApiClient.isConnected()){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = location;
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
