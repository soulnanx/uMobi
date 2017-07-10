package br.com.umobi.ui.app;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import br.com.umobi.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by renan on 10/07/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initParse();
        initFacebook();
        initFonts();
    }

    private void initParse() {
        //ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("br.com.metasix.skedly")
                .clientKey(getString(R.string.client_key))
                .server(getString(R.string.parse_url))
                .build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void initFacebook() {

    }

    private void initFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Bariol_Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
