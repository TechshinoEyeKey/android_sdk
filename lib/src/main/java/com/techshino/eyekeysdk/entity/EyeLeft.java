package com.techshino.eyekeysdk.entity;
/**
 * 左眼位置
 * @author wangzhi
 *
 */
public class EyeLeft {
	private int x;

	private int y;

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return this.x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public String toString() {
		return "EyeLeft [x=" + x + ", y=" + y + "]";
	}
}
