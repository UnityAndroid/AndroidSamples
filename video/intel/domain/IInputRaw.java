package com.positive.mbit.intel.domain;

public abstract interface IInputRaw
{
  public abstract boolean canConnectFirst(IOutputRaw paramIOutputRaw);

  public abstract CommandQueue getInputCommandQueue();

  public abstract void fillCommandQueues();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IInputRaw
 * JD-Core Version:    0.6.1
 */