package net.illandril.games.playntest.core;

import static playn.core.PlayN.graphics;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import playn.core.CanvasLayer;
import playn.core.DebugDrawBox2D;

public abstract class PhysicsScene extends Scene {

  protected final static boolean ALLOW_DEBUG = true;

  private final static Vec2 DEFAULT_GRAVITY = new Vec2(0.0f, -9.8f);
  private final static float STEP_SIZE = 1 /* second *// 30.0f /* PhysUpdatesPerSec */;
  private final static int VELOCITY_ITERATIONS = 10;
  private final static int POSITION_ITERATIONS = 10;
  private final static float DEFAULT_METERS_PER_PIXEL = 10.0f;

  private final float metersPerPixel;
  private final float pixelsPerMeter;
  private final Vec2 gravity;

  protected boolean showDebug = ALLOW_DEBUG;
  protected World world;
  protected DebugDrawBox2D debugDraw;

  /**
   * The top-left corner of the viewing area
   */
  private Vec2 camera = new Vec2(0.0f, 0.0f);

  /**
   * Elapsed seconds that have not yet been used for physics calculations
   */
  protected float deltaQueue = 0.0f;

  protected PhysicsScene() {
    this(DEFAULT_METERS_PER_PIXEL, DEFAULT_GRAVITY);
  }

  protected PhysicsScene(float metersPerPixel, Vec2 gravity) {
    this.metersPerPixel = metersPerPixel;
    this.pixelsPerMeter = 1 / metersPerPixel;
    this.gravity = gravity;
  }

  public final void doInitialize() {
    world = new World(gravity, true /* allowSleep */);
    if (ALLOW_DEBUG) {
      debugDraw = new DebugDrawBox2D();
      debugDraw.setFlipY(true);
      debugDraw.setStrokeAlpha(100);
      debugDraw.setFillAlpha(75);
      debugDraw.setStrokeWidth(metersPerPixel);
      // debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_aabbBit | DebugDraw.e_centerOfMassBit
      // | DebugDraw.e_dynamicTreeBit | DebugDraw.e_jointBit | DebugDraw.e_pairBit);
      debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit
          | DebugDraw.e_centerOfMassBit | DebugDraw.e_pairBit);

      CanvasLayer layer = graphics().createCanvasLayer(graphics().width(), graphics().height());
      layer.setDepth(10000);
      debugDraw.setCanvas(layer);
      sceneRoot.add(layer);

      world.setDebugDraw(debugDraw);
    }
    populateWorld();
  }

  protected abstract void populateWorld();

  public void paint(float alpha) {
    if (showDebug) {
      debugDraw.setCamera(camera.x, camera.y, metersPerPixel);
      debugDraw.getCanvas().canvas().clear();
      world.drawDebugData();
    }
  }

  protected final Vec2 screenToWorldSize(float screenX, float screenY) {
    return new Vec2(screenX / metersPerPixel, screenY / metersPerPixel);
  }

  protected final Vec2 screenToWorldPosition(float screenX, float screenY) {
    Vec2 worldPos = screenToWorldSize(screenX, screenY);
    worldPos.y = worldPos.y * -1;
    worldPos.addLocal(camera);
    return worldPos;
  }

  protected final void lookAt(Vec2 position) {
    Vec2 halfScreen = screenToWorldSize(graphics().screenWidth() / -2.0f,
        graphics().screenHeight() / 2.0f);
    camera.set(position).addLocal(halfScreen);
  }

  public void update(float delta) {
    deltaQueue += (delta / 1000);
    while (deltaQueue > STEP_SIZE) {
      world.step(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
      deltaQueue -= STEP_SIZE;
    }
  }

  protected void doCleanup() {
    world = null;
    debugDraw = null;
  }
}
