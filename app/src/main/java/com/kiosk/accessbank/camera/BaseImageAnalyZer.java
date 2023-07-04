package com.kiosk.accessbank.camera;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;


public abstract class BaseImageAnalyZer<T> implements ImageAnalysis.Analyzer{

     GraphicOverlay overlay;
     public void setGraphicOverlay(GraphicOverlay overlay) {
         this.overlay = overlay;
     }


    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void analyze(@NonNull ImageProxy image) {
      Image mediaImage = image.getImage();

      if (mediaImage != null) {
          detectInImage(InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees()))
                  .addOnSuccessListener(t -> {
                      BaseImageAnalyZer.this.onSuccess(
                              t,
                              overlay,
                              mediaImage.getCropRect()
                      );
                      image.close();
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          onFailure(e);
                          image.close();
                      }
                  });
      }
    }


    protected abstract Task<T> detectInImage(InputImage image);

    public abstract void stop();

    protected abstract void onSuccess(
            T results,
            GraphicOverlay graphicOverlay,
            Rect rect
    );

    protected abstract void onFailure(Exception e);
}
