package com.poliveira.apps.simpleaugmentedreality;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.Surface;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by poliveira on 30/09/2014.
 */
public class Utils
{
    public static float calculateFilteredAngle(float x, float y)
    {
        final float alpha = 0.3f;
        float diff = x - y;
        diff = restrictAngle(diff);
        y += alpha * diff;
        y = restrictAngle(y);
        return y;
    }

    public static float restrictAngle(float tmpAngle)
    {
        while (tmpAngle >= 180) tmpAngle -= 360;
        while (tmpAngle < -180) tmpAngle += 360;
        return tmpAngle;
    }

    public static float[] lowPass(float[] input, float[] output, float alpha)
    {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++)
        {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }
    public static double coordinateBearing(LatLng startPoint, LatLng destinationPoint)
    {
        double dLong1InRad = startPoint.longitude * (Math.PI / 180.0);
        double dLong2InRad = destinationPoint.longitude * (Math.PI / 180.0);

        double dLat1InRad = startPoint.latitude * Math.PI / 180.0;
        double dLat2InRad = destinationPoint.latitude * Math.PI / 180.0;

        double dLongitude = dLong2InRad - dLong1InRad;
        double y = Math.sin(dLongitude) * Math.cos(dLat2InRad);
        double x = Math.cos(dLat1InRad) * Math.sin(dLat2InRad) - Math.sin(dLat1InRad) * Math.cos(dLat2InRad) * Math.cos(dLongitude);
        double dBearing = Math.atan2(y, x) * 180 / Math.PI;

        return (dBearing + 360) % 360;
    }
    public static void remapCoordinateSystem(Activity c, float[] input, float[] output)
    {
        int mScreenRotation = c.getWindowManager().getDefaultDisplay().getRotation();
        int axisX = SensorManager.AXIS_X;
        int axisY = SensorManager.AXIS_Y;
        switch (mScreenRotation)
        {
            case Surface.ROTATION_0:
                axisX = SensorManager.AXIS_X;
                axisY = SensorManager.AXIS_MINUS_Y;
                break;

            case Surface.ROTATION_90:
                axisX = SensorManager.AXIS_Y;
                axisY = SensorManager.AXIS_MINUS_X;
                break;

            case Surface.ROTATION_180:
                axisX = SensorManager.AXIS_MINUS_X;
                axisY = SensorManager.AXIS_MINUS_Y;
                break;

            case Surface.ROTATION_270:
                axisX = SensorManager.AXIS_MINUS_Y;
                axisY = SensorManager.AXIS_X;
                break;

            default:
                break;
        }
        SensorManager.remapCoordinateSystem(input, axisX, axisY, output);
    }
    public static int getCameraDisplayOrientation(Activity activity,
                                                  int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
    //transform an angle between -180 180 to 0 - 360
    public static double transformAngle(double _angle)
    {
        if (_angle < 0)
            return 360 - Math.abs(_angle);
        return _angle;
    }

    /**
     * Calculates the distance in km between two lat/long points
     * using the haversine formula
     */
    public static double haversine(
            double lat1, double lng1, double lat2, double lng2) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }
    public static Vector3 convLocToVec(LatLng source, LatLng destination) {
        float[] z = new float[1];
        z[0] = 0;
        Location.distanceBetween(source.latitude, source.longitude, destination
                .latitude, source.longitude, z);
        float[] x = new float[1];
        Location.distanceBetween(source.latitude, source.longitude, source
                .latitude, destination.longitude, x);
        if (source.latitude < destination.latitude)
            z[0] *= -1;
        if (source.longitude > destination.longitude)
            x[0] *= -1;

        return new Vector3(x[0], (float) 0, z[0]);
    }
    public static double calculateZoomFactorFromDistance(Parameters parameters, float distance)
    {
        float zoomPerMeter = (parameters.getMarkerMaxZoom()-parameters.getMarkerMinZoom())/parameters.getDistanceOfView();
        return parameters.getMarkerMaxZoom() -  Math.min(parameters.getMarkerMaxZoom(),Math.max(zoomPerMeter*distance+parameters.getMarkerMinZoom(),parameters.getMarkerMinZoom()))  + parameters.getMarkerMinZoom();
    }
}
