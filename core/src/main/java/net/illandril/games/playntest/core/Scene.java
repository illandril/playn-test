package net.illandril.games.playntest.core;

import static playn.core.PlayN.graphics;

import org.jbox2d.common.Vec2;

import playn.core.GroupLayer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.Touch;
import playn.core.Touch.Event;

public abstract class Scene implements Keyboard.Listener, Pointer.Listener, Touch.Listener {

  protected final static int CAMERA_DEPTH = 10;

  private final GroupLayer sceneRoot;
  private final GroupLayer cameraRoot;

  private boolean isInitialized = false;

  /**
   * The top-left corner of the viewing area
   */
  private Vec2 camera = new Vec2(0.0f, 0.0f);

  protected Scene() {
    sceneRoot = graphics().createGroupLayer();
    cameraRoot = graphics().createGroupLayer();
    sceneRoot.add(cameraRoot);
    cameraRoot.setDepth(CAMERA_DEPTH);
  }

  public final void initialize(GroupLayer parent, float depth) {
    if (!isInitialized) {
      doInitialize();
    }
    sceneRoot.setDepth(depth);
    if (sceneRoot.parent() != parent) {
      parent.add(sceneRoot);
    }
    isInitialized = true;
  }

  public final boolean isInitialized() {
    return isInitialized;
  }

  public final void cleanup() {
    sceneRoot.clear();
    cameraRoot.clear();
    doCleanup();
    isInitialized = false;
  }

  protected final void setCamera(Vec2 position) {
    camera.set(position);
    cameraRoot.setTranslation(-camera.x, -camera.y);
  }

  protected final float cameraX() {
    return camera.x;
  }

  protected final float cameraY() {
    return camera.y;
  }

  public final void deactivateScene() {
    sceneRoot.parent().remove(sceneRoot);
  }

  protected final void addToCameraLayer(Layer layer) {
    cameraRoot.add(layer);
  }

  protected final void addToStaticLayer(Layer layer) {
    sceneRoot.add(layer);
  }

  public abstract void paint(float alpha);

  public abstract void update(float delta);

  protected abstract void doInitialize();

  protected abstract void doCleanup();

  @Override
  public void onTouchStart(Event[] touches) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTouchMove(Event[] touches) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onTouchEnd(Event[] touches) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPointerStart(playn.core.Pointer.Event event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPointerEnd(playn.core.Pointer.Event event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onPointerDrag(playn.core.Pointer.Event event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onKeyDown(playn.core.Keyboard.Event event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onKeyTyped(TypedEvent event) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onKeyUp(playn.core.Keyboard.Event event) {
    // TODO Auto-generated method stub

  }

}
