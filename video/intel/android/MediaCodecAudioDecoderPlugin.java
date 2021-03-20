package com.positive.mbit.intel.android;

import android.media.MediaCodec;

import com.positive.mbit.intel.domain.ISurfaceWrapper;
import com.positive.mbit.intel.domain.MediaFormat;

public class MediaCodecAudioDecoderPlugin extends MediaCodecDecoderPlugin {
    public MediaCodecAudioDecoderPlugin() {
        super("audio/mp4a-latm");
    }

    public void configure(MediaFormat mediaFormat, ISurfaceWrapper surface, int flags) {
        this.mediaCodec.configure(MediaFormatTranslator.from(mediaFormat), null, null, flags);
    }

    public void release() {
        this.mediaCodec.release();
    }

    public void recreate() {
        release();
        try {
            this.mediaCodec = MediaCodec.createDecoderByType("audio/mp4a-latm");
        } catch (Exception e) {

        }
    }
}

/* Location:           E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name:     com.positive.mbit.intel.android.MediaCodecAudioDecoderPlugin
 * JD-Core Version:    0.6.1
 */