package br.com.umobi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import br.com.umobi.R;
import br.com.umobi.adapter.QuestionAdapter;
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
    private List<Answer> answersToSave;
    private NewPlaceActivity newPlaceActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_place_review, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);

        answersToSave = new ArrayList<>();
        newPlaceActivity = ((NewPlaceActivity)NewPlaceReviewFragment.this.getActivity());
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
        setList(selectedPlaceCategory.getQuestions());
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
    }

    private QuestionAdapter.CallbackClick onClickAnswer() {
        return new QuestionAdapter.CallbackClick() {
            @Override
            public void onClick(Question question, int answer) {
                createAnswerListToSave(question, answer);
            }
        };
    }

    private SaveCallback onSaveAllAnswers() {
        return new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e("saveAllAnswer", e.getMessage());
                }
            }
        };
    }

    private void createAnswerListToSave(Question question, int answer) {

        if (validateQuestion(question)){
            answersToSave.add(buildAnswer(null, question, answer));
        } else {
            updateAnswer(question, answer);
        }

    }

    private void updateAnswer(Question question, int answer) {
        for (Answer answerItem : answersToSave){
            if (answerItem.getQuestion().equals(question)){
                buildAnswer(answerItem, question, answer);
            }
        }
    }

    private boolean validateQuestion(Question question) {

        for (Answer answerItem : answersToSave){
            if (answerItem.getQuestion().equals(question)){
                return false;
            }
        }
        return true;
    }

    private Answer buildAnswer(Answer answer, Question question, int answerValue) {

        if (answer == null){
            answer = new Answer();
        }

        answer.setAnswer(answerValue);
        answer.setQuestion(question);
        //answer.setUser();
        answer.setPlaceCategory(selectedPlaceCategory);
        answer.setPlace(newPlaceActivity.getNewPlace());

        return answer;
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
        newPlaceActivity.getNewPlace().saveInBackground();
        newPlaceActivity.changeFragment(new NewPlaceFinishFragment(), NewPlaceFinishFragment.TAG);
        Answer.saveAllInBackground(answersToSave);
    }


}
