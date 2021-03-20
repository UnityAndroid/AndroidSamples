 package com.positive.mbit.intel.android;
 
 public class MediaFormatTranslator
 {
   public static android.media.MediaFormat from(com.positive.mbit.intel.domain.MediaFormat mediaFormat)
   {
     if ((mediaFormat instanceof VideoFormatAndroid)) {
       return ((VideoFormatAndroid)mediaFormat).getNativeFormat();
     }
 
     if ((mediaFormat instanceof AudioFormatAndroid)) {
       return ((AudioFormatAndroid)mediaFormat).getNativeFormat();
     }
 
     throw new UnsupportedOperationException("Please, don't use MediaFormatTranslator function with this type:" + mediaFormat.getClass().toString());
   }
 
   public static com.positive.mbit.intel.domain.MediaFormat toDomain(android.media.MediaFormat mediaFormat) {
     if (mediaFormat.getString("mime").startsWith("video")) {
       return new VideoFormatAndroid(mediaFormat);
     }
 
     if (mediaFormat.getString("mime").startsWith("audio")) {
       return new AudioFormatAndroid(mediaFormat);
     }
 
     throw new UnsupportedOperationException("Unrecognized mime type:" + mediaFormat.getString("mime"));
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.MediaFormatTranslator
 * JD-Core Version:    0.6.1
 */