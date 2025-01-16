package com.example.graphicsmaker.msl.demo.listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import androidx.core.view.MotionEventCompat;

import com.example.graphicsmaker.msl.demo.listener.ScaleGestureDetector.SimpleOnScaleGestureListener;
import com.example.graphicsmaker.msl.demo.view.ResizableStickerView;

public class MultiTouchListener implements OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    Bitmap bitmap = null;
    boolean bt = false;
    boolean checkstickerWH = false;
    private boolean disContinueHandleTransparecy = true;
    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isTranslateEnabled = true;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    Context mContext;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector;
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;

    public interface TouchCallbackListener {
        void onCenterPosX(View view);

        void onCenterPosXY(View view);

        void onCenterPosY(View view);

        void onOtherPos(View view);

        void onTouchCallback(View view);

        void onTouchMoveCallback(View view);

        void onTouchUpCallback(View view);
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            this.mPivotX = detector.getFocusX();
            this.mPivotY = detector.getFocusY();
            this.mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector detector) {
            float focusX;
            float f = 0.0f;
            TransformInfo info = new TransformInfo();
            info.deltaAngle = MultiTouchListener.this.isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            if (MultiTouchListener.this.isTranslateEnabled) {
                focusX = detector.getFocusX() - this.mPivotX;
            } else {
                focusX = 0.0f;
            }
            info.deltaX = focusX;
            if (MultiTouchListener.this.isTranslateEnabled) {
                f = detector.getFocusY() - this.mPivotY;
            }
            info.deltaY = f;
            info.pivotX = this.mPivotX;
            info.pivotY = this.mPivotY;
            info.minimumScale = MultiTouchListener.this.minimumScale;
            info.maximumScale = MultiTouchListener.this.maximumScale;
            MultiTouchListener.this.move(view, info);
            return false;
        }
    }

    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener l) {
        this.listener = l;
        return this;
    }

    public MultiTouchListener(Context context) {
        this.mContext = context;
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    }

    public MultiTouchListener enableRotation(boolean b) {
        this.isRotationEnabled = b;
        return this;
    }

    public MultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }

    private void move(View view, TransformInfo info) {
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + info.deltaAngle));
        }
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            return degrees - 360.0f;
        }
        if (degrees < -180.0f) {
            return degrees + 360.0f;
        }
        return degrees;
    }

    private void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = new float[]{deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        float transY = view.getTranslationY() + deltaVector[1];
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(transY);
        float pWidth = ((ResizableStickerView) view).getMainWidth();
        float pHeight = ((ResizableStickerView) view).getMainHeight();
        this.mContext.getResources();
        int dp5 = (int) (5.0f * Resources.getSystem().getDisplayMetrics().density);
        int vw = view.getWidth();
        int vh = view.getHeight();
        boolean isInCenterX = false;
        boolean isInCenterY = false;
        int cx = (int) (view.getX() + ((float) (vw / 2)));
        int cy = (int) (view.getY() + ((float) (vh / 2)));
        if (((float) cx) > (pWidth / 2.0f) - ((float) dp5) && ((float) cx) < (pWidth / 2.0f) + ((float) dp5)) {
            view.setX((pWidth / 2.0f) - ((float) (vw / 2)));
            isInCenterX = true;
        }
        if (((float) cy) > (pHeight / 2.0f) - ((float) dp5) && ((float) cy) < (pHeight / 2.0f) + ((float) dp5)) {
            view.setY((pHeight / 2.0f) - ((float) (vh / 2)));
            isInCenterY = true;
        }
        if (isInCenterX && isInCenterY) {
            if (this.listener != null) {
                this.listener.onCenterPosXY(view);
            }
        } else if (isInCenterX) {
            if (this.listener != null) {
                this.listener.onCenterPosX(view);
            }
        } else if (isInCenterY) {
            if (this.listener != null) {
                this.listener.onCenterPosY(view);
            }
        } else if (this.listener != null) {
            this.listener.onOtherPos(view);
        }
        float rotation = view.getRotation();
        if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
            if (rotation > 0.0f) {
                rotation = 90.0f;
            } else {
                rotation = -90.0f;
            }
        }
        if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
            if (rotation > 0.0f) {
                rotation = 0.0f;
            } else {
                rotation = -0.0f;
            }
        }
        if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
            if (rotation > 0.0f) {
                rotation = 180.0f;
            } else {
                rotation = -180.0f;
            }
        }
        view.setRotation(rotation);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() != pivotX || view.getPivotY() != pivotY) {
            float[] prevPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(prevPoint);
            view.setPivotX(pivotX);
            view.setPivotY(pivotY);
            float[] currPoint = new float[]{0.0f, 0.0f};
            view.getMatrix().mapPoints(currPoint);
            float offsetY = currPoint[1] - prevPoint[1];
            view.setTranslationX(view.getTranslationX() - (currPoint[0] - prevPoint[0]));
            view.setTranslationY(view.getTranslationY() - offsetY);
        }
    }

    public boolean handleTransparency(View view, MotionEvent event) {
        try {
            Log.i("MOVE_TESTs", "touch test: " + view.getWidth() + " / " + ((ResizableStickerView) view).getMainWidth());
            boolean isSmaller = ((float) view.getWidth()) < ((ResizableStickerView) view).getMainWidth() && ((float) view.getHeight()) < ((ResizableStickerView) view).getMainHeight();
            if (isSmaller && ((ResizableStickerView) view).getBorderVisbilty()) {
                return false;
            }
            if (event.getAction() == 0 && isSmaller) {
            }
            if (event.getAction() == 2 && this.bt) {
                return true;
            }
            if (event.getAction() == 1 && this.bt) {
                this.bt = false;
                if (this.bitmap != null) {
                    this.bitmap.recycle();
                }
                return true;
            }
            int[] posXY = new int[2];
            view.getLocationOnScreen(posXY);
            int rx = (int) (event.getRawX() - ((float) posXY[0]));
            int ry = (int) (event.getRawY() - ((float) posXY[1]));
            float r = view.getRotation();
            Matrix mat = new Matrix();
            mat.postRotate(-r);
            float[] point = new float[]{(float) rx, (float) ry};
            mat.mapPoints(point);
            rx = (int) point[0];
            ry = (int) point[1];
            if (event.getAction() == 0) {
                this.bt = false;
                boolean borderVisbilty = ((ResizableStickerView) view).getBorderVisbilty();
                if (borderVisbilty) {
                    ((ResizableStickerView) view).setBorderVisibility(false);
                }
                this.bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
                view.draw(new Canvas(this.bitmap));
                if (borderVisbilty) {
                    ((ResizableStickerView) view).setBorderVisibility(true);
                }
                rx = (int) (((float) rx) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getWidth()) * view.getScaleX())));
                ry = (int) (((float) ry) * (((float) this.bitmap.getHeight()) / (((float) this.bitmap.getHeight()) * view.getScaleX())));
            }
            boolean b = false;
            if (rx >= 0 && ry >= 0 && rx <= this.bitmap.getWidth() && ry <= this.bitmap.getHeight()) {
                b = this.bitmap.getPixel(rx, ry) == 0;
                if (event.getAction() == 0) {
                    this.bt = b;
                    if (b && !isSmaller) {
                        ((ResizableStickerView) view).setBorderVisibility(false);
                    }
                }
            }
            this.bitmap.recycle();
            this.bitmap = null;
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View view, MotionEvent event) {
        RelativeLayout rl = (RelativeLayout) view.getParent();
        this.mScaleGestureDetector.onTouchEvent(view, event);
        if (this.disContinueHandleTransparecy) {
            if (handleTransparency(view, event)) {
                return false;
            }
            this.disContinueHandleTransparecy = false;
        }
        if (!this.isTranslateEnabled) {
            return true;
        }
        int action = event.getAction();
        int pointerIndex;
        switch (event.getActionMasked() & action) {
            case 0:
                if (rl != null) {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
                if (this.listener != null) {
                    this.listener.onTouchCallback(view);
                }
                view.bringToFront();
                if (view instanceof ResizableStickerView) {
                    ((ResizableStickerView) view).setBorderVisibility(true);
                }
                this.mPrevX = event.getX();
                this.mPrevY = event.getY();
                this.mActivePointerId = event.getPointerId(0);
                break;
            case 1:
                this.mActivePointerId = -1;
                this.disContinueHandleTransparecy = true;
                if (this.listener != null) {
                    this.listener.onTouchUpCallback(view);
                }
                float rotation = view.getRotation();
                if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 90.0f;
                    } else {
                        rotation = -90.0f;
                    }
                }
                if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 0.0f;
                    } else {
                        rotation = -0.0f;
                    }
                }
                if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
                    if (rotation > 0.0f) {
                        rotation = 180.0f;
                    } else {
                        rotation = -180.0f;
                    }
                }
                view.setRotation(rotation);
                Log.i("testing", "Final Rotation : " + rotation);
                break;
            case 2:
                if (rl != null) {
                    rl.requestDisallowInterceptTouchEvent(true);
                }
                if (this.listener != null) {
                    this.listener.onTouchMoveCallback(view);
                }
                pointerIndex = event.findPointerIndex(this.mActivePointerId);
                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);
                    if (!this.mScaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - this.mPrevX, currY - this.mPrevY);
                        break;
                    }
                }
                break;
            case 3:
                this.mActivePointerId = -1;
                break;
            case 6:
                pointerIndex = (MotionEventCompat.ACTION_POINTER_INDEX_MASK & action) >> 8;
                if (event.getPointerId(pointerIndex) == this.mActivePointerId) {
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    this.mPrevX = event.getX(newPointerIndex);
                    this.mPrevY = event.getY(newPointerIndex);
                    this.mActivePointerId = event.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        return true;
    }
}
