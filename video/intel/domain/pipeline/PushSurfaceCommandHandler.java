 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IPluginOutput;
 import com.positive.mbit.intel.domain.MediaCodecPlugin;
 
 class PushSurfaceCommandHandler
   implements ICommandHandler
 {
   protected final IPluginOutput output;
   protected final MediaCodecPlugin input;
 
   public PushSurfaceCommandHandler(IPluginOutput output, MediaCodecPlugin input)
   {
     this.output = output;
     this.input = input;
   }
 
   public void handle()
   {
     Frame frame = this.output.getFrame();
 
     if (!frame.equals(Frame.EOF())) {
       this.output.releaseOutputBuffer(frame.getBufferIndex());
     }
     if ((!frame.equals(Frame.EOF())) && (0 != frame.getLength())) {
       this.output.waitForSurface(frame.getSampleTime());
     }
 
     if ((!frame.equals(Frame.EOF())) && (0 != frame.getLength())) {
       this.input.notifySurfaceReady(this.output.getSurface());
     }
     this.input.push(frame);
     this.input.checkIfOutputQueueHasData();
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.PushSurfaceCommandHandler
 * JD-Core Version:    0.6.1
 */