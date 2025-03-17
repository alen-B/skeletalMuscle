package com.fjp.skeletalmuscle.app.weight.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.fjp.skeletalmuscle.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

/**
 * 精美进度风格
 * Created by huanghaibin on 2018/2/8.
 */

public class ProgressWeekView extends WeekView {

    private Paint mProgressPaint = new Paint();
    private Paint mNoneProgressPaint = new Paint();
    private int mRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    private int mPadding;
    private float mPointRadius;

    public ProgressWeekView(Context context) {
        super(context);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mProgressPaint.setColor(0xBBf54a00);

        mNoneProgressPaint.setAntiAlias(true);
        mNoneProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mNoneProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mNoneProgressPaint.setColor(Color.parseColor("#DEE8F8"));
        mPadding = dipToPx(getContext(), 3);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setColor(Color.RED);
        mPointRadius = dipToPx(context, 12);


    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 4;
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;

        int angle = getAngle(Integer.parseInt(calendar.getScheme()));
        if (angle > 288) {
            mProgressPaint.setColor(getContext().getResources().getColor(R.color.color_4e71ff));
        } else if (angle > 216) {
            mProgressPaint.setColor(getContext().getResources().getColor(R.color.color_ffc019));
        } else if (angle > 108) {
            mProgressPaint.setColor(getContext().getResources().getColor(R.color.color_ff824c));
        } else {
            mProgressPaint.setColor(getContext().getResources().getColor(R.color.color_ff574c));
        }

        RectF progressRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(progressRectF, -90, angle, false, mProgressPaint);

        RectF noneRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(noneRectF, angle - 90, 360 - angle, false, mNoneProgressPaint);
        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.WHITE);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }

        canvas.drawCircle(cx, mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    /**
     * 获取角度
     *
     * @param progress 进度
     * @return 获取角度
     */
    private static int getAngle(int progress) {
        return (int) (progress * 3.6);
    }


    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    @SuppressWarnings("all")
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
