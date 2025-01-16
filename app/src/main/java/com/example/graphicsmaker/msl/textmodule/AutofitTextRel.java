package com.example.graphicsmaker.msl.textmodule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import com.example.graphicsmaker.R;
import com.example.graphicsmaker.msl.textmodule.listener.MultiTouchListener;
import com.example.graphicsmaker.msl.textmodule.listener.MultiTouchListener.TouchCallbackListener;

public class AutofitTextRel extends RelativeLayout implements TouchCallbackListener {
    double angle = 0.0d;
    private ImageView background_iv;
    int baseh;
    int basew;
    int basex;
    int basey;
    private int bgAlpha = 255;
    private int bgColor = 0;
    private String bgDrawable = "0";
    private ImageView border_iv;
    private int btnmargin;
    private int btnsize;
    float cX = 0.0f;
    float cY = 0.0f;
    private Context context;
    double dAngle = 0.0d;
    private ImageView delete_iv;
    private String field_four = "";
    private int field_one = 0;
    private String field_two = "0,0";
    private String fontName = "";
    private GestureDetector gd = null;
    private int he;
    float heightMain = 0.0f;
    private int imgAlpha = 100;
    private boolean isBorderVisible = false;
    public boolean isMultiTouchEnabled = true;
    private int leftMargin = 0;
    private int limitsize;
    private TouchEventListener listener = null;
    String lockStatus = "UNLOCKED";
    private OnTouchListener mTouchListener1 = new OnTouchListener() {
        @SuppressLint({"NewApi", "ClickableViewAccessibility"})
        public boolean onTouch(View view, MotionEvent event) {
            AutofitTextRel rl = (AutofitTextRel) view.getParent();
            int j = (int) event.getRawX();
            int i = (int) event.getRawY();
            LayoutParams layoutParams = (LayoutParams) AutofitTextRel.this.getLayoutParams();
            switch (event.getAction()) {
                case 0:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onScaleDown(AutofitTextRel.this);
                    }
                    AutofitTextRel.this.invalidate();
                    AutofitTextRel.this.basex = j;
                    AutofitTextRel.this.basey = i;
                    AutofitTextRel.this.basew = AutofitTextRel.this.getWidth();
                    AutofitTextRel.this.baseh = AutofitTextRel.this.getHeight();
                    AutofitTextRel.this.getLocationOnScreen(new int[2]);
                    AutofitTextRel.this.margl = layoutParams.leftMargin;
                    AutofitTextRel.this.margt = layoutParams.topMargin;
                    break;
                case 1:
                    AutofitTextRel.this.wi = AutofitTextRel.this.getLayoutParams().width;
                    AutofitTextRel.this.he = AutofitTextRel.this.getLayoutParams().height;
                    AutofitTextRel.this.field_two = String.valueOf(AutofitTextRel.this.leftMargin) + "," + String.valueOf(AutofitTextRel.this.topMargin);
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onScaleUp(AutofitTextRel.this);
                        break;
                    }
                    break;
                case 2:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onScaleMove(AutofitTextRel.this);
                    }
                    float f2 = (float) Math.toDegrees(Math.atan2((double) (i - AutofitTextRel.this.basey), (double) (j - AutofitTextRel.this.basex)));
                    float f1 = f2;
                    if (f2 < 0.0f) {
                        f1 = f2 + 360.0f;
                    }
                    j -= AutofitTextRel.this.basex;
                    int k = i - AutofitTextRel.this.basey;
                    i = (int) (Math.sqrt((double) ((j * j) + (k * k))) * Math.cos(Math.toRadians((double) (f1 - AutofitTextRel.this.getRotation()))));
                    j = (int) (Math.sqrt((double) ((i * i) + (k * k))) * Math.sin(Math.toRadians((double) (f1 - AutofitTextRel.this.getRotation()))));
                    k = (i * 2) + AutofitTextRel.this.basew;
                    int m = (j * 2) + AutofitTextRel.this.baseh;
                    if (k > AutofitTextRel.this.limitsize && ((float) k) < AutofitTextRel.this.widthMain) {
                        layoutParams.width = k;
                        layoutParams.leftMargin = AutofitTextRel.this.margl - i;
                    }
                    if (m > AutofitTextRel.this.limitsize && ((float) m) < AutofitTextRel.this.heightMain) {
                        layoutParams.height = m;
                        layoutParams.topMargin = AutofitTextRel.this.margt - j;
                    }
                    AutofitTextRel.this.setLayoutParams(layoutParams);
                    if (!AutofitTextRel.this.bgDrawable.equals("0")) {
                        AutofitTextRel.this.wi = AutofitTextRel.this.getLayoutParams().width;
                        AutofitTextRel.this.he = AutofitTextRel.this.getLayoutParams().height;
                        AutofitTextRel.this.setBgDrawable(AutofitTextRel.this.bgDrawable);
                        break;
                    }
                    break;
            }
            return true;
        }
    };
    private int margin;
    int margl;
    int margt;
    Paint paint;
    int posX;
    int posY;
    private int prog_20 = 0;
    private int prog_40 = 0;
    private int prog_50 = 0;
    private int prog_60 = 0;
    private int prog_80;
    private int progress = 0;
    private OnTouchListener rTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            AutofitTextRel rl = (AutofitTextRel) view.getParent();
            switch (event.getAction()) {
                case 0:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onRotateDown(AutofitTextRel.this);
                    }
                    Rect rect = new Rect();
                    ((View) view.getParent()).getGlobalVisibleRect(rect);
                    AutofitTextRel.this.cX = rect.exactCenterX();
                    AutofitTextRel.this.cY = rect.exactCenterY();
                    AutofitTextRel.this.vAngle = (double) ((View) view.getParent()).getRotation();
                    AutofitTextRel.this.tAngle = (Math.atan2((double) (AutofitTextRel.this.cY - event.getRawY()), (double) (AutofitTextRel.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    AutofitTextRel.this.dAngle = AutofitTextRel.this.vAngle - AutofitTextRel.this.tAngle;
                    break;
                case 1:
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onRotateUp(AutofitTextRel.this);
                        break;
                    }
                    break;
                case 2:
                    if (rl != null) {
                        rl.requestDisallowInterceptTouchEvent(true);
                    }
                    if (AutofitTextRel.this.listener != null) {
                        AutofitTextRel.this.listener.onRotateMove(AutofitTextRel.this);
                    }
                    AutofitTextRel.this.angle = (Math.atan2((double) (AutofitTextRel.this.cY - event.getRawY()), (double) (AutofitTextRel.this.cX - event.getRawX())) * 180.0d) / 3.141592653589793d;
                    float rotation = (float) (AutofitTextRel.this.angle + AutofitTextRel.this.dAngle);
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
    private RelativeLayout rel_artv;
    private ImageView rotate_iv;
    private float rotation;
    Animation scale;
    private ImageView scale_iv;
    private int shadowColor = 0;
    private int shadowProg = 0;
    private String stkr_path = "";
    private int tAlpha = 100;
    double tAngle = 0.0d;
    private int tColor = Color.parseColor("#000000");
    private String text = "";
    private TextRelativeDraw textRelative;
    public AutoResizeTextView text_iv;
    private int topMargin = 0;
    private String txtGravity = "C";
    double vAngle = 0.0d;
    private int wi;
    float widthMain = 0.0f;
    private int xRotateProg = 0;
    private int yRotateProg = 0;
    private int zRotateProg = 0;
    Animation zoomInScale;
    Animation zoomOutScale;

    public interface TouchEventListener {
        void onCenterX(View view);

        void onCenterXY(View view);

        void onCenterY(View view);

        void onDelete();

        void onDoubleTap();

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

    public AutofitTextRel setOnTouchCallbackListener(TouchEventListener l) {
        this.listener = l;
        this.textRelative.invalidate();
        return this;
    }

    public AutofitTextRel(Context context) {
        super(context);
        init(context);
        invalidate();
    }

    public AutofitTextRel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutofitTextRel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init(Context ctx) {
        this.paint = new Paint();
        this.paint.setColor(0);
        this.context = ctx;
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.background_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.rotate_iv = new ImageView(this.context);
        this.text_iv = new AutoResizeTextView(this.context);
        this.rel_artv = new RelativeLayout(this.context);
        this.textRelative = new TextRelativeDraw(this.context, this.rel_artv);
        this.margin = dpToPx(this.context, 25);
        this.btnmargin = dpToPx(this.context, 5);
        this.btnsize = dpToPx(this.context, 25);
        this.limitsize = dpToPx(this.context, 55);
        this.wi = dpToPx(this.context, 300);
        this.he = dpToPx(this.context, 300);
        this.scale_iv.setImageResource(R.drawable.textlib_scale);
        this.background_iv.setImageResource(0);
        this.rotate_iv.setImageResource(R.drawable.rotate);
        this.delete_iv.setImageResource(R.drawable.textlib_clear);
        LayoutParams lp = new LayoutParams(this.wi, this.he);
        LayoutParams bglp = new LayoutParams(-1, -1);
        LayoutParams slp = new LayoutParams(this.btnsize, this.btnsize);
        slp.addRule(12);
        slp.addRule(11);
        slp.setMargins(this.btnmargin, this.btnmargin, this.btnmargin, this.btnmargin);
        LayoutParams tlp = new LayoutParams(-1, -1);
        tlp.setMargins(0, 0, 0, 0);
        tlp.addRule(17);
        LayoutParams tlp_textrel = new LayoutParams(-1, -1);
        tlp_textrel.setMargins(this.margin, this.margin, this.margin, this.margin);
        tlp_textrel.addRule(17);
        LayoutParams tlp_drawrel = new LayoutParams(-1, -1);
        tlp_drawrel.setMargins(this.margin, this.margin, this.margin, this.margin);
        tlp_drawrel.addRule(17);
        LayoutParams dlp = new LayoutParams(this.btnsize, this.btnsize);
        dlp.addRule(10);
        dlp.addRule(9);
        dlp.setMargins(this.btnmargin, this.btnmargin, this.btnmargin, this.btnmargin);
        LayoutParams blp = new LayoutParams(-1, -1);
        setLayoutParams(lp);
        setBackgroundResource(R.drawable.textlib_border_gray);
        LayoutParams elp = new LayoutParams(this.btnsize, this.btnsize);
        elp.addRule(12);
        elp.addRule(9);
        elp.setMargins(this.btnmargin, this.btnmargin, this.btnmargin, this.btnmargin);
        this.background_iv.setLayoutParams(bglp);
        this.background_iv.setScaleType(ScaleType.FIT_XY);
        addView(this.background_iv);
        this.text_iv.setText(this.text);
        this.text_iv.setTextColor(this.tColor);
        this.text_iv.setTextSize(1000.0f);
        this.text_iv.setLayoutParams(tlp);
        this.text_iv.setPadding(0, 0, 0, 0);
        this.text_iv.setGravity(17);
        this.text_iv.setMinTextSize(5.0f);
        this.rel_artv.setLayoutParams(tlp_textrel);
        this.rel_artv.addView(this.text_iv);
        addView(this.rel_artv);
        this.textRelative.setLayoutParams(tlp_drawrel);
        addView(this.textRelative);
        this.rel_artv.setVisibility(View.INVISIBLE);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(blp);
        this.border_iv.setTag("border_iv");
        addView(this.rotate_iv);
        this.rotate_iv.setLayoutParams(elp);
        this.rotate_iv.setOnTouchListener(this.rTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(dlp);
        this.delete_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final ViewGroup parent = (ViewGroup) AutofitTextRel.this.getParent();
                AutofitTextRel.this.zoomInScale.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        parent.removeView(AutofitTextRel.this);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                AutofitTextRel.this.background_iv.startAnimation(AutofitTextRel.this.zoomInScale);
                AutofitTextRel.this.textRelative.startAnimation(AutofitTextRel.this.zoomInScale);
                AutofitTextRel.this.setBorderVisibility(false);
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onDelete();
                }
            }
        });
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(slp);
        this.scale_iv.setTag("scale_iv");
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.textlib_scale_anim);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.textlib_scale_zoom_out);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.textlib_scale_zoom_in);
        initGD();
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
    }

    public boolean setDefaultTouchListener(boolean enable) {
        if (enable) {
            this.lockStatus = "UNLOCKED";
            setOnTouchListener(new MultiTouchListener(this.context).enableRotation(true).setOnTouchCallbackListener(this).setGestureListener(this.gd));
            return true;
        }
        this.lockStatus = "LOCKED";
        setOnTouchListener(null);
        return false;
    }

    public void refreshText() {
        this.text_iv.post(new Runnable() {
            public void run() {
                AutofitTextRel.this.text_iv.requestLayout();
                AutofitTextRel.this.text_iv.postInvalidate();
                AutofitTextRel.this.rel_artv.post(new Runnable() {
                    public void run() {
                        AutofitTextRel.this.rel_artv.requestLayout();
                        AutofitTextRel.this.rel_artv.postInvalidate();
                        AutofitTextRel.this.post(new Runnable() {
                            public void run() {
                                AutofitTextRel.this.requestLayout();
                                AutofitTextRel.this.postInvalidate();
                            }
                        });
                    }
                });
            }
        });
    }

    public void setBorderVisibility(boolean ch) {
        this.isBorderVisible = ch;
        if (!ch) {
            this.border_iv.setVisibility(View.GONE);
            this.scale_iv.setVisibility(View.GONE);
            this.delete_iv.setVisibility(View.GONE);
            this.rotate_iv.setVisibility(View.GONE);
            setBackgroundResource(0);
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(View.VISIBLE);
            this.scale_iv.setVisibility(View.VISIBLE);
            this.delete_iv.setVisibility(View.VISIBLE);
            this.rotate_iv.setVisibility(View.VISIBLE);
            setBackgroundResource(R.drawable.textlib_border_gray);
            this.textRelative.startAnimation(this.scale);
            this.textRelative.invalidate();
        }
    }

    public boolean getBorderVisibility() {
        return this.isBorderVisible;
    }

    public void setText(String text) {
        this.text = text;
        this.text_iv.setText(text);
        this.textRelative.post(new Runnable() {
            public void run() {
                AutofitTextRel.this.textRelative.startAnimation(AutofitTextRel.this.zoomOutScale);
            }
        });
    }

    public String getText() {
        return this.text_iv.getText().toString();
    }

    public void setTextFont(String fontName) {
        try {
            Typeface ttf;
            if (fontName.equals("default") || fontName.equals("")) {
                ttf = Typeface.DEFAULT;
            } else {
                ttf = Typeface.createFromAsset(this.context.getAssets(), fontName);
            }
            this.text_iv.setTypeface(ttf);
            this.fontName = fontName;
            this.textRelative.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTextColor() {
        return this.tColor;
    }

    public int getTextAlpha() {
        return this.tAlpha;
    }

    public int getBgAlpha() {
        return this.bgAlpha;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setTextColor(int color) {
        this.text_iv.setTextColor(color);
        this.tColor = color;
        this.textRelative.invalidate();
    }

    public void setTextAlpha(int prog) {
        this.text_iv.setAlpha(((float) prog) / 100.0f);
        this.tAlpha = prog;
        this.textRelative.invalidate();
    }

    public void setTextShadowColor(int color) {
        this.shadowColor = color;
        this.text_iv.setShadowLayer((float) this.shadowProg, 0.0f, 0.0f, this.shadowColor);
        this.textRelative.invalidate();
    }

    public int getTextShadowColor() {
        return this.shadowColor;
    }

    public void setTextShadowProg(int prog) {
        this.shadowProg = prog;
        this.text_iv.setShadowLayer((float) this.shadowProg, 0.0f, 0.0f, this.shadowColor);
        this.textRelative.invalidate();
    }

    public int getTextShadowProg() {
        return this.shadowProg;
    }

    public void setBgDrawable(String did) {
        this.bgDrawable = did;
        this.bgColor = 0;
        this.background_iv.setImageBitmap(getTiledBitmap(this.context, getResources().getIdentifier(did, "drawable", this.context.getPackageName()), this.wi, this.he));
        this.background_iv.setBackgroundColor(this.bgColor);
    }

    public String getBgDrawable() {
        return this.bgDrawable;
    }

    public void setBgColor(int c) {
        this.bgDrawable = "0";
        this.bgColor = c;
        this.background_iv.setImageBitmap(null);
        this.background_iv.setBackgroundColor(c);
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setBgAlpha(int prog) {
        this.background_iv.setAlpha(((float) prog) / 255.0f);
        this.bgAlpha = prog;
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

    public void setTextGravity(String gravityIs) {
        this.txtGravity = gravityIs;
        if (gravityIs == null) {
            this.text_iv.setGravity(17);
        } else if (gravityIs.equals("L")) {
            this.text_iv.setGravity(19);
        } else if (gravityIs.equals("R")) {
            this.text_iv.setGravity(21);
        } else {
            this.text_iv.setGravity(17);
        }
        this.textRelative.invalidate();
    }

    public String getTextGravity() {
        return this.txtGravity;
    }

    public void setTextRotateProg(int xRotateProg, int yRotateProg, int zRotateProg, int x, int y, int z, int k) {
        this.xRotateProg = x;
        this.yRotateProg = y;
        this.zRotateProg = z;
        this.progress = k;
        applyTransformation(xRotateProg, yRotateProg, zRotateProg, k);
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

    public int getCurveRotateProg() {
        return this.progress;
    }

    public void setTextCurveRotateProg(int curveRotateProg) {
        this.progress = curveRotateProg;
        this.prog_60 = (curveRotateProg * 60) / 100;
        this.prog_40 = (curveRotateProg * 40) / 100;
        this.prog_50 = (curveRotateProg * 50) / 100;
        this.prog_80 = (curveRotateProg * 80) / 100;
        this.prog_20 = (curveRotateProg * 20) / 100;
        this.textRelative.setTextCurveRotateProg(curveRotateProg);
        this.textRelative.invalidate();
    }

    protected void applyTransformation(int angle1, int angle2, int angle3, int k) {
        this.textRelative.setRotationX((float) angle1);
        this.textRelative.setRotationY((float) angle2);
        this.textRelative.setTextCurveRotateProg(k);
        setVisibility(View.VISIBLE);
        this.textRelative.setVisibility(View.VISIBLE);
        this.rel_artv.requestLayout();
        this.rel_artv.postInvalidate();
        this.textRelative.requestLayout();
        this.textRelative.postInvalidate();
        this.textRelative.invalidate();
        requestLayout();
        postInvalidate();
    }

    public TextInfo getTextInfo() {
        TextInfo textInfo = new TextInfo();
        textInfo.setPOS_X(getX());
        textInfo.setPOS_Y(getY());
        textInfo.setWIDTH(this.wi);
        textInfo.setHEIGHT(this.he);
        textInfo.setTEXT(this.text);
        textInfo.setFONT_NAME(this.fontName);
        textInfo.setTEXT_COLOR(this.tColor);
        textInfo.setTEXT_ALPHA(this.tAlpha);
        textInfo.setSHADOW_COLOR(this.shadowColor);
        textInfo.setSHADOW_PROG(this.shadowProg);
        textInfo.setBG_COLOR(this.bgColor);
        textInfo.setBG_DRAWABLE(this.bgDrawable);
        textInfo.setBG_ALPHA(this.bgAlpha);
        textInfo.setROTATION(getRotation());
        textInfo.setXRotateProg(this.xRotateProg);
        textInfo.setYRotateProg(this.yRotateProg);
        textInfo.setZRotateProg(this.zRotateProg);
        textInfo.setCurveRotateProg(this.progress);
        textInfo.setFIELD_ONE(this.field_one);
        textInfo.setFIELD_TWO(this.field_two);
        textInfo.setFIELD_THREE(this.lockStatus);
        textInfo.setFIELD_FOUR(this.field_four);
        textInfo.setTEXT_GRAVITY(this.txtGravity);
        return textInfo;
    }

    public void setTextInfo(TextInfo textInfo, boolean b) {
        this.wi = textInfo.getWIDTH();
        this.he = textInfo.getHEIGHT();
        this.text = textInfo.getTEXT();
        this.fontName = textInfo.getFONT_NAME();
        this.tColor = textInfo.getTEXT_COLOR();
        this.tAlpha = textInfo.getTEXT_ALPHA();
        this.shadowColor = textInfo.getSHADOW_COLOR();
        this.shadowProg = textInfo.getSHADOW_PROG();
        this.bgColor = textInfo.getBG_COLOR();
        this.bgDrawable = textInfo.getBG_DRAWABLE();
        this.bgAlpha = textInfo.getBG_ALPHA();
        this.rotation = textInfo.getROTATION();
        this.field_two = textInfo.getFIELD_TWO();
        this.txtGravity = textInfo.getTEXT_GRAVITY();
        this.lockStatus = textInfo.getFIELD_THREE();
        this.xRotateProg = textInfo.getXRotateProg();
        this.yRotateProg = textInfo.getYRotateProg();
        this.zRotateProg = textInfo.getZRotateProg();
        this.progress = textInfo.getCurveRotateProg();
        if (this.bgColor != 0) {
            setBgColor(this.bgColor);
        } else {
            this.background_iv.setBackgroundColor(0);
        }
        if (this.bgDrawable.equals("0")) {
            this.background_iv.setImageBitmap(null);
        } else {
            setBgDrawable(this.bgDrawable);
        }
        setBgAlpha(this.bgAlpha);
        setText(this.text);
        setTextFont(this.fontName);
        setTextColor(this.tColor);
        setTextAlpha(this.tAlpha);
        setTextShadowColor(this.shadowColor);
        setTextShadowProg(this.shadowProg);
        if (this.progress == Callback.DEFAULT_SWIPE_ANIMATION_DURATION) {
            applyTransformation(45 - this.xRotateProg, 45 - this.yRotateProg, 180 - this.zRotateProg, 0);
        } else {
            applyTransformation(45 - this.xRotateProg, 45 - this.yRotateProg, 180 - this.zRotateProg, this.progress);
        }
        setRotation(textInfo.getROTATION());
        setTextGravity(this.txtGravity);
        if (this.field_two.equals("")) {
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(textInfo.getPOS_X());
            setY(textInfo.getPOS_Y());
        } else {
            String[] parts = this.field_two.split(",");
            int leftMergin = Integer.parseInt(parts[0]);
            int topMergin = Integer.parseInt(parts[1]);
            ((LayoutParams) getLayoutParams()).leftMargin = leftMergin;
            ((LayoutParams) getLayoutParams()).topMargin = topMergin;
            getLayoutParams().width = this.wi;
            getLayoutParams().height = this.he;
            setX(textInfo.getPOS_X() + ((float) (leftMergin * -1)));
            setY(textInfo.getPOS_Y() + ((float) (topMergin * -1)));
        }
        if (this.lockStatus.equals("LOCKED")) {
            this.isMultiTouchEnabled = setDefaultTouchListener(false);
        } else {
            this.isMultiTouchEnabled = setDefaultTouchListener(true);
        }
    }

    public void optimize(float wr, float hr) {
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

    public int dpToPx(Context c, int dp) {
        float f = (float) dp;
        c.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }

    private Bitmap getTiledBitmap(Context ctx, int resId, int width, int height) {
        Rect rect = new Rect(0, 0, width, height);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(ctx.getResources(), resId, new Options()), TileMode.REPEAT, TileMode.REPEAT));
        Bitmap b = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        new Canvas(b).drawRect(rect, paint);
        return b;
    }

    private void initGD() {
        this.gd = new GestureDetector(this.context, new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                if (AutofitTextRel.this.listener != null) {
                    AutofitTextRel.this.listener.onDoubleTap();
                }
                return true;
            }

            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }

            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }

            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
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
