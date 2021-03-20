 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.AudioEncoder;
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IMicrophoneSource;
 
 class MicrophoneSourcePullFrameCommandHandler
   implements ICommandHandler
 {
   private IMicrophoneSource source;
   private AudioEncoder encoder;
 
   public MicrophoneSourcePullFrameCommandHandler(IMicrophoneSource source, AudioEncoder encoder)
   {
     this.source = source;
     this.encoder = encoder;
   }
 
   public void handle()
   {
     Frame encoderFrame = this.encoder.findFreeFrame();
     if (encoderFrame == null) {
       handleNoFreeInputBuffer();
       return;
     }
 
     this.source.getOutputCommandQueue().queue(Command.NextPair, Integer.valueOf(this.encoder.getTrackId()));
 
     this.source.pull(encoderFrame);
     this.encoder.push(encoderFrame);
 
     if (!encoderFrame.equals(Frame.EOF()))
       this.encoder.checkIfOutputQueueHasData();
   }
 
   private void handleNoFreeInputBuffer()
   {
     this.source.getOutputCommandQueue().queue(Command.HasData, Integer.valueOf(this.encoder.getTrackId()));
     this.encoder.skipProcessing();
     this.encoder.getInputCommandQueue().queue(Command.NeedData, Integer.valueOf(this.encoder.getTrackId()));
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.MicrophoneSourcePullFrameCommandHandler
 * JD-Core Version:    0.6.1
 */