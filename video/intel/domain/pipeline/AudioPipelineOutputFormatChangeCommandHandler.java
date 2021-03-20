 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.domain.Frame;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.MediaCodecPlugin;
 import com.positive.mbit.intel.domain.MediaFormat;
 
 class AudioPipelineOutputFormatChangeCommandHandler
   implements ICommandHandler
 {
   private MediaCodecPlugin output;
   private MediaCodecPlugin input;
 
   public AudioPipelineOutputFormatChangeCommandHandler(MediaCodecPlugin output, MediaCodecPlugin input)
   {
     this.output = output;
     this.input = input;
   }
 
   public void handle()
   {
     MediaFormat decoderMediaFormat = this.output.getOutputMediaFormat();
     this.input.setInputMediaFormat(decoderMediaFormat);
     this.input.push(Frame.empty());
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.AudioPipelineOutputFormatChangeCommandHandler
 * JD-Core Version:    0.6.1
 */