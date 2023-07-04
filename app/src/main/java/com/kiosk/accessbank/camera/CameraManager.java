package com.kiosk.accessbank.camera;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.kiosk.accessbank.facedetection.FaceContourDetectionProcessor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ExperimentalGetImage
public class CameraManager {
    private static final String TAG = "cameraManager";
    private PositionFaceListener listener;
    private Preview preview;

    private Camera camera;
    private ExecutorService cameraExecutor;
    private int cameraSelectorOption = CameraSelector.LENS_FACING_BACK;
    private ProcessCameraProvider cameraProvider;

    private ImageAnalysis imageAnalyzer;

    private Context context;
    private PreviewView previewView;
    private LifecycleOwner lifecycleOwner;
    private GraphicOverlay graphicOverlay;


    public CameraManager(
            Context context,
            PreviewView finderView,
            LifecycleOwner lifecycleOwner,
            GraphicOverlay graphicOverlay,
            PositionFaceListener listener
    ) {
        this.context = context;
        this.previewView = finderView;
        this.lifecycleOwner = lifecycleOwner;
        this.graphicOverlay = graphicOverlay;
        this.listener = listener;
        createNewExecutor();
    }


    private void createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
                    try {
                        cameraProvider = cameraProviderFuture.get();
                        preview = new Preview.Builder()
                                .build();

                        imageAnalyzer = new ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build();

                        imageAnalyzer.setAnalyzer(cameraExecutor, selectAnalyzer());


                        CameraSelector cameraSelector = new CameraSelector.Builder()
                                .requireLensFacing(cameraSelectorOption)
                                .build();

                        setCameraConfig(cameraProvider, cameraSelector);
                    } catch (ExecutionException |
                             InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }, ContextCompat.getMainExecutor(context)
        );
    }

    private ImageAnalysis.Analyzer selectAnalyzer() {
        return new FaceContourDetectionProcessor(graphicOverlay, listener);
    }

    private void setCameraConfig(
            ProcessCameraProvider cameraProvider,
            CameraSelector cameraSelector
    ) {
        try {
            cameraProvider.unbindAll();
            camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
            );
            preview.setSurfaceProvider(
                    previewView.getSurfaceProvider()
            );
        } catch (Exception e) {
            Log.e(TAG, "Use case binding failed", e);
        }
    }


    private void changeCameraSelector() {
        cameraProvider.unbindAll();
        cameraSelectorOption =
                (cameraSelectorOption == CameraSelector.LENS_FACING_BACK) ? CameraSelector.LENS_FACING_FRONT
                        : CameraSelector.LENS_FACING_BACK;
        graphicOverlay.toggleSelector();
        startCamera();
    }


}

