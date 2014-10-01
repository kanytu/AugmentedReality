package com.poliveira.apps.simpleaugmentedreality;

import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.util.List;

/**
 * Created by poliveira on 30/09/2014.
 */
public class RealityView extends RelativeLayout implements SensorListener.SensorCallback, GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks
{
    final String TAG = "AugmentedRealityLibrary";
    CameraSurface mCameraSurface;
    MarkerSurface mMarkerSurface;
    SensorListener mSensorListener;
    Parameters mParameters;
    SensorManager mSensorManager;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;

    public RealityView(Context context)
    {
        super(context);
        init();
    }

    public RealityView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initializeAttrs(attrs);
        init();
    }

    public RealityView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initializeAttrs(attrs);
        init();
    }

    private void initializeAttrs(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RealityView);
        mParameters = new Parameters();
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.RealityView_lowPassAlphaAccelerometer:
                    mParameters.setLowPassAlphaAccelerometer(a.getFloat(attr, 0));
                    break;
                case R.styleable.RealityView_lowPassAlphaMagnetic:
                    mParameters.setLowPassAlphaMagnetic(a.getFloat(attr, 0));
                    break;
                case R.styleable.RealityView_smoothListSize:
                    mParameters.setSmoothListSample(a.getInt(attr, 0));
                    break;
                case R.styleable.RealityView_markerResource:
                    mParameters.setMarkerResource(a.getResourceId(attr, R.drawable.defaultmarker));
                    break;
                case R.styleable.RealityView_markerMaxZoom:
                    mParameters.setMarkerMaxZoom(a.getFloat(attr, 0.3f));
                    break;
                case R.styleable.RealityView_markerMinZoom:
                    mParameters.setMarkerMinZoom(a.getFloat(attr, 2f));
                    break;
            }
        }
        a.recycle();
    }

    private void init()
    {
        mCameraSurface = new CameraSurface(getContext(), new CameraSurface.CameraCallback()
        {
            @Override
            public void onReady()
            {
                getParameters().setCameraAngleOfView(new double[]{mCameraSurface.getCamera().getParameters().getHorizontalViewAngle(), mCameraSurface.getCamera().getParameters().getVerticalViewAngle()});
                mSensorListener = new SensorListener(getParameters());
                mSensorListener.setSensorCallback(RealityView.this);
                mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
                List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                if (sensors.size() > 0)
                {
                    mSensorManager.registerListener(mSensorListener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                }

                sensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
                if (sensors.size() > 0)
                {
                    mSensorManager.registerListener(mSensorListener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                }
                Log.v(TAG, "added sensors");
            }
        });
        mCameraSurface.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mMarkerSurface = new MarkerSurface(getContext(), mParameters);
        mMarkerSurface.setLayoutParams (new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mMarkerSurface);
        addView(mCameraSurface);
        mLocationClient = new LocationClient(getContext(), this, this);
        mLocationClient.connect();
        Log.v(TAG, "view initialized");
    }

    public void pause()
    {
        mCameraSurface.pause();
        mSensorManager.unregisterListener(mSensorListener);
        mSensorManager = null;
        Log.v(TAG, "pause called");
    }

    public void resume()
    {
        mCameraSurface.resume(new CameraSurface.CameraCallback()
        {
            @Override
            public void onReady()
            {
                getParameters().setCameraAngleOfView(new double[]{mCameraSurface.getCamera().getParameters().getHorizontalViewAngle(), mCameraSurface.getCamera().getParameters().getVerticalViewAngle()});
                mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
                mSensorListener = new SensorListener(getParameters());
                mSensorListener.setSensorCallback(RealityView.this);
                List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                if (sensors.size() > 0)
                {
                    mSensorManager.registerListener(mSensorListener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                }

                sensors = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
                if (sensors.size() > 0)
                {
                    mSensorManager.registerListener(mSensorListener, sensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
                }
                Log.v(TAG, "added sensors");
            }
        });
        Log.v(TAG, "resume called");
    }

    @Override
    public void onSensorChanged(double leftAngle, double rightAngle,double bottomAngle, double topAngle)
    {
        mMarkerSurface.updateView(leftAngle, rightAngle,bottomAngle,topAngle, mCurrentLocation);
    }

    public Parameters getParameters()
    {
        return mParameters;
    }

    public void setParameters(Parameters parameters)
    {
        mParameters = parameters;
    }

    public void setAdapter(MarkerAdapter adapter)
    {
        mMarkerSurface.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mCurrentLocation = mLocationClient.getLastLocation();
    }

    @Override
    public void onDisconnected()
    {

    }
}
