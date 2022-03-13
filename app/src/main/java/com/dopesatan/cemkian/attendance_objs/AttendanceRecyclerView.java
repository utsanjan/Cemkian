package com.dopesatan.cemkian.attendance_objs;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceRecyclerView extends RecyclerView {
	
	private static final float MANUAL_SCROLL_SLOW_RATIO = 0.01f;
	
	public AttendanceRecyclerView(@NonNull Context context) {
		super(context);
	}
	
	public AttendanceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AttendanceRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean fling(int velocityX, int velocityY) {
		return super.fling(velocityX, (int) (velocityY * MANUAL_SCROLL_SLOW_RATIO));
	}
}
