package com.positive.mbit.intel.domain;

import com.positive.mbit.intel.IRecognitionPlugin;

public abstract interface IAudioContentRecognition
{
  public abstract void setRecognizer(IRecognitionPlugin paramIRecognitionPlugin);

  public abstract void start();

  public abstract void stop();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IAudioContentRecognition
 * JD-Core Version:    0.6.1
 */