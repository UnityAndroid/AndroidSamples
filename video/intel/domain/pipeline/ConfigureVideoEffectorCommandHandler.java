 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IVideoOutput;
 import com.positive.mbit.intel.domain.Plugin;
 
 class ConfigureVideoEffectorCommandHandler
   implements ICommandHandler
 {
   private final IVideoOutput output;
   private final Plugin input;
 
   public ConfigureVideoEffectorCommandHandler(IVideoOutput output, Plugin decoder)
   {
     this.output = output;
     this.input = decoder;
   }
 
   public void handle()
   {
     this.output.getOutputCommandQueue().queue(Command.OutputFormatChanged, Integer.valueOf(this.input.getTrackId()));
     this.input.getInputCommandQueue().queue(Command.NeedData, Integer.valueOf(this.input.getTrackId()));
 
     this.input.setInputResolution(this.output.getOutputResolution());
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.ConfigureVideoEffectorCommandHandler
 * JD-Core Version:    0.6.1
 */