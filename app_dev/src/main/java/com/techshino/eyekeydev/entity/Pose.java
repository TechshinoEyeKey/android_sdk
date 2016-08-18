package com.techshino.eyekeydev.entity;

public class Pose {
  private int raise;

  private int tilting;

  private int turn;

  public int getRaise() {
    return this.raise;
  }

  public void setRaise(int raise) {
    this.raise = raise;
  }

  public int getTilting() {
    return this.tilting;
  }

  public void setTilting(int tilting) {
    this.tilting = tilting;
  }

  public int getTurn() {
    return this.turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  @Override
  public String toString() {
    return "Pose [raise=" + raise + ", tilting=" + tilting + ", turn="
        + turn + "]";
  }

}
