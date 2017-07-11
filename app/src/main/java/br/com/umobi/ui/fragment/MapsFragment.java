package br.com.umobi.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.SaveCallback;

import br.com.umobi.R;
import br.com.umobi.entity.Place;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

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

    }

    @Override
    public void onStart() {
        super.onStart();
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

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setupMap() {
        mMap.setOnMapLongClickListener(onLongClickListener());
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
