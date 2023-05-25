package com.kiosk.accessbank.facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.mlkit.vision.face.Face;
import com.kiosk.accessbank.camerax.GraphicOverlay;

class FaceContourGraphic extends GraphicOverlay.Graphic {

    GraphicOverlay overlay;
    Face face;
    Rect imageRect;

    FaceContourGraphic(
            GraphicOverlay overlay,
            Face face,
            Rect imageRect
    ) {
        super(overlay);
        this.overlay = overlay;
        this.face = face;
        this.imageRect = imageRect;
        int selectedColor = Color.WHITE;

        facePositionPaint = new Paint();
        facePositionPaint.setColor(selectedColor);

        idPaint = new Paint();
        idPaint.setColor(selectedColor);

        boxPaint = new Paint();
        boxPaint.setColor(selectedColor);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    private Paint facePositionPaint;
    private Paint idPaint;
    private Paint boxPaint;

    @Override
    public void draw(Canvas canvas) {
        RectF rect = calculateRect(
                (float) imageRect.height(),
                (float) imageRect.width(),
                face.getBoundingBox()
        );
        canvas.drawRect(rect, boxPaint);
    }

    private static final float BOX_STROKE_WIDTH = 5.0f;

}