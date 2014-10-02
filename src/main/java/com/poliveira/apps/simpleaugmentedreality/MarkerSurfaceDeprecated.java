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
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by poliveira on 30/09/2014.
 */
public class MarkerSurfaceDeprecated extends RelativeLayout implements View.OnTouchListener
{
    private MarkerAdapter mAdapter;
    private List<Marker> mVisibleMarkers;
    private SurfaceView mSurfaceView;
    private Parameters mParameters;
    private Paint mPaint;
    private RelativeLayout mInfoWindowView;
    Bitmap mMarker;

    public MarkerSurfaceDeprecated(Context context)
    {
        super(context);
        init();
    }

    public MarkerSurfaceDeprecated(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MarkerSurfaceDeprecated(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        mPaint = new Paint();
        mSurfaceView = new SurfaceView(getContext());
        mSurfaceView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSurfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
       // mMarker = BitmapFactory.decodeResource(getContext().getResources(), mParameters.getMarkerResource());
        mSurfaceView.setOnTouchListener(this);
        mInfoWindowView = (RelativeLayout) getParent();
        addView(mSurfaceView);
    }

    public void setAdapter(MarkerAdapter adapter)
    {
        mAdapter = adapter;
    }

    public void updateView(double leftAngle, double rightAngle, double bottomAngle, double topAngle, Location currentLocation)
    {
        if (currentLocation == null)
            return;
        leftAngle = Math.min(leftAngle, rightAngle);
        rightAngle = Math.max(leftAngle, rightAngle);
        LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        List<Marker> temporary = new ArrayList<Marker>();

        for (Marker marker : mAdapter.mMarkers)
        {
            marker.setBearing(Utils.coordinateBearing(marker.getCoordinates(), currentLatLng));
            marker.setDistance(Utils.haversine(marker.getCoordinates().latitude, marker.getCoordinates().longitude, currentLatLng.latitude, currentLatLng.longitude) * 1000);
            Vector3 teste = Utils.convLocToVec(currentLatLng, marker.getCoordinates());

            if (marker.getBearing() > leftAngle && marker.getBearing() < rightAngle)
            {
                marker.setViewPosition(new double[]{(((100 - ((rightAngle - marker.getBearing()) * 100) / (rightAngle - leftAngle))) / 100f) * getWidth(), (((100 - ((topAngle - marker.getAltitude()) * 100) / (topAngle - bottomAngle))) / 100f) * getHeight()});
                temporary.add(marker);
            }
        }
        drawMarkers(temporary);
    }

    public void drawMarkers(List<Marker> visibleMarkers)
    {
        mVisibleMarkers = visibleMarkers;
        Canvas c = mSurfaceView.getHolder().lockCanvas();
        if (c != null)
        {
            c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
            for (Marker marker : visibleMarkers)
            {
                double zoomFactor = Utils.calculateZoomFactorFromDistance(mParameters, (float) marker.getDistance());
              //  Log.v("teste", "zoom  for distance " + marker.getDistance() + " -> " + zoomFactor);
                Rect src = new Rect(0, 0, mMarker.getWidth(), mMarker.getHeight());
                Rect dst = new Rect((int) marker.getViewPosition()[0] - mMarker.getWidth() / 2, (int) marker.getViewPosition()[1] - mMarker.getHeight() / 2, (int) marker.getViewPosition()[0] + (int) ((mMarker.getWidth()/2) * zoomFactor), (int) marker.getViewPosition()[1] + (int) ((mMarker.getHeight()/2) * zoomFactor));
                marker.setArea(dst);
                marker.setViewPosition(new double[]{(int) marker.getViewPosition()[0] - mMarker.getWidth() / 2, (int) marker.getViewPosition()[1] - mMarker.getHeight() / 2});
               // c.drawRect((int) marker.getViewPosition()[0] - mMarker.getWidth() / 2, (int) marker.getViewPosition()[1] - mMarker.getHeight() / 2,(int) marker.getViewPosition()[0] + (int) ((mMarker.getWidth()/2) * zoomFactor), (int) marker.getViewPosition()[1] + (int) ((mMarker.getHeight()/2) * zoomFactor),mPaint);
                c.drawBitmap(mMarker, src, dst, mPaint);
            }
            mSurfaceView.getHolder().unlockCanvasAndPost(c);
        }
    }

    public List<Marker> getVisibleMarkers()
    {
        return mVisibleMarkers;
    }

    public void setVisibleMarkers(List<Marker> visibleMarkers)
    {
        mVisibleMarkers = visibleMarkers;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(((RealityView)getParent().getParent()).getMarkerClickListener() ==null)
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            for (Marker m : mAdapter.mMarkers)
                if (m.isOnScreen((int)event.getX(), (int)event.getY()))
                    reportMarkerClick(m);
        return false;
    }

    private void reportMarkerClick(Marker m)
    {
        ((RealityView)getParent().getParent()).getMarkerClickListener().onMarkerTouch(m, mInfoWindowView);
    }

    public View getInfoWindowView()
    {
        return mInfoWindowView;
    }

    public void setInfoWindowView(int resource)
    {
        /*if(mInfoWindowView.getChildCount()<=0)
           LayoutInflater.from(getContext()).inflate(resource, mInfoWindowView, true);*/
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
}
