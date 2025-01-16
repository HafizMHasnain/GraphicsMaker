package com.example.graphicsmaker.create;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.graphicsmaker.msl.demo.ImageUtils;


public class GuidelineImageView extends AppCompatImageView {
    boolean isInCenterX = false;
    boolean isInCenterY = false;
    private Context mContext;
    private Paint paint;

    public GuidelineImageView(Context context) {
        super(context);
        intiView(context);
    }

    public GuidelineImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        intiView(context);
    }

    public GuidelineImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView(context);
    }

    private void intiView(Context context) {
        this.mContext = context;
        this.paint = new Paint();
        this.paint.setColor(-1);
        this.paint.setStrokeWidth((float) ImageUtils.dpToPx(context, 2));
        this.paint.setPathEffect(new DashPathEffect(new float[]{(float) ImageUtils.dpToPx(context, 3), (float) ImageUtils.dpToPx(context, 3)}, 1.0f));
        this.paint.setStyle(Style.STROKE);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        this.paint.setColor(-1);
        drawLine(canvas, w / 4, 0, w / 4, h, this.paint);
        drawLine(canvas, w / 2, 0, w / 2, h, this.paint);
        drawLine(canvas, (w * 3) / 4, 0, (w * 3) / 4, h, this.paint);
        drawLine(canvas, 0, h / 4, w, h / 4, this.paint);
        drawLine(canvas, 0, h / 2, w, h / 2, this.paint);
        drawLine(canvas, 0, (h * 3) / 4, w, (h * 3) / 4, this.paint);
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        drawLine(canvas, (w / 4) + 2, 0, (w / 4) + 2, h, this.paint);
        if (this.isInCenterX) {
            this.paint.setColor(SupportMenu.CATEGORY_MASK);
            this.isInCenterX = false;
        }
        drawLine(canvas, (w / 2) + 2, 0, (w / 2) + 2, h, this.paint);
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        drawLine(canvas, ((w * 3) / 4) + 2, 0, ((w * 3) / 4) + 2, h, this.paint);
        drawLine(canvas, 0, (h / 4) + 2, w, (h / 4) + 2, this.paint);
        if (this.isInCenterY) {
            this.paint.setColor(SupportMenu.CATEGORY_MASK);
            this.isInCenterY = false;
        }
        drawLine(canvas, 0, (h / 2) + 2, w, (h / 2) + 2, this.paint);
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        drawLine(canvas, 0, ((h * 3) / 4) + 2, w, ((h * 3) / 4) + 2, this.paint);
    }

    public void setCenterValues(boolean X, boolean Y) {
        this.isInCenterX = X;
        this.isInCenterY = Y;
        invalidate();
    }

    private void drawLine(Canvas canvas, int l, int t, int r, int b, Paint paint) {
        Path baseline = new Path();
        baseline.moveTo((float) l, (float) t);
        baseline.lineTo((float) r, (float) b);
        canvas.drawPath(baseline, paint);
    }
}
