package com.lolclone.matchmaking_domain.domain;

public enum PositionType {
  TOP("탑"),
  JUNGLE("정글"), 
  MID("미드"),
  BOT("원딜"),
  SUPPORT("서포터");

  private final String description;

  PositionType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
