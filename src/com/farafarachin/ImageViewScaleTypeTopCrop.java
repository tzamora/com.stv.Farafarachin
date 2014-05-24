package com.farafarachin;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageViewScaleTypeTopCrop extends ImageView 
{
    public ImageViewScaleTypeTopCrop(Context context) {
        super(context);
        setup();
    }

    public ImageViewScaleTypeTopCrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public ImageViewScaleTypeTopCrop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {

        float frameWidth = frameRight - frameLeft;
        float frameHeight = frameBottom - frameTop;

        if (getDrawable() != null) {

            Matrix matrix = getImageMatrix();
            float scaleFactor, scaleFactorWidth, scaleFactorHeight;

            scaleFactorWidth = (float) frameWidth / (float) getDrawable().getIntrinsicWidth();
            scaleFactorHeight = (float) frameHeight / (float) getDrawable().getIntrinsicHeight();

            if (scaleFactorHeight > scaleFactorWidth) {
                scaleFactor = scaleFactorHeight;
            } else {
                scaleFactor = scaleFactorWidth;
            }

            matrix.setScale(scaleFactor, scaleFactor, 0, 0);
            setImageMatrix(matrix);
        }

        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }

}