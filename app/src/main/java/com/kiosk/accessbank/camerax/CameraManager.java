package com.kiosk.accessbank.camerax;

import android.content.Context;
import android.util.Log;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.kiosk.accessbank.facedetection.FaceContourDetectionProcessor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraManager {

    private Preview preview = null;

    private Camera camera = null;
    private ExecutorService cameraExecutor;
    private int cameraSelectorOption = CameraSelector.LENS_FACING_FRONT;
    private ProcessCameraProvider cameraProvider = null;

    private ImageAnalysis imageAnalyzer = null;

    private Context context;
    private PreviewView finderView;
    private LifecycleOwner lifecycleOwner;
    private GraphicOverlay graphicOverlay;

    public CameraManager(Context contextm, PreviewView finderView, LifecycleOwner lifecycleOwner, GraphicOverlay graphicOverlay) {
        this.context = contextm;
        this.finderView = finderView;
        this.lifecycleOwner = lifecycleOwner;
        this.graphicOverlay = graphicOverlay;
        createNewExecutor();
    }

    private void createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                preview = new Preview.Builder()
                        .build();

                cameraProvider = cameraProviderFuture.get();
                imageAnalyzer = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalyzer.setAnalyzer(cameraExecutor, selectAnalyzer());

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraSelectorOption)
                        .build();

                setCameraConfig(cameraProvider, cameraSelector);

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }


        }, ContextCompat.getMainExecutor(context));

    }

    private ImageAnalysis.Analyzer selectAnalyzer() {
        return new FaceContourDetectionProcessor(graphicOverlay);
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
                    finderView.getSurfaceProvider()
            );
        } catch (Exception e){
            Log.e(TAG, "Use case binding failed");
        }
    }

    public void changeCameraSelector() {
        cameraProvider.unbindAll();
        cameraSelectorOption =
         (cameraSelectorOption == CameraSelector.LENS_FACING_BACK)?
            CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK;
        graphicOverlay.toggleSelector();
        startCamera();
    }

   public static final String TAG = "CameraXBasic";

}