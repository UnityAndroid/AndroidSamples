 package com.positive.mbit.intel.domain;
 
 public class Wrapper<T>
   implements IWrapper
 {
   private T t;
 
   public Wrapper(T t)
   {
     this.t = t;
   }
 
   public T getNativeObject()
   {
     return this.t;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.Wrapper
 * JD-Core Version:    0.6.1
 */