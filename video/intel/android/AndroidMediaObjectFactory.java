package com.positive.mbit.intel.android;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Surface;

import com.positive.mbit.intel.AudioFormat;
import com.positive.mbit.intel.IProgressListener;
import com.positive.mbit.intel.StreamingParameters;
import com.positive.mbit.intel.Uri;
import com.positive.mbit.intel.android.graphics.EglUtil;
import com.positive.mbit.intel.android.graphics.FrameBuffer;
import com.positive.mbit.intel.domain.AudioDecoder;
import com.positive.mbit.intel.domain.AudioEffector;
import com.positive.mbit.intel.domain.AudioEncoder;
import com.positive.mbit.intel.domain.IAndroidMediaObjectFactory;
import com.positive.mbit.intel.domain.IAudioContentRecognition;
import com.positive.mbit.intel.domain.ICameraSource;
import com.positive.mbit.intel.domain.ICaptureSource;
import com.positive.mbit.intel.domain.IEffectorSurface;
import com.positive.mbit.intel.domain.IEglContext;
import com.positive.mbit.intel.domain.IFrameBuffer;
import com.positive.mbit.intel.domain.IMediaFormatWrapper;
import com.positive.mbit.intel.domain.IMicrophoneSource;
import com.positive.mbit.intel.domain.IPreview;
import com.positive.mbit.intel.domain.ISurfaceWrapper;
import com.positive.mbit.intel.domain.MediaSource;
import com.positive.mbit.intel.domain.MuxRender;
import com.positive.mbit.intel.domain.ProgressTracker;
import com.positive.mbit.intel.domain.Render;
import com.positive.mbit.intel.domain.Resampler;
import com.positive.mbit.intel.domain.VideoDecoder;
import com.positive.mbit.intel.domain.VideoEffector;
import com.positive.mbit.intel.domain.VideoEncoder;
import com.positive.mbit.intel.domain.graphics.IEglUtil;

public class AndroidMediaObjectFactory
        implements IAndroidMediaObjectFactory
{
    private final Context context;
    MediaCodecEncoderPlugin audioMediaCodec;

    public AndroidMediaObjectFactory(Context context)
    {
        this.context = context;
    }

    public MediaSource createMediaSource(String fileName) throws IOException
    {
        MediaExtractorPlugin mediaExtractor = new MediaExtractorPlugin();
        mediaExtractor.setDataSource(fileName);
        return new MediaSource(mediaExtractor);
    }

    public MediaSource createMediaSource(FileDescriptor fileDescriptor)
            throws IOException
    {
        MediaExtractorPlugin mediaExtractor = new MediaExtractorPlugin();
        mediaExtractor.setDataSource(fileDescriptor);
        return new MediaSource(mediaExtractor);
    }

    public MediaSource createMediaSource(Uri uri) throws IOException
    {
        MediaExtractorPlugin mediaExtractor = new MediaExtractorPlugin();
        mediaExtractor.setDataSource(this.context, uri);
        return new MediaSource(mediaExtractor);
    }

    public VideoDecoder createVideoDecoder(
            com.positive.mbit.intel.domain.MediaFormat format)
    {
        VideoDecoder videoDecoder = new VideoDecoder(new MediaCodecVideoDecoderPlugin(format));
        videoDecoder.setTimeout(getDeviceSpecificTimeout());
        return videoDecoder;
    }

    public VideoEncoder createVideoEncoder()
    {
        VideoEncoder videoEncoder = new VideoEncoder(new MediaCodecEncoderPlugin("video/avc",
                getEglUtil()));
        videoEncoder.setTimeout(getDeviceSpecificTimeout());
        return videoEncoder;
    }

    public AudioDecoder createAudioDecoder()
    {
        AudioDecoder audioDecoder = new AudioDecoder(new MediaCodecAudioDecoderPlugin());
        audioDecoder.setTimeout(getDeviceSpecificTimeout());
        return audioDecoder;
    }

    public AudioEncoder createAudioEncoder(String mime)
    {
        this.audioMediaCodec = MediaCodecEncoderPlugin.createByCodecName(mime != null ? mime
                : "audio/mp4a-latm",
                getEglUtil());
        AudioEncoder audioEncoder = new AudioEncoder(this.audioMediaCodec);
        audioEncoder.setTimeout(getDeviceSpecificTimeout());
        return audioEncoder;
    }

    public Resampler createAudioResampler(AudioFormat audioFormat)
    {
        return new ResamplerAndroid(audioFormat);
    }

    public Render createSink(
            String fileName, IProgressListener progressListener,
            ProgressTracker progressTracker) throws IOException
    {
        if (fileName != null) {
            return new MuxRender(new MediaMuxerPlugin(fileName, 0),
                    progressListener,
                    progressTracker);
        }
        return null;
    }

    public Render createSink(
            StreamingParameters parameters, IProgressListener progressListener,
            ProgressTracker progressTracker)
    {
        //TODO: Error cannot use
        Log.d("record",
                "something error hanppend. ");
        throw new RuntimeException();
        //        return new MuxRender(null,
        //                progressListener,
        //                progressTracker);
    }

    public ICaptureSource createCaptureSource()
    {
        return new GameCapturerSource();
    }

    public com.positive.mbit.intel.domain.MediaFormat createVideoFormat(
            String mimeType, int width, int height)
    {
        return new VideoFormatAndroid(mimeType, width, height);
    }

    public com.positive.mbit.intel.domain.MediaFormat createAudioFormat(
            String mimeType, int channelCount, int sampleRate)
    {
        return new AudioFormatAndroid(mimeType, sampleRate, channelCount);
    }

    public VideoEffector createVideoEffector()
    {
        return new VideoEffector(new MediaCodecEncoderPlugin("video/avc",
                getEglUtil()), this);
    }

    public IEffectorSurface createEffectorSurface() {
        return new EffectorSurface(getEglUtil());
    }

    public IPreview createPreviewRender(Object glView, Object camera)
    {
        return new PreviewRender((GLSurfaceView) glView,
                getEglUtil(),
                (Camera) camera);
    }

    public AudioEffector createAudioEffects()
    {
        return new AudioEffector(null);
    }

    public ICameraSource createCameraSource()
    {
        return new CameraSource(getEglUtil());
    }

    public IMicrophoneSource createMicrophoneSource()
    {
        return new MicrophoneSource();
    }

    public IAudioContentRecognition createAudioContentRecognition()
    {
        return new AudioContentRecognition();
    }

    public IEglContext getCurrentEglContext()
    {
        return new EGLContextWrapper(EGL14.eglGetCurrentContext());
    }

    public IEglUtil getEglUtil()
    {
        return EglUtil.getInstance();
    }

    public IFrameBuffer createFrameBuffer()
    {
        return new FrameBuffer(getEglUtil());
    }

    private int getDeviceSpecificTimeout()
    {
        return 10;
    }

    public static class Converter
    {
        public static ISurfaceWrapper convert(Surface surface) {
            return new SurfaceWrapper(surface);
        }

        public static IMediaFormatWrapper convert(
                android.media.MediaFormat mediaFormat) {
            return new MediaFormatWrapper(mediaFormat);
        }
    }
}

/*
 * Location: E:\SouceCode\recordGame\gdxDemo\libs\android-1.2.2415.jar
 * Qualified Name: com.positive.mbit.intel.android.AndroidMediaObjectFactory
 * JD-Core Version: 0.6.1
 */
