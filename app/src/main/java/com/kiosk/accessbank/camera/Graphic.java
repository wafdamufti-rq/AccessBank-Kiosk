package com.kiosk.accessbank.camera;

import static java.lang.Math.ceil;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kiosk.accessbank.R;

public abstract class Graphic {
    private GraphicOverlay overlay;

    public Graphic(GraphicOverlay graphicOverlay) {
        this.overlay = graphicOverlay;
    }

    public abstract void draw(Canvas canvas);

    public RectF calculateRect(float height, float width, Rect boundingBoxT) {


        // for land scape
        boolean isLandScapeMode = overlay.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;


        float whenLandScapeModeWidth = (isLandScapeMode) ? width : height;

        float whenLandScapeModeHeight = (isLandScapeMode) ? height : width;


        float scaleX = ((float) overlay.getWidth()) / whenLandScapeModeWidth;
        float scaleY = ((float) overlay.getHeight()) / whenLandScapeModeHeight;
        float scale = Math.max(scaleX, scaleY);
        overlay.mScale = scale;

        // Calculate offset (we need to center the overlay on the target)
        float offsetX = (float) ((((float) overlay.getWidth()) - ceil(whenLandScapeModeWidth * scale)) / 2.0f);
        float offsetY = (float) ((((float) overlay.getHeight()) - ceil(whenLandScapeModeHeight * scale)) / 2.0f);

        overlay.mOffsetX = offsetX;
        overlay.mOffsetY = offsetY;

        RectF mappedBox = new RectF();
        {
            mappedBox.left = boundingBoxT.right * scale + offsetX;
            mappedBox.top = boundingBoxT.top * scale + offsetY;
            mappedBox.right = boundingBoxT.left * scale + offsetX;
            mappedBox.bottom = boundingBoxT.bottom * scale + offsetY;
        }

        // for front mode
        if (overlay.isFrontMode) {
            float centerX = ((float) overlay.getWidth()) / 2;
            mappedBox.left = centerX + (centerX - mappedBox.left);
            mappedBox.right = centerX - (mappedBox.right - centerX);

        }
        return mappedBox;
    }
}
