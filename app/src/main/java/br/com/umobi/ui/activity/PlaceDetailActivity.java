package br.com.umobi.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.umobi.R;
import br.com.umobi.adapter.AnswerPlaceDetailAdapter;
import br.com.umobi.contants.ConstantsBundle;
import br.com.umobi.entity.Answer;
import br.com.umobi.entity.UMobiPlace;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailActivity extends AppCompatActivity {

    private UMobiPlace place;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.answer_list)
    RecyclerView answerListRecyclerView;

    @BindView(R.id.include_drawer_place_detail_access_percent)
    TextView accessPercent;

    @BindView(R.id.activity_place_detail_description)
    TextView description;

    @BindView(R.id.activity_place_detail_address)
    TextView address;

    @BindView(R.id.app_bar_main_image)
    ImageView image;

    @BindView(R.id.include_drawer_place_detail_premium_badge)
    ImageView badgeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        init();
    }

    private void init() {
        ButterKnife.bind(this);

        loadValues();
        setEvents();
    }

    private void setEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
    }

    private void setToolbar(final String title) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
    }

    private void loadValues() {
        String idPlace = getIntent().getExtras().getString(ConstantsBundle.PLACE_ID, "");

        if (!idPlace.isEmpty()){
            UMobiPlace.getById(idPlace, onGetPlaceById());
        }
    }

    private GetCallback<UMobiPlace> onGetPlaceById() {
        return new GetCallback<UMobiPlace>() {
            @Override
            public void done(UMobiPlace selectedPlaceFromParse, ParseException e) {
                if (e == null){
                    place = selectedPlaceFromParse;
                    setValues(place);
                    Answer.getByIdPlace(place, onGetAnswerByPlaceCallback());
                }
            }
        };
    }

    private void setValues(UMobiPlace place) {
        setToolbar(place.getTitle());
        description.setText(place.getDescription());
        address.setText(place.getAddress());
        loadImage(place.getImage());

    }

    private void loadImage(String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.with(this)
                    .load(url)
                    .noFade()
                    .into(image);
        } else {
            image.setImageResource(R.drawable.ic_broken_image);
        }
    }

    private FindCallback<Answer> onGetAnswerByPlaceCallback() {
        return new FindCallback<Answer>() {
            @Override
            public void done(List<Answer> answerListFromParse, ParseException e) {
                if (e == null){
                    setAnswerList(answerListFromParse);
                }
            }
        };
    }

    private void setAnswerList(List<Answer> answerList) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(PlaceDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        answerListRecyclerView.setLayoutManager(layoutManager);
        answerListRecyclerView.setAdapter(new AnswerPlaceDetailAdapter(PlaceDetailActivity.this, answerList, onClickAnswer()));
    }

    private AnswerPlaceDetailAdapter.CallbackClick onClickAnswer() {
        return new AnswerPlaceDetailAdapter.CallbackClick() {
            @Override
            public void onClick(Answer answer) {

            }
        };
    }


}
