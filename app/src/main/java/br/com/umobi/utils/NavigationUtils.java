package br.com.umobi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

public class NavigationUtils {

	public static void navigate(Activity activity, Class<?> contextoDestino, boolean clearStack){
		Intent i = new Intent(activity, contextoDestino);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(i);

		if (clearStack){
			activity.finish();
		}
	}

	public static void navigate(Context context, Class clazz, Bundle bundle, boolean clearTop){
		Intent intent = new Intent(context, clazz);

		if (clearTop){
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}

		if (bundle != null){
			intent.putExtras(bundle);
		}

		context.startActivity(intent);
	}

	public static void navigate(Activity context, Class clazz, Bundle bundle, int requestCode, boolean clearTop){
		Intent intent = new Intent(context, clazz);

		if (clearTop){
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		}

		if (bundle != null){
			intent.putExtras(bundle);
		}

		context.startActivityForResult(intent, requestCode);
	}


	public static void navigateWithExtra(Activity activity, Class clazz, Map<String, Serializable> extras, boolean clearStack){
		Intent intent = new Intent(activity, clazz);
		for (Entry<String, Serializable> current : extras.entrySet()) {
			intent.putExtra(current.getKey(), current.getValue());
		}
		activity.startActivity(intent);
        if (clearStack){
            activity.finish();
        }
	}

	public static Bundle createBundle(String key, Serializable value){
		Bundle bundle = new Bundle();
		bundle.putSerializable(key, value);
		return bundle;
	}

	public static Bundle createBundle(String key, Parcelable value){
		Bundle bundle = new Bundle();
		bundle.putParcelable(key, value);
		return bundle;
	}

	public static Serializable getSerializableFromBundle(Intent intent, String key){
		return intent.getExtras().getSerializable(key);
	}

}
