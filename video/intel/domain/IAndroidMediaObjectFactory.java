package com.positive.mbit.intel.domain;

import com.positive.mbit.intel.AudioFormat;
import com.positive.mbit.intel.IProgressListener;
import com.positive.mbit.intel.StreamingParameters;
import com.positive.mbit.intel.Uri;
import com.positive.mbit.intel.domain.graphics.IEglUtil;
import java.io.FileDescriptor;
import java.io.IOException;

public abstract interface IAndroidMediaObjectFactory
{
  public abstract MediaSource createMediaSource(String paramString)
    throws IOException;

  public abstract MediaSource createMediaSource(FileDescriptor paramFileDescriptor)
    throws IOException;

  public abstract MediaSource createMediaSource(Uri paramUri)
    throws IOException;

  public abstract VideoDecoder createVideoDecoder(MediaFormat paramMediaFormat);

  public abstract VideoEncoder createVideoEncoder();

  public abstract Plugin createAudioDecoder();

  public abstract AudioEncoder createAudioEncoder(String paramString);

  public abstract Resampler createAudioResampler(AudioFormat paramAudioFormat);

  public abstract Render createSink(String paramString, IProgressListener paramIProgressListener, ProgressTracker paramProgressTracker)
    throws IOException;

  public abstract Render createSink(StreamingParameters paramStreamingParameters, IProgressListener paramIProgressListener, ProgressTracker paramProgressTracker);

  public abstract ICaptureSource createCaptureSource();

  public abstract MediaFormat createVideoFormat(String paramString, int paramInt1, int paramInt2);

  public abstract MediaFormat createAudioFormat(String paramString, int paramInt1, int paramInt2);

  public abstract VideoEffector createVideoEffector();

  public abstract IEffectorSurface createEffectorSurface();

  public abstract IPreview createPreviewRender(Object paramObject1, Object paramObject2);

  public abstract AudioEffector createAudioEffects();

  public abstract ICameraSource createCameraSource();

  public abstract IMicrophoneSource createMicrophoneSource();

  public abstract IAudioContentRecognition createAudioContentRecognition();

  public abstract IEglContext getCurrentEglContext();

  public abstract IEglUtil getEglUtil();

  public abstract IFrameBuffer createFrameBuffer();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IAndroidMediaObjectFactory
 * JD-Core Version:    0.6.1
 */