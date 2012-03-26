package net.illandril.games.playntest.core;


public class PlayNTest extends Game {
  @Override
  protected void loadFirstScene() {
    SpriteSheet.get("smbsheet");
    loadScene(new TestScene());
  }

}
