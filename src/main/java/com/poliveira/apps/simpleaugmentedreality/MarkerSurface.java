package com.poliveira.apps.simpleaugmentedreality;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.location.Location;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poliveira on 02/10/2014.
 */
public class MarkerSurface extends SurfaceView implements View.OnTouchListener
{
    private Parameters mParameters;
    private MarkerAdapter mAdapter;
    private Paint mPaint;
    private List<Marker> mVisibleMarkers;
    private Bitmap mMarker;

    public MarkerSurface(Context context)
    {
        super(context);
        init();
    }

    public MarkerSurface(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MarkerSurface(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        mPaint = new Paint();
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        mMarker = BitmapFactory.decodeResource(getResources(), R.drawable.defaultmarker);
        // mMarker = BitmapFactory.decodeResource(getContext().getResources(), mParameters.getMarkerResource());
        setOnTouchListener(this);
    }

    public Parameters getParameters()
    {
        return mParameters;
    }

    public void setParameters(Parameters parameters)
    {
        mParameters = parameters;
        mMarker = BitmapFactory.decodeResource(getContext().getResources(), mParameters.getMarkerResource());
    }

    public void updateView(float[] rotationMatrix, Location currentLocation)
    {
        if (currentLocation == null)
            return;
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        List<Marker> temporary = new ArrayList<Marker>();

        for (Marker marker : mAdapter.getMarkers())
        {
            marker.projectCoordinate(currentLatLng);
            marker.setProjectedCoordinate(new Matrix(rotationMatrix).multiplyVector(marker.getProjectedCoordinate()));
            marker.toFieldOfView(mParameters.getCameraAngleOfView()[0],mParameters.getCameraAngleOfView()[1],getHeight(),getWidth());


            float[] f = new float[3];
            Location.distanceBetween(marker.getCoordinates().latitude, marker.getCoordinates().longitude, currentLatLng.latitude, currentLatLng.longitude,f);
            marker.setDistance(f[0]);


           //marker.setViewPosition(new double[]{(((100 - ((rightAngle - marker.getBearing()) * 100) / (rightAngle - leftAngle))) / 100f) * getWidth(), (((100 - ((topAngle - marker.getAltitude()) * 100) / (topAngle - bottomAngle))) / 100f) * getHeight()});
           temporary.add(marker);
        }
        drawMarkers(temporary);
    }

    public void drawMarkers(List<Marker> visibleMarkers)
    {
        mVisibleMarkers = visibleMarkers;
        Canvas c = getHolder().lockCanvas();
        if (c != null)
        {
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
            for (Marker marker : visibleMarkers)
            {
                double zoomFactor = Utils.calculateZoomFactorFromDistance(mParameters, (float) marker.getDistance());
                //  Log.v("teste", "zoom  for distance " + marker.getDistance() + " -> " + zoomFactor);
                Rect src = new Rect(0, 0, mMarker.getWidth(), mMarker.getHeight());
                Rect dst = new Rect((int) marker.getViewPosition().getX() - mMarker.getWidth() / 2, (int) marker.getViewPosition().getY() - mMarker.getHeight() / 2, (int)marker.getViewPosition().getX() + (int) ((mMarker.getWidth() / 2) * zoomFactor), (int) marker.getViewPosition().getY() + (int) ((mMarker.getHeight() / 2) * zoomFactor));
                marker.setArea(dst);
                //marker.setViewPosition(new double[]{(int) marker.getViewPosition().getX() - mMarker.getWidth() / 2, (int) marker.getViewPosition().getY() - mMarker.getHeight() / 2});
                // c.drawRect((int) marker.getViewPosition()[0] - mMarker.getWidth() / 2, (int) marker.getViewPosition()[1] - mMarker.getHeight() / 2,(int) marker.getViewPosition()[0] + (int) ((mMarker.getWidth()/2) * zoomFactor), (int) marker.getViewPosition()[1] + (int) ((mMarker.getHeight()/2) * zoomFactor),mPaint);
                c.drawBitmap(mMarker, src, dst, mPaint);
                Log.v("teste", "marker drawn at " +  marker.getViewPosition().getX() +  "     " + marker.getViewPosition().getY());
            }
            getHolder().unlockCanvasAndPost(c);
        }
    }

    public MarkerAdapter getAdapter()
    {
        return mAdapter;
    }

    public void setAdapter(MarkerAdapter adapter)
    {
        mAdapter = adapter;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        boolean flag = false;
        if (((RealityView) getParent()).getMarkerClickListener() == null)
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            for (Marker m : mAdapter.getMarkers())
                if (m.isOnScreen((int) event.getX(), (int) event.getY()))
                {
                    flag = true;
                    reportMarkerTouch(m);
                }
        if(!flag)
            reportSpaceTouch();
        return false;
    }

    private void reportSpaceTouch()
    {
        ((RealityView) getParent()).onSpaceTouch();
    }

    private void reportMarkerTouch(Marker m)
    {
        ((RealityView) getParent()).onMarkerTouch(m);
    }

    public List<Marker> getVisibleMarkers()
    {
        return mVisibleMarkers;
    }
}
