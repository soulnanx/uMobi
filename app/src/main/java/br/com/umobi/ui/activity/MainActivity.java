package br.com.umobi.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.umobi.R;
import br.com.umobi.ui.fragment.MapsFragment;
import br.com.umobi.ui.fragment.ProfileFragment;

public class MainActivity extends BaseAppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    changeFragment(new MapsFragment(), MapsFragment.TAG);
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment(new MapsFragment(), MapsFragment.TAG);
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment(), ProfileFragment.TAG);
                    return true;
            }
            return false;
        }

    };

    public void changeFragment(Fragment frag, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, frag, tag);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
