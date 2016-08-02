package com.techshino.eyekeysdk.entity;

/**
 * 右眼坐标
 *
 * @author wangzhi
 */
public class EyeRight {
  private int x;

  private int y;

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  @Override
  public String toString() {
    return "EyeRight [x=" + x + ", y=" + y + "]";
  }
}
