package com.poliveira.apps.simpleaugmentedreality;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by poliveira on 02/10/2014.
 */
public class InfoWindowView extends RelativeLayout
{
    private View mView;

    public InfoWindowView(Context context)
    {
        super(context);
        init();
    }

    public InfoWindowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public InfoWindowView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
    }

    public void showInfoWindow()
    {
        mView.setVisibility(VISIBLE);
    }

    public boolean isInfoWindowVisible()
    {
        return mView.getVisibility() == VISIBLE;
    }

    public void hideInfoWindow()
    {
        mView.setVisibility(INVISIBLE);
    }

    public void setInfoView(int resource)
    {
        mView = LayoutInflater.from(getContext()).inflate(resource, this, false);
        mView.setVisibility(INVISIBLE);
        addView(mView);
    }
}
