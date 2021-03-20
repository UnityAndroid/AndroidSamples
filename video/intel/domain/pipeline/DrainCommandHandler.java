 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.MediaCodecPlugin;
 
 class DrainCommandHandler
   implements ICommandHandler
 {
   protected final MediaCodecPlugin plugin;
 
   public DrainCommandHandler(MediaCodecPlugin plugin)
   {
     this.plugin = plugin;
   }
 
   public void handle()
   {
     this.plugin.drain(0);
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.DrainCommandHandler
 * JD-Core Version:    0.6.1
 */