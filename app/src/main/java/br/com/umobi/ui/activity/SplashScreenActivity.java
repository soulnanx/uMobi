package br.com.umobi.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import br.com.umobi.R;
import br.com.umobi.contants.ConstantsPermissions;
import br.com.umobi.utils.NavigationUtils;
import br.com.umobi.utils.PermissionsUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splashscreen);
        if (PermissionsUtils.hasGPSPermission(this)) {
            startWithLocation();
        } else {
            PermissionsUtils.requestGPSPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ConstantsPermissions.TAG_CODE_PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWithLocation();
                } else {
                    startWithoutLocation();
                }
            }
        }
    }

    private void startWithoutLocation() {
        navigate();
    }

    private void startWithLocation() {
        navigate();

    }

    private void navigate() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NavigationUtils.navigate(SplashScreenActivity.this, LoginActivity.class, true);
            }
        }, 2000);
    }

}
