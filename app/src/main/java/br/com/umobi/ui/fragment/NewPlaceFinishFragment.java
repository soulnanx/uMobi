package br.com.umobi.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.umobi.R;
import br.com.umobi.contants.ConstantsResultCode;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewPlaceFinishFragment extends Fragment {

    public static final String TAG = "NewPlaceFinishFragment";

    @BindView(R.id.fragment_new_place_review_next)
    Button next;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_place_finish, container, false);
        init();
        return view;
    }

    private void init() {
        ButterKnife.bind(this, view);
        setEvents();
    }

    private void setEvents() {
        next.setOnClickListener(onClickNext());
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPlaceFinishFragment.this.getActivity().setResult(ConstantsResultCode.NEW_PLACE_FINISHED);
                NewPlaceFinishFragment.this.getActivity().finish();
            }
        };
    }

}
