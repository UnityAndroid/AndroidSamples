 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IPluginOutput;
 import com.positive.mbit.intel.domain.Render;
 
 class PullDataCommandHandler
   implements ICommandHandler
 {
   protected Render input;
   protected IPluginOutput output;
 
   public PullDataCommandHandler(IPluginOutput output, Render input)
   {
     this.input = input;
     this.output = output;
   }
 
   public void handle()
   {
     Frame frame = this.output.getFrame();
 
     if (Frame.EOF().equals(frame)) {
       this.input.drain(frame.getBufferIndex());
     } else {
       this.input.push(frame);
       this.output.releaseOutputBuffer(frame.getBufferIndex());
     }
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.PullDataCommandHandler
 * JD-Core Version:    0.6.1
 */