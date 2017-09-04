package br.com.umobi.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by renan on 5/14/15.
 */
public class DialogLoading extends ProgressDialog {

    public DialogLoading(Context context) {
        super(context);
    }

    public static DialogLoading show(Context context, int body){
        return create(context, 0, body, true);
    }

    public static DialogLoading show(Context context, int title, int body){
        return create(context, title, body, true);
    }

    public static DialogLoading create(Context context, int title, int body, boolean show){
        DialogLoading loadingDialog = new DialogLoading(context);
        loadingDialog.setMessage(context.getString(body));

        if (title > 0){
            loadingDialog.setTitle(title);
        }

        if (show){
            loadingDialog.show();
        }

        return loadingDialog;
    }

}