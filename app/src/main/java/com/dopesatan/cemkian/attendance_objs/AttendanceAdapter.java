package com.dopesatan.cemkian.attendance_objs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;

import com.dopesatan.cemkian.R;

public class AttendanceAdapter extends AttendanceRecyclerView.Adapter<AttendanceAdapter.SubjectWiseHolder> {

    private final List<AttendanceItem> mData;
    private final LayoutInflater mInflater;

    // data is passed into the constructor
    public AttendanceAdapter(Context context, List<AttendanceItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public SubjectWiseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubjectWiseHolder(mInflater.inflate(R.layout.sub_row_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubjectWiseHolder holder, int position) {
        AttendanceItem item = mData.get(position);
        holder.sub.setText(item.getSubject().trim());
        holder.total.setText((item.getHoursTotal() + " hrs of total").trim());
        holder.current.setText((item.getHoursObtained() + " hrs attended").trim());
        holder.percent.setProgress((int) (item.getPercentInPoint() * 100));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class SubjectWiseHolder extends AttendanceRecyclerView.ViewHolder {
        TextView sub, total, current;
        DonutProgress percent;

        SubjectWiseHolder(View itemView) {
            super(itemView);
            sub = itemView.findViewById(R.id.sub);
            total = itemView.findViewById(R.id.total);
            current = itemView.findViewById(R.id.cur);
            percent = itemView.findViewById(R.id.per);
        }
    }

}