package net.illandril.games.playntest.core.characters;

import net.illandril.games.playntest.core.GameObject;
import net.illandril.games.playntest.core.Scene;
import net.illandril.games.playntest.core.SpriteSheet;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Wall extends GameObject {

  public Wall(Scene scene, World world, Vec2 position, String type, float scale) {
    super(scene, position);
    BodyDef def = getBodyDef(BodyType.STATIC, position.mulLocal(scale));
    Body p = world.createBody(def);

    PolygonShape shape = new PolygonShape();
    shape.setAsBox(16 * scale, 16 * scale);
    FixtureDef fd = getFixtureDef(shape);
    p.createFixture(fd);

    SpriteSheet.get("smbsheet").updateLayer(layer, "wall", type, 0);
  }
}
