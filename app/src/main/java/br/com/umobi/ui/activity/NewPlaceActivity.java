package br.com.umobi.ui.activity;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.List;

import br.com.umobi.R;
import br.com.umobi.contants.ConstantsResultCode;
import br.com.umobi.entity.GooglePlace;
import br.com.umobi.entity.UMobiPlace;
import br.com.umobi.entity.PlaceCategory;
import br.com.umobi.entity.Question;
import br.com.umobi.ui.dialog.DialogLoading;
import br.com.umobi.ui.fragment.NewPlaceBasicDataFragment;
import butterknife.ButterKnife;

public class NewPlaceActivity extends BaseAppCompatActivity {

    private UMobiPlace newPlace;
    private Question newQuestion;
    private Address selectedAddress;
    private GooglePlace selectedPlace;
    public List<PlaceCategory> availableCategories;
    private DialogLoading dialogLoading;
    public PlaceCategory selectedPlaceCategory;
    public byte[] selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        newPlace = new UMobiPlace();
        newQuestion = new Question();
        selectedAddress = getIntent().getExtras().getParcelable("selectedAddress");
        selectedPlace = (GooglePlace) getIntent().getExtras().getSerializable("selectedPlace");

        loadValues();
    }

    private void loadValues() {
        dialogLoading = DialogLoading.show(this,R.string.waiting_message);
        PlaceCategory.getAllAvailable(onGetPlaceCategoryCallback());
    }

    public void changeFragment(Fragment frag, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.activity_new_place_fragments, frag, tag);

        transaction.commit();
    }

    private FindCallback<PlaceCategory> onGetPlaceCategoryCallback() {
        return new FindCallback<PlaceCategory>() {
            @Override
            public void done(List<PlaceCategory> categoriesFromParse, ParseException e) {
                dialogLoading.dismiss();
                if (e == null){
                    if (!categoriesFromParse.isEmpty()){
                        availableCategories = categoriesFromParse;
                    }
                    changeFragment(new NewPlaceBasicDataFragment(), NewPlaceBasicDataFragment.TAG);
                }

            }
        };
    }

    public UMobiPlace getNewPlace(){
        return newPlace;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public GooglePlace getSelectedPlace(){
        return selectedPlace;
    }

    @Override
    public void onBackPressed() {
        setResult(ConstantsResultCode.NEW_PLACE_CANCELLED);
        super.onBackPressed();
    }

    public void saveNewPlace() {
        if (selectedImage != null) {
            final ParseFile file = new ParseFile(newPlace.getObjectId(), selectedImage);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        newPlace.setImage(file.getUrl());
                        newPlace.saveInBackground();
                    }
                }
            });
        } else {
            newPlace.saveInBackground();
        }
    }

}
