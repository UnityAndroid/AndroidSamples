package com.positive.mbit.intel.domain;

public abstract interface IMediaSource extends IOutput, IVideoOutput
{
  public abstract int getTrackIdByMediaType(MediaFormatType paramMediaFormatType);

  public abstract void selectTrack(int paramInt);
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IMediaSource
 * JD-Core Version:    0.6.1
 */