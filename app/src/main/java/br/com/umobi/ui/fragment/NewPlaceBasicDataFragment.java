package br.com.umobi.ui.fragment;

import android.content.DialogInterface;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.umobi.R;
import br.com.umobi.entity.GooglePlace;
import br.com.umobi.entity.UMobiPlace;
import br.com.umobi.entity.PlaceCategory;
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

    @BindView(R.id.fragment_new_place_basic_data_category)
    TextView categoryTxt;

    @BindView(R.id.fragment_new_place_basic_data_address)
    EditText addressTxt;

    @BindView(R.id.fragment_new_place_basic_data_title)
    EditText titleTxt;

    private View view;
    private Address selectedAddress;
    private GooglePlace selectedPlace;
    private List<PlaceCategory> availableCategories;
    private PlaceCategory selectedPlaceCategory;

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
        selectedAddress = ((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity())
                .getSelectedAddress();
        selectedPlace = ((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity()).
                getSelectedPlace();
        availableCategories = ((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity())
                .availableCategories;

        addressTxt.setText(AddressUtils.getFullAddress(selectedAddress));
        if(selectedPlace != null){
            titleTxt.setText(selectedPlace.getName());
        }
    }

    private void setEvents() {
        next.setOnClickListener(onClickNext());
        categoryTxt.setOnClickListener(onClickCategory());
    }

    private View.OnClickListener onClickCategory() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        };
    }

    private void showCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPlaceBasicDataFragment.this.getActivity());
        builder.setTitle(R.string.dialog_title_select_category);
        final ArrayAdapter<String> adapter = new ArrayAdapter(NewPlaceBasicDataFragment.this.getActivity(), android.R.layout.simple_list_item_1);
        adapter.addAll(getTitleFromCategoriesAvailable());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPlaceCategory = getCategoriesAvailableByTitle(adapter.getItem(which));
                categoryTxt.setText(selectedPlaceCategory.getTitle());
            }
        });
        builder.show();
    }

    private List<String> getTitleFromCategoriesAvailable() {
        List<String> categoriesTitle = new ArrayList<>();
        for (PlaceCategory category : availableCategories) {
            categoriesTitle.add(category.getTitle());
        }

        return categoriesTitle;
    }

    private PlaceCategory getCategoriesAvailableByTitle(final String title) {
        for (PlaceCategory category : availableCategories) {
            if (category.getTitle().equals(title))
                return category;
        }
        return null;
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO field validation

                if (selectedPlaceCategory == null) {
                    Toast.makeText(NewPlaceBasicDataFragment.this.getActivity(), "Selecione uma categoria!", Toast.LENGTH_SHORT).show();
                } else if (titleTxt.getText().toString().isEmpty()) {
                    Toast.makeText(NewPlaceBasicDataFragment.this.getActivity(), "Preencha o titulo do local!!", Toast.LENGTH_SHORT).show();
                } else {
                    next();
                }
            }
        };
    }

    private void next() {
        fillNewPlaceBasicData(((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity())
                .getNewPlace());

        ((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity())
                .changeFragment(new NewPlaceAddImageFragment(), NewPlaceBasicDataFragment.TAG);

        ((NewPlaceActivity) NewPlaceBasicDataFragment.this.getActivity())
                .selectedPlaceCategory = selectedPlaceCategory;
    }

    private void fillNewPlaceBasicData(UMobiPlace newPlace) {
        newPlace.setEnabled(true);
        newPlace.setDescription(descriptionTxt.getText().toString());
        newPlace.setTitle(titleTxt.getText().toString());
        newPlace.setAddress(addressTxt.getText().toString());
        newPlace.setPostalCode(selectedAddress.getPostalCode());
        newPlace.setCity(selectedAddress.getLocality());
        newPlace.setLatLong(LatLongUtils.parseAddress(selectedAddress));
    }

}
