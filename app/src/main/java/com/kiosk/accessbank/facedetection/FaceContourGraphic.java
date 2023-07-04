package com.kiosk.accessbank.facedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.google.mlkit.vision.face.Face;
import com.kiosk.accessbank.R;
import com.kiosk.accessbank.camera.Graphic;
import com.kiosk.accessbank.camera.GraphicOverlay;

public class FaceContourGraphic extends Graphic {
    public static final float BOX_STROKE_WIDTH = 5.0f;
    private GraphicOverlay overlay;
    private Face face;
    private Rect imageRect;

    private Paint facePositionPaint;
    private Paint idPaint;
    private Paint boxPaint;

    public FaceContourGraphic(GraphicOverlay graphicOverlay, Face face, Rect imageRect) {
        super(graphicOverlay);
        this.overlay = graphicOverlay;
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

    @Override
    public void draw(Canvas canvas) {
        if (face.getBoundingBox().top > overlay.getContext().getResources().getInteger(R.integer.face_top_position) &&
                face.getBoundingBox().left > overlay.getContext().getResources().getInteger(R.integer.face_left_Position) &&
                face.getBoundingBox().right < overlay.getContext().getResources().getInteger(R.integer.face_right_position) &&
                face.getBoundingBox().bottom < overlay.getContext().getResources().getInteger(R.integer.face_bottom_Position)
        ) {
            RectF rect = calculateRect(
                    (float) imageRect.height(),
                    (float) imageRect.width(),
                    face.getBoundingBox()
            );
            canvas.drawRect(rect, boxPaint);
        }


    }
}
