package br.com.umobi.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import br.com.umobi.R;
import br.com.umobi.ui.fragment.MapsFragment;
import br.com.umobi.ui.fragment.ProfileFragment;

public class MainActivity extends BaseAppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private int attempts;
    private LatLng lastLocation;
    private GoogleApiClient client;
    private boolean needsToShowDialog;
    private Handler handler;
    private static final int REQUEST_ERROR_PLAY_SERVICE = 1;
    private static final int REQUEST_VERIFY_GPS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener());
        changeFragment(new MapsFragment(), MapsFragment.TAG);
        init();
    }

    private void init() {
        setupGoogleApiClient();
        handler = new Handler();
    }

    private void setupGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void changeFragment(Fragment frag, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, frag, tag);
        transaction.commit();
    }

    public void findLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(client);

        if (location != null) {
            attempts = 0;
            lastLocation = new LatLng(location.getLatitude(), location.getLongitude());
        } else if (attempts < 10) {
            attempts++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findLastLocation();
                }
            }, 2000);
        }
    }

    private void verifyStatusGps() {
        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder locationsSettingRequest = new LocationSettingsRequest.Builder();
        locationsSettingRequest.setAlwaysShow(true);
        locationsSettingRequest.addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(client, locationsSettingRequest.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        findLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if (needsToShowDialog) {
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_VERIFY_GPS);
                                needsToShowDialog = false;
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.wtf("Error", "error!!!");
                        break;
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        changeFragment(new MapsFragment(), MapsFragment.TAG);
                        return true;
                    case R.id.navigation_dashboard:
                        changeFragment(new MapsFragment(), MapsFragment.TAG);
                        return true;
                    case R.id.navigation_profile:
                        changeFragment(new ProfileFragment(), ProfileFragment.TAG);
                        return true;
                }
                return false;
            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        findLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        verifyStatusGps();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(MainActivity.this, REQUEST_ERROR_PLAY_SERVICE);
            } catch (IntentSender.SendIntentException e) {
                Log.e("map", "connectionFailed");
                e.printStackTrace();
            }
        } else {
            showDialogError(MainActivity.this, connectionResult.getErrorCode());
        }
    }

    private void showDialogError(FragmentActivity activity, final int errorCode) {
        final String TAG = "DIALOG_ERROR_PLAY_SERVICE";

        if (MainActivity.this.getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode, MainActivity.this, REQUEST_ERROR_PLAY_SERVICE);
            dialog.show();
        }
    }

    public LatLng getLastLocation() {
        return lastLocation;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (client != null) {
            client.connect();
        }
    }

    @Override
    protected void onStop() {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }

        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }


}
