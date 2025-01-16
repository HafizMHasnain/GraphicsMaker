package com.example.graphicsmaker.msl.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import com.bumptech.glide.Glide;
import com.example.graphicsmaker.R;
import com.example.graphicsmaker.utility.StorageConfiguration;
import com.example.graphicsmaker.msl.demo.listener.MultiTouchListener;
import com.example.graphicsmaker.msl.demo.listener.MultiTouchListener.TouchCallbackListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ResizableStickerView extends RelativeLayout implements TouchCallbackListener {
    private static final int SELF_SIZE_DP = 30;
    public static final String TAG = "ResizableStickerView";
    private int alphaProg = 0;
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    private ImageView border_iv;
    private Bitmap btmp = null;
    float cX = 0.0f;
    float cY = 0.0f;
    private double centerX;
    private double centerY;
    private String colorType = "colored";
    private Context context;
    double dAngle = 0.0d;
    private ImageView delete_iv;
    private String drawableId;
    private String field_four = "";
    private int field_one = 0;
    private String field_two = "0,0";
    private ImageView flip_iv;
    boolean ft = true;
    private int he;
    float heightMain = 0.0f;
    private int hueProg = 2;
    private int imgAlpha = 100;
    private int imgColor = 0;
    private boolean isBorderVisible = false;
    private boolean isColorFilterEnable = false;
    public boolean isMultiTouchEnabled = true;
    private boolean isSticker = true;
    private boolean isStrickerEditEnable = false;
    private int leftMargin = 0;
    private TouchEventListener listener = null;
    String lockStatus = "UNLOCKED";
    private OnTouchListener mTouchListener = new OnTouchListener() {
        @SuppressLint({"NewApi", "ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    ResizableStickerView.this.this_orgX = ResizableStickerView.this.getX();
                    ResizableStickerView.this.this_orgY = ResizableStickerView.this.getY();
                    ResizableStickerView.this.scale_orgX = event.getRawX();
                    ResizableStickerView.this.scale_orgY = event.getRawY();
                    ResizableStickerView.this.scale_orgWidth = (double) ResizableStickerView.this.getLayoutParams().width;
                    ResizableStickerView.this.scale_orgHeight = (double) ResizableStickerView.this.getLayoutParams().height;
                    ResizableStickerView.this.centerX = (double) ((((View) ResizableStickerView.this.getParent()).getX() + ResizableStickerView.this.getX()) + (((float) ResizableStickerView.this.getWidth()) / 2.0f));
                    int result = 0;
                    int resourceId = ResizableStickerView.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        result = ResizableStickerView.this.getResources().getDimensionPixelSize(resourceId);
                    }
                    double statusBarHeight = (double) result;
                    ResizableStickerView.this.centerY = (((double) (((View) ResizableStickerView.this.getParent()).getY() + ResizableStickerView.this.getY())) + statusBarHeight) + ((double) (((float) ResizableStickerView.this.getHeight()) / 2.0f));
                    break;
                case 1:
                    ResizableStickerView.this.wi = ResizableStickerView.this.getLayoutParams().width;
                    ResizableStickerView.this.he = ResizableStickerView.this.getLayoutParams().height;
                    break;
                case 2:
                    double angle_diff = (Math.abs(Math.atan2((double) (event.getRawY() - ResizableStickerView.this.scale_orgY), (double) (event.getRawX() - ResizableStickerView.this.scale_orgX)) - Math.atan2(((double) ResizableStickerView.this.scale_orgY) - ResizableStickerView.this.centerY, ((double) ResizableStickerView.this.scale_orgX) - ResizableStickerView.this.centerX)) * 180.0d) / 3.141592653589793d;
                    Log.v(ResizableStickerView.TAG, "angle_diff: " + angle_diff);
                    double length1 = ResizableStickerView.this.getLength(ResizableStickerView.this.centerX, ResizableStickerView.this.centerY, (double) ResizableStickerView.this.scale_orgX, (double) ResizableStickerView.this.scale_orgY);
                    double length2 = ResizableStickerView.this.getLength(ResizableStickerView.this.centerX, ResizableStickerView.this.centerY, (double) event.getRawX(), (double) event.getRawY());
                    int size = ResizableStickerView.this.dpToPx(ResizableStickerView.this.getContext(), 30);
                    double offset;
                    LayoutParams layoutParams;
                    if (length2 > length1 && (angle_diff < 25.0d || Math.abs(angle_diff - 180.0d) < 25.0d)) {
                        offset = (double) Math.round(Math.max((double) Math.abs(event.getRawX() - ResizableStickerView.this.scale_orgX), (double) Math.abs(event.getRawY() - ResizableStickerView.this.scale_orgY)));
                        layoutParams = (LayoutParams) ResizableStickerView.this.getLayoutParams();
                        layoutParams.width = (int) (((double) layoutParams.width) + offset);
                        layoutParams = (LayoutParams) ResizableStickerView.this.getLayoutParams();
                        layoutParams.height = (int) (((double) layoutParams.height) + offset);
                    } else if (length2 < length1 && ((angle_diff < 25.0d || Math.abs(angle_diff - 180.0d) < 25.0d) && ResizableStickerView.this.getLayoutParams().width > size / 2 && ResizableStickerView.this.getLayoutParams().height > size / 2)) {
                        offset = (double) Math.round(Math.max((double) Math.abs(event.getRawX() - ResizableStickerView.this.scale_orgX), (double) Math.abs(event.getRawY() - ResizableStickerView.this.scale_orgY)));
                        layoutParams = (LayoutParams) ResizableStickerView.this.getLayoutParams();
                        layoutParams.width = (int) (((double) layoutParams.width) - offset);
                        layoutParams = (LayoutParams) ResizableStickerView.this.getLayoutParams();
                        layoutParams.height = (int) (((double) layoutParams.height) - offset);
                    }
                    ResizableStickerView.this.scale_orgX = event.getRawX();
                    ResizableStickerView.this.scale_orgY = event.getRawY();
                    ResizableStickerView.this.postInvalidate();
                    ResizableStickerView.this.requestLayout();
                    break;
            }
            return true;
        }
    };
    private OnTouchListener mTouchListener1 = new OnTouchListener() {
        @SuppressLint({"NewApi"})
        public boolean onTouch(View view, MotionEvent event) {
            ResizableStickerView rl = (ResizableStickerView) view.getParent();
            int j = (int) event.getRawX();
            int i = (int) event.getRawY();
            LayoutParams layoutParams = (LayoutParams) ResizableStickerView.this.getLayoutParams();
            switch (event.getAction()) {
                case 0:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onScaleDown(ResizableStickerView.this);
                    }
                    ResizableStickerView.this.invalidate();
                    ResizableStickerView.this.basex = j;
                    ResizableStickerView.this.basey = i;
                    ResizableStickerView.this.basew = ResizableStickerView.this.getWidth();
                    ResizableStickerView.this.baseh = ResizableStickerView.this.getHeight();
                    ResizableStickerView.this.getLocationOnScreen(new int[2]);
                    ResizableStickerView.this.margl = layoutParams.leftMargin;
                    ResizableStickerView.this.margt = layoutParams.topMargin;
                    break;
                case 1:
                    ResizableStickerView.this.wi = ResizableStickerView.this.getLayoutParams().width;
                    ResizableStickerView.this.he = ResizableStickerView.this.getLayoutParams().height;
                    ResizableStickerView.this.leftMargin = ((LayoutParams) ResizableStickerView.this.getLayoutParams()).leftMargin;
                    ResizableStickerView.this.topMargin = ((LayoutParams) ResizableStickerView.this.getLayoutParams()).topMargin;
                    ResizableStickerView.this.field_two = String.valueOf(ResizableStickerView.this.leftMargin) + "," + String.valueOf(ResizableStickerView.this.topMargin);
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onScaleUp(ResizableStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onScaleMove(ResizableStickerView.this);
                    }
                    float f2 = (float) Math.toDegrees(Math.atan2((double) (i - ResizableStickerView.this.basey), (double) (j - ResizableStickerView.this.basex)));
                    float f1 = f2;
                    if (f2 < 0.0f) {
                        f1 = f2 + 360.0f;
                    }
                    j -= ResizableStickerView.this.basex;
                    int k = i - ResizableStickerView.this.basey;
                    i = (int) (Math.sqrt((double) ((j * j) + (k * k))) * Math.cos(Math.toRadians((double) (f1 - ResizableStickerView.this.getRotation()))));
                    j = (int) (Math.sqrt((double) ((i * i) + (k * k))) * Math.sin(Math.toRadians((double) (f1 - ResizableStickerView.this.getRotation()))));
                    k = (i * 2) + ResizableStickerView.this.basew;
                    int m = (j * 2) + ResizableStickerView.this.baseh;
                    if (k > ResizableStickerView.this.s2) {
                        layoutParams.width = k;
                        layoutParams.leftMargin = ResizableStickerView.this.margl - i;
                    }
                    if (m > ResizableStickerView.this.s2) {
                        layoutParams.height = m;
                        layoutParams.topMargin = ResizableStickerView.this.margt - j;
                    }
                    ResizableStickerView.this.setLayoutParams(layoutParams);
                    ResizableStickerView.this.performLongClick();
                    break;
            }
            return true;
        }
    };
    public ImageView main_iv;
    private int margin;
    int margl;
    int margt;
    double onTouchAngle = 0.0d;
    private OnTouchListener rTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            ResizableStickerView rl = (ResizableStickerView) view.getParent();
            switch (event.getAction()) {
                case 0:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onRotateDown(ResizableStickerView.this);
                    }
                    Rect rect = new Rect();
                    ((View) view.getParent()).getGlobalVisibleRect(rect);
                    ResizableStickerView.this.cX = rect.exactCenterX();
                    ResizableStickerView.this.cY = rect.exactCenterY();
                    ResizableStickerView.this.vAngle = (double) ((View) view.getParent()).getRotation();
                    ResizableStickerView.this.tAngle = (Math.atan2((double) (ResizableStickerView.this.cY - event.getRawY()), (double) (ResizableStickerView.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    ResizableStickerView.this.dAngle = ResizableStickerView.this.vAngle - ResizableStickerView.this.tAngle;
                    break;
                case 1:
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onRotateUp(ResizableStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (ResizableStickerView.this.listener != null) {
                        ResizableStickerView.this.listener.onRotateMove(ResizableStickerView.this);
                    }
                    ResizableStickerView.this.angle = (Math.atan2((double) (ResizableStickerView.this.cY - event.getRawY()), (double) (ResizableStickerView.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    float rotation = (float) (ResizableStickerView.this.angle + ResizableStickerView.this.dAngle);
                    ((View) view.getParent()).setRotation(rotation);
                    ((View) view.getParent()).invalidate();
                    ((View) view.getParent()).requestLayout();
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
                    ((View) view.getParent()).setRotation(rotation);
                    break;
            }
            return true;
        }
    };
    private Uri resUri = null;
    private ImageView rotate_iv;
    private float rotation;
    private int s;
    private int s2;
    Animation scale;
    private int scaleRotateProg = 0;
    private ImageView scale_iv;
    private double scale_orgHeight = -1.0d;
    private double scale_orgWidth = -1.0d;
    private float scale_orgX = -1.0f;
    private float scale_orgY = -1.0f;
    int screenHeight = 300;
    int screenWidth = 300;
    private String stkr_path = "";
    double tAngle = 0.0d;
    private float this_orgX = -1.0f;
    private float this_orgY = -1.0f;
    private int topMargin = 0;
    double vAngle = 0.0d;
    private int wi;
    float widthMain = 0.0f;
    private int xRotateProg = 0;
    private int yRotateProg = 0;
    private float yRotation = 0.0f;
    private int zRotateProg = 0;
    Animation zoomInScale;
    Animation zoomOutScale;

    public interface TouchEventListener {
        byte[] getResBytes(Context context, String str);

        void onCenterX(View view);

        void onCenterXY(View view);

        void onCenterY(View view);

        void onDelete();

        void onEdit(View view, Uri uri);

        void onOtherXY(View view);

        void onRotateDown(View view);

        void onRotateMove(View view);

        void onRotateUp(View view);

        void onScaleDown(View view);

        void onScaleMove(View view);

        void onScaleUp(View view);

        void onTouchDown(View view);

        void onTouchMove(View view);

        void onTouchUp(View view);
    }

    public ResizableStickerView setOnTouchCallbackListener(TouchEventListener l) {
        this.listener = l;
        return this;
    }

    public ResizableStickerView(Context context) {
        super(context);
        init(context);
    }

    public ResizableStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ResizableStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context ctx) {
        this.context = ctx;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.rotate_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.margin = dpToPx(this.context, 5);
        this.s = dpToPx(this.context, 25);
        this.s2 = dpToPx(this.context, 55);
        this.wi = dpToPx(this.context, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.he = dpToPx(this.context, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.scale_iv.setImageResource(R.drawable.sticker_scale);
        this.border_iv.setImageResource(R.drawable.sticker_border_gray);
        this.flip_iv.setImageResource(R.drawable.sticker_flip);
        this.rotate_iv.setImageResource(R.drawable.rotate);
        this.delete_iv.setImageResource(R.drawable.sticker_delete1);
        LayoutParams lp = new LayoutParams(this.wi, this.he);
        LayoutParams mlp = new LayoutParams(-1, -1);
        mlp.setMargins(this.s, this.s, this.s, this.s);
        mlp.addRule(17);
        LayoutParams slp = new LayoutParams(this.s, this.s);
        slp.addRule(12);
        slp.addRule(11);
        slp.setMargins(this.margin, this.margin, this.margin, this.margin);
        LayoutParams flp = new LayoutParams(this.s, this.s);
        flp.addRule(10);
        flp.addRule(11);
        flp.setMargins(this.margin, this.margin, this.margin, this.margin);
        LayoutParams elp = new LayoutParams(this.s, this.s);
        elp.addRule(12);
        elp.addRule(9);
        elp.setMargins(this.margin, this.margin, this.margin, this.margin);
        LayoutParams dlp = new LayoutParams(this.s, this.s);
        dlp.addRule(10);
        dlp.addRule(9);
        dlp.setMargins(this.margin, this.margin, this.margin, this.margin);
        LayoutParams blp = new LayoutParams(-1, -1);
        setLayoutParams(lp);
        setBackgroundResource(R.drawable.textlib_border_gray);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(blp);
        this.border_iv.setScaleType(ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.main_iv);
        this.main_iv.setLayoutParams(mlp);
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(flp);
        this.flip_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((double) ResizableStickerView.this.yRotation) == 0.0d) {
                    ResizableStickerView.this.yRotation = 1.0f;
                } else {
                    ResizableStickerView.this.yRotation = 0.0f;
                }
                if (!ResizableStickerView.this.stkr_path.equals("")) {
                    ResizableStickerView.this.setStrPath(ResizableStickerView.this.stkr_path, false);
                } else if (ResizableStickerView.this.drawableId.equals("0")) {
                    ResizableStickerView.this.addStkrBitmap(false);
                } else {
                    ResizableStickerView.this.setBgDrawable(ResizableStickerView.this.drawableId, false);
                }
            }
        });
        addView(this.rotate_iv);
        this.rotate_iv.setLayoutParams(elp);
        this.rotate_iv.setOnTouchListener(this.rTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(dlp);
        this.delete_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final ViewGroup parent = (ViewGroup) ResizableStickerView.this.getParent();
                ResizableStickerView.this.zoomInScale.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        parent.removeView(ResizableStickerView.this);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                ResizableStickerView.this.main_iv.startAnimation(ResizableStickerView.this.zoomInScale);
                ResizableStickerView.this.setBorderVisibility(false);
                if (ResizableStickerView.this.listener != null) {
                    ResizableStickerView.this.listener.onDelete();
                }
            }
        });
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(slp);
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.scale_iv.setTag("scale_iv");
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_anim);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in);
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
    }

    public boolean setDefaultTouchListener(boolean enable) {
        if (enable) {
            this.lockStatus = "UNLOCKED";
            setOnTouchListener(new MultiTouchListener(this.context).enableRotation(true).setOnTouchCallbackListener(this));
            return true;
        }
        this.lockStatus = "LOCKED";
        setOnTouchListener(null);
        return false;
    }

    public void setBorderVisibility(boolean ch) {
        this.isBorderVisible = ch;
        if (!ch) {
            this.border_iv.setVisibility(View.GONE);
            this.scale_iv.setVisibility(View.GONE);
            this.flip_iv.setVisibility(View.GONE);
            this.rotate_iv.setVisibility(View.GONE);
            this.delete_iv.setVisibility(View.GONE);
            setBackgroundResource(0);
            if (this.isColorFilterEnable) {
                this.main_iv.setColorFilter(Color.parseColor("#303828"));
            }
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(View.VISIBLE);
            this.scale_iv.setVisibility(View.VISIBLE);
            this.flip_iv.setVisibility(View.VISIBLE);
            this.rotate_iv.setVisibility(View.VISIBLE);
            this.delete_iv.setVisibility(View.VISIBLE);
            setBackgroundResource(R.drawable.textlib_border_gray);
            this.main_iv.startAnimation(this.scale);
        }
    }

    public boolean getBorderVisbilty() {
        return this.isBorderVisible;
    }

    public void opecitySticker(int process) {
        try {
            this.main_iv.setAlpha(((float) process) / 100.0f);
            this.imgAlpha = process;
        } catch (Exception e) {
        }
    }

    public int getHueProg() {
        return this.hueProg;
    }

    public void setHueProg(int hueProg) {
        this.hueProg = hueProg;
        if (this.hueProg == 0) {
            this.main_iv.setColorFilter(-1);
        } else if (this.hueProg == 360) {
            this.main_iv.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
        } else if (hueProg < 1 || hueProg > 5) {
            this.main_iv.setColorFilter(ColorFilterGenerator.adjustHue((float) hueProg));
        } else {
            this.main_iv.setColorFilter(0);
        }
    }

    public int getXRotateProg() {
        return this.xRotateProg;
    }

    public int getYRotateProg() {
        return this.yRotateProg;
    }

    public int getZRotateProg() {
        return this.zRotateProg;
    }

    public int geScaleProg() {
        return this.scaleRotateProg;
    }

    public void setStickerRotateProg(int xRotateProg, int yRotateProg, int zRotateProg, int x, int y, int z) {
        this.xRotateProg = x;
        this.yRotateProg = y;
        this.zRotateProg = z;
        applyTransformation(xRotateProg, yRotateProg, zRotateProg);
    }

    public void setScaleViewProg(int scale) {
        this.scaleRotateProg = scale;
        float scal = ((float) scale) / 10.0f;
        this.main_iv.setScaleX(scal);
        this.main_iv.setScaleY(scal);
    }

    public String getColorType() {
        return this.colorType;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }

    public int getAlphaProg() {
        return this.alphaProg;
    }

    public void setAlphaProg(int alphaProg) {
        this.alphaProg = alphaProg;
        this.main_iv.setAlpha(((float) alphaProg) / 100.0f);
    }

    public int getColor() {
        return this.imgColor;
    }

    public void setColor(int color) {
        try {
            this.main_iv.setColorFilter(color);
            this.imgColor = color;
        } catch (Exception e) {
        }
    }

    public void setBgDrawable(String redId1, boolean viewStkr) {
        this.drawableId = redId1;
        addStkrBitmap(viewStkr);
    }

    public void setStrPath(String stkr_path1, boolean viewStkr) {
        Uri uri = Uri.parse(stkr_path1);
        if (this.yRotation != 0.0f) {
            Glide.with(this.context).load(uri.toString()).dontAnimate().override(dpToPx(this.context, 300), dpToPx(this.context, 300)).transform(new MyTransformation(this.context, true)).placeholder(R.drawable.loading2).error(R.drawable.error2).into(this.main_iv);
        } else {
            Glide.with(this.context).load(uri.toString()).dontAnimate().override(dpToPx(this.context, 300), dpToPx(this.context, 300)).placeholder(R.drawable.loading2).error(R.drawable.error2).into(this.main_iv);
        }
        this.stkr_path = stkr_path1;
        if (viewStkr) {
            this.main_iv.startAnimation(this.zoomOutScale);
        }
    }

    protected void applyTransformation(int angle1, int angle2, int angle3) {
        this.main_iv.setRotationX((float) angle1);
        this.main_iv.setRotationY((float) angle2);
        this.main_iv.setRotation((float) angle3);
        setVisibility(View.VISIBLE);
        this.main_iv.setVisibility(View.VISIBLE);
        this.main_iv.requestLayout();
        this.main_iv.postInvalidate();
        requestLayout();
        postInvalidate();
    }

    public void setComponentInfo(ComponentInfo ci) {
        this.wi = ci.getWIDTH();
        this.he = ci.getHEIGHT();
        this.drawableId = ci.getRES_ID();
        this.resUri = ci.getRES_URI();
        this.btmp = ci.getBITMAP();
        this.rotation = ci.getROTATION();
        this.imgColor = ci.getSTC_COLOR();
        this.yRotation = ci.getY_ROTATION();
        this.alphaProg = ci.getSTC_OPACITY();
        this.stkr_path = ci.getSTKR_PATH();
        this.colorType = ci.getCOLORTYPE();
        this.hueProg = ci.getSTC_HUE();
        this.lockStatus = ci.getFIELD_THREE();
        this.field_two = ci.getFIELD_TWO();
        this.xRotateProg = ci.getXRotateProg();
        this.yRotateProg = ci.getYRotateProg();
        this.zRotateProg = ci.getZRotateProg();
        this.scaleRotateProg = ci.getScaleProg();
        applyTransformation(45 - this.xRotateProg, 45 - this.yRotateProg, 180 - this.zRotateProg);
        if (!this.stkr_path.equals("")) {
            setStrPath(this.stkr_path, true);
        } else if (this.drawableId.equals("0")) {
            addStkrBitmap(true);
        } else {
            setBgDrawable(this.drawableId, true);
        }
        if (this.colorType.equals("white")) {
            setColor(this.imgColor);
        } else {
            setHueProg(this.hueProg);
        }
        setRotation(this.rotation);
        setScaleViewProg(this.scaleRotateProg);
        setAlphaProg(this.alphaProg);
        if (this.field_two.equals("")) {
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(ci.getPOS_X());
            setY(ci.getPOS_Y());
        } else {
            String[] parts = this.field_two.split(",");
            int leftMergin = Integer.parseInt(parts[0]);
            int topMergin = Integer.parseInt(parts[1]);
            ((LayoutParams) getLayoutParams()).leftMargin = leftMergin;
            ((LayoutParams) getLayoutParams()).topMargin = topMergin;
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(ci.getPOS_X() + ((float) (leftMergin * -1)));
            setY(ci.getPOS_Y() + ((float) (topMergin * -1)));
        }
        if (ci.getTYPE() == "SHAPE") {
            this.flip_iv.setVisibility(View.GONE);
            this.isSticker = false;
        }
        if (ci.getTYPE() == "STICKER") {
            this.flip_iv.setVisibility(View.VISIBLE);
            this.isSticker = true;
        }
        if (this.lockStatus.equals("LOCKED")) {
            this.isMultiTouchEnabled = setDefaultTouchListener(false);
        } else {
            this.isMultiTouchEnabled = setDefaultTouchListener(true);
        }
    }

    protected void addStkrBitmap(boolean viewStkr) {
        if (this.drawableId.equals("0")) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.btmp.compress(CompressFormat.PNG, 100, stream);
            if (this.yRotation != 0.0f) {
                Glide.with(this.context).asBitmap().load(stream.toByteArray()).override(dpToPx(this.context, 300), dpToPx(this.context, 300)).placeholder(R.drawable.loading2).error(R.drawable.error2).transform(new MyTransformation(this.context, true)).into(this.main_iv);
            } else {
                Glide.with(this.context).asBitmap().load(stream.toByteArray()).override(dpToPx(this.context, 300), dpToPx(this.context, 300)).placeholder(R.drawable.loading2).error(R.drawable.error2).into(this.main_iv);
            }
        } else if (this.listener != null) {
            byte[] resByte = this.listener.getResBytes(this.context, this.drawableId);
            if (this.yRotation != 0.0f) {
                Glide.with(this.context).load(resByte).dontAnimate().override(dpToPx(this.context, 300), dpToPx(this.context, 300)).transform(new MyTransformation(this.context, true)).placeholder(R.drawable.loading2).error(R.drawable.error2).into(this.main_iv);
            } else {
                Glide.with(this.context).load(resByte).dontAnimate().override(dpToPx(this.context, 300), dpToPx(this.context, 300)).placeholder(R.drawable.loading2).error(R.drawable.error2).into(this.main_iv);
            }
        }
        if (viewStkr) {
            this.main_iv.startAnimation(this.zoomOutScale);
        }
    }

    public void optimize(float wr, float hr) {
    }

    public void optimizeScreen(float wr, float hr) {
        this.screenHeight = (int) hr;
        this.screenWidth = (int) wr;
    }

    public void setMainLayoutWH(float wMLay, float hMLay) {
        this.widthMain = wMLay;
        this.heightMain = hMLay;
    }

    public float getMainWidth() {
        return this.widthMain;
    }

    public float getMainHeight() {
        return this.heightMain;
    }

    public void incrX() {
        setX(getX() + 1.0f);
    }

    public void decX() {
        setX(getX() - 1.0f);
    }

    public void incrY() {
        setY(getY() + 1.0f);
    }

    public void decY() {
        setY(getY() - 1.0f);
    }

    public ComponentInfo getComponentInfo() {
        if (this.btmp != null) {
            this.stkr_path = saveBitmapObject1(this.btmp);
        }
        ComponentInfo ci = new ComponentInfo();
        ci.setPOS_X(getX());
        ci.setPOS_Y(getY());
        ci.setWIDTH(this.wi);
        ci.setHEIGHT(this.he);
        ci.setRES_ID(this.drawableId);
        ci.setSTC_COLOR(this.imgColor);
        ci.setRES_URI(this.resUri);
        ci.setSTC_OPACITY(this.alphaProg);
        ci.setCOLORTYPE(this.colorType);
        ci.setBITMAP(this.btmp);
        ci.setROTATION(getRotation());
        ci.setY_ROTATION(this.yRotation);
        ci.setXRotateProg(this.xRotateProg);
        ci.setYRotateProg(this.yRotateProg);
        ci.setZRotateProg(this.zRotateProg);
        ci.setScaleProg(this.scaleRotateProg);
        ci.setSTKR_PATH(this.stkr_path);
        ci.setSTC_HUE(this.hueProg);
        ci.setFIELD_ONE(this.field_one);
        ci.setFIELD_TWO(this.field_two);
        ci.setFIELD_THREE(this.lockStatus);
        ci.setFIELD_FOUR(this.field_four);
        return ci;
    }

    private String saveBitmapObject1(Bitmap bitmap) {
        String temp_path = "";
        File myDir = StorageConfiguration.getDesignPath();
        myDir.mkdirs();
        File file1 = new File(myDir, "IMG_" + System.currentTimeMillis() + ".png");
        temp_path = file1.getAbsolutePath();
        if (file1.exists()) {
            file1.delete();
        }
        try {
            FileOutputStream ostream = new FileOutputStream(file1);
            bitmap.compress(CompressFormat.PNG, 100, ostream);
            ostream.close();
            return temp_path;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return "";
        }
    }

    public int dpToPx(Context c, int dp) {
        float f = (float) dp;
        c.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2.0d) + Math.pow(x2 - x1, 2.0d));
    }

    public void enableColorFilter(boolean b) {
        this.isColorFilterEnable = b;
    }

    public void onTouchCallback(View v) {
        if (this.listener != null) {
            this.listener.onTouchDown(v);
        }
    }

    public void onTouchUpCallback(View v) {
        if (this.listener != null) {
            this.listener.onTouchUp(v);
        }
    }

    public void onTouchMoveCallback(View v) {
        if (this.listener != null) {
            this.listener.onTouchMove(v);
        }
    }

    public void onCenterPosX(View v) {
        if (this.listener != null) {
            this.listener.onCenterX(v);
        }
    }

    public void onCenterPosY(View v) {
        if (this.listener != null) {
            this.listener.onCenterY(v);
        }
    }

    public void onCenterPosXY(View v) {
        if (this.listener != null) {
            this.listener.onCenterXY(v);
        }
    }

    public void onOtherPos(View v) {
        if (this.listener != null) {
            this.listener.onOtherXY(v);
        }
    }
}
