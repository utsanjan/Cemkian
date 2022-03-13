package com.dopesatan.cemkian.ui_access;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.dopesatan.cemkian.R;
import com.dopesatan.cemkian.attendance_objs.AttendanceData;
import com.dopesatan.cemkian.workers.DTM;
import com.dopesatan.cemkian.workers.Runner;
import com.dopesatan.cemkian.workers.WebParser;
import com.google.android.material.snackbar.Snackbar;

import soup.neumorphism.NeumorphImageButton;

public class LoginActivity extends AppCompatActivity {
	
	EditText username, password;
	ProgressBar loader;
	NeumorphImageButton log_in;
	AttendanceData currentData;
	TextView passText;
	ImageView visiblePass;
	
	public static Snackbar specialSnack(View view, String text, int len, String hexTextColor, int backColor) {
		Snackbar snackbar = Snackbar.make(view,
				Html.fromHtml("<font color=\"" + hexTextColor + "\">" + text + "</font>"),
				len);
		View snack = snackbar.getView();
		snack.setBackgroundColor(backColor);
		return snackbar;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		if (getSupportActionBar() != null)
			getSupportActionBar().setTitle("");
		
		username = findViewById(R.id.username_ent);
		password = findViewById(R.id.pass_ent);
		
		loader = findViewById(R.id.progress_login);
		log_in = findViewById(R.id.login_btn);
		
		visiblePass = findViewById(R.id.view_pass_Login);
		passText = findViewById(R.id.visiblePassLogin);
		
		visiblePass.setOnClickListener(v -> {
			passText.setVisibility((passText.getVisibility() == View.VISIBLE) ? View.INVISIBLE: View.VISIBLE);
		});
		
		password.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				passText.setText(password.getText());
			}
		});
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			Drawable drawableProgress = DrawableCompat.wrap(loader.getIndeterminateDrawable());
			DrawableCompat.setTint(drawableProgress, getResources().getColor(android.R.color.white));
			loader.setIndeterminateDrawable(DrawableCompat.unwrap(drawableProgress));
		} else {
			loader.getIndeterminateDrawable().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
		}
		
		username.requestFocus();
		
		log_in.setOnClickListener(v -> {
			if (username.getText().toString().equals("") || password.getText().toString().equals("")) {
				specialSnack(log_in, "Don't forget to enter username/password", Snackbar.LENGTH_LONG,
						"#e1ceff", Color.parseColor("#303133"))
						.show();
			} else {
				loader.setVisibility(View.VISIBLE);
				log_in.setVisibility(View.INVISIBLE);
				AsyncTask.execute(() -> {
					try {
						new WebParser().start(username.getText().toString(),
								password.getText().toString(),
								new Runner() {
									@Override
									public void onComplete(AttendanceData data) {
										currentData = data;
									}
									
									@Override
									public void onCheckComplete(boolean result) {
										runOnUiThread(() -> {
											if (!result) {
												Handler one = new Handler();
												
												Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
												DTM.getInstance(getBaseContext()).setUname(username.getText().toString());
												DTM.getInstance(getBaseContext()).setPwd(password.getText().toString());
												
												one.post(new Runnable() {
													@Override
													public void run() {
														if (currentData != null) {
															runOnUiThread(() -> {
																startActivity(new Intent(LoginActivity.this, MainActivity.class)
																		.putExtra(MainActivity.ATTENDANCE_ITEM, currentData));
																finish();
															});
														} else {
															one.postDelayed(this, 500);
														}
													}
												});
											} else {
												loader.setVisibility(View.INVISIBLE);
												log_in.setVisibility(View.VISIBLE);
												specialSnack(log_in, "Check your credentials", Snackbar.LENGTH_LONG,
														"#e1ceff", Color.parseColor("#303133")).show();
											}
										});
									}
									
									@Override
									public void onError() {
										loader.setVisibility(View.INVISIBLE);
										log_in.setVisibility(View.VISIBLE);
										specialSnack(log_in, "Connection Error", Snackbar.LENGTH_LONG,
												"#e1ceff", Color.parseColor("#303133")).show();
									}
								});
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		});
		
	}
}
