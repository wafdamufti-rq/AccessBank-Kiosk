package com.kiosk.accessbank.facedetection;

import static android.content.Context.CAMERA_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
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
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.google.mlkit.vision.face.FaceLandmark;
import com.zkteco.android.biometric.core.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FaceRecognitionHandler {

    private static final String TAG = "FaceRecognition";
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
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setMinFaceSize(0.15f)
                .enableTracking()
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
                        InputImage inputImage = InputImage.fromMediaImage(mediaImage,image.getImageInfo().getRotationDegrees());
                        faceDetector.process(inputImage)
                                .addOnSuccessListener(faces -> {
                                    for (Face face : faces) {
                                        Rect bounds = face.getBoundingBox();
                                        float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                        float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                        // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                        // nose available):
                                        FaceLandmark leftEar = face.getLandmark(FaceLandmark.LEFT_EAR);
                                        if (leftEar != null) {
                                            PointF leftEarPos = leftEar.getPosition();
                                        }

                                        // If contour detection was enabled:
                                        List<PointF> leftEyeContour =
                                                face.getContour(FaceContour.LEFT_EYE) != null ?  face.getContour(FaceContour.LEFT_EYE).getPoints(): new ArrayList<>();
                                        List<PointF> upperLipBottomContour =
                                                face.getContour(FaceContour.UPPER_LIP_BOTTOM).getPoints();

                                        // If classification was enabled:
                                        if (face.getSmilingProbability() != null) {
                                            float smileProb = face.getSmilingProbability();
                                        }
                                        if (face.getRightEyeOpenProbability() != null) {
                                            float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                        }

                                        // If face tracking was enabled:
                                        if (face.getTrackingId() != null) {
                                            int id = face.getTrackingId();
                                        }
                                        Log.d(TAG, "bound = " +bounds + ", headeulerAngleY "+ rotY +", headeulerAngleZ "+rotZ+ ", leftEArPos "+ (leftEar != null ? leftEar.getPosition() : "null") + ", leftEyeContour "+leftEyeContour+", upperlipBottomContour="+upperLipBottomContour+", smileProb "+ (face.getSmilingProbability() != null ? face.getSmilingProbability(): "null")+", rightEyeOpenProb "+(face.getRightEyeOpenProbability() != null ? face.getRightEyeOpenProbability(): "null"));
                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("face recognition", e.getMessage());

                                    }
                                });


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

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    private int getRotationCompensation(String cameraId, Activity activity, boolean isFrontFacing)
            throws CameraAccessException {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        int deviceRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int rotationCompensation = ORIENTATIONS.get(deviceRotation);

        // Get the device's sensor orientation.
        CameraManager cameraManager = (CameraManager) activity.getSystemService(CAMERA_SERVICE);
        int sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION);

        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360;
        } else { // back-facing
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360;
        }
        return rotationCompensation;
    }

}
