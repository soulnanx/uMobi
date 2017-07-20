package br.com.umobi.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.umobi.R;
import br.com.umobi.entity.Place;
import br.com.umobi.ui.activity.MainActivity;
import br.com.umobi.ui.activity.PlaceDetailActivity;
import br.com.umobi.utils.LatLongUtils;
import br.com.umobi.utils.NavigationUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    public static final String TAG = "MapFragment";
    public static final int ONE_KILOMETER = 1000;
    public static final int FIFTY_KILOMETER = 50000;

    private GoogleMap mMap;
    private View view;
    private LatLng cameraPositionLastSearch;

    @BindView(R.id.fragment_maps_content_pin)
    View contentPin;

    @BindView(R.id.fragment_maps_content_pin_title)
    TextView title;

    @BindView(R.id.fragment_maps_content_pin_description)
    TextView description;

    @BindView(R.id.fragment_maps_content_pin_address)
    TextView address;

    @BindView(R.id.fragment_maps_content_add)
    View contentAdd;

    @BindView(R.id.fragment_maps_content_add_place)
    Button addPlace;

    @BindView(R.id.fragment_maps_content_add_problem)
    Button addProblem;

    @BindView(R.id.fragment_maps_content_pin_more)
    Button placeMore;

    private Marker newMarker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        init();

        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
        loadMap();
        setEvents();
    }

    private void setEvents() {
        placeMore.setOnClickListener(onClickPlaceMore());
        addPlace.setOnClickListener(onClickAddPlace());
        addProblem.setOnClickListener(onClickAddProblem());
    }

    private View.OnClickListener onClickPlaceMore() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigate(MapsFragment.this.getActivity(), PlaceDetailActivity.class, false);
            }
        };
    }

    private View.OnClickListener onClickAddPlace() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPlace(newMarker.getPosition());
            }
        };
    }

    private View.OnClickListener onClickAddProblem() {
        return null;
    }

    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();

        moveCameraToUserPosition();
    }

    private FindCallback<Place> onGetPlacesNearMe() {
        return new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (e == null) {
                    mMap.clear();
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
                mMap.addMarker(new MarkerOptions().position(place.getLatLong())).setTag(place);
            }
        }
    }

    private void setupMap() {
        mMap.setOnMapLongClickListener(onLongClickListener());
        mMap.setOnCameraIdleListener(onCameraIdle());
        mMap.setOnMarkerClickListener(onMarkerClick());
        mMap.setOnMapClickListener(onMapClick());
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

    private GoogleMap.OnMapClickListener onMapClick() {
        return new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clearNewMarker();
                hideContents();
            }
        };
    }

    private GoogleMap.OnMarkerClickListener onMarkerClick() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearNewMarker();
                hideContents();
                showContentPin(marker);
                return false;
            }
        };
    }

    private void hideContents() {
        contentPin.setVisibility(View.GONE);
        contentAdd.setVisibility(View.GONE);
    }

    private void clearNewMarker(){
        if (newMarker != null){
            newMarker.remove();
            newMarker = null;
        }
    }

    private void showContentPin(Marker marker) {
        contentPin.setVisibility(View.VISIBLE);
        fillContentPin(marker);
    }

    private void fillContentPin(Marker marker) {
        Place place = (Place) marker.getTag();
        title.setText(place.getTitle());
        description.setText(place.getDescription());
        address.setText(place.getAddress());

    }

    private GoogleMap.OnCameraIdleListener onCameraIdle() {
        return new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (cameraPositionLastSearch != null){
                    double dist = LatLongUtils.calcDistance(cameraPositionLastSearch, mMap.getCameraPosition().target);
                    if (dist > ONE_KILOMETER && dist < FIFTY_KILOMETER){
                        Place.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
                        cameraPositionLastSearch = mMap.getCameraPosition().target;
                    }
                }
            }
        };
    }

    private void moveCameraToUserPosition(){

        if (mMap != null && ((MainActivity)this.getActivity()).getLastLocation() != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(((MainActivity)this.getActivity()).getLastLocation()));
            Place.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
        } else {
            ((MainActivity)this.getActivity()).findLastLocation(new MainActivity.OnLocationFound() {
                @Override
                public void received(Location lastLocation) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())

                    ));
                    Place.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
                    cameraPositionLastSearch = mMap.getCameraPosition().target;
                }

                @Override
                public void failed() {
                    // TODO get last location saved
                }
            });
        }
    }

    private GoogleMap.OnMapLongClickListener onLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                clearNewMarker();
                newMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in" + latLng.latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                hideContents();
                showContentAdd(latLng);

                //saveNewPlace(latLng);
            }
        };
    }

    private void showContentAdd(LatLng latLng) {
        contentAdd.setVisibility(View.VISIBLE);
    }

    private void saveNewPlace(LatLng latLng) {
        hideContents();

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
                } else {
                    clearNewMarker();
                }
            }
        });
    }

}