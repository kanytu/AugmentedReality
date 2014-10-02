package com.poliveira.apps.simpleaugmentedreality;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by poliveira on 30/09/2014.
 */
public class CameraSurface extends SurfaceView implements SurfaceHolder.Callback
{
    public interface CameraCallback
    {
        void onReady();
    }

    Camera mCamera;
    CameraCallback mCameraCallback;

    public Camera getCamera()
    {
        return mCamera;
    }

    public CameraSurface(Context context, CameraCallback cameraCallback)
    {
        super(context);
        mCameraCallback = cameraCallback;
        init();
    }

    public CameraSurface(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CameraSurface(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            if (mCamera != null)
            {
                try
                {
                    mCamera.stopPreview();
                } catch (Exception ignore)
                {
                }
                try
                {
                    mCamera.release();
                } catch (Exception ignore)
                {
                }
                mCamera = null;
            }

            mCamera = Camera.open(0);
            mCamera.setPreviewDisplay(holder);
            if (mCameraCallback != null)
                mCameraCallback.onReady();
        } catch (Exception ex)
        {
            try
            {
                if (mCamera != null)
                {
                    try
                    {
                        mCamera.stopPreview();
                    } catch (Exception ignore)
                    {
                    }
                    try
                    {
                        mCamera.release();
                    } catch (Exception ignore)
                    {
                    }
                    mCamera = null;
                }
            } catch (Exception ignore)
            {

            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        try
        {
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            Camera.Size selected = sizes.get(0);
            params.setPreviewSize(selected.width, selected.height);
            mCamera.setParameters(params);
            mCamera.startPreview();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        try
        {
            if (mCamera != null)
            {
                try
                {
                    mCamera.stopPreview();
                } catch (Exception ignore)
                {
                }
                try
                {
                    mCamera.release();
                } catch (Exception ignore)
                {
                }
                mCamera = null;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void pause()
    {
        if (mCamera == null)
            return;
        mCamera.stopPreview();
        mCamera.release();
    }

    public void resume(CameraCallback cameraCallback)
    {
        if (mCamera == null)
            return;
        try
        {
            mCamera.reconnect();
            mCamera.startPreview();
            cameraCallback.onReady();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public CameraCallback getCameraCallback()
    {
        return mCameraCallback;
    }

    public void setCameraCallback(CameraCallback cameraCallback)
    {
        mCameraCallback = cameraCallback;
    }
}
