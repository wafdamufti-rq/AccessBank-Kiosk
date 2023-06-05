package com.kiosk.accessbank.facedetection;

import android.content.Context;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.concurrent.ExecutionException;

public class FaceRecognitionHandler {

    Context context;

    ProcessCameraProvider cameraProvider;

    PreviewView previewView;

    Preview preview;

    ImageAnalysis imageAnalysis;

    CameraSelector cameraSelector;

    FaceDetector faceDetector;

    public FaceRecognitionHandler(Context context, PreviewView previewView) {

        this.context = context;
        this.previewView = previewView;

        preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                .build();
        faceDetector = FaceDetection.getClient(options);


        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();
        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();


    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    public void bindCamera(LifecycleOwner lifecycleOwner) {
        ListenableFuture<ProcessCameraProvider> future = ProcessCameraProvider.getInstance(context);
        future.addListener(() -> {
            try {
                cameraProvider = future.get();


                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), image -> {
                    Image mediaImage = image.getImage();
                    if (mediaImage != null) {
                        InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                        faceDetector.process(inputImage)
                                .addOnSuccessListener(faces -> Log.d("face recognition", "ada muka")).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("face recognition", e.getMessage());

                                    }
                                });


//                        mediaImage.close();
//                        image.close();
                    }
                });
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis, preview);

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }


        }, context.getMainExecutor());
    }

    public void unBindCamera() {
        if (cameraProvider != null)
            cameraProvider.unbindAll();
    }

}
