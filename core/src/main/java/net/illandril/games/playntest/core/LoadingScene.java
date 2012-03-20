package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import playn.core.CanvasImage;
import playn.core.ImageLayer;

public class LoadingScene extends Scene {

  private Scene sceneToWaitFor;
  private final Game game;

  public LoadingScene(Game game) {
    this.game = game;
  }

  @Override
  public void paint(float alpha) {
  }

  @Override
  public void update(float delta) {
    if (sceneToWaitFor != null && sceneToWaitFor.isInitialized() && assets().isDone()) {
      game.loadScene(sceneToWaitFor);
    } else {
      // log().error(Integer.toString(assets().getPendingRequestCount()));
    }
  }

  @Override
  protected void doInitialize() {
    {
      ImageLayer layer = graphics().createImageLayer(assets().getImage("/images/bg.png"));
      addToStaticLayer(layer);
    }

  }

  @Override
  protected void doCleanup() {
    sceneToWaitFor = null;
  }

  public void monitorScene(Scene scene) {
    sceneToWaitFor = scene;
  }

}
