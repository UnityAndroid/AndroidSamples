 package com.positive.mbit.intel.android;
 
 import com.positive.mbit.intel.domain.CaptureSource;
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.ISurface;
 import com.positive.mbit.intel.domain.ISurfaceListener;
 
 public class GameCapturerSource extends CaptureSource
 {
   ISurface renderingSurface = null;
   private boolean swapBuffers = true;
   private EglContextSwitcher contextSwitcher;
   private ISurfaceListener listener;
 
   public GameCapturerSource()
   {
     this.contextSwitcher = new EglContextSwitcher();
   }
 
   public void addSetSurfaceListener(ISurfaceListener listenMe)
   {
     this.listener = listenMe;
   }
 
   public ISurface getSurface()
   {
     return this.renderingSurface;
   }
 
   public void setSurfaceSize(int width, int height)
   {
     this.contextSwitcher.init(width, height);
     this.contextSwitcher.saveEglState();
 
     if (this.listener != null) {
       this.listener.onSurfaceAvailable(null);
     }
 
     this.renderingSurface.makeCurrent();
     this.contextSwitcher.restoreEglState();
   }
 
   public void beginCaptureFrame()
   {
     if (this.renderingSurface == null) {
       return;
     }
 
     this.contextSwitcher.saveEglState();
 
     this.renderingSurface.makeCurrent();
     this.renderingSurface.setProjectionMatrix(this.contextSwitcher.getProjectionMatrix());
     this.renderingSurface.setViewport();
   }
 
   public void endCaptureFrame()
   {
     super.endCaptureFrame();
 
     long presentationTimeUs = System.nanoTime() - this.startTime;
     this.renderingSurface.setPresentationTime(presentationTimeUs);
 
     if (this.swapBuffers) {
       this.renderingSurface.swapBuffers();
     }
 
     this.contextSwitcher.restoreEglState();
     this.commandQueue.queue(Command.HasData, Integer.valueOf(0));
   }
 
   public void setOutputSurface(ISurface surface)
   {
     this.renderingSurface = surface;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.GameCapturerSource
 * JD-Core Version:    0.6.1
 */