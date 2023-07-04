package com.kiosk.accessbank.camera;

public interface PositionFaceListener {

    public void onProgress(float progress);

    public void onHalfOfProgressReached();

    public void onFUllProgressReached();

    public void onProgressStillNotHalfReached();
}
