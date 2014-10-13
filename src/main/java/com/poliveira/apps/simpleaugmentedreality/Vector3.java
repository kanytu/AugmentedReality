package com.poliveira.apps.simpleaugmentedreality;

/**
 * Created by poliveira on 01/10/2014.
 */
public class Vector3
{
    private float x;
    private float y;
    private float z;

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(float[] values){
        if(values.length!=3) throw new RuntimeException("Invalid array size");
        x=values[0];
        y=values[1];
        z=values[2];
    }
    public float[] getValues(){
        return new float[]{x,y,z};
    }
    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "X = " + x + " | Y =" + y + " | Z =" + z;
    }
}
