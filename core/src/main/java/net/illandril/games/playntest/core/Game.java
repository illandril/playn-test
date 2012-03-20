package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import static playn.core.PlayN.pointer;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;

public abstract class Game implements playn.core.Game {

  private LoadingScene loadingScene;
  private int MAX_ACTIVE_SCENES = 20;
  private Scene[] activeScenes;
  private int topScene = -1;
  private boolean loading = true;

  private int WIDTH = 990;
  private int HEIGHT = 700;

  private Canvas textArea;
  private float fps = 60;

  @Override
  public final void init() {
    log().error(graphics().screenWidth() + "x" + graphics().screenWidth());
    graphics().setSize(WIDTH, HEIGHT);
    log().error(graphics().screenWidth() + "x" + graphics().screenWidth());
    activeScenes = new Scene[MAX_ACTIVE_SCENES];
    loadingScene = new LoadingScene(this);
    loadingScene.initialize(graphics().rootLayer(), MAX_ACTIVE_SCENES);
    loadFirstScene();
    {
      CanvasImage image = graphics().createImage(150, 50);
      textArea = image.canvas();
      textArea.setStrokeWidth(2);
      textArea.setStrokeColor(0xffffffff);
      textArea.setFillColor(0xffffffff);
      textArea.drawText("Hi", 10, 10);
      ImageLayer layer = graphics().createImageLayer(image);
      layer.setTranslation(75, 25);
      layer.setDepth(MAX_ACTIVE_SCENES + 1);
      graphics().rootLayer().add(layer);
    }
  }

  protected abstract void loadFirstScene();

  public void loadScene(Scene newScene) {
    log().debug("loading scene...");
    if (topScene == -1 || activeScenes[topScene] != newScene) {
      topScene++;
      if (topScene >= MAX_ACTIVE_SCENES) {
        log().error("TOO MANY SCENES!");
        return;
      }
      activeScenes[topScene] = newScene;
      log().debug("Top Scene:" + Integer.toString(topScene));
    }
    newScene.initialize(graphics().rootLayer(), topScene);
    if (newScene.isInitialized() && assets().isDone()) {
      loading = false;
      hideScene(loadingScene);
      keyboard().setListener(newScene);
      pointer().setListener(newScene);
      // touch().setListener(newScene);
      log().debug("loaded scene");
    } else {
      loading = true;
      loadingScene.monitorScene(newScene);
      loadingScene.initialize(graphics().rootLayer(), MAX_ACTIVE_SCENES);
      log().debug("loaded loading scene");
    }
  }

  public void hideScene(Scene scene) {
    log().debug("hiding scene");
    if (activeScenes[topScene] == scene) {
      activeScenes[topScene] = null;
      topScene--;
      loadScene(activeScenes[topScene]);
    }
    scene.deactivateScene();
    log().debug("hid scene");
  }

  @Override
  public void paint(float alpha) {
    if (loading) {
      loadingScene.paint(alpha);
    } else {
      activeScenes[topScene].paint(alpha);
    }
  }

  @Override
  public void update(float delta) {
    if ( delta > 0 ) {
      fps = fps * 0.9f + (1000.0f / delta) * 0.1f;
    }
    // log().debug("FPS: " + fps);
    textArea.clear();
    textArea.drawText(Integer.toString(Math.round(fps)) + " FPS (delta: " + Float.toString(delta)
        + ")", 10, 10);
    if (loading) {
      loadingScene.update(delta);
    } else {
      activeScenes[topScene].update(delta);
    }
  }

  @Override
  public int updateRate() {
    return 0;
  }

}
