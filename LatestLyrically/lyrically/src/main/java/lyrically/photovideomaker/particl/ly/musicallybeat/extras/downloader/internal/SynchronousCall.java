package lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.internal;

import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Response;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.request.DownloadRequest;

public class SynchronousCall {

    public final DownloadRequest request;

    public SynchronousCall(DownloadRequest request) {
        this.request = request;
    }

    public Response execute() {
        DownloadTask downloadTask = DownloadTask.create(request);
        return downloadTask.run();
    }

}
