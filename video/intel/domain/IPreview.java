package com.positive.mbit.intel.domain;

import com.positive.mbit.intel.IVideoEffect;

public abstract interface IPreview
{
  public abstract void setActiveEffect(IVideoEffect paramIVideoEffect);

  public abstract void renderSurfaceFromFrameBuffer(int paramInt);

  public abstract void requestRendering();

  public abstract PreviewContext getSharedContext();

  public abstract void updateCameraParameters();

  public abstract void start();

  public abstract void stop();

  public abstract void setListener(IOnFrameAvailableListener paramIOnFrameAvailableListener);
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IPreview
 * JD-Core Version:    0.6.1
 */