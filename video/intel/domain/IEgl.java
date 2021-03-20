package com.positive.mbit.intel.domain;

public abstract interface IEgl
{
  public abstract void init(int paramInt1, int paramInt2);

  public abstract void saveEglState();

  public abstract void restoreEglState();

  public abstract float[] getProjectionMatrix();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IEgl
 * JD-Core Version:    0.6.1
 */