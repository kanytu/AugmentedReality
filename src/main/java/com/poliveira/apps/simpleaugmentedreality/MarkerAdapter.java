package com.poliveira.apps.simpleaugmentedreality;

import java.util.List;

/**
 * Created by poliveira on 30/09/2014.
 */
public class MarkerAdapter
{
    private List<Marker> mMarkers;

    public MarkerAdapter(List<Marker> markers)
    {
        mMarkers = markers;
    }

    public List<Marker> getMarkers()
    {
        return mMarkers.subList(0, mMarkers.size());
    }

    public void setMarkers(List<Marker> markers)
    {
        mMarkers = markers;
    }

    public void notifyDataSetChanged(List<Marker> data)
    {
        mMarkers = data;
    }
}
