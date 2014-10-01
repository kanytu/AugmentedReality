package com.poliveira.apps.simpleaugmentedreality;

/**
 * Created by poliveira on 30/09/2014.
 */
public class Parameters
{
    private float mLowPassAlphaAccelerometer;
    private float mLowPassAlphaMagnetic;
    private int mSmoothListSample;
    private double[] mCameraAngleOfView;
    private int mMarkerResource;
    private int mDistanceOfView; //in meters
    private float mMarkerMaxZoom;
    private float mMarkerMinZoom;


    public float getLowPassAlphaAccelerometer()
    {
        return mLowPassAlphaAccelerometer;
    }

    public void setLowPassAlphaAccelerometer(float lowPassAlphaAccelerometer)
    {
        this.mLowPassAlphaAccelerometer = lowPassAlphaAccelerometer;
    }

    public double[] getCameraAngleOfView()
    {
        return mCameraAngleOfView;
    }

    public void setCameraAngleOfView(double[] cameraAngleOfView)
    {
        mCameraAngleOfView = cameraAngleOfView;
    }

    public int getMarkerResource()
    {
        return mMarkerResource;
    }

    public void setMarkerResource(int markerResource)
    {
        mMarkerResource = markerResource;
    }

    public float getLowPassAlphaMagnetic()
    {
        return mLowPassAlphaMagnetic;
    }

    public void setLowPassAlphaMagnetic(float lowPassAlphaMagnetic)
    {
        mLowPassAlphaMagnetic = lowPassAlphaMagnetic;
    }

    public int getSmoothListSample()
    {
        return mSmoothListSample;
    }

    public void setSmoothListSample(int smoothListSample)
    {
        mSmoothListSample = smoothListSample;
    }

    public int getDistanceOfView()
    {
        return mDistanceOfView;
    }

    public void setDistanceOfView(int distanceOfView)
    {
        mDistanceOfView = distanceOfView;
    }

    public float getMarkerMaxZoom()
    {
        return mMarkerMaxZoom;
    }

    public void setMarkerMaxZoom(float markerMaxZoom)
    {
        mMarkerMaxZoom = markerMaxZoom;
    }

    public float getMarkerMinZoom()
    {
        return mMarkerMinZoom;
    }

    public void setMarkerMinZoom(float markerMinZoom)
    {
        mMarkerMinZoom = markerMinZoom;
    }
}
