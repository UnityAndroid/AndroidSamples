package com.positive.mbit.intel.domain;

public abstract interface IPluginOutput extends IOutput, IReadyFrameProvider, ISurfaceProvider
{
  public abstract void setOutputSurface(ISurface paramISurface);

  public abstract MediaFormatType getMediaFormatType();

  public abstract MediaFormat getOutputMediaFormat();

  public abstract void setTrackId(int paramInt);

  public abstract void setOutputTrackId(int paramInt);

  public abstract void releaseOutputBuffer(int paramInt);
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IPluginOutput
 * JD-Core Version:    0.6.1
 */