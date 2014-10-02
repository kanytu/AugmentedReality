package com.poliveira.apps.simpleaugmentedreality;

import android.graphics.Rect;

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
    private Rect mArea;

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

    public boolean isOnScreen(int x, int y){
        if(mArea==null)
            return false;
        return mArea.intersects(x,y,x,y);
        //return !(mViewPosition == null || mMarkerSize == null) && x > mViewPosition[0] - mMarkerSize[0]/2 && x < mViewPosition[0] + mMarkerSize[0]/2 && y > mViewPosition[1] - mMarkerSize[1]/2 && y < mViewPosition[1] + mMarkerSize[1]/2;
    }

    public Rect getArea()
    {
        return mArea;
    }

    public void setArea(Rect area)
    {
        mArea = area;
    }
}
