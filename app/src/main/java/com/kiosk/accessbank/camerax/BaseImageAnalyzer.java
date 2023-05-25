package com.kiosk.accessbank.camerax;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;

public abstract class BaseImageAnalyzer<T> implements ImageAnalysis.Analyzer {


    public abstract GraphicOverlay graphicOverlay();

    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
       Image mediaImage = image.getImage();
       if (mediaImage != null){
            detectInImage(InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees())).addOnSuccessListener(t -> {
                onSuccess(t,graphicOverlay(),mediaImage.getCropRect());
            }).addOnFailureListener(e -> {
                onFailure(e);
                image.close();
            });
       }
    }
    protected abstract  Task<T> detectInImage(InputImage image);

    public abstract void stop();

    protected abstract void onSuccess(
            T results,
            GraphicOverlay graphicOverlay,
            Rect rect
    );

    protected abstract void onFailure(Exception e);
}
