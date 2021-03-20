 package com.positive.mbit.intel.android;
 
 import android.opengl.EGL14;
 import android.opengl.EGLContext;
 import com.positive.mbit.intel.domain.IEglContext;
 import com.positive.mbit.intel.domain.IWrapper;
 
 public class EGLContextWrapper
   implements IWrapper<EGLContext>, IEglContext
 {
   private EGLContext eglContext;
 
   public EGLContextWrapper(EGLContext eglContext)
   {
     this.eglContext = eglContext;
   }
 
   public EGLContext getNativeObject()
   {
     return this.eglContext;
   }
 
   public IEglContext getCurrentContext()
   {
     return new EGLContextWrapper(EGL14.eglGetCurrentContext());
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.EGLContextWrapper
 * JD-Core Version:    0.6.1
 */