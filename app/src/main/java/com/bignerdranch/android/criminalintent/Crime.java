package com.bignerdranch.android.criminalintent;

import android.text.format.Time;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Time mTime;
    private boolean mSolved;

    public Crime() {
        this(UUID.randomUUID());
    }



    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
        mTime = new Time();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Time getTime() {
        return mTime;
    }
    public void setTime(Time mTime) {
        this.mTime = mTime;
    }
}
