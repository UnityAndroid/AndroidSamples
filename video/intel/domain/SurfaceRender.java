package com.positive.mbit.intel.domain;

public abstract class SurfaceRender extends Render
{
  public abstract void onSurfaceAvailable(IOnSurfaceReady paramIOnSurfaceReady);

  public abstract ISurface getSurface();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.SurfaceRender
 * JD-Core Version:    0.6.1
 */