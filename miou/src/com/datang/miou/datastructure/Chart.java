package com.datang.miou.datastructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.datang.miou.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Chart extends View{

	private static final String TAG = "Chart";
	private int mHorizontalAxeNum = 5;
	private int mVerticalAxeNum = 10;
	
	//private ArrayList<Point>[] mPoints;
	//private ArrayList<Point>[] mCurrentPoints;
	private ArrayList<Point>[] mPoints = new ArrayList[] {new ArrayList<Point>(), new ArrayList<Point>(), new ArrayList<Point>()};
	private ArrayList<Point>[] mCurrentPoints = new ArrayList[] {new ArrayList<Point>(), new ArrayList<Point>(), new ArrayList<Point>()};
	private Point mLastPoint;
	
	private Map<String, Integer> mItemInPopWindow;
	
	private Paint axisPaint;
	private Paint[] pointPaint = new Paint[3];
	private Paint popWindowPaint;
	
	private int measuredWidth;
	private int measuredHeight;
	private int popWindowHeight = 0;
	private float mRate;
	
	private int mStartIndex = -1;
	private int mEndIndex = -1;

	private int mMaxStartIndex;
	private int mMaxEndIndex;
	
	private int mAxisColor = R.color.menu_border_gray;
	private int mPointColor = R.color.title_blue;
	private int mTextColor = R.color.black;
	
	public void setAxisColor(int axisColor) {
		this.mAxisColor = axisColor;
	}

	public void setPointColor(int pointColor) {
		this.mPointColor = pointColor;
	}

	public void setSelectedColor(int selectedColor) {
		Resources r = this.getResources();
		this.mTextColor = selectedColor;
		textPaint.setColor(r.getColor(selectedColor));
	}

	public int getHorizontalAxeNum() {
		return mHorizontalAxeNum;
	}

	public void setHorizontalAxeNum(int horizontalAxeNum) {
		this.mHorizontalAxeNum = horizontalAxeNum;
	}

	public int getVerticalAxeNum() {
		return mVerticalAxeNum;
	}

	public void setVerticalAxeNum(int verticalAxeNum) {
		this.mVerticalAxeNum = verticalAxeNum;
	}

	public interface mCallback {
		public void onClickOnPoint(Chart.Point p);
	}
	private mCallback cb;
	private Paint textPaint;
	
	public void setCb(mCallback cb) {
		this.cb = cb;
	}
	
	public class Point {
		int px;
		int py;
		public DataSet data;
		boolean selected = false;
		
		public Point(int px, int py, DataSet data) {
			this.px = px;
			this.py = py;
			this.data = data;
		}
	}
	
	public class DataSet {
		public double[] mParams;
		public int mMaxParamNum = 4;
		
		public DataSet() {
			mParams = new double[mMaxParamNum];
		}
		
		public void setMaxParamNum(int num) {
			this.mMaxParamNum = num;
			mParams = new double[mMaxParamNum];
		}
	}
	
	public Chart(Context context) {
		super(context);
		cb = (mCallback) context;
		initChart();
	}

	public Chart(Context context, AttributeSet attrs) {
		super(context, attrs);
		//cb = (mCallback) context;
		initChart();
	}
	
	public Chart(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		cb = (mCallback) context;
		initChart();
	}
	
	private void initChart() {
		Resources r = this.getResources();
		
		axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setColor(r.getColor(mAxisColor));
		axisPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			pointPaint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
			pointPaint[i].setColor(r.getColor(Globals.chartLineColor[i]));
			pointPaint[i].setStyle(Paint.Style.FILL_AND_STROKE);
			pointPaint[i].setStrokeWidth((float) 5.0);
		}
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(mTextColor));
		textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		textPaint.setTextSize(20);
		textPaint.setStrokeWidth(1);
		
		popWindowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		popWindowPaint.setColor(r.getColor(R.color.button_green));
		popWindowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		popWindowPaint.setStrokeWidth((float) 5.0);
		/*
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			mPoints[i] = new ArrayList<Point>();
			mCurrentPoints[i] = new ArrayList<Point>();
		}
		*/
		mItemInPopWindow = new HashMap<String, Integer>();
	}

	private int[] transformDataToPoints(int[] data) {
		int result[] = new int[data.length];
		mRate = (getMaxInArray(data) - getMinInArray(data)) / (float) measuredHeight;
		for (int i = 0; i < data.length; i++) {
			result[i] = measuredHeight - (int) (data[i] / mRate);
		}
		return result;
	}
	
	private int getMinInArray(int[] data) {
		int min;
		min = data[0];
		for (int i : data) {
			if (i < min) {
				min = i;
			}
		}
		return min;
	}

	private int getMaxInArray(int[] data) {
		int max;
		max = data[0];
		for (int i : data) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		measuredWidth = getMeasuredWidth();
		measuredHeight = getMeasuredHeight();
		
		for (int i = 0; i < mVerticalAxeNum + 1; i++) {
			int x = (measuredWidth / mVerticalAxeNum) * i;
			for (int j = 0; j < mHorizontalAxeNum + 1; j++) {
				int y = (measuredHeight / mHorizontalAxeNum) * j;
				//mPoints[i][j] = new Point(x, y);
			}
		}
		
		for (int i = 1; i < mHorizontalAxeNum; i++) {
			int startY = (measuredHeight / mHorizontalAxeNum) * i;
			int stopY = (measuredHeight / mHorizontalAxeNum) * i;
			int startX = 0;
			int stopX = measuredWidth;
			canvas.drawLine(startX, startY, stopX, stopY, axisPaint);
		}
		canvas.save();
		
		for (int i = 1; i < mVerticalAxeNum; i++) {
			int startX = (measuredWidth / mVerticalAxeNum) * i;
			int stopX = (measuredWidth / mVerticalAxeNum) * i;
			int startY = 0;
			int stopY = measuredWidth;
			canvas.drawLine(startX, startY, stopX, stopY, axisPaint);
		}
		canvas.save();
		
		int[] data = {100, 600, 300, 400, 500, 1000, 600, 700, 1200};
		int[] result = new int[data.length];
		result = transformDataToPoints(data);
		
		/*
		for (int i = 0; i < result.length; i++) {
			Point point = new Point(xValue[i], result[i]);
			mPoints.add(point);
			mCurrentPoints.add(point);
		}
		*/
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			for (Point p : mCurrentPoints[i]) {
				if (p.selected) {
					//canvas.drawCircle(p.px, p.py, 5, pointPaint[i]);
					showDetailWindow(canvas, p);
				} else {
					canvas.drawCircle(p.px, p.py, 2, pointPaint[i]);
				}
			}
		}
		canvas.save();
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			for (int j = 0; j < mCurrentPoints[i].size() - 1; j++) {
				Point start = mCurrentPoints[i].get(j);
				Point end = mCurrentPoints[i].get(j + 1);
				canvas.drawLine(start.px, start.py, end.px, end.py, pointPaint[i]);
			}
		}
		canvas.save();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Point current = new Point((int) event.getX(), (int) event.getY(), null);
		switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				Point point = null;
				int bias = getDragDistance(current);
				if (bias != 0) {
					adjustCurrentPoints(bias);			
				} else {
					point = getNearbyPoint(current);
					if (point != null) {
						for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
							for (Point p : mCurrentPoints[i]) {
								p.selected = false;
							}
						}
						point.selected = true;
						cb.onClickOnPoint(point);
					}
				}
				invalidate();
				/*
				if (point != null) {
					showDetailWindow(point);
				}
				*/
				break;
			case MotionEvent.ACTION_DOWN:
				mLastPoint = new Point((int) event.getX(), (int) event.getY(), null);
				break;
			default:
				break;
		}
		return true;
	}

	private void showDetailWindow(Canvas canvas, Point point) {
		// TODO 自动生成的方法存根
		
		Rect rect = adjustWindowPosition(point);
		canvas.drawRect(rect, popWindowPaint);

		Point p = adjustTextPosition(point);
		
		Collection<String> c = mItemInPopWindow.keySet();
		Iterator it = c.iterator();
		int textY = p.py;
		while (it.hasNext()) {
			String name = (String) it.next();
			int type = mItemInPopWindow.get(name);
			int value = (int) p.data.mParams[type];
			//这里得到的type只是参数的类型，需要通过这个value获得对应的即时数据
			/*
			switch (type) {
				case Globals.CHART_LINE_PARAM_BLER:
					
					break;
				case Globals.CHART_LINE_PARAM_RSCP:
					break;
				case Globals.CHART_LINE_PARAM_SNR:
					break;
				case Globals.CHART_LINE_PARAM_SPEED:
					break;
			}
			*/
			canvas.drawText(name + ": " + value, p.px, textY , textPaint);
			textY += 20;
		}
		
		//canvas.drawText("hello", p.px, p.py , textPaint);
	}

	private Point adjustTextPosition(Point point) {
		// TODO 自动生成的方法存根
		Point p = new Point(point.px, point.py, point.data);
		
		int left = point.px - 50;
		int right = point.px + 50;
		int top = point.py - popWindowHeight;
		int bottom = point.py;
		int textLeft = point.px - 40;
		int textTop = point.py - popWindowHeight + 15;
		
		Log.i(TAG, "baseHeight = " + popWindowHeight);
		if (left < 0) {
			left = 0;
			right = 100;
			textLeft = 10;
		}
		if (right > measuredWidth) {
			right = measuredWidth;
			left = measuredWidth - 100;
			textLeft = measuredWidth - 90;
		}
		if (top < 0) {
			top = 0;
			bottom = popWindowHeight;
			textTop = 15;
		}
		if (bottom > measuredHeight) {
			bottom = measuredHeight;
			top = measuredHeight - popWindowHeight;
			textTop = measuredHeight - popWindowHeight + 5;
		}
		p.px = textLeft;
		p.py = textTop;
		return p;
	}

	private Rect adjustWindowPosition(Point point) {
		// TODO 自动生成的方法存根
		Rect rect = new Rect();
		int left = point.px - 50;
		int right = point.px + 50;
		int top = point.py - popWindowHeight;
		int bottom = point.py;
		
		if (left < 0) {
			left = 0;
			right = 100;
		}
		if (right > measuredWidth) {
			right = measuredWidth;
			left = measuredWidth - 100;
		}
		if (top < 0) {
			top = 0;
			bottom = popWindowHeight;
		}
		if (bottom > measuredHeight) {
			bottom = measuredHeight;
			top = measuredHeight - 50;
		}
		rect.bottom = bottom;
		rect.left = left;
		rect.top = top;
		rect.right = right;
		
		return rect;
	}

	private void adjustCurrentPoints(int bias) {
		if (bias < 0) {
			for (int i = 0; i < Math.abs(bias); i++) {
				if (mStartIndex > 0) {
					for (int j = 0; j < Globals.CHART_INDEX_NUM; j++) {
						mCurrentPoints[j].remove(mVerticalAxeNum);
					}
					mStartIndex--;
					mEndIndex--;
					for (int j = 0; j < Globals.CHART_INDEX_NUM; j++) {
						mCurrentPoints[j].add(0, mPoints[j].get(mStartIndex));	
					}
				}
			}
		} else {
			for (int i = 0; i < bias; i++) {
				if (mEndIndex < mPoints[0].size() - 1) {
					for (int j = 0; j < Globals.CHART_INDEX_NUM; j++) {
						mCurrentPoints[j].remove(0);
						mCurrentPoints[j].add(mPoints[j].get(mEndIndex + 1));
					}
					mEndIndex++;
					mStartIndex++;
				}		
			}
		}
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			for (int j = 0; j < mCurrentPoints[i].size(); j++) {
				mCurrentPoints[i].get(j).px = j * (measuredWidth / mVerticalAxeNum);
			}
		}
	}

	private int getDragDistance(Point current) {
		return (int) ((double) (mLastPoint.px - current.px) / (measuredWidth / 10));
	}

	private Point getNearbyPoint(Point current) {
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			for (Point p : mCurrentPoints[i]) {
				double distance = (Math.pow((double) (current.px - p.px), 2) + Math.pow((double) (current.py - p.py), 2));
				double toleration = 1000;
				if (distance < toleration) {
					return p;
				}
			}
		}
		return null;
	}

	public void addPoint(DataSet data) {
		//data中传进来3个参数值，在设置中可以设定是哪个类型的参数
		//根据需求，可能会更复杂
		int nowX = 0;
		
		Point[] point = new Point[3];
		
		mStartIndex = mMaxStartIndex;
		mEndIndex = mMaxEndIndex;
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			if (mPoints[i].size() > mVerticalAxeNum) {
				nowX = (mVerticalAxeNum + 1) * (measuredWidth / mVerticalAxeNum);
				mCurrentPoints[i].remove(0);
				
			} else {
				nowX = mPoints[i].size() * (measuredWidth / mVerticalAxeNum);
			}
			point[i] = new Point(nowX, measuredHeight - (int) (data.mParams[i] / mRate), data);
		}
		
		
		mEndIndex++;
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			mPoints[i].add(point[i]);
			mCurrentPoints[i].add(point[i]);
			//if (mCurrentPoints[i].size() > mVerticalAxeNum) {
			//	mStartIndex++;
			//}
		}
		
		if (mCurrentPoints[0].size() > mVerticalAxeNum) {
			mStartIndex++;
		}
		
		mMaxStartIndex = mStartIndex;
		mMaxEndIndex = mEndIndex;
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			if (mPoints[i].size() > mVerticalAxeNum) {
				mCurrentPoints[i] = new ArrayList<Point>();
				for (int j = mPoints[i].size() - mVerticalAxeNum - 1; j < mPoints[i].size(); j++) {
					mCurrentPoints[i].add(mPoints[i].get(j));
				}
			}
		}
		
		for (int i = 0; i < Globals.CHART_INDEX_NUM; i++) {
			for (int j = 0; j < mCurrentPoints[i].size(); j++) {
				mCurrentPoints[i].get(j).px = j * (measuredWidth / mVerticalAxeNum);
			}
		}
		
		this.invalidate();
	}
	
	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 200;
		} else {
			result = specSize;
		}	
		return result;
	}	
	
	public void addItemInPopWindow(String name, int type) {
		mItemInPopWindow.put(name, type);
		popWindowHeight += 20;
	}
	
	public void removeItemInPopWindow(String name) {
		mItemInPopWindow.remove(name);
		popWindowHeight -= 20;
	}
}
