package com.choryan.opengglpacket.gpuImage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/5
 */
public class GPUImageView extends FrameLayout {

    private View surfaceView;

    private GPUImage gpuImage;

    public GPUImageView(Context context) {
        super(context);
        init(context, null);
    }

    public GPUImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        gpuImage = new GPUImage(context);
    }


}
