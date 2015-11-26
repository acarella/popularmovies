package com.superflousjazzhands.popularmovies.views;

/**
 * Created by antoniocarella on 11/17/15.
 * Written by Jake Wharton
 * From https://github.com/square/picasso/blob/a35c785202ef7351f1e071a0191b201b745da168/picasso-sample/
 * src/main/java/com/example/picasso/SquaredImageView.java
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

final public class SquaredImageView extends ImageView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
