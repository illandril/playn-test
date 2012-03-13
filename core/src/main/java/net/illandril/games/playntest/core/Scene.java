package net.illandril.games.playntest.core;

import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;
import playn.core.Touch;
import playn.core.Touch.Event;

public abstract class Scene implements Keyboard.Listener, Pointer.Listener, Touch.Listener {

  protected final GroupLayer sceneRoot = graphics().createGroupLayer();

  private boolean isInitialized = false;

  public final void initialize() {
    if (!isInitialized) {
      doInitialize();
    }
    isInitialized = true;
  }

  public final boolean isInitialized() {
    return isInitialized;
  }

  public final void cleanup() {
    sceneRoot.clear();
    doCleanup();
    isInitialized = false;
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
