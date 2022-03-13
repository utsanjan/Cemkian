package com.dopesatan.cemkian.jobs;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class DailyCheckerCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String s) {
        if (DailyChecker.TAG.equals(s)) {
            return new DailyChecker();
        }
        return null;
    }
}
