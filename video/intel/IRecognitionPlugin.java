 package com.positive.mbit.intel;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.MediaFormat;
 
 public abstract interface IRecognitionPlugin
 {
   public abstract void start();
 
   public abstract void stop();
 
   public abstract RecognitionOutput recognize(RecognitionInput paramRecognitionInput);
 
   public static abstract interface RecognitionEvent
   {
     public abstract void onContentRecognized(IRecognitionPlugin paramIRecognitionPlugin, IRecognitionPlugin.RecognitionOutput paramRecognitionOutput);
   }
 
   public static class RecognitionInput
   {
     private MediaFormat mediaFormat;
     private Frame frame;
 
     public void setMediaFormat(MediaFormat mediaFormat)
     {
       this.mediaFormat = mediaFormat;
     }
 
     public MediaFormat getMediaFormat()
     {
       return this.mediaFormat;
     }
 
     public void setFrame(Frame frame)
     {
       this.frame = frame;
     }
 
     public Frame getFrame()
     {
       return this.frame;
     }
   }
 
   public static class RecognitionOutput
   {
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.IRecognitionPlugin
 * JD-Core Version:    0.6.1
 */