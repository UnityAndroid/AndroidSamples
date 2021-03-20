package com.positive.mbit.intel;

import com.positive.mbit.intel.domain.Pair;
import com.positive.mbit.intel.domain.Resolution;

public abstract interface IVideoEffect
{
  public abstract void setSegment(Pair<Long, Long> paramPair);

  public abstract Pair<Long, Long> getSegment();

  public abstract void start();

  public abstract void applyEffect(int paramInt, long paramLong, float[] paramArrayOfFloat);

  public abstract void setInputResolution(Resolution paramResolution);

  public abstract boolean fitToCurrentSurface(boolean paramBoolean);
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.IVideoEffect
 * JD-Core Version:    0.6.1
 */