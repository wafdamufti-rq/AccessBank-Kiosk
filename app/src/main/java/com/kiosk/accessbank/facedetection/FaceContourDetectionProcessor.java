package com.kiosk.accessbank.facedetection;


import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.kiosk.accessbank.camerax.BaseImageAnalyzer;
import com.kiosk.accessbank.camerax.GraphicOverlay;

import java.util.List;

public class FaceContourDetectionProcessor extends
        BaseImageAnalyzer<List<Face>> {

    GraphicOverlay view;

    public FaceContourDetectionProcessor(GraphicOverlay view) {
        this.view
                = view;

    }


    private FaceDetectorOptions realTimeOpts = new FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .build();

    private FaceDetector detector =  FaceDetection.getClient(realTimeOpts);

    @Override
    public GraphicOverlay graphicOverlay() {
        return view;
    }

    @Override
    protected Task<List<Face>> detectInImage(InputImage image) {
        return detector.process(image);
    }

    @Override
    public void stop() {

    }

    @Override
    protected void onSuccess(List<Face> results, GraphicOverlay graphicOverlay, Rect rect) {
            graphicOverlay.clear();
            results.forEach(face -> {
                FaceContourGraphic facegraphic = new FaceContourGraphic(graphicOverlay,face,rect);
                graphicOverlay.add(facegraphic);
            });

            graphicOverlay.postInvalidate();
        }

        @Override
        protected void onFailure (Exception e){
            Log.w(TAG, "Face Detector failed.$e");
        }

        public static final String TAG = "FaceDetectorProcessor";
    }