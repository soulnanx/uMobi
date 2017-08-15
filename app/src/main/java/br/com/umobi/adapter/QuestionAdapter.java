package br.com.umobi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import br.com.umobi.R;
import br.com.umobi.contants.ConstantsAnswer;
import br.com.umobi.entity.Question;

/**
 * Created by renan on 21/10/15.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private final Context context;
    private final List<Question> questionList;
    private final CallbackClick clickServices;

    public QuestionAdapter(Context context, List<Question> questionList, CallbackClick clickSegments) {
        super();
        this.context = context;
        this.questionList = questionList;
        this.clickServices = clickSegments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Question question = questionList.get(position);

        holder.title.setText(question.getTitle());

        if (question.isRating()) {
            //holder.note.setText(service.getNote());
        }

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        RadioButton radioYes;
        RadioButton radioNo;
        RadioButton radioIDoNotKnow;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_question_title);
            radioYes = (RadioButton) itemView.findViewById(R.id.item_answer_radio_yes);
            radioNo = (RadioButton) itemView.findViewById(R.id.item_answer_radio_no);
            radioIDoNotKnow = (RadioButton) itemView.findViewById(R.id.item_answer_radio_i_do_not_know);

            //itemView.setOnClickListener(this);
            radioYes.setOnClickListener(this);
            radioNo.setOnClickListener(this);
            radioIDoNotKnow.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int answer = 0;
            switch (view.getId()){
                case R.id.item_answer_radio_yes:
                    answer = ConstantsAnswer.ANSWER_YES;
                    break;
                case R.id.item_answer_radio_no:
                    answer = ConstantsAnswer.ANSWER_NO;
                    break;
                case R.id.item_answer_radio_i_do_not_know:
                    answer = ConstantsAnswer.ANSWER_I_DO_NOT_KNOW;
                    break;
            }
            clickServices.onClick(questionList.get(getAdapterPosition()), answer);
        }
    }

    public interface CallbackClick {
        void onClick(Question question, int answer);
    }
}
