package br.com.umobi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import br.com.umobi.R;
import br.com.umobi.adapter.QuestionAdapter;
import br.com.umobi.contants.ConstantsAnswer;
import br.com.umobi.entity.Answer;
import br.com.umobi.entity.PlaceCategory;
import br.com.umobi.entity.Question;
import br.com.umobi.ui.activity.NewPlaceActivity;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewPlaceReviewFragment extends Fragment {

    public static final String TAG = "NewPlaceReviewFragment";

    @BindView(R.id.fragment_new_place_review_next)
    Button next;

    @BindView(R.id.fragment_new_place_review_recycler_view)
    RecyclerView recyclerView;

    private View view;
    private PlaceCategory selectedPlaceCategory;
    private List<Question> questionList;
    private List<Answer> answersToSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_place_review, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);

        questionList = new ArrayList<>();
        answersToSave = new ArrayList<>();
        setEvents();
        loadValues();
    }

    private void loadValues() {
        PlaceCategory.getOnlyPlaceTest(onGetOnlyPlaceTestCallback());
    }

    private FindCallback<PlaceCategory> onGetOnlyPlaceTestCallback() {
        return new FindCallback<PlaceCategory>() {
            @Override
            public void done(List<PlaceCategory> categories, ParseException e) {
                if (e == null){
                    if (!categories.isEmpty()){
                        selectedPlaceCategory = categories.get(0);
                        loadQuestions();
                    }
                }
            }
        };
    }

    private void loadQuestions() {
        Question.getByPlaceCategory(selectedPlaceCategory, onGetByPlaceCategory());
    }

    private FindCallback<Question> onGetByPlaceCategory() {
        return new FindCallback<Question>() {
            @Override
            public void done(List<Question> questions, ParseException e) {
                if (e == null){
                    fillQuestions(questions);
                }
            }
        };
    }

    private void fillQuestions(List<Question> questions) {
        setList(questions);
    }

    private void setList(List<Question> questions) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(NewPlaceReviewFragment.this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new QuestionAdapter(NewPlaceReviewFragment.this.getActivity(), questions, onClickAnswer()));
        questions.toString();
    }

    private QuestionAdapter.CallbackClick onClickAnswer() {
        return new QuestionAdapter.CallbackClick() {
            @Override
            public void onClick(Question question, int answer) {
                question.toString();

            }
        };
    }

    private void setEvents() {
        next.setOnClickListener(onClickNext());
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        };
    }

    private void next() {
        ((NewPlaceActivity)NewPlaceReviewFragment.this.getActivity())
                .changeFragment(new NewPlaceFinishFragment(), NewPlaceFinishFragment.TAG);

        ((NewPlaceActivity)NewPlaceReviewFragment.this.getActivity())
                .getNewPlace().saveEventually();
    }


}
