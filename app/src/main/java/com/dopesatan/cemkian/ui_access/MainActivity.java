package com.dopesatan.cemkian.ui_access;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

import com.dopesatan.cemkian.R;
import com.dopesatan.cemkian.attendance_objs.AttendanceAdapter;
import com.dopesatan.cemkian.attendance_objs.AttendanceData;
import com.dopesatan.cemkian.attendance_objs.AttendanceItem;
import com.dopesatan.cemkian.attendance_objs.AttendanceRecyclerView;
import com.dopesatan.cemkian.jobs.DailyChecker;
import com.dopesatan.cemkian.workers.DTM;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	public static final String ATTENDANCE_ITEM = "atm";
	ArcProgress progress;
	AttendanceRecyclerView recyclerView;
	AttendanceAdapter adapter;
	ImageButton error;
	boolean autoScrollable = true;
	DrawerLayout drawer;
	private Object abouts;

	@SuppressLint("NotifyDataSetChanged")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupUI();
		setupConstraints();
		
		ArrayList<AttendanceItem> attendanceItems = new ArrayList<>();
		if (getIntent() != null) {
			AttendanceData data = (AttendanceData) getIntent().getSerializableExtra(ATTENDANCE_ITEM);
			if (data != null) {
				if (!data.isAvailable()) {
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Oops!")
							.setCancelable(false)
							.setMessage("Probably Attendance Data Not Available. Check out website & submit a bug report")
							.show();
					return;
				}
				progress.setProgress(
						(data.getHourObt(data.getSubLen() - 1) * 100) /
								data.getHourTotal(data.getSubLen() - 1));
				for (int i = 0; i < data.getSubLen(); i++) {
					attendanceItems.add(new AttendanceItem()
							.setSubject(data.getSub(i))
							.setHoursTotal(data.getHourTotal(i))
							.setHoursObtained(data.getHourObt(i))
							.setPercentInPoint((float) data.getHourObt(i) / data.getHourTotal(i))
					);
					recyclerView.setHasFixedSize(true);
					adapter = new AttendanceAdapter(this, attendanceItems);
					recyclerView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
				if (data.isCached()) {
					//io
					progress.setTextColor(Color.argb(255, 200, 255, 0));
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(DTM.getInstance(getBaseContext()).getLastCheckTimeStamp());
					
					//errorLevel notification
					error.setImageResource(R.drawable.ic_cached);
					error.setOnClickListener(cacheListener(calendar));
				} else {
					DTM.getInstance(getBaseContext()).setLastCheckTimeStamp(System.currentTimeMillis());
					DTM.getInstance(getBaseContext()).updateAttendanceData(data);
				}
				
				if (attendanceItems.get(attendanceItems.size() - 1).getPercentInPoint() < .75f) {
					//low attendance
					error.setImageResource(R.drawable.ic_warning);
					error.setOnClickListener(lowAttendanceListener());
					progress.setTextColor(R.color.colorPrimarySettingAct);
				} else {
					if (!data.isCached())
						error.setVisibility(View.GONE);
				}
			}
		}
		if (DTM.getInstance(this).isCycleEnabled()) {
			DailyChecker.schedule(this);
		}
	}
	
	private View.OnClickListener lowAttendanceListener() {
		return v -> {
			if (!MainActivity.this.isFinishing()) {
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("Low Attendance")
						.setMessage("As per University rules and decorum 75% attendance is the minimum requirement to sit for semester examinations. Please try to maintain proper attendance!")
						.setPositiveButton("Got it", null)
						.show();
			}
		};
	}
	
	private View.OnClickListener cacheListener(Calendar calendar) {
		return v -> {
			if (!MainActivity.this.isFinishing()) {
				int hr = calendar.get(Calendar.HOUR_OF_DAY);
				int mn = calendar.get(Calendar.MINUTE);
				int sn = calendar.get(Calendar.SECOND);
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("Limitations")
						.setMessage("Loading Cached Data from : " +
								(hr > 9 ? hr : "0" + hr) +
								":" +
								(mn > 9 ? mn : "0" + mn) +
								":" +
								(sn > 9 ? sn : "0" + sn) +
								"\n\n• To prevent unwanted spam, cyclical re-fetching of attendance data, data caching is implemented." +
								"\n\n• Also, no one is going to change attendance minute-by-minute. This will reduce server overhead too." +
								"\n\n• You can only refresh attendance data 24 times in a day with uniform time difference." +
								"\n\n• Additionally, the app checks the attendance at a specified certain time (Check Settings) and reports the changes." +
								"\n\n• The 'Circular Progress Text' will have yellow color when the result is cached. & red when you got less than 75% of attendance.")
						.setPositiveButton("Understood", null)
						.show();
			}
		};
	}
	
	private void setupConstraints() {
		if (DTM.getInstance(getBaseContext()).isNewUser()) {
			recyclerView.post(() -> {
				if (adapter != null) {
					recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
				}
				recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this) {
					@Override
					public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
						LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {
							
							private static final float SPEED = 300f;// default=25f
							
							@Override
							protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
								return SPEED / displayMetrics.densityDpi;
							}
							
						};
						smoothScroller.setTargetPosition(position);
						startSmoothScroll(smoothScroller);
					}
					
					@Override
					public void onScrollStateChanged(int state) {
						if (state == RecyclerView.SCROLL_STATE_IDLE && autoScrollable) {
							recyclerView.smoothScrollToPosition(0); // ?
							autoScrollable = false;
							DTM.getInstance(getBaseContext()).setNewUserNoMore();
						}
						super.onScrollStateChanged(state);
					}
				});
			});
			new Handler().postDelayed(() -> {
				if (!MainActivity.this.isFinishing()) {
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Notice")
							.setMessage("This app doesn't depend on any official API based services. Sometimes it may not work as expected due to unexpected changes in the college website. Thank you for understanding and being patient.")
							.setPositiveButton("Okay", null)
							.show();
				}
			}, 3000);
		} else {
			recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
		}
	}
	
	private void setupUI() {
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar); //fix #46216551/8572503
		
		progress = findViewById(R.id.arc_progress);
		recyclerView = findViewById(R.id.recyclerAtt);
		error = findViewById(R.id.error);
		NavigationView navigationView = findViewById(R.id.nav_view);
		drawer = findViewById(R.id.drawer_layout);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this,
				drawer,
				toolbar,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close);
		
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.setItemIconTintList(null);
	}
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		switch (item.getItemId()) {
			case R.id.menu_settings:
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
				break;
			case R.id.menu_bug:
				{Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/utsanjan/Cemkian/issues/new/choose"));
				String title = "Open with";
				Intent chooser = Intent.createChooser(intent, title);
				startActivity(chooser);}
				break;
			case R.id.menu_about:
				{Intent intent = new Intent(this, About.class);
				startActivity(intent);}
				break;
			case R.id.menu_logout:
				{Toast.makeText(this, "Logged out, reopen the app to Login again", Toast.LENGTH_LONG).show();
				SharedPreferences myPrefs = getSharedPreferences("Activity", MODE_PRIVATE);
				@SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPrefs.edit();
				try {
					Runtime runtime = Runtime.getRuntime();
					runtime.exec("pm clear " + getApplicationContext().getPackageName() + " HERE");}
				catch (Exception e) {
					e.printStackTrace();}
				Intent intent = new Intent(this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();}
				break;
			default:
				break;
		}
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
}
