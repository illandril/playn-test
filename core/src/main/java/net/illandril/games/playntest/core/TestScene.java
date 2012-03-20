package net.illandril.games.playntest.core;

import net.illandril.games.playntest.core.characters.Mario;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class TestScene extends PhysicsScene {
  
  private Mario m;
  private Body p;
  private Vec2 pointerScreenPos;
  
  public TestScene() {
    super(1.0f, new Vec2(0.0f, 0.0f));
  }
  
  @Override
  protected void populateWorld() {
    for (int i = 0; i < 250; i++) {
      BodyDef def = new BodyDef();
      def.position = new Vec2(i * 10, 0);
      def.type = BodyType.DYNAMIC;
      Body b = world.createBody(def);
      FixtureDef fd = new FixtureDef();
      CircleShape shape = new CircleShape();
      shape.m_radius = 10f;
      fd.shape = shape;
      b.createFixture(fd);
    }
    BodyDef def = new BodyDef();
    def.position = new Vec2(0.0f, 0.0f);
    def.type = BodyType.DYNAMIC;
    p = world.createBody(def);
    FixtureDef fd = new FixtureDef();
    CircleShape shape = new CircleShape();
    shape.m_radius = 10f;
    fd.shape = shape;
    p.createFixture(fd);
    
    m = new Mario(p.getWorldCenter());
    addToCameraLayer(m.layer);
  }

  @Override
  public void onPointerStart(playn.core.Pointer.Event event) {
    super.onPointerStart(event);
    pointerScreenPos = new Vec2(event.x(), event.y());
  }

  @Override
  public void onPointerEnd(playn.core.Pointer.Event event) {
    super.onPointerEnd(event);
    pointerScreenPos = null;
  }

  @Override
  public void onPointerDrag(playn.core.Pointer.Event event) {
    super.onPointerDrag(event);
    pointerScreenPos = new Vec2(event.x(), event.y());
  }
  
  public void update(float delta) {
    if ( pointerScreenPos != null ) {
      Vec2 target = screenToWorldPosition(pointerScreenPos.x, pointerScreenPos.y);
      Vec2 ppos = p.getWorldCenter();
      Vec2 vel = target.sub(ppos);
      vel.normalize();
      vel.mulLocal(140);
      p.setLinearVelocity(vel);
    } else {
      p.setLinearVelocity(new Vec2(0,0));
    }
    super.update(delta);
//    System.out.println(p.getWorldCenter());
    lookAt(p.getWorldCenter());
    m.setPosition(p.getWorldCenter());
    m.update(delta);
  }
}
