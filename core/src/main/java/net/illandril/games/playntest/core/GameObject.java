package net.illandril.games.playntest.core;

import static playn.core.PlayN.graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import playn.core.ImageLayer;

public abstract class GameObject {

  private final static BodyDef bodyDef = new BodyDef();
  private final static FixtureDef fixtureDef = new FixtureDef();

  protected static BodyDef getBodyDef(BodyType type, Vec2 position) {
    bodyDef.userData = null;
    bodyDef.position = position;
    bodyDef.angle = 0f;
    bodyDef.linearVelocity.set(0f, 0f);
    bodyDef.angularVelocity = 0f;
    bodyDef.linearDamping = 0f;
    bodyDef.angularDamping = 0f;
    bodyDef.allowSleep = true;
    bodyDef.awake = true;
    bodyDef.fixedRotation = false;
    bodyDef.bullet = false;
    bodyDef.type = type;
    bodyDef.active = true;
    bodyDef.inertiaScale = 1.0f;
    return bodyDef;
  }

  protected static Shape getSafeBox(float w, float h) {
    PolygonShape shape = new PolygonShape();
    //shape.SetAsBox(size.x / 2, size.y / 2);
    // Make the boxes have slightly angled edges to avoid having things get stuck (lousy floating point rounding!)
    Vec2 halfSize = new Vec2(w, h);
    halfSize.mulLocal(0.5f);
    float edging = 0.01f;
    shape.set(new Vec2[]{
        new Vec2(0, halfSize.y),
        new Vec2(-halfSize.x + edging, halfSize.y - edging),
        new Vec2(-halfSize.x, 0),
        new Vec2(-halfSize.x + edging, -halfSize.y + edging),
        new Vec2(0, -halfSize.y),
        new Vec2(halfSize.x - edging, -halfSize.y + edging),
        new Vec2(halfSize.x, 0),
        new Vec2(halfSize.x - edging, halfSize.y - edging)
    }, 8);
    return shape;
  }
  
  protected static FixtureDef getFixtureDef(Shape shape) {
    fixtureDef.shape = shape;
    fixtureDef.userData = null;
    fixtureDef.friction = 1.0f;
    fixtureDef.restitution = 0f;
    fixtureDef.density = 0f;
    fixtureDef.filter.categoryBits = 0;
    fixtureDef.filter.maskBits = 0;
    fixtureDef.filter.groupIndex = 0;
    fixtureDef.filter.categoryBits = 0x0001;
    fixtureDef.filter.maskBits = 0xFFFF;
    fixtureDef.filter.groupIndex = 0;
    fixtureDef.isSensor = false;
    return fixtureDef;
  }

  public final ImageLayer layer;

  protected GameObject(Scene scene, Vec2 position) {
    layer = graphics().createImageLayer(graphics().createImage(10, 10));
    scene.addToCameraLayer(layer);
    setPosition(position);
  }

  public void setPosition(float x, float y) {
    layer.setTranslation(x, y);
  }

  public void setPosition(Vec2 pos) {
    setPosition(pos.x, pos.y);
  }

  public void update(float delta) {

  }

  public void paint(float alpha) {

  }
}
