package com.wedo.client.netframe.activities.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.wedo.client.netframe.R;
import com.wedo.client.netframe.androidext.ChenApplication;

/**
 * @author wxc
 * 
 * @date 2012-5-29 下午10:36:46
 */
public class BaseActivity extends Activity {

	protected ChenApplication shenApplication;

	protected static Drawable defaultImg140;
	
	protected static Drawable defaultLargeImage;
	
	protected static Drawable point1Drawable;
	
	protected static Drawable point2Drawable;
	
	protected static Bitmap defaultSmallImage; 
	
	protected final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shenApplication = (ChenApplication) this.getApplication();

	}


	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	protected ProgressDialog showProgressDialog(int message) {
		String title = getString(R.string.app_name);
		String messageString = getString(message);
		return ProgressDialog.show(this, title, messageString);
	}


	protected String getLogTag() {
		return getClass().getSimpleName();
	}

	protected final void logError(String msg, Throwable e) {
		Log.e(getLogTag(), msg, e);
	}

	protected final void logError(String msg) {
		Log.e(getLogTag(), msg);
	}

	protected final void logWarn(String msg) {
		Log.w(getLogTag(), msg);
	}

	protected final void logInfo(String msg) {
		Log.i(getLogTag(), msg);
	}

	protected final void logDebug(String msg) {
		Log.d(getLogTag(), msg);
	}

	
	private void setView(final boolean isShow, final View... views) {
		if (views == null || views.length == 0) {
			return;
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				for (View view : views) {
					if (view == null) {
						continue;
					}
					int v = view.getVisibility();
					if (isShow && v == View.VISIBLE) {
						continue;
					}
					if (!isShow && v == View.GONE) {
						continue;
					}
					if (isShow) {
						view.setVisibility(View.VISIBLE);
					} else {
						view.setVisibility(View.GONE);
					}
				}
			}
		});
	}
	
	public void setViewGoneBySynchronization(final View... views) {
		setViewBySynchronization(false, views);
	}
	
	public void setViewVisiableBySynchronization(final View... views) {
		setViewBySynchronization(true, views);
	}
	
	protected void goToScreen() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		finish();
	}
	
	/**
	 * 隐藏键盘
	 */
	public void hiddenBoard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	private void setViewBySynchronization(final boolean isShow, final View... views) {
		if (views == null || views.length == 0) {
			return;
		}
		for (View view : views) {
			if (view == null) {
				continue;
			}
			int v = view.getVisibility();
			if (isShow && v == View.VISIBLE) {
				continue;
			}
			if (!isShow && v == View.GONE) {
				continue;
			}
			if (isShow) {
				view.setVisibility(View.VISIBLE);
			} else {
				view.setVisibility(View.GONE);
			}
		}
	}

	protected void toastShort(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	protected void toastLong(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
}