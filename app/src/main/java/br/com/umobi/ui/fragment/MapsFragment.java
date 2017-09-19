package br.com.umobi.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.umobi.R;
import br.com.umobi.contants.ConstantsBundle;
import br.com.umobi.contants.ConstantsRequestCode;
import br.com.umobi.contants.ConstantsResultCode;
import br.com.umobi.entity.GooglePlace;
import br.com.umobi.entity.UMobiPlace;
import br.com.umobi.entity.User;
import br.com.umobi.ui.activity.MainActivity;
import br.com.umobi.ui.activity.NewPlaceActivity;
import br.com.umobi.ui.activity.PlaceDetailActivity;
import br.com.umobi.utils.LatLongUtils;
import br.com.umobi.utils.NavigationUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapFragment";
    public static final int ONE_KILOMETER = 1000;
    public static final int FIFTY_KILOMETER = 50000;

    private static final int PLACE_PICKER_REQUEST = 1;

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

    @BindView(R.id.fragment_maps_content_add_address)
    TextView addAddress;

    @BindView(R.id.fragment_maps_content_pin_more)
    Button placeMore;

    @BindView(R.id.fab_add_place)
    FloatingActionButton fabAddPlace;

    @BindView(R.id.fab_add_problem)
    FloatingActionButton fabAddProblem;

    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;

    private Marker newMarker;
    private Address selectedAddress;
    private UMobiPlace selectedPlace;


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
        fabAddPlace.setOnClickListener(onAddPlaceClickListener());
        fabAddProblem.setOnClickListener(onAddProblemClickListener());
    }

    private View.OnClickListener onClickPlaceMore() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavigationUtils.navigate(
                        MapsFragment.this.getActivity(),
                        PlaceDetailActivity.class,
                        NavigationUtils.createBundle(ConstantsBundle.PLACE_ID, selectedPlace.getObjectId()),
                        false);
            }
        };
    }

    private View.OnClickListener onClickAddPlace() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsFragment.this.getActivity(), NewPlaceActivity.class);
                intent.putExtra("selectedAddress", selectedAddress);
                startActivityForResult(intent, ConstantsRequestCode.NEW_PLACE);
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

    private FindCallback<UMobiPlace> onGetPlacesNearMe() {
        return new FindCallback<UMobiPlace>() {
            @Override
            public void done(List<UMobiPlace> places, ParseException e) {
                if (e == null) {
                    mMap.clear();
                    addPinsToMap(places, false);
                } else {
                    Toast.makeText(MapsFragment.this.getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void addPinsToMap(List<UMobiPlace> places, boolean isNew) {
        for (UMobiPlace place : places) {
            if (place.getLatLong() != null) {
                createPin(place);
            }
        }
    }

    private void createPin(UMobiPlace place){
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_ok_50))
                .position(place.getLatLong())).setTag(place);
    }

    private void setupMap() {
        mMap.setOnMapLongClickListener(onLongClickListener());
        mMap.setOnCameraIdleListener(onCameraIdle());
        mMap.setOnMarkerClickListener(onMarkerClick());
        mMap.setOnMapClickListener(onMapClick());
        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(14);

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

    private View.OnClickListener onAddPlaceClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener onAddProblemClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        };
    }

    private void hideContents() {
        fabMenu.close(true);
        contentPin.setVisibility(View.GONE);
        contentAdd.setVisibility(View.GONE);
        fabAddPlace.setVisibility(View.VISIBLE);
    }

    private void clearNewMarker() {
        if (newMarker != null) {
            newMarker.remove();
            newMarker = null;
        }
    }

    private void showContentPin(Marker marker) {
        contentPin.setVisibility(View.VISIBLE);
        fabAddPlace.setVisibility(View.GONE);
        selectedPlace = (UMobiPlace) marker.getTag();
        fillContentPin(marker);
    }

    private void fillContentPin(Marker marker) {
        UMobiPlace place = (UMobiPlace) marker.getTag();
        title.setText(place.getTitle());
        description.setText(place.getDescription());
        address.setText(place.getAddress());

    }

    private GoogleMap.OnCameraIdleListener onCameraIdle() {
        return new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (cameraPositionLastSearch != null) {
                    double dist = LatLongUtils.calcDistance(cameraPositionLastSearch, mMap.getCameraPosition().target);
                    if (dist > ONE_KILOMETER && dist < FIFTY_KILOMETER) {
                        UMobiPlace.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
                        cameraPositionLastSearch = mMap.getCameraPosition().target;
                    }
                }
            }
        };
    }

    private void moveCameraToUserPosition() {

        if (mMap != null && ((MainActivity) this.getActivity()).getLastLocation() != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(((MainActivity) this.getActivity()).getLastLocation()));
            UMobiPlace.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
        } else {
            moveCameraToUserLastLocation();
            ((MainActivity) this.getActivity()).findLastLocation(new MainActivity.OnLocationFound() {
                @Override
                public void received(Location lastLocation) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(
                            new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())

                    ));

                    UMobiPlace.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
                    cameraPositionLastSearch = mMap.getCameraPosition().target;
                    User.saveLastLocation(
                            MapsFragment.this.getActivity(),
                            new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                }

                @Override
                public void failed() {
                    // TODO get last location saved
                }
            });
        }
    }

    private void moveCameraToUserLastLocation() {
        if (User.hasLastLocation(MapsFragment.this.getActivity())) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(User.getLastLocation(MapsFragment.this.getActivity())));
        }
    }

    private GoogleMap.OnMapLongClickListener onLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                clearNewMarker();
                newMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_new_50))
                        .position(latLng).title("Marker in" + latLng.latitude));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                hideContents();
                showContentAdd(latLng);
            }
        };
    }

    private void showContentAdd(LatLng latLng) {
        contentAdd.setVisibility(View.VISIBLE);
        fabAddPlace.setVisibility(View.GONE);
        findAddress(latLng);
    }

    private void findAddress(LatLng latLng){
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null && !addresses.isEmpty()){
            String address = addresses.get(0).getAddressLine(0);
            selectedAddress = addresses.get(0);
            addAddress.setText(address);
        } else {
            Toast.makeText(MapsFragment.this.getActivity(), "Endereço não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsRequestCode.NEW_PLACE){

            switch (resultCode){
                case ConstantsResultCode.NEW_PLACE_FINISHED:
                    hideContents();
                    UMobiPlace.getPlacesNearMe(1, mMap.getCameraPosition().target, onGetPlacesNearMe());
                    break;
                case ConstantsResultCode.NEW_PLACE_CANCELLED:

                    break;
            }

        }else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                final GooglePlace place = GooglePlace.placeToGooglePlace(PlacePicker.getPlace(getActivity(), data));
                Intent intent = new Intent(MapsFragment.this.getActivity(), NewPlaceActivity.class);
                findAddress(new LatLng(place.getLatitude(), place.getLongitude()));
                intent.putExtra("selectedAddress", selectedAddress);
                intent.putExtra("selectedPlace", place);
                startActivityForResult(intent, ConstantsRequestCode.NEW_PLACE);
            }
        }
    }
}