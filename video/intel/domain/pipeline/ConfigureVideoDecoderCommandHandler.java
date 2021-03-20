 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IVideoOutput;
 import com.positive.mbit.intel.domain.Plugin;
 
 class ConfigureVideoDecoderCommandHandler
   implements ICommandHandler
 {
   protected final IVideoOutput output;
   private final Plugin input;
 
   public ConfigureVideoDecoderCommandHandler(IVideoOutput output, Plugin decoder)
   {
     this.output = output;
     this.input = decoder;
   }
 
   public void handle()
   {
     this.output.getOutputCommandQueue().queue(Command.HasData, Integer.valueOf(this.input.getTrackId()));
     this.input.setInputResolution(this.output.getOutputResolution());
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.ConfigureVideoDecoderCommandHandler
 * JD-Core Version:    0.6.1
 */