package com.positive.mbit.intel.domain;

public abstract interface IEffectorSurface extends ISurface
{
  public abstract void getTransformMatrix(float[] paramArrayOfFloat);

  public abstract int getSurfaceId();

  public abstract void drawImage(int paramInt, float[] paramArrayOfFloat);

  public abstract void drawImage2D(int paramInt, float[] paramArrayOfFloat);

  public abstract void release();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IEffectorSurface
 * JD-Core Version:    0.6.1
 */