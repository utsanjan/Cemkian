package com.dopesatan.cemkian.ui_access;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;

import java.util.concurrent.TimeUnit;

import com.dopesatan.cemkian.R;
import com.dopesatan.cemkian.attendance_objs.AttendanceData;
import com.dopesatan.cemkian.workers.DTM;
import com.dopesatan.cemkian.workers.Runner;
import com.dopesatan.cemkian.workers.WebParser;
import com.google.android.material.snackbar.Snackbar;

public class Splash extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
		
		Glide.with(this)
				.load(R.mipmap.splash)
				.into((ImageView) findViewById(R.id.splashStarter));
		
		if (DTM.getInstance(getBaseContext()).getUname().equals("") || DTM.getInstance(getBaseContext()).getPWD().equals("")) {
			new Handler().postDelayed(() ->
					runOnUiThread(() -> {
						startActivity(new Intent(Splash.this, LoginActivity.class));
						finish();
					}), 3000);
		} else {
			if (System.currentTimeMillis() - DTM.getInstance(getBaseContext()).getLastCheckTimeStamp() >= TimeUnit.HOURS.toMillis(1)) {
				AsyncTask.execute(() -> {
					try {
						new WebParser().start(DTM.getInstance(getBaseContext()).getUname(),
								DTM.getInstance(getBaseContext()).getPWD(), new Runner() {
									@Override
									public void onComplete(AttendanceData data) {
										startActivity(new Intent(Splash.this, MainActivity.class)
												.putExtra(MainActivity.ATTENDANCE_ITEM, data));
										finish();
									}
									
									@Override
									public void onCheckComplete(boolean isNull) {
										if (isNull) {
											runOnUiThread(() -> {
												Toast.makeText(Splash.this, "Wrong Username/Password Combo", Toast.LENGTH_SHORT).show();
												startActivity(new Intent(Splash.this, LoginActivity.class));
											});
										}
									}
									
									@Override
									public void onError() {
										LoginActivity.specialSnack(findViewById(R.id.splashStarter), "Connection Error", Snackbar.LENGTH_LONG,
												"#e1eff", Color.parseColor("#303133")).show();
									}
								});
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				new Handler().postDelayed(() ->
						runOnUiThread(() -> {
							startActivity(new Intent(Splash.this, MainActivity.class)
									.putExtra(MainActivity.ATTENDANCE_ITEM, DTM.getInstance(getBaseContext()).getLastSavedATD()));
							finish();
						}), 1000);
			}
		}
		
		ProgressBar bar = findViewById(R.id.spl_progress);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			Drawable drawableProgress = DrawableCompat.wrap(bar.getIndeterminateDrawable());
			DrawableCompat.setTint(drawableProgress, getResources().getColor(android.R.color.white));
			bar.setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));
		} else {
			bar.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
		}
	}
}
