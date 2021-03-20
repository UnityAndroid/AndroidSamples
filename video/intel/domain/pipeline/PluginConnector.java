 package com.positive.mbit.intel.domain.pipeline;
 
 import com.positive.mbit.intel.AudioFormat;
 import com.positive.mbit.intel.domain.AudioDecoder;
 import com.positive.mbit.intel.domain.AudioEffector;
 import com.positive.mbit.intel.domain.AudioEncoder;
 import com.positive.mbit.intel.domain.Command;
 import com.positive.mbit.intel.domain.CommandHandlerFactory;
 import com.positive.mbit.intel.domain.CommandQueue;
 import com.positive.mbit.intel.domain.Encoder;
 import com.positive.mbit.intel.domain.ICameraSource;
 import com.positive.mbit.intel.domain.ICaptureSource;
 import com.positive.mbit.intel.domain.ICommandHandler;
 import com.positive.mbit.intel.domain.ICommandProcessor;
 import com.positive.mbit.intel.domain.IEglContext;
 import com.positive.mbit.intel.domain.IFrameAllocator;
 import com.positive.mbit.intel.domain.IHandlerCreator;
 import com.positive.mbit.intel.domain.IMediaSource;
 import com.positive.mbit.intel.domain.IMicrophoneSource;
 import com.positive.mbit.intel.domain.IOnSurfaceReady;
 import com.positive.mbit.intel.domain.IOutput;
 import com.positive.mbit.intel.domain.IPluginOutput;
 import com.positive.mbit.intel.domain.ISurface;
 import com.positive.mbit.intel.domain.ISurfaceListener;
 import com.positive.mbit.intel.domain.IVideoOutput;
 import com.positive.mbit.intel.domain.MediaCodecPlugin;
 import com.positive.mbit.intel.domain.MediaFormatType;
 import com.positive.mbit.intel.domain.OutputInputPair;
 import com.positive.mbit.intel.domain.Pair;
 import com.positive.mbit.intel.domain.PassThroughPlugin;
 import com.positive.mbit.intel.domain.Plugin;
 import com.positive.mbit.intel.domain.Render;
 import com.positive.mbit.intel.domain.SurfaceRender;
 import com.positive.mbit.intel.domain.VideoDecoder;
 import com.positive.mbit.intel.domain.VideoEffector;
 import com.positive.mbit.intel.domain.VideoEncoder;
 
 class PluginConnector
   implements IConnector
 {
   private final ICommandProcessor commandProcessor;
 
   public PluginConnector(ICommandProcessor commandProcessor)
   {
     this.commandProcessor = commandProcessor;
   }
 
   public void connect(IMediaSource mediaSource, Plugin decoder) {
     int trackId = mediaSource.getTrackIdByMediaType(decoder.getMediaFormatType());
     mediaSource.selectTrack(trackId);
     decoder.setTrackId(trackId);
 
     configureCommandProcessorPush(mediaSource, mediaSource, decoder);
   }
 
   public void connect(VideoDecoder decoder, VideoEncoder videoEncoder) {
     configureCommandProcessorPushSurfaceDecoderEncoder(decoder, videoEncoder);
 
     videoEncoder.configure();
     ISurface surface = videoEncoder.getSurface();
     decoder.setOutputSurface(surface);
     videoEncoder.start();
 
     decoder.configure();
     decoder.start();
   }
 
   public void connect(AudioDecoder decoder, AudioEncoder encoder, AudioFormat mediaFormat)
   {
     if (mediaFormat == null) {
       throw new UnsupportedOperationException("Audio format not specified.");
     }
 
     configureAudioPipelineCommandProcessorCopy(decoder, encoder);
 
     decoder.configure();
     decoder.start();
 
     encoder.configure();
     encoder.start();
   }
 
   public void connect(final VideoEffector effector, final VideoEncoder encoder) {
     configureCommandProcessorPushSurfaceEffector(effector, encoder);
 
     effector.onSurfaceAvailable(new ISurfaceListener()
     {
       public void onSurfaceAvailable(IEglContext eglContext) {
         encoder.configure();
 
         ISurface surface = encoder.getSimpleSurface(eglContext);
         effector.setOutputSurface(surface);
 
         effector.configure();
         effector.start();
         encoder.start();
       }
     });
   }
 
   public void connect(VideoDecoder decoder, VideoEffector effector) {
     configureCommandProcessorPushSurfaceEffector(decoder, decoder, effector);
 
     ISurface surface = effector.getSurface();
     decoder.setOutputSurface(surface);
 
     decoder.configure();
     decoder.start();
   }
 
   public void connect(AudioEffector effector, AudioEncoder audioEncoder, AudioFormat mediaFormat)
   {
     if (mediaFormat == null) {
       throw new UnsupportedOperationException("Audio format not specified.");
     }
 
     configureAudioPipelineCommandProcessorCopy(effector, audioEncoder);
 
     audioEncoder.configure();
     audioEncoder.start();
 
     effector.configure();
     effector.start();
   }
 
   public void connect(AudioDecoder decoder, AudioEffector effector) {
     configureAudioPipelineCommandProcessorCopy(decoder, effector);
 
     decoder.configure();
     decoder.start();
   }
 
   private void configureCommandProcessorPush(final IOutput mediaSource, final IVideoOutput videoOutput, final Plugin decoder)
   {
     if ((decoder instanceof IFrameAllocator)) {
       CommandHandlerFactory factory = new CommandHandlerFactory();
       factory.register(new Pair(Command.HasData, Integer.valueOf(decoder.getTrackId())), new Pair(Command.NeedData, Integer.valueOf(decoder.getTrackId())), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new PushDataCommandHandler(mediaSource, decoder, (IFrameAllocator)decoder);
         }
       });
       factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(decoder.getTrackId())), new Pair(Command.NeedData, Integer.valueOf(decoder.getTrackId())), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new OutputFormatChangedHandler(mediaSource, decoder, (IFrameAllocator)decoder);
         }
       });
       factory.register(new Pair(Command.EndOfFile, Integer.valueOf(decoder.getTrackId())), new Pair(Command.NeedData, Integer.valueOf(decoder.getTrackId())), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new EofCommandHandler(mediaSource, decoder, (IFrameAllocator)decoder);
         }
       });
       factory.register(new Pair(Command.EndOfFile, Integer.valueOf(decoder.getTrackId())), new Pair(Command.NeedInputFormat, Integer.valueOf(decoder.getTrackId())), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new EofCommandHandler(mediaSource, decoder, (IFrameAllocator)decoder);
         }
       });
       factory.register(new Pair(Command.HasData, Integer.valueOf(decoder.getTrackId())), new Pair(Command.NeedInputFormat, Integer.valueOf(decoder.getTrackId())), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new ConfigureVideoDecoderCommandHandler(videoOutput, decoder);
         }
       });
       this.commandProcessor.add(new OutputInputPair(mediaSource, decoder, factory));
     }
     decoder.setMediaFormat(mediaSource.getMediaFormatByType(decoder.getMediaFormatType()));
     mediaSource.incrementConnectedPluginsCount();
   }
 
   private void configureCommandProcessorPushSurfaceDecoderEncoder(final IPluginOutput decoder, final MediaCodecPlugin encoder) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PushSurfaceCommandHandler(decoder, encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(decoder, encoder, factory));
   }
 
   private void configureCommandProcessorPushSurfaceSurfaceRender(final IPluginOutput decoder, final SurfaceRender render) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PushSurfaceCommandHandlerForSurfaceRender(decoder, render);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(render);
       }
     });
     this.commandProcessor.add(new OutputInputPair(decoder, render, factory));
   }
 
   private void configureCommandProcessorPushSurfaceEffector(final IPluginOutput decoder, final MediaCodecPlugin encoder)
   {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PushSurfaceCommandHandlerForEffector(decoder, encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(decoder, encoder, factory));
   }
 
   private void configureCommandProcessorPushSurfaceEffector(final IPluginOutput decoder, final IVideoOutput videoOutput, final MediaCodecPlugin encoder) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PushSurfaceCommandHandlerForEffector(decoder, encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new ConfigureVideoEffectorCommandHandler(videoOutput, encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(decoder, encoder, factory));
   }
 
   private void configureAudioPipelineCommandProcessorCopy(final MediaCodecPlugin decoder, final MediaCodecPlugin encoder) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new CopyDataCommandHandler(decoder, encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new AudioPipelineOutputFormatChangeCommandHandler(decoder, encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(decoder, encoder, factory));
   }
 
   public void connect(final ICaptureSource source, final Encoder encoder) {
     configureCommandProcessorPushSurfaceDecoderEncoder(source, encoder);
 
     source.addSetSurfaceListener(new ISurfaceListener()
     {
       public void onSurfaceAvailable(IEglContext eglContext) {
         encoder.configure();
         ISurface surface = encoder.getSurface();
         source.setOutputSurface(surface);
         encoder.start();
       }
     });
   }
 
   public void connect(final VideoDecoder decoder, final SurfaceRender render) {
     configureCommandProcessorPushSurfaceSurfaceRender(decoder, render);
 
     render.onSurfaceAvailable(new IOnSurfaceReady()
     {
       public void onSurfaceReady() {
         decoder.setOutputSurface(render.getSurface());
         decoder.configure();
         decoder.start();
       }
     });
   }
 
   public void connect(final IPluginOutput plugin, final Render render) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     if (((plugin instanceof Encoder)) || ((plugin instanceof PassThroughPlugin))) {
       factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
       {
         public ICommandHandler create() {
           return new EncoderMediaFormatChangedCommandHandler((Plugin)plugin, render);
         }
       });
     }
 
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PullDataCommandHandler(plugin, render);
       }
     });
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PullDataCommandHandler(plugin, render);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainRenderCommandHandler(render);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainRenderCommandHandler(render);
       }
     });
     this.commandProcessor.add(new OutputInputPair(plugin, render, factory));
     render.configure();
   }
 
   public void connect(final IMediaSource source, final Render render) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
 
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new PushNewDataCommandHandler(source, render);
       }
     });
     this.commandProcessor.add(new OutputInputPair(source, render, factory));
     render.setMediaFormat(source.getMediaFormatByType(MediaFormatType.VIDEO));
 
     render.configure();
     render.getInputCommandQueue().clear();
 
     render.start();
 
     int trackId = source.getTrackIdByMediaType(MediaFormatType.VIDEO);
     source.selectTrack(trackId);
   }
 
   public void connect(final ICameraSource source, final Encoder encoder) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new CaptureSourcePullSurfaceCommandHandler(source, encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(source, encoder, factory));
 
     encoder.configure();
     source.setOutputSurface(encoder.getSurface());
     encoder.start();
     source.configure();
   }
 
   public void connect(final IMicrophoneSource source, final AudioEncoder encoder) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new MicrophoneSourcePullFrameCommandHandler(source, encoder);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(encoder);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(encoder);
       }
     });
     this.commandProcessor.add(new OutputInputPair(source, encoder, factory));
 
     encoder.configure();
     encoder.start();
   }
 
   public void connect(final ICameraSource camera, final VideoEffector effector) {
     CommandHandlerFactory factory = new CommandHandlerFactory();
     factory.register(new Pair(Command.HasData, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new CaptureSourcePullSurfaceCommandHandler(camera, effector);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new SkipOutputFormatChangeCommandHandler(effector);
       }
     });
     factory.register(new Pair(Command.OutputFormatChanged, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new ConfigureVideoEffectorCommandHandler(camera, effector);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedData, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(effector);
       }
     });
     factory.register(new Pair(Command.EndOfFile, Integer.valueOf(0)), new Pair(Command.NeedInputFormat, Integer.valueOf(0)), new IHandlerCreator()
     {
       public ICommandHandler create() {
         return new DrainCommandHandler(effector);
       }
     });
     this.commandProcessor.add(new OutputInputPair(camera, effector, factory));
 
     effector.onSurfaceAvailable(new ISurfaceListener()
     {
       public void onSurfaceAvailable(IEglContext eglContext) {
         camera.setPreview(effector.getPreview());
         camera.setOutputSurface(effector.getSurface());
         camera.configure();
       }
     });
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.pipeline.PluginConnector
 * JD-Core Version:    0.6.1
 */