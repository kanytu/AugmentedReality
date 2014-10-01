package com.poliveira.apps.simpleaugmentedreality;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by poliveira on 30/09/2014.
 */
public class SensorListener implements SensorEventListener
{
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private Parameters mParameters;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mAngle;
    private float[] mI = new float[9];
    List<ArrayList<Float>> mGravitySmoother;
    List<ArrayList<Float>> mMagneticFieldSmoother;

    public SensorListener(Parameters parameters)
    {
        mParameters = parameters;
        mGravitySmoother = Arrays.asList(new ArrayList<Float>(), new ArrayList<Float>(), new ArrayList<Float>());
        mMagneticFieldSmoother = Arrays.asList(new ArrayList<Float>(), new ArrayList<Float>(), new ArrayList<Float>());
    }

    public interface SensorCallback
    {
        void onSensorChanged(double leftAngle, double rightAngle, double bottomAngle, double topAngle);
    }

    private SensorCallback mSensorCallback;

    @Override
    public synchronized void onSensorChanged(SensorEvent event)
    {
        switch (event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                mGravity = Utils.lowPass(smoothAccelerometer(event.values.clone()), mGravity, mParameters.getLowPassAlphaAccelerometer());
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mGeomagnetic = Utils.lowPass(smoothMagneticField(event.values.clone()), mGeomagnetic, mParameters.getLowPassAlphaMagnetic());
                break;
        }
        processSensor();
    }

    private float[] smoothMagneticField(float[] values)
    {
        if (mParameters.getSmoothListSample() <= 0)
            return values;
        if (mMagneticFieldSmoother.get(0).size() > mParameters.getSmoothListSample())
        {
            mMagneticFieldSmoother.get(0).remove(0);
            mMagneticFieldSmoother.get(1).remove(0);
            mMagneticFieldSmoother.get(2).remove(0);
        }

        mMagneticFieldSmoother.get(0).add(values[0]);
        mMagneticFieldSmoother.get(1).add(values[1]);
        mMagneticFieldSmoother.get(2).add(values[2]);

        float[] temp = new float[]{0,0,0};

        for(int i =0;i< mMagneticFieldSmoother.get(0).size();i++){
            temp[0] +=  mMagneticFieldSmoother.get(0).get(i);
            temp[1] +=  mMagneticFieldSmoother.get(1).get(i);
            temp[2] +=  mMagneticFieldSmoother.get(2).get(i);
        }
        temp[0]/=mParameters.getSmoothListSample();
        temp[1]/=mParameters.getSmoothListSample();
        temp[2]/=mParameters.getSmoothListSample();
        return temp;
    }
    private float[] smoothAccelerometer(float[] values)
    {
        if (mParameters.getSmoothListSample() <= 0)
            return values;
        if (mGravitySmoother.get(0).size() > mParameters.getSmoothListSample())
        {
            mGravitySmoother.get(0).remove(0);
            mGravitySmoother.get(1).remove(0);
            mGravitySmoother.get(2).remove(0);
        }

        mGravitySmoother.get(0).add(values[0]);
        mGravitySmoother.get(1).add(values[1]);
        mGravitySmoother.get(2).add(values[2]);

        float[] temp = new float[]{0,0,0};

        for(int i =0;i<mGravitySmoother.get(0).size();i++){
            temp[0] +=  mGravitySmoother.get(0).get(i);
            temp[1] +=  mGravitySmoother.get(1).get(i);
            temp[2] +=  mGravitySmoother.get(2).get(i);
        }
        temp[0]/=mParameters.getSmoothListSample();
        temp[1]/=mParameters.getSmoothListSample();
        temp[2]/=mParameters.getSmoothListSample();
        return temp;
    }

    private void processSensor()
    {
        if (mGravity != null && mGeomagnetic != null)
        {
            SensorManager.getRotationMatrix(mR, mI, mGravity, mGeomagnetic);
            SensorManager.getOrientation(mR, mOrientation);
            mAngle = Utils.calculateFilteredAngle((float) Math.toDegrees((double) mOrientation[0]) - 90, mAngle);
            double top = 90 - Math.toDegrees(Math.atan2(mGravity[0], mGravity[2]));
            if (mSensorCallback != null)
                mSensorCallback.onSensorChanged(Utils.transformAngle(mAngle) - mParameters.getCameraAngleOfView()[0] / 2, Utils.transformAngle(mAngle) + mParameters.getCameraAngleOfView()[0] / 2, top - mParameters.getCameraAngleOfView()[1] / 2, top + mParameters.getCameraAngleOfView()[1] / 2);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public SensorCallback getSensorCallback()
    {
        return mSensorCallback;
    }

    public void setSensorCallback(SensorCallback sensorCallback)
    {
        mSensorCallback = sensorCallback;
    }
}
