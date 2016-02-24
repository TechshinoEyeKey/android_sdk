package com.techshino.eyekeydemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.techshino.eyekeydemo.R;

/**
 * TODO: document your custom view class.
 */
public class IconView extends View {
	private String mIconString; // TODO: use a default from R.string...
	private int mIconColor = Color.RED; // TODO: use a default from R.color...
	private float mIconDimension = 0; // TODO: use a default from R.dimen...\
	private float mIconTextSize = 0;
	private int mIconTextColor = Color.BLACK;
	private float mIconPaddingBottom = 0;

	private Drawable mIconDrawable;
	private Bitmap mBitmap;
	private int mImageWidth;
	private int mImageHeight;

	private TextPaint mTextPaint;
	private float mTextWidth;
	private float mTextHeight;
	private Rect mTextBound;

	private enum Ratio {
		WIDTH, HEIGHT;
	}

	public IconView(Context context) {
		super(context);
		init(null, 0);
	}

	public IconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public IconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.IconView, defStyle, 0);

		mIconString = a.getString(R.styleable.IconView_iconString);
		mIconColor = a.getColor(R.styleable.IconView_iconColor, mIconColor);
		// Use getDimensionPixelSize or getDimensionPixelOffset when dealing
		// with
		// values that should fall on pixel boundaries.
		mIconDimension = a.getDimension(R.styleable.IconView_iconDimension,
				mIconDimension);
		mIconTextSize = a.getDimension(R.styleable.IconView_iconTextSize,
				mIconTextSize);
		mIconTextColor = a.getColor(R.styleable.IconView_iconTextColor,
				mIconTextColor);
		mIconPaddingBottom = a.getDimension(
				R.styleable.IconView_iconPaddingBottom, mIconPaddingBottom);

		if (a.hasValue(R.styleable.IconView_iconDrawable)) {
			mIconDrawable = a.getDrawable(R.styleable.IconView_iconDrawable);
			mIconDrawable.setCallback(this);
			mBitmap = BitmapFactory.decodeResource(getResources(),
					a.getResourceId(R.styleable.IconView_iconDrawable, 0));
		}

		a.recycle();

		mTextBound = new Rect();
		invalidateIconMeasurements();

		// Set up a default TextPaint object
		mTextPaint = new TextPaint();
		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.LEFT);

		// Update TextPaint and text measurements from attributes
		invalidateTextPaintAndMeasurements();
	}

	private void invalidateTextPaintAndMeasurements() {
		mTextPaint.setTextSize(mIconTextSize);
		mTextPaint.setColor(mIconTextColor);
		mTextWidth = mTextPaint.measureText(mIconString);

		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		mTextHeight = fontMetrics.bottom;
	}

	/**
	 * 初始化icon大小
	 */
	private void invalidateIconMeasurements() {
		if (mIconDimension != 0) {
			mImageHeight = (int) mIconDimension;
			mImageWidth = (int) mIconDimension;
		} else {
			mImageWidth = mBitmap.getWidth();
			mImageHeight = mBitmap.getHeight();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredSize(widthMeasureSpec, Ratio.WIDTH),
				getMeasuredSize(heightMeasureSpec, Ratio.HEIGHT));
	}

	/**
	 * 获取测量的尺寸
	 *
	 * @param meaureSpect
	 * @param ratio
	 * @return
	 */
	private int getMeasuredSize(int meaureSpect, Ratio ratio) {
		int mode = MeasureSpec.getMode(meaureSpect);
		int size = MeasureSpec.getSize(meaureSpect);
		int layout_size = 0;

		if (mode == MeasureSpec.EXACTLY) {
			layout_size = size;
		} else {
			/**
			 * 设置宽度
			 */
			if (ratio == Ratio.WIDTH) {
				mTextPaint.setTextSize(mIconTextSize);
				mTextPaint.getTextBounds(mIconString, 0, mIconString.length(),
						mTextBound);
				float textWidth = mTextBound.width();

				// 文字决定宽
				int desireByText = getPaddingLeft() + (int) textWidth
						+ getPaddingRight();
				// 图片决定宽
				int desireByImage = getPaddingLeft() + mImageWidth
						+ getPaddingRight();
				layout_size = Math.max(desireByText, desireByImage);
				if (mode == MeasureSpec.AT_MOST) {
					layout_size = Math.min(layout_size, size);
				}
			} else {
				/**
				 * 设置高度
				 */
				float textHeight = mTextBound.height();
				layout_size = getPaddingTop() + mImageHeight
						+ (int) mIconPaddingBottom + (int) textHeight
						+ getPaddingBottom();
				if (mode == MeasureSpec.AT_MOST) {
					layout_size = Math.min(layout_size, size);
				}
			}
		}

		return layout_size;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// TODO: consider storing these as member variables to reduce
		// allocations per draw cycle.
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		// Draw the text.
		canvas.drawText(mIconString, (getWidth() - mTextWidth) / 2, getHeight()
				- paddingBottom, mTextPaint);

		// Draw the example drawable on top of the text.
		if (mIconDrawable != null) {
			mIconDrawable.setBounds((getWidth() - mImageWidth) / 2, paddingTop,
					(getWidth() + mImageWidth) / 2, paddingTop + mImageHeight);
			mIconDrawable.draw(canvas);
		}
	}

	/**
	 * Gets the example string attribute value.
	 *
	 * @return The example string attribute value.
	 */
	public String geticonString() {
		return mIconString;
	}

	/**
	 * Sets the view's example string attribute value. In the example view, this
	 * string is the text to draw.
	 *
	 * @param exampleString
	 *            The example string attribute value to use.
	 */
	public void setIconString(String exampleString) {
		mIconString = exampleString;
		invalidateTextPaintAndMeasurements();
		postInvalidate();
	}

	/**
	 * Gets the example color attribute value.
	 *
	 * @return The example color attribute value.
	 */
	public int getIconColor() {
		return mIconColor;
	}

	/**
	 * Sets the view's example color attribute value. In the example view, this
	 * color is the font color.
	 *
	 * @param iconColor
	 *            The example color attribute value to use.
	 */
	public void setIconColor(int iconColor) {
		mIconColor = iconColor;
	}

	/**
	 * Gets the example dimension attribute value.
	 *
	 * @return The example dimension attribute value.
	 */
	public float getIconDimension() {
		return mIconDimension;
	}

	/**
	 * Sets the view's example dimension attribute value. In the example view,
	 * this dimension is the font size.
	 *
	 * @param iconDimension
	 *            The example dimension attribute value to use.
	 */
	public void setIconDimension(float iconDimension) {
		mIconDimension = iconDimension;
		invalidateTextPaintAndMeasurements();
		postInvalidate();
	}

	/**
	 * Gets the example drawable attribute value.
	 *
	 * @return The example drawable attribute value.
	 */
	public Drawable getIconDrawable() {
		return mIconDrawable;
	}

	/**
	 * Sets the view's example drawable attribute value. In the example view,
	 * this drawable is drawn above the text.
	 *
	 * @param iconDrawable
	 *            The example drawable attribute value to use.
	 */
	public void setIconDrawable(Drawable iconDrawable) {
		mIconDrawable = iconDrawable;
		postInvalidate();
	}

	public float getIconTextSize() {
		return mIconTextSize;
	}

	public void setIconTextSize(float iconTextSize) {
		mIconTextSize = iconTextSize;
	}

	public int getIconTextColor() {
		return mIconTextColor;
	}

	public void setIconTextColor(int iconTextColor) {
		mIconTextColor = iconTextColor;
		invalidateTextPaintAndMeasurements();
		postInvalidate();
	}
}
