package org.jolly.ch6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleThreadRenderer {
    void renderPage(CharSequence source) {
        renderText(source);
        List<ImageData> imageData = new ArrayList<>();
        for (ImageInfo imageInfo : scanForImageInfo(source)) {
            imageData.add(imageInfo.downloadImage());
        }
        for (ImageData data : imageData) {
            renderImage(data);
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
