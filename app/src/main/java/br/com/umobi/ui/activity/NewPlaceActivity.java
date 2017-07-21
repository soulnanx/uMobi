package br.com.umobi.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import br.com.umobi.R;
import br.com.umobi.ui.fragment.NewPlaceBasicDataFragment;
import butterknife.ButterKnife;

public class NewPlaceActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        init();
    }

    private void init() {
        ButterKnife.bind(this);

        changeFragment(new NewPlaceBasicDataFragment(), NewPlaceBasicDataFragment.TAG);
    }

    public void changeFragment(Fragment frag, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.activity_new_place_fragments, frag, tag);

//        if (frag.getTag().equals(this.tag)) {
//            transaction.disallowAddToBackStack();
//        } else {
//            transaction.addToBackStack(null);
//        }

        transaction.commit();
    }
}
