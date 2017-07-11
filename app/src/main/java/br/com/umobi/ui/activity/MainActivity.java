package br.com.umobi.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import br.com.umobi.R;
import br.com.umobi.ui.fragment.MapsFragment;
import br.com.umobi.ui.fragment.ProfileFragment;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener());
        changeFragment(new MapsFragment(), MapsFragment.TAG);
    }


    public void changeFragment(Fragment frag, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, frag, tag);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {

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
    }


}
