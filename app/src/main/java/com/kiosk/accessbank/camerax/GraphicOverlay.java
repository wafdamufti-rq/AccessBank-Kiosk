package com.kiosk.accessbank.camerax;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.camera.core.CameraSelector;

import java.util.ArrayList;
import java.util.List;

public class GraphicOverlay extends View {


    final Sender lock = new Sender();
    private ArrayList<Graphic> graphics = new ArrayList<>();
    Float mScale = null;
    Float mOffsetX = null;
    Float mOffsetY = null;

    Context context;
    int cameraSelector = CameraSelector.LENS_FACING_FRONT;

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }

    public abstract static class Graphic {
        GraphicOverlay overlay;

        public Graphic(GraphicOverlay overlay) {
            this.overlay = overlay;
        }

        public abstract void draw(Canvas canvas);

        public RectF calculateRect(Float height, Float width, Rect boundingBoxT) {

            // for land scape
            Boolean isLandScapeMode = overlay.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;


            Float whenLandScapeModeWidth =
                    (isLandScapeMode) ? width : height;

            Float whenLandScapeModeHeight = isLandScapeMode ? height : width;

            float scaleX = (float) overlay.getWidth() / whenLandScapeModeWidth;
            float scaleY = (float) overlay.getHeight() / whenLandScapeModeHeight;
            float scale = Math.max(scaleX, scaleY);

            // Calculate offset (we need to center the overlay on the target)
            float offsetX = ((float) overlay.getWidth() - (float) Math.ceil((double) whenLandScapeModeWidth * scale)) / 2.0f;
            float offsetY = ((float) overlay.getHeight() - (float) Math.ceil((double) whenLandScapeModeHeight * scale)) / 2.0f;

            overlay.mOffsetX = offsetX;
            overlay.mOffsetY = offsetY;

            RectF mappedBox = new RectF();
            mappedBox.set(boundingBoxT.right * scale + offsetX, boundingBoxT.top * scale + offsetY, boundingBoxT.left * scale + offsetX, boundingBoxT.bottom * scale + offsetY);

            // for front mode
            if (overlay.isFrontMode) {
                float centerX = (float) overlay.getWidth() / 2;
                mappedBox.left = centerX + (centerX - mappedBox.left);
                mappedBox.right = centerX - (mappedBox.right - centerX);
            }

            return mappedBox;
        }
    }

    Boolean isFrontMode = cameraSelector == CameraSelector.LENS_FACING_FRONT;

    public void toggleSelector() {
        cameraSelector =
                (cameraSelector == CameraSelector.LENS_FACING_BACK) ? CameraSelector.LENS_FACING_FRONT :
                        CameraSelector.LENS_FACING_BACK;
    }

    public void clear() {
        synchronized (lock) {
            graphics.clear();
        }
        postInvalidate();
    }

    public void add(Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }

    public void remove(Graphic graphic) {
        synchronized (lock) {
            graphics.remove(graphic);
        }
        postInvalidate();
    }

    class Sender {
        public void send(String msg)
        {
            System.out.println("Sending\t" + msg);
            try {
                Thread.sleep(1000);
            }
            catch (Exception e) {
                System.out.println("Thread  interrupted.");
            }
            System.out.println("\n" + msg + "Sent");
        }
    }
}
