package com.superflousjazzhands.popularmovies.views;

/**
 *
 * Created by antoniocarella on 11/17/15.
 * With help from http://stackoverflow.com/questions/15261088/gridview-with-two-columns-and-auto-resized-images
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //TODO: this seems like a hack to make the images rectangular. Find a better solution
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredWidth()*1.5)); //Hack is here
    }
}
