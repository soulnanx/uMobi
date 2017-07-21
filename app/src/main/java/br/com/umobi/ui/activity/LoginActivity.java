package br.com.umobi.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.umobi.R;
import br.com.umobi.utils.NavigationUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseAppCompatActivity {

    @BindView(R.id.login_activity_btn_facebook)
    Button btnFacebook;

    @BindView(R.id.login_activity_btn_twitter)
    Button btnTwitter;

    @BindView(R.id.login_activity_btn_googleplus)
    Button btnGooglePlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setEvents();
    }

    private void setEvents() {
        btnFacebook.setOnClickListener(onClick());
        btnGooglePlus.setOnClickListener(onClick());
        btnTwitter.setOnClickListener(onClick());
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.navigate(LoginActivity.this, MainActivity.class, false);
            }
        };
    }
}
