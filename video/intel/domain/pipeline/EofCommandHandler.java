 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IFrameAllocator;
 import com.positive.mbit.intel.domain.IOutput;
 import com.positive.mbit.intel.domain.Plugin;
 
 class EofCommandHandler
   implements ICommandHandler
 {
   protected IOutput output;
   protected Plugin plugin;
   private IFrameAllocator inputWithAllocator;
 
   public EofCommandHandler(IOutput output, Plugin plugin, IFrameAllocator frameAllocator)
   {
     this.output = output;
     this.plugin = plugin;
     this.inputWithAllocator = frameAllocator;
   }
 
   public void handle()
   {
     Frame frame = this.inputWithAllocator.findFreeFrame();
 
     if (frame != null)
       this.plugin.drain(frame.getBufferIndex());
     else
       handleNoFreeInputBuffer();
   }
 
   private void handleNoFreeInputBuffer()
   {
     this.output.getOutputCommandQueue().queue(Command.EndOfFile, Integer.valueOf(this.plugin.getTrackId()));
     this.plugin.skipProcessing();
     this.plugin.getInputCommandQueue().queue(Command.NeedData, Integer.valueOf(this.plugin.getTrackId()));
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.EofCommandHandler
 * JD-Core Version:    0.6.1
 */