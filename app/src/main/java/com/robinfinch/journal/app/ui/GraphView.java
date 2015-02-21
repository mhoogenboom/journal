package com.robinfinch.journal.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.robinfinch.journal.app.R;
import com.robinfinch.journal.app.util.Function;

import java.util.ArrayList;
import java.util.List;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Displays a graph
 * @author Mark Hoogenboom
 */
public class GraphView extends View {

    private List<Pair<Long, Long>> ps;
    private long minX;
    private long minY;
    private long maxX;
    private long maxY;

    private Function<Long, CharSequence> xAxisLabel;
    private Function<Long, CharSequence> yAxisLabel;
    private float gridLeft;
    private float gridRight;
    private float gridTop;
    private float gridBottom;
    private Paint gridPaint;
    private Paint linePaint;

    public GraphView(Context context) {
        super(context);
        init(null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ps = new ArrayList<>();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GraphView);
            try {
                gridPaint = new Paint();
                gridPaint.setColor(a.getColor(R.styleable.GraphView_gridColor, 0));
                gridPaint.setStrokeWidth(a.getDimensionPixelSize(R.styleable.GraphView_gridWidth, 0));

                linePaint = new Paint();
                linePaint.setColor(a.getColor(R.styleable.GraphView_lineColor, 0));
                linePaint.setStrokeWidth(a.getDimensionPixelSize(R.styleable.GraphView_lineWidth, 0));

                linePaint.setTextSize(getResources().getDimension(R.dimen.graph_label_size));
                linePaint.setTextAlign(Paint.Align.RIGHT);
            } finally {
                a.recycle();
            }
        }
    }

    public void setXAxisLabel(Function<Long, CharSequence> label) {
        xAxisLabel = label;
    }

    public void setYAxisLabel(Function<Long, CharSequence> label) {
        yAxisLabel = label;
    }

    public void add(long x, long y) {
        if (ps.isEmpty()) {
            minX = maxX = x;
            minY = maxY = y;
        } else {
            if (x < minX) {
                minX = x;
            }
            if (x > maxX) {
                maxX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }
        ps.add(new Pair<>(x, y));
    }

    public void clear() {
        ps.clear();
        minX = maxX = minY = maxY = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        CharSequence sample = yAxisLabel.apply(minY);

        gridLeft = linePaint.measureText(sample, 0, sample.length());
        gridRight = getMeasuredWidth();

        sample = xAxisLabel.apply(minX);

        gridTop = 0;
        gridBottom = getMeasuredHeight() - linePaint.measureText(sample, 0, sample.length());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.v(LOG_TAG, String.format("Draw line (%1$d,%2$d)-(%3$d,%4$d) in grid (%5$f,%6$f)-(%7$f,%8$f).", minX, minY, maxX, maxY, gridLeft, gridTop, gridRight, gridBottom));

        float cX = (gridRight - gridLeft) / (maxX - minX);
        float cY = (gridTop - gridBottom) / (maxY - minY);

        PointF p1;
        PointF p2 = null;

        for (int i = 1; i < 12; i++) {
            long y = minY + (maxY - minY) * i / 12;
            p1 = pf(cX, minX, cY, y);
            canvas.drawText(yAxisLabel.apply(y).toString(), p1.x, p1.y, linePaint);
            p2 = pf(cX, maxX, cY, y);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, gridPaint);
        }

        for (int i = 1; i < 12; i++) {
            long x = minX + (maxX - minX) * i / 12;
            p1 = pf(cX, x, cY, minY);
            canvas.rotate(-90, p1.x, p1.y);
            canvas.drawText(xAxisLabel.apply(x).toString(), p1.x, p1.y, linePaint);
            canvas.rotate(90, p1.x, p1.y);
            p2 = pf(cX, x, cY, maxY);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, gridPaint);
        }

        p2 = null;

        for (Pair<Long, Long> p : ps) {
            p1 = p2;
            p2 = pf(cX, p.first, cY, p.second);

            if (p1 != null) {
                canvas.drawLine(p1.x, p1.y, p2.x, p2.y, linePaint);
            }
        }
    }

    private PointF pf(float cX, long x, float cY, long y) {
        return new PointF(gridLeft + cX * (x - minX), gridBottom + cY * (y - minY));
    }
}
