package com.techshino.eyekeysdk.entity;

public class Pose {
	private int raise;

	private int tilting;

	private int turn;

	public void setRaise(int raise) {
		this.raise = raise;
	}

	public int getRaise() {
		return this.raise;
	}

	public void setTilting(int tilting) {
		this.tilting = tilting;
	}

	public int getTilting() {
		return this.tilting;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public int getTurn() {
		return this.turn;
	}

	@Override
	public String toString() {
		return "Pose [raise=" + raise + ", tilting=" + tilting + ", turn="
				+ turn + "]";
	}

}
