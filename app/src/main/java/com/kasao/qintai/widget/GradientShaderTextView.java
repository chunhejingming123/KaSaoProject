package com.kasao.qintai.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.kasao.qintai.R;

/**
 * Created by Administrator on 2018/2/2.
 */

public class GradientShaderTextView extends android.support.v7.widget.AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;

    private boolean mAnimating = true;
    private int delta = 15;

    public GradientShaderTextView(Context ctx) {
        this(ctx, null);
    }

    public GradientShaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                String text = getText().toString();
                // float textWidth = mPaint.measureText(text);
                int size;
                if (text.length() > 0) {
                    size = mViewWidth * 2 / text.length();
                } else {
                    size = mViewWidth;
                }
                //ffffffff
                mLinearGradient = new LinearGradient(-size, 0, 0, 0,
                        new int[]{0x99e5e5e5, 0xffe5e5e5, 0x99e5e5e5},
                        new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP); //边缘融合
                mPaint.setShader(mLinearGradient);

                mGradientMatrix = new Matrix();

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = Math.max(length(), 1);
        if (mAnimating && mGradientMatrix != null) {
            float mTextWidth = getPaint().measureText(getText().toString());
            mTranslate += delta;
            if (mTranslate > mTextWidth + 1 || mTranslate < 1) {
                //delta  = -delta;// 反复
                mTranslate = 0;//单方向
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mGradientMatrix.postRotate(-45);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(30);
        }
    }

}