 package com.positive.mbit.intel;
 
 import com.positive.mbit.intel.domain.MediaFormatType;
 import com.positive.mbit.intel.domain.MediaSource;
 import com.positive.mbit.intel.domain.Pair;
 import java.io.FileDescriptor;
 import java.util.Collection;
 
 public class MediaFile
 {
   private MediaSource mediaSource;
 
   public MediaFile(MediaSource mediaSource)
   {
     this.mediaSource = mediaSource;
   }
 
   public MediaSource getMediaSource()
   {
     return this.mediaSource;
   }
 
   public void addSegment(Pair segment)
   {
     this.mediaSource.add(segment);
   }
 
   public Collection<Pair<Long, Long>> getSegments()
   {
     return this.mediaSource.getSegments();
   }
 
   public void insertSegment(int index, Pair segment)
   {
     this.mediaSource.insert(segment, index);
   }
 
   public void removeSegment(int index)
   {
     this.mediaSource.removeSegment(index);
   }
 
   public VideoFormat getVideoFormat(int index)
   {
     return (VideoFormat)this.mediaSource.getMediaFormatByType(MediaFormatType.VIDEO);
   }
 
   public AudioFormat getAudioFormat(int index)
   {
     return (AudioFormat)this.mediaSource.getMediaFormatByType(MediaFormatType.AUDIO);
   }
 
   public void setSelectedAudioTrack(int index)
   {
   }
 
   public long getDurationInMicroSec()
   {
     return this.mediaSource.getDurationInMicroSec();
   }
 
   public long getSegmentsDurationInMicroSec()
   {
     return this.mediaSource.getSegmentsDurationInMicroSec();
   }
 
   public void start()
   {
     this.mediaSource.start();
   }
 
   public int getRotation()
   {
     return this.mediaSource.getRotation();
   }
 
   public String getFilePath()
   {
     return this.mediaSource.getFilePath();
   }
 
   public FileDescriptor getFileDescriptor()
   {
     return this.mediaSource.getFileDescriptor();
   }
 
   public Uri getUri()
   {
     return this.mediaSource.getUri();
   }
 }

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.MediaFile
 * JD-Core Version:    0.6.1
 */