package com.poliveira.apps.simpleaugmentedreality;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by poliveira on 30/09/2014.
 */
public class Marker
{
    private LatLng mCoordinates;
    private float mAltitude;
    private double[] mViewPosition;
    private double mBearing;
    private double mDistance;

    public Marker(LatLng coordinates)
    {
        mCoordinates = coordinates;
        mAltitude = 3 - new Random().nextInt(10);
    }

    public LatLng getCoordinates()
    {
        return mCoordinates;
    }

    public void setCoordinates(LatLng coordinates)
    {
        mCoordinates = coordinates;
    }

    public double[] getViewPosition()
    {
        return mViewPosition;
    }

    public void setViewPosition(double[] viewPosition)
    {
        mViewPosition = viewPosition;
    }

    public float getAltitude()
    {
        return mAltitude;
    }

    public void setAltitude(float altitude)
    {
        mAltitude = altitude;
    }

    public double getBearing()
    {
        return mBearing;
    }

    public void setBearing(double bearing)
    {
        mBearing = bearing;
    }

    public double getDistance()
    {
        return mDistance;
    }

    public void setDistance(double distance)
    {
        mDistance = distance;
    }
}
