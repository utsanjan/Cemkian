package com.dopesatan.cemkian.ui_access;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.dopesatan.cemkian.R;
import com.dopesatan.cemkian.workers.DTM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {
	
	FloatingActionButton saveBtn;
	EditText usr, pwd;
	SwitchCompat daily_chk;
	TextView timeRes, passText;
	Button changeTime;
	TimePickerDialog dialog;
	LinearLayout timeCover;
	ImageView visiblePass;
	ImageView SettingsBack;
	
	private static String time(int hr, int min) {
		String rex;
		if (hr < 10) {
			rex = "0" + hr + ":";
		} else {
			rex = hr + ":";
		}
		if (min < 10) {
			rex = rex + "0" + min;
		} else {
			rex = rex + min;
		}
		return rex;
	}
	
	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		saveBtn = findViewById(R.id.sv);
		usr = findViewById(R.id.usr);
		pwd = findViewById(R.id.pwd);
		daily_chk = findViewById(R.id.daily_chk);
		timeRes = findViewById(R.id.timeRes);
		changeTime = findViewById(R.id.changeTime);
		timeCover = findViewById(R.id.timeCover);
		visiblePass = findViewById(R.id.view_pass);
		passText = findViewById(R.id.visiblePass);
		SettingsBack = findViewById(R.id.settings_back);
		
		visiblePass.setOnClickListener(v -> passText.setVisibility((passText.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE));
		
		pwd.addTextChangedListener(new TextWatcher()  {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				passText.setText(s.toString());
			}
		});
		
		timeRes.setText("Next check scheduled at " +
				time(DTM.getInstance(getBaseContext()).getCycleT_Hour(), DTM.getInstance(getBaseContext()).getCycleT_Min()) +
				" and will be active for one hour to meet network criteria.");
		
		dialog = new TimePickerDialog(SettingsActivity.this, R.style.AppTheme_SettingsActivity_TimePicker, (view, hourOfDay, minute) -> {
			dialog.getWindow().setLayout(10, 20);
			timeRes.setText("Next check scheduled at " +
					time(hourOfDay, minute) +
					" and will be active for one hour to meet network criteria.");
			DTM.getInstance(getBaseContext()).setCycleT_Hour(hourOfDay);
			DTM.getInstance(getBaseContext()).setCycleT_Min(minute);
			
		}, DTM.getInstance(getBaseContext()).getCycleT_Hour(), DTM.getInstance(getBaseContext()).getCycleT_Min(), true);

		
		daily_chk.setChecked(DTM.getInstance(getBaseContext()).isCycleEnabled());
		daily_chk.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				timeCover.setVisibility(View.VISIBLE);
			} else {
				timeCover.setVisibility(View.INVISIBLE);
			}
		});
		
		saveBtn.setOnClickListener(v -> {
			String usr_0 = usr.getText().toString();
			String pwd_0 = pwd.getText().toString();

			if (!usr_0.equals("") && !pwd_0.equals("")) {
				DTM.getInstance(getBaseContext()).setUname(usr_0);
				DTM.getInstance(getBaseContext()).setUname(pwd_0);
			}
			Toast.makeText(this, "Settings updated", Toast.LENGTH_LONG).show();
			DTM.getInstance(getBaseContext()).setCycleState(daily_chk.isChecked());
			
			finish();
		});
		
		changeTime.setOnClickListener(v -> dialog.show());

		SettingsBack.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View arg0)
			{
				finish();
			}
		});
	}
}
