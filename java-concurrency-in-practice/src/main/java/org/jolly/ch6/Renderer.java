package org.jolly.ch6;

import org.jolly.utils.ExecutorUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Renderer {
    private final ExecutorService executor;

    public Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source) {
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
        for (final ImageInfo imageInfo : info) {
            completionService.submit(() -> imageInfo.downloadImage());
        }

        renderText(source);

        try {
            for (int t = 0, n = info.size(); t < n; t++) {
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw ExecutorUtils.launderThrowable(e.getCause());
        }
    }

    private static final long TIME_BUDGET = 3000;
    private static final Ad DEFAULT_AD = new Ad();

    Page renderPageWithAd() {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = executor.submit(new Callable<Ad>() {
            @Override
            public Ad call() throws Exception {
                return new Ad();
            }
        });
        // render page body while waiting for the ad
        Page page = renderPageBody();
        Ad ad;
        try {
            // only wait for the remaining time budget
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ad = DEFAULT_AD;
            f.cancel(true);
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }
        page.setAd(ad);
        return page;
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

    Page renderPageBody() {
        return null;
    }

    static class Ad {}
    public interface Page {
        void setAd(Ad ad);
    }
}
