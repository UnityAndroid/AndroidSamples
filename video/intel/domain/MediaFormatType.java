 package com.positive.mbit.intel.domain;
 
 public enum MediaFormatType
 {
   VIDEO("video"), 
   AUDIO("audio");
 
   private final String type;
 
   private MediaFormatType(String type) {
     this.type = type;
   }
 
   public String toString()
   {
     return this.type;
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.MediaFormatType
 * JD-Core Version:    0.6.1
 */