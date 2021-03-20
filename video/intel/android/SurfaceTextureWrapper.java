 package com.positive.mbit.intel.android;
 
 import android.graphics.SurfaceTexture;
 import com.positive.mbit.intel.domain.ISurfaceTexture;
 import com.positive.mbit.intel.domain.Wrapper;
 
 public class SurfaceTextureWrapper extends Wrapper<SurfaceTexture>
   implements ISurfaceTexture
 {
   public SurfaceTextureWrapper(SurfaceTexture surfaceTexture)
   {
     super(surfaceTexture);
   }
 
   public float[] getTransformMatrix()
   {
     float[] transformMatrix = new float[16];
     ((SurfaceTexture)getNativeObject()).getTransformMatrix(transformMatrix);
     return transformMatrix;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.SurfaceTextureWrapper
 * JD-Core Version:    0.6.1
 */