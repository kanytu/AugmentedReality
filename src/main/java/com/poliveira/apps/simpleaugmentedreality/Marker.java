package com.poliveira.apps.simpleaugmentedreality;

import android.graphics.Rect;
import android.hardware.Camera;

import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

/**
 * Created by poliveira on 30/09/2014.
 */
public class Marker
{
    private LatLng mCoordinates;
    private float mAltitude;
    private Vector3 mViewPosition;
    private double mBearing;
    private double mDistance;
    private Rect mArea;
    private Object mObject;
    private Vector3 mProjectedCoordinate;

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

    public Vector3 getViewPosition()
    {
        return mViewPosition;
    }

    public void setViewPosition(Vector3 viewPosition)
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

    @Deprecated
    public double getBearing()
    {
        return mBearing;
    }

    @Deprecated
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

    public boolean isOnScreen(int x, int y)
    {
        if (mArea == null)
            return false;
        return mArea.intersects(x, y, x, y);
        //return !(mViewPosition == null || mMarkerSize == null) && x > mViewPosition[0] - mMarkerSize[0]/2 && x < mViewPosition[0] + mMarkerSize[0]/2 && y > mViewPosition[1] - mMarkerSize[1]/2 && y < mViewPosition[1] + mMarkerSize[1]/2;
    }

    public void projectCoordinate(LatLng origin)
    {
        mProjectedCoordinate = Utils.convLocToVec(origin, mCoordinates);
    }

    public Vector3 getProjectedCoordinate()
    {
        return mProjectedCoordinate;
    }

    public void setProjectedCoordinate(Vector3 projectedCoordinate)
    {
        mProjectedCoordinate = projectedCoordinate;
    }

    /**
     * use this if you want to store an object for this marker and retrieve it later. Like a house or other information
     *
     * @return
     */
    public Object getObject()
    {
        return mObject;
    }

    public void setObject(Object object)
    {
        mObject = object;
    }

    public Rect getArea()
    {
        return mArea;
    }

    public void setArea(Rect area)
    {
        mArea = area;
    }

    public void toFieldOfView(double horizontalAngle, double verticalAngle, int height, int width)
    {
        float x = (float) ((mProjectedCoordinate.getX() / mProjectedCoordinate.getY()) * ((width / 2) / Math.tan(Math.toRadians(verticalAngle / 2))));
        x += width / 2;
        x = width - x;
        float y = (float) ((mProjectedCoordinate.getZ() / mProjectedCoordinate.getY()) * ((height / 2) / Math.tan(Math.toRadians(horizontalAngle/ 2))));
        y += height / 2;
        y = height - y;
        mViewPosition = new Vector3(new float[]{x, y,0});
    }
}
