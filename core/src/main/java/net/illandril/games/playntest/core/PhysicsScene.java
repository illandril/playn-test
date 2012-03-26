package net.illandril.games.playntest.core;

import static playn.core.PlayN.graphics;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import playn.core.CanvasLayer;
import playn.core.DebugDrawBox2D;
import playn.core.Keyboard.TypedEvent;

public abstract class PhysicsScene extends Scene {
  protected final static int DEBUG_DEPTH = 1000;
  protected final static boolean ALLOW_DEBUG = true;

  private final static Vec2 DEFAULT_GRAVITY = new Vec2(0.0f, 9.8f);
  private final static float STEP_SIZE = 1 /* second *// 30.0f /* PhysUpdatesPerSec */;
  private final static int VELOCITY_ITERATIONS = 10;
  private final static int POSITION_ITERATIONS = 10;

  // 6 feet = 1.8288
  // small mario = 32 pixels
  // large mario = 64 pixels
  // So if large mario is 6 ft... 64 / 1.8288 pixels per meter
  private final static float DEFAULT_PIXELS_PER_METER = 64.0f /* pixels *// 1.8288f /* meter */;

  protected final float pixelsPerMeter;
  protected final float metersPerPixel;
  private final Vec2 gravity;

  protected boolean showDebug = ALLOW_DEBUG;
  protected World world;
  protected DebugDrawBox2D debugDraw;
  private CanvasLayer debugLayer;

  /**
   * Elapsed seconds that have not yet been used for physics calculations
   */
  protected float deltaQueue = 0.0f;

  protected PhysicsScene() {
    this(DEFAULT_PIXELS_PER_METER, DEFAULT_GRAVITY);
  }

  protected PhysicsScene(float metersPerPixel, Vec2 gravity) {
    this.pixelsPerMeter = metersPerPixel;
    this.metersPerPixel = 1 / metersPerPixel;
    this.gravity = gravity;
  }

  public final void doInitialize() {
    world = new World(gravity, true /* allowSleep */);
    if (ALLOW_DEBUG) {
      debugDraw = new DebugDrawBox2D();
      debugDraw.setFlipY(false);
      debugDraw.setStrokeAlpha(100);
      debugDraw.setFillAlpha(75);
      debugDraw.setStrokeWidth(2);
      // debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_aabbBit | DebugDraw.e_centerOfMassBit
      // | DebugDraw.e_dynamicTreeBit | DebugDraw.e_jointBit | DebugDraw.e_pairBit);
      debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit
          | DebugDraw.e_centerOfMassBit | DebugDraw.e_pairBit);

      debugLayer = graphics().createCanvasLayer(graphics().width(), graphics().height());
      debugLayer.setDepth(CAMERA_DEPTH + DEBUG_DEPTH);
      debugDraw.setCanvas(debugLayer);
      addToStaticLayer(debugLayer);

      world.setDebugDraw(debugDraw);
    }
    populateWorld();
  }

  protected abstract void populateWorld();

  public void paint(float alpha) {
    if (ALLOW_DEBUG) {
      debugDraw.getCanvas().canvas().clear();
      if (showDebug) {
        debugDraw.setCamera(cameraX() * metersPerPixel, cameraY() * metersPerPixel, pixelsPerMeter);
        world.drawDebugData();
      }
    }
  }

  protected final Vec2 screenToWorldSize(float screenX, float screenY) {
    return new Vec2(screenX * metersPerPixel, screenY * metersPerPixel);
  }

  protected final Vec2 screenToWorldPosition(float screenX, float screenY) {
    return screenToWorldSize(screenX + cameraX(), screenY + cameraY());
  }

  protected final Vec2 worldToScreenSize(Vec2 worldPos) {
    return worldPos.mul(pixelsPerMeter);
  }

  protected final Vec2 worldToScreenPosition(Vec2 worldPos) {
    Vec2 newWorldPos = worldToScreenSize(worldPos);
    newWorldPos.addLocal(-cameraX(), -cameraY());
    return newWorldPos;
  }

  protected final void lookAt(Vec2 position) {
    Vec2 screenPos = worldToScreenSize(position);
    screenPos.addLocal(graphics().width() / -2.0f, graphics().height() / -2.0f);
    setCamera(screenPos);
  }

  @Override
  public void onKeyTyped(TypedEvent event) {
    super.onKeyTyped(event);
    if (ALLOW_DEBUG) {
      if (event.typedChar() == 'd') {
        showDebug = !showDebug;
      }
    }

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
