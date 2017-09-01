package br.com.umobi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.umobi.R;
import br.com.umobi.entity.Answer;

/**
 * Created by renan on 21/10/15.
 */
public class AnswerPlaceDetailAdapter extends RecyclerView.Adapter<AnswerPlaceDetailAdapter.ViewHolder> {
    private final Context context;
    private final List<Answer> answerList;
    private final CallbackClick clickServices;

    public AnswerPlaceDetailAdapter(Context context, List<Answer> answerList, CallbackClick clickSegments) {
        super();
        this.context = context;
        this.answerList = answerList;
        this.clickServices = clickSegments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_place_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Answer answer = answerList.get(position);

        holder.title.setText(answer.getQuestion().getLabel());


        if (answer.getQuestion().getIcon() != null) {
            Picasso.with(context)
                    .load(answer.getQuestion().getIcon().getUrl())
                    .noFade()
                    .into(holder.icon);
        }


    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        RatingBar ratingBar;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_answer_place_detail_title);
            ratingBar = (RatingBar) itemView.findViewById(R.id.item_answer_place_detail_rating);
            icon = (ImageView) itemView.findViewById(R.id.item_answer_place_detail_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickServices.onClick(answerList.get(getAdapterPosition()));
        }
    }

    public interface CallbackClick {
        void onClick(Answer answer);
    }
}
