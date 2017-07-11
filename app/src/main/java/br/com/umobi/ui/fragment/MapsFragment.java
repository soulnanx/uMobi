package br.com.umobi.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import br.com.umobi.R;
import br.com.umobi.entity.Place;
import br.com.umobi.ui.activity.MainActivity;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    public static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        init();

        return view;
    }

    private void init() {
        loadMap();
    }

    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();

        if (((MainActivity)this.getActivity()).getLastLocation() != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(((MainActivity)this.getActivity()).getLastLocation()));
        }
    }

    private FindCallback<Place> onGetPlacesNearMe() {
        return new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (e == null) {
                    addPinsToMap(places);
                } else {
                    Toast.makeText(MapsFragment.this.getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void addPinsToMap(List<Place> places) {
        for (Place place : places) {
            if (place.getLatLong() != null){
                mMap.addMarker(new MarkerOptions().position(place.getLatLong()));
            }
        }
    }

    private void setupMap() {
        mMap.setOnMapLongClickListener(onLongClickListener());
        mMap.setOnCameraIdleListener(onCameraIdle());
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(18);

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private GoogleMap.OnCameraIdleListener onCameraIdle() {
        return new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mMap.clear();
                Place.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
            }
        };
    }

    private GoogleMap.OnMapLongClickListener onLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in" + latLng.latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                saveNewPlace(latLng);
            }
        };
    }

    private void saveNewPlace(LatLng latLng) {
        Place place = new Place();
        place.setLatLong(latLng);
        place.setAddress("address" + latLng.latitude);
        place.setTitle("title" + latLng.latitude);
        place.setDescription("description" + latLng.latitude);
        place.setPostalCode(String.valueOf(latLng.latitude));
        place.setEnabled(true);


        place.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    Toast.makeText(MapsFragment.this.getActivity(), "saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}