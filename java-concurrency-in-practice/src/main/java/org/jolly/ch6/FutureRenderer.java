package org.jolly.ch6;

import org.jolly.utils.ExecutorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class FutureRenderer {
    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    void renderPage(CharSequence source) {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = () -> {
            List<ImageData> result = new ArrayList<>();
            for (ImageInfo imageInfo : imageInfos) {
                result.add(imageInfo.downloadImage());
            }
            return result;
        };
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData) {
                renderImage(data);
            }
        } catch (ExecutionException e) {
            throw ExecutorUtils.launderThrowable(e.getCause());
        } catch (InterruptedException e) {
            // reassert the thread's interrupted status
            Thread.currentThread().interrupt();
            // don't need the result, so cancel the task too
            future.cancel(true);
        }
    }

    void renderText(CharSequence source) {}

    void renderImage(ImageData data) {}

    List<ImageInfo> scanForImageInfo(CharSequence source) {
        return Collections.emptyList();
    }

    class ImageData {}

    class ImageInfo {
        ImageData downloadImage() {
            return null;
        }
    }
}
