package com.example.android.quakereport;

/**
 * Created by hp on 19/12/2016.
 */


    public class Earthquake {

        private double mMagnitude;
        private String mLocation;
    /** Time of the earthquake */
    private long mTimeInMilliseconds;
    private String mUrl;

        public Earthquake(double Magnitude,String Location,Long Date,String url )
        {
            mMagnitude=Magnitude;
            mLocation=Location;
            mTimeInMilliseconds=Date;
            mUrl=url;
        }

        public double getmMagnitude()
        {
            return mMagnitude;
        }
        public String getmLocation()
        {
            return mLocation;
        }
        public Long getTimeInMilliseconds()
        {
            return mTimeInMilliseconds;
        }
    public  String getmUrl()
    {
        return mUrl;
    }

    }

