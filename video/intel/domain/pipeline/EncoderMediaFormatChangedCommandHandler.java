 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.Plugin;
 import com.positive.mbit.intel.domain.Render;
 
 class EncoderMediaFormatChangedCommandHandler
   implements ICommandHandler
 {
   protected Plugin plugin;
   protected Render render;
 
   public EncoderMediaFormatChangedCommandHandler(Plugin plugin, Render render)
   {
     this.plugin = plugin;
     this.render = render;
   }
 
   public void handle()
   {
     this.render.setMediaFormat(this.plugin.getOutputMediaFormat());
     this.plugin.setOutputTrackId(this.render.getTrackIdByMediaFormat(this.plugin.getOutputMediaFormat()));
     this.render.start();
     this.plugin.checkIfOutputQueueHasData();
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.EncoderMediaFormatChangedCommandHandler
 * JD-Core Version:    0.6.1
 */