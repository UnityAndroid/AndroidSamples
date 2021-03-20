package com.positive.mbit.intel.domain;

import com.positive.mbit.intel.Uri;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;

public abstract interface IMediaExtractor
{
  public static final int SEEK_TO_PREVIOUS_SYNC = 0;
  public static final int SEEK_TO_NEXT_SYNC = 1;
  public static final int SEEK_TO_CLOSEST_SYNC = 2;

  public abstract int readSampleData(ByteBuffer paramByteBuffer);

  public abstract MediaFormat getTrackFormat(int paramInt);

  public abstract long getSampleTime();

  public abstract boolean advance();

  public abstract int getTrackCount();

  public abstract void selectTrack(int paramInt);

  public abstract void unselectTrack(int paramInt);

  public abstract int getSampleTrackIndex();

  public abstract void release();

  public abstract int getSampleFlags();

  public abstract void seekTo(long paramLong, int paramInt);

  public abstract int getRotation();

  public abstract String getFilePath();

  public abstract FileDescriptor getFileDescriptor();

  public abstract Uri getUri();
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\domain-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.domain.IMediaExtractor
 * JD-Core Version:    0.6.1
 */