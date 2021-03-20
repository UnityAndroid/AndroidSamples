package com.positive.mbit.intel.domain;

import java.util.Collection;

abstract interface ITopologyTree<T>
{
  public abstract T current();

  public abstract Collection<ITopologyTree<T>> next();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.ITopologyTree
 * JD-Core Version:    0.6.1
 */