 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.Render;
 
 public class DrainRenderCommandHandler
   implements ICommandHandler
 {
   protected final Render render;
 
   public DrainRenderCommandHandler(Render render)
   {
     this.render = render;
   }
 
   public void handle()
   {
     this.render.drain(0);
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.DrainRenderCommandHandler
 * JD-Core Version:    0.6.1
 */