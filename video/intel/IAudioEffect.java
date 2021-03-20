package com.positive.mbit.intel;

import com.positive.mbit.intel.domain.MediaFormat;
import com.positive.mbit.intel.domain.Pair;
import java.nio.ByteBuffer;

public abstract interface IAudioEffect
{
  public abstract void setSegment(Pair<Long, Long> paramPair);

  public abstract Pair<Long, Long> getSegment();

  public abstract void applyEffect(ByteBuffer paramByteBuffer, long paramLong);

  public abstract MediaFormat getMediaFormat();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.IAudioEffect
 * JD-Core Version:    0.6.1
 */