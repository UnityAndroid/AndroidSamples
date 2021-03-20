 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IInput;
 
 class SkipOutputFormatChangeCommandHandler
   implements ICommandHandler
 {
   private IInput encoder;
 
   public SkipOutputFormatChangeCommandHandler(IInput encoder)
   {
     this.encoder = encoder;
   }
 
   public void handle()
   {
     this.encoder.push(Frame.empty());
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.SkipOutputFormatChangeCommandHandler
 * JD-Core Version:    0.6.1
 */