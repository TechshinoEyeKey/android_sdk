package com.turui.bank.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class RotateImageView
    extends ImageView {
  private static final String TAG = "RotateImageView";
  private static final int ANIMATION_SPEED = 270;
  private int mCurrentDegree = 0;
  private int mStartDegree = 0;
  private int mTargetDegree = 0;
  private boolean mClockwise = false;
  private boolean mEnableAnimation = true;
  private long mAnimationStartTime = 0L;
  private long mAnimationEndTime = 0L;
  private Bitmap mThumb;
  private Drawable[] mThumbs;
  private TransitionDrawable mThumbTransition;
  public RotateImageView(Context context) {
    super(context);
  }

  public void enableAnimation(boolean enable) {
    this.mEnableAnimation = enable;
  }

  protected int getDegree() {
    return this.mTargetDegree;
  }

  public void setOrientation(int degree) {
    degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
    if (degree == this.mTargetDegree) {
      return;
    }
    this.mTargetDegree = degree;
    this.mStartDegree = this.mCurrentDegree;
    this.mAnimationStartTime = AnimationUtils.currentAnimationTimeMillis();

    int diff = this.mTargetDegree - this.mCurrentDegree;
    diff = diff >= 0 ? diff : 360 + diff;


    diff = diff > 180 ? diff - 360 : diff;

    this.mClockwise = (diff >= 0);
    this.mAnimationEndTime =
        (this.mAnimationStartTime + Math.abs(diff) * 1000 / 270);

    invalidate();
  }

  protected void onDraw(Canvas canvas) {
    Drawable drawable = getDrawable();
    if (drawable == null) {
      return;
    }
    Rect bounds = drawable.getBounds();
    int w = bounds.right - bounds.left;
    int h = bounds.bottom - bounds.top;
    if ((w == 0) || (h == 0)) {
      return;
    }
    if (this.mCurrentDegree != this.mTargetDegree) {
      long time = AnimationUtils.currentAnimationTimeMillis();
      if (time < this.mAnimationEndTime) {
        int deltaTime = (int) (time - this.mAnimationStartTime);
        int degree = this.mStartDegree + 270 * (
            this.mClockwise ? deltaTime : -deltaTime) / 1000;
        degree = degree >= 0 ? degree % 360 : degree % 360 + 360;
        this.mCurrentDegree = degree;
        invalidate();
      } else {
        this.mCurrentDegree = this.mTargetDegree;
      }
    }
    int left = getPaddingLeft();
    int top = getPaddingTop();
    int right = getPaddingRight();
    int bottom = getPaddingBottom();
    int width = getWidth() - left - right;
    int height = getHeight() - top - bottom;

    int saveCount = canvas.getSaveCount();
    if ((getScaleType() == ImageView.ScaleType.FIT_CENTER) && (
        (width < w) || (height < h))) {
      float ratio = Math.min(width / w, height / h);
      canvas.scale(ratio, ratio, width / 2.0F, height / 2.0F);
    }
    canvas.translate(left + width / 2, top + height / 2);
    canvas.rotate(-this.mCurrentDegree);
    canvas.translate(-w / 2, -h / 2);
    drawable.draw(canvas);
    canvas.restoreToCount(saveCount);
  }

  public void setBitmap(Bitmap bitmap) {
    if (bitmap == null) {
      this.mThumb = null;
      this.mThumbs = null;
      setImageDrawable(null);
      setVisibility(8);
      return;
    }
    ViewGroup.LayoutParams param = getLayoutParams();
    int miniThumbWidth = param.width -
        getPaddingLeft() - getPaddingRight();
    int miniThumbHeight = param.height -
        getPaddingTop() - getPaddingBottom();
    this.mThumb = ThumbnailUtils.extractThumbnail(
        bitmap, miniThumbWidth, miniThumbHeight);
    if ((this.mThumbs == null) || (!this.mEnableAnimation)) {
      this.mThumbs = new Drawable[2];
      this.mThumbs[1] = new BitmapDrawable(getContext().getResources(), this.mThumb);
      setImageDrawable(this.mThumbs[1]);
    } else {
      this.mThumbs[0] = this.mThumbs[1];
      this.mThumbs[1] = new BitmapDrawable(getContext().getResources(), this.mThumb);
      this.mThumbTransition = new TransitionDrawable(this.mThumbs);
      setImageDrawable(this.mThumbTransition);
      this.mThumbTransition.startTransition(500);
    }
    setVisibility(0);
  }
}
