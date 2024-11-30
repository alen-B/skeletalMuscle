package com.fjp.skeletalmuscle.app.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fjp.skeletalmuscle.R;

public class DashboardView extends View {
    private Paint paint;
    private int maxCircleRadius=95;
    private int minCircleRadius=70;


    private float pointerLength=20;
    private float pointerAngle=90;
    private Paint textPaint;
    private Paint pointerPaint;
    private String text = "力量流失";
    private float centerX;
    private float centerY;

    public DashboardView(Context context) {
        super(context);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(28);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getContext().getColor(R.color.color_1c1c1c));
        textPaint.setTextSize(22);

        pointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointerPaint.setStrokeWidth(5);
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);
        pointerPaint.setColor(getContext().getColor(R.color.white));
        pointerPaint.setShadowLayer(3,3,3,getContext().getColor(R.color.color_801c1c1c));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置半圆的中心点和半径
        centerX = getWidth() / 2;
        centerY = getHeight();
        drawOutLine(canvas);
        drawPointer(canvas);
        drawText(canvas);
    }

    private void drawPointer(Canvas canvas) {
        // 根据45度角和高度一半计算线的终点坐标
        float halfHeight = getHeight() / 1.3f;
        int endX = (int) (centerX - halfHeight * Math.cos(Math.toRadians(pointerAngle)));
        int endY = (int) (getHeight() - halfHeight * Math.sin(Math.toRadians(pointerAngle)));

        canvas.drawLine(centerX, getHeight(), endX, endY, pointerPaint);

    }

    private void drawText(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        RectF rectf = new RectF(centerX - minCircleRadius, centerY - minCircleRadius, centerX + minCircleRadius, centerY + minCircleRadius);
        canvas.drawArc(rectf,
                180,
                180,
                true,
                paint);
        // 测量文字宽度
        float textWidth = paint.measureText(text);
        // 测量文字高度
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;

        canvas.drawText(text, centerX - textWidth, centerY - textHeight, textPaint);
    }

    private void drawOutLine(Canvas canvas) {

        paint.setStyle(Paint.Style.STROKE);
        // 从12点钟位置开始（0度），绘制一个180度的弧形（半圆）
        RectF rectf = new RectF(centerX - maxCircleRadius, centerY - maxCircleRadius, centerX + maxCircleRadius, centerY + maxCircleRadius);
        canvas.drawArc(rectf,
                180,
                180,
                false,
                paint);


        //画蓝色区域
        paint.setStrokeWidth(14);
        paint.setColor(Color.parseColor("#dfe6ff"));
        canvas.drawArc(rectf,
                180,
                45,
                false,
                paint);
        paint.setColor(Color.parseColor("#88B0FF"));
        canvas.drawArc(rectf,
                225,
                90,
                false,
                paint);

        paint.setColor(Color.parseColor("#4E71FF"));
        canvas.drawArc(rectf,
                315,
                45,
                false,
                paint);

        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(8);
        canvas.drawArc(rectf,
                182,
                2,
                false,
                paint);
        canvas.drawArc(rectf,
                224,
                2,
                false,
                paint);
        canvas.drawArc(rectf,
                314,
                2,
                false,
                paint);
        canvas.drawArc(rectf,
                356,
                2,
                false,
                paint);

    }

    public void setValue(String text) {
        this.text = text;
        invalidate();
    }
    public void setPointerAngle(float angle) {
        this.pointerAngle = angle;
        invalidate();  // 调用此方法会触发重绘，使得角度改变时视图更新
    }
}
