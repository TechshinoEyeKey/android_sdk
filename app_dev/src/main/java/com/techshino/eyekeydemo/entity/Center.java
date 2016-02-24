package com.techshino.eyekeydemo.entity;
/**
 * 居中位置
 * @author wangzhi
 *
 */
public class Center {
	
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
		return "Center [x=" + x + ", y=" + y + "]";
	}
}
