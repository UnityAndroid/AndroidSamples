package com.positive.mbit.intel.domain;

import java.io.Closeable;

public abstract interface ICameraSource extends IRunnable, IOutputRaw, IVideoOutput, Closeable
{
  public abstract void setOutputSurface(ISurface paramISurface);

  public abstract void setPreview(IPreview paramIPreview);

  public abstract void setCamera(Object paramObject);

  public abstract void configure();

  public abstract Frame getFrame();

  public abstract ISurface getSurface();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.ICameraSource
 * JD-Core Version:    0.6.1
 */