package com.kiosk.accessbank.facedetection;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;
import android.util.Size;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.kiosk.accessbank.R;
import com.kiosk.accessbank.camera.BaseImageAnalyZer;
import com.kiosk.accessbank.camera.CameraManager;
import com.kiosk.accessbank.camera.GraphicOverlay;
import com.kiosk.accessbank.camera.PositionFaceListener;

import java.util.List;
import java.util.function.Consumer;

public class FaceContourDetectionProcessor extends BaseImageAnalyZer<List<Face>> {

    private static final String TAG = FaceContourDetectionProcessor.class.getName();
    private GraphicOverlay overlay;

    private FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .build();

    private FaceDetector detector = FaceDetection.getClient(realTimeOpts);

    private PositionFaceListener listener;

    public FaceContourDetectionProcessor(GraphicOverlay overlay, PositionFaceListener listener) {
        this.overlay = overlay;
        this.listener = listener;
        setGraphicOverlay(overlay);

    }


    @Override
    protected Task<List<Face>> detectInImage(InputImage image) {
        return  detector.process(image);
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e");
        }
    }

    @Override
    protected void onSuccess(List<Face> results, GraphicOverlay graphicOverlay, Rect rect) {
        graphicOverlay.clear();
        results.forEach(new Consumer<Face>() {
            @Override
            public void accept(Face face) {
                FaceContourGraphic faceGraphic = new  FaceContourGraphic(overlay, face, rect);
                graphicOverlay.add(faceGraphic);

                Log.d(TAG,face.getBoundingBox().toShortString());
                if (face.getBoundingBox().top > overlay.getContext().getResources().getInteger(R.integer.face_top_position) &&
                        face.getBoundingBox().left > overlay.getContext().getResources().getInteger(R.integer.face_left_Position) &&
                        face.getBoundingBox().right < overlay.getContext().getResources().getInteger(R.integer.face_right_position) &&
                        face.getBoundingBox().bottom < overlay.getContext().getResources().getInteger(R.integer.face_bottom_Position)
                ){
                    listener.onFUllProgressReached();
                }
            }
        });


        graphicOverlay.postInvalidate();

    }

    @Override
    protected void onFailure(Exception e) {
        Log.w(TAG, "Face Detector failed.$e");
    }

    @Nullable
    @Override
    public Size getDefaultTargetResolution() {
        return super.getDefaultTargetResolution();
    }

    @Override
    public int getTargetCoordinateSystem() {
        return super.getTargetCoordinateSystem();
    }

    @Override
    public void updateTransform(@Nullable Matrix matrix) {
        super.updateTransform(matrix);
    }
}
