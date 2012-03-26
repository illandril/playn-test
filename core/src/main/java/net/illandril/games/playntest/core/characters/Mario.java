package net.illandril.games.playntest.core.characters;

import net.illandril.games.playntest.core.GameObject;
import net.illandril.games.playntest.core.Scene;
import net.illandril.games.playntest.core.SpriteSheet;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Mario extends GameObject {
  private enum Status {
    Standing("stand"), Walking("walk"), Running("run"), Reversal("reverse"), Jumping("jump"), ;

    private final String spriteSubtype;

    private Status(String spriteSubtype) {
      this.spriteSubtype = spriteSubtype;
    }
  }

  private float deltaInStatus = 0;
  private Body p;

  private Status status = Status.Standing;
  private boolean reversed = false;
  private final float scale;

  public Mario(Scene scene, World world, Vec2 position, float scale) {
    super(scene, position);
    this.scale = scale;
    BodyDef def = getBodyDef(BodyType.DYNAMIC, position.mulLocal(scale));
    p = world.createBody(def);

    FixtureDef fd = getFixtureDef(getSafeBox(24 * scale, 32 * scale));
    p.createFixture(fd);

    SpriteSheet.get("smbsheet");
  }

  public void setLinearVelocity(Vec2 vel) {
    float oldX = p.getLinearVelocity().x;
    boolean wasLeftNowRight = oldX < 0 && vel.x > 0;
    boolean wasRightNowLeft = oldX > 0 && vel.x < 0;
    if (status == Status.Running && (wasLeftNowRight || wasRightNowLeft)) {
      status = Status.Reversal;
      deltaInStatus = 0;
    } else if (status != Status.Reversal) {
      p.setLinearVelocity(vel);
      Status newStatus = runWalkOrStand();
      if (newStatus != status) {
        status = newStatus;
        deltaInStatus = 0;
      }
    }
  }

  public Vec2 getWorldCenter() {
    return p.getWorldCenter();
  }

  private Status runWalkOrStand() {
    Vec2 velocity = p.getLinearVelocity();
    if (velocity.x == 0) {
      return Status.Standing;
    } else if (velocity.x > 100 * scale || velocity.x < -100 * scale) {
      return Status.Running;
    } else {
      return Status.Walking;
    }
  }

  public void update(float delta) {
    Vec2 center = p.getWorldCenter();
    setPosition(center.x / scale, center.y / scale);
    deltaInStatus += delta;
    if (status == Status.Reversal) {
      if (deltaInStatus >= 300) {
        status = runWalkOrStand();
        deltaInStatus = 0;
      } else {
        // p.setLinearVelocity(new Vec2(0, 0));
      }
    } else {
      status = runWalkOrStand();
    }
    SpriteSheet.get("smbsheet").updateLayer(layer, "mario_small", status.spriteSubtype,
        deltaInStatus);
    Vec2 velocity = p.getLinearVelocity();
    // log().debug(Float.toString(velocity.x));
    if (velocity.x < 0 && !reversed) {
      reversed = true;
      layer.setScale(-1, 1);
    } else if (velocity.x > 0 && reversed) {
      layer.setScale(-1, 1);
      reversed = false;
    }
  }

  public void paint(float alpha) {

  }
}
