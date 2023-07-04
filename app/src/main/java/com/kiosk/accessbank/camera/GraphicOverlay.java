package com.kiosk.accessbank.camera;

import static java.lang.Math.ceil;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;

import java.util.ArrayList;

public class GraphicOverlay extends View {

    private final Locker lock = new Locker();
    float mScale = 0f;
    float mOffsetX = 0f;
    float mOffsetY = 0f;
    private ArrayList<Graphic> graphics = new ArrayList();
    private int cameraSelector = CameraSelector.LENS_FACING_FRONT;
    boolean isFrontMode = (cameraSelector == CameraSelector.LENS_FACING_FRONT) ? true : false;
    private Context context;

    public GraphicOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public GraphicOverlay(Context context) {
        super(context);
        this.context = context;
    }

    public GraphicOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GraphicOverlay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void toggleSelector() {
        cameraSelector =
                (cameraSelector == CameraSelector.LENS_FACING_BACK) ? CameraSelector.LENS_FACING_FRONT
                        : CameraSelector.LENS_FACING_BACK;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (lock) {
            for (Graphic graphic : graphics) {
                graphic.draw(canvas);
            }
        }
    }


    public class Locker {

    }
}
