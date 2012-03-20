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

  private final static Vec2 DEFAULT_GRAVITY = new Vec2(0.0f, -9.8f);
  private final static float STEP_SIZE = 1 /* second *// 30.0f /* PhysUpdatesPerSec */;
  private final static int VELOCITY_ITERATIONS = 10;
  private final static int POSITION_ITERATIONS = 10;
  private final static float DEFAULT_METERS_PER_PIXEL = 1.0f;

  private final float metersPerPixel;
  private final float pixelsPerMeter;
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
      debugDraw.setFlipY(false);
      debugDraw.setStrokeAlpha(100);
      debugDraw.setFillAlpha(75);
      debugDraw.setStrokeWidth(metersPerPixel);
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
        debugDraw.setCamera(cameraX(), cameraY(), metersPerPixel);
        world.drawDebugData();
      }
    }
  }

  protected final Vec2 screenToWorldSize(float screenX, float screenY) {
    return new Vec2(screenX / metersPerPixel, screenY / metersPerPixel);
  }

  protected final Vec2 screenToWorldPosition(float screenX, float screenY) {
    Vec2 worldPos = screenToWorldSize(screenX, screenY);
//    worldPos.y = worldPos.y * -1;
    worldPos.addLocal(cameraX(), cameraY());
    return worldPos;
  }

  protected final void lookAt(Vec2 position) {
    Vec2 halfScreen = screenToWorldSize(graphics().width() / -2.0f,
        graphics().height() / -2.0f);
    super.setCamera(halfScreen.addLocal(position));
  }

  @Override
  public void onKeyTyped(TypedEvent event) {
    super.onKeyTyped(event);
    if (ALLOW_DEBUG) {
      if ( event.typedChar() == 'd') {
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
