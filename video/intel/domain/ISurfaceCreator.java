package com.positive.mbit.intel.domain;

public abstract interface ISurfaceCreator
{
  public abstract void onSurfaceAvailable(IOnSurfaceReady paramIOnSurfaceReady);

  public abstract ISurface getSurface();

  public abstract ISurface getSimpleSurface(IEglContext paramIEglContext);

  public abstract void notifySurfaceReady(ISurface paramISurface);
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.ISurfaceCreator
 * JD-Core Version:    0.6.1
 */