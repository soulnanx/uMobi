package br.com.umobi.ui.fragment;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import br.com.umobi.R;
import br.com.umobi.app.App;
import br.com.umobi.entity.Place;
import br.com.umobi.ui.activity.NewPlaceActivity;
import br.com.umobi.utils.AddressUtils;
import br.com.umobi.utils.LatLongUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPlaceBasicDataFragment extends Fragment {

    public static final String TAG = "NewPlaceBasicDataFragment";

    @BindView(R.id.fragment_new_place_basic_data_next)
    Button next;

    @BindView(R.id.fragment_new_place_basic_data_description)
    EditText descriptionTxt;

    @BindView(R.id.fragment_new_place_basic_data_address)
    EditText addressTxt;

    @BindView(R.id.fragment_new_place_basic_data_title)
    EditText titleTxt;

    private View view;
    private Address selectedAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_place_basic_data, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
        loadValues();
        setEvents();
    }

    private void loadValues() {
        selectedAddress = ((NewPlaceActivity)NewPlaceBasicDataFragment.this.getActivity())
                .getSelectedAddress();

        addressTxt.setText(AddressUtils.getFullAddress(selectedAddress));
    }

    private void setEvents() {
        next.setOnClickListener(onClickNext());
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // TODO field validation
            next();
            }
        };
    }

    private void next(){
        fillNewPlaceBasicData(((NewPlaceActivity)NewPlaceBasicDataFragment.this.getActivity())
                .getNewPlace());

        ((NewPlaceActivity)NewPlaceBasicDataFragment.this.getActivity())
                .changeFragment(new NewPlaceReviewFragment(), NewPlaceBasicDataFragment.TAG);
    }

    private void fillNewPlaceBasicData(Place newPlace) {
        newPlace.setEnabled(true);
        newPlace.setDescription(descriptionTxt.getText().toString());
        newPlace.setTitle(titleTxt.getText().toString());
        newPlace.setAddress(addressTxt.getText().toString());
        newPlace.setPostalCode(selectedAddress.getPostalCode());
        newPlace.setLatLong(LatLongUtils.parseAddress(selectedAddress));
    }

}
