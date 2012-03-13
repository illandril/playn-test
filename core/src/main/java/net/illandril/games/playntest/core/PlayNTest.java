package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.touch;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;

public class PlayNTest implements Game {

  private Canvas textArea;
  private float fps = 60;
  private Scene activeScene;

  @Override
  public void init() {
    Image bgImage = assets().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);

    setActiveScene(new TestScene());
    {
      CanvasImage image = graphics().createImage(150, 50);
      textArea = image.canvas();
      textArea.setStrokeWidth(2);
      textArea.setStrokeColor(0xffff0000);
      textArea.drawText("Hi", 10, 10);
      ImageLayer layer = graphics().createImageLayer(image);
      layer.setTranslation(75, 25);
      graphics().rootLayer().add(layer);
    }
  }

  private void setActiveScene(Scene newScene) {
    Scene lastScene = activeScene;
    activeScene = newScene;
    if (lastScene != null) {
      graphics().rootLayer().remove(lastScene.sceneRoot);
    }
    newScene.initialize();
    graphics().rootLayer().add(newScene.sceneRoot);
    keyboard().setListener(newScene);
    pointer().setListener(newScene);
    //touch().setListener(newScene);
  }

  @Override
  public void paint(float alpha) {
    activeScene.paint(alpha);
  }

  @Override
  public void update(float delta) {
    fps = fps * 0.9f + (1000.0f / delta) * 0.1f;
    textArea.clear();
    textArea.drawText(Integer.toString(Math.round(fps)) + " FPS (delta: " + Float.toString(delta)
        + ")", 10, 10);
    activeScene.update(delta);
  }

  @Override
  public int updateRate() {
    return 0;
  }
}
