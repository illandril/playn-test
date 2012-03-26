package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Layer;
import playn.core.TextFormat;

public class LoadingScene extends Scene {

  private CanvasImage loadingImage;
  private Scene sceneToWaitFor;
  private final Game game;

  private boolean isCanvasDirty = false;
  private float pdone = 0;

  public LoadingScene(Game game) {
    this.game = game;
  }

  @Override
  public void paint(float alpha) {
    if (isCanvasDirty) {
      loadingImage.canvas().clear();
      float bwidth = graphics().width() - 50;
      loadingImage.canvas().fillRect(25 + (bwidth - 40) * (pdone / 100), graphics().height() / 2 - 20, 40, 40);
      loadingImage.canvas().strokeRect(25, graphics().height() / 2 - 20, graphics().width() - 50,
          40);
    }
  }

  @Override
  public void update(float delta) {
    if (isSceneReady(sceneToWaitFor)) {
      game.loadScene(sceneToWaitFor);
    } else {
      isCanvasDirty = true;
      pdone = (pdone + delta / 20) % 100;
      // log().error(Integer.toString(assets().getPendingRequestCount()));
    }
  }

  @Override
  protected void doInitialize() {
    CanvasImage blackBG = graphics().createImage(graphics().width(), graphics().height());
    blackBG.canvas().setFillColor(0xff000000);
    blackBG.canvas().fillRect(0, 0, graphics().width(), graphics().height());
    Layer layer = graphics().createImageLayer(blackBG);
    layer.setDepth(0);
    addToStaticLayer(layer);

    loadingImage = graphics().createImage(graphics().width(), graphics().height());
    loadingImage.canvas().setStrokeWidth(2);
    loadingImage.canvas().setStrokeColor(0xffff0000);
    loadingImage.canvas().setFillColor(0xff0000ff);
    layer = graphics().createImageLayer(loadingImage);
    layer.setDepth(1);
    addToStaticLayer(layer);

    Font font = graphics().createFont("Courier", Font.Style.BOLD, 16);
    CanvasImage loadingText = graphics().createImage(graphics().width(), graphics().height());
    loadingText.canvas().setStrokeColor(0xffffffff);
    loadingText.canvas().setFillColor(0xffffffff);
    loadingText.canvas().drawText("Loading!..", graphics().width() / 2,
        graphics().height() / 2 - 50);
    layer = graphics().createImageLayer(loadingText);
    layer.setDepth(2);
    addToStaticLayer(layer);
  }

  @Override
  protected void doCleanup() {
    sceneToWaitFor = null;
  }

  public void monitorScene(Scene scene) {
    sceneToWaitFor = scene;
  }

  public static boolean isSceneReady(Scene scene) {
    return false && scene != null && scene.isInitialized() && assets().isDone();
  }

}
