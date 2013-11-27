package com.me.mygdxgame;

import com.me.mygdxgame.ActionResolver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class ActionResolverAndroid implements ActionResolver {
	Handler uiThread;
	Context appContext;

	public ActionResolverAndroid(Context appContext) {
		uiThread = new Handler();
		this.appContext = appContext;
	}


	@Override
	public void showAlertBox(final String alertBoxTitle, final String alertBoxMessage, final String alertBoxButtonText) {
		uiThread.post(new Runnable() {
			public void run() {
				new AlertDialog.Builder(appContext).setTitle(alertBoxTitle).setMessage(alertBoxMessage)
						.setNeutralButton(alertBoxButtonText, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						}).create().show();
			}
		});
	}

	@Override
	public void openUri(String uri) {
		Uri myUri = Uri.parse(uri);
		Intent intent = new Intent(Intent.ACTION_VIEW, myUri);
		appContext.startActivity(intent);
	}

	@Override
	public void showMyList() {
		// appContext.startActivity(new Intent(this.appContext,
		// MyListActivity.class));
	}

	@Override
	public void showToast(final CharSequence toastMessage, final int toastDuration) {
		uiThread.post(new Runnable() {
			public void run() {
				Toast.makeText(appContext, toastMessage, toastDuration).show();
			}
		});
	}
	
	@Override
	public void showShortToast(final CharSequence toastMessage) {
		uiThread.post(new Runnable() {
			public void run() {
				Toast.makeText(appContext, toastMessage, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showLongToast(final CharSequence toastMessage) {
		uiThread.post(new Runnable() {
			public void run() {
				Toast.makeText(appContext, toastMessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
