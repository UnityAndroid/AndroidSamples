 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.IFrameAllocator;
 import com.positive.mbit.intel.domain.IOutput;
 import com.positive.mbit.intel.domain.Plugin;
 
 class PushDataCommandHandler
   implements ICommandHandler
 {
   protected IOutput output;
   protected Plugin plugin;
   private IFrameAllocator inputWithAllocator;
 
   public PushDataCommandHandler(IOutput output, Plugin plugin, IFrameAllocator frameAllocator)
   {
     this.output = output;
     this.plugin = plugin;
     this.inputWithAllocator = frameAllocator;
   }
 
   public void handle()
   {
     Frame frame = this.inputWithAllocator.findFreeFrame();
 
     if (frame == null)
     {
       restoreCommands();
       return;
     }
 
     this.output.pull(frame);
     this.plugin.push(frame);
     this.plugin.checkIfOutputQueueHasData();
   }
 
   private void restoreCommands() {
     this.output.getOutputCommandQueue().queue(Command.HasData, Integer.valueOf(this.plugin.getTrackId()));
     this.plugin.skipProcessing();
     this.plugin.getInputCommandQueue().queue(Command.NeedData, Integer.valueOf(this.plugin.getTrackId()));
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.PushDataCommandHandler
 * JD-Core Version:    0.6.1
 */