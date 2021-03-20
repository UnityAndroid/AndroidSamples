 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IOutput;
 import com.positive.mbit.intel.domain.Render;
 import java.nio.ByteBuffer;
 
 class PushNewDataCommandHandler
   implements ICommandHandler
 {
   private IOutput output;
   private Render render;
 
   public PushNewDataCommandHandler(IOutput output, Render render)
   {
     this.output = output;
     this.render = render;
   }
 
   public void handle()
   {
     Frame frame = new Frame(ByteBuffer.allocate(1048576), 1048576, 0L, 0, 0, 0);
     this.output.pull(frame);
     this.render.push(frame);
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.PushNewDataCommandHandler
 * JD-Core Version:    0.6.1
 */