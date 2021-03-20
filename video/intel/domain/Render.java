 package com.positive.mbit.intel.domain;
 
 import com.positive.mbit.intel.domain.pipeline.IOnStopListener;
 
 public abstract class Render extends Input
 {
   protected IOnStopListener onStopListener;
 
   public abstract int getTrackIdByMediaFormat(MediaFormat paramMediaFormat);
 
   public abstract void start();
 
   public void addOnStopListener(IOnStopListener onStopListener)
   {
     this.onStopListener = onStopListener;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.Render
 * JD-Core Version:    0.6.1
 */