package com.poliveira.apps.simpleaugmentedreality;

/**
 * Created by poliveira on 08/10/2014.
 */
public class Matrix
{
    public float[][] values;

    public Matrix(float[] rotationMatrix)
    {
        try
        {
            setValues(rotationMatrix);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int[] getMatrizSize(){
        return new int[]{values.length,values[0].length};
    }
    public void setValues(float[] _val) throws Exception
    {
        double size = Math.sqrt(_val.length);
        if(size != (int) size ) throw new RuntimeException("Illegal matrix dimensions.");
        int s=(int) size;
        values = new float[s][s];
        for(int i = 0;i<s;i++)
            System.arraycopy(_val, (i * s), values[i], 0, s);
    }

    public Vector3 multiplyVector(Vector3 x)
    {
        int[] matrizSizes = getMatrizSize();
        if (3 != matrizSizes[1]) throw new RuntimeException("Illegal matrix dimensions.");
        float[] y = new float[matrizSizes[0]];
        for (int i = 0; i < matrizSizes[0]; i++)
            for (int j = 0; j < matrizSizes[1]; j++)
                y[i] += (values[i][j] * x.getValues()[j]);
        return new Vector3(y);
    }
}
