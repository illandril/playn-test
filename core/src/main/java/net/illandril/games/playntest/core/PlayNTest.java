package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.touch;
import static playn.core.PlayN.log;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;

public class PlayNTest extends Game {
  @Override
  protected void loadFirstScene() {
    SpriteSheet.get("smbsheet");
//    for( int i = 0; i < 3000; i++) {
//      assets().getImage("missing" + i + ".png");
//    }
    loadScene(new TestScene());
  }

}
