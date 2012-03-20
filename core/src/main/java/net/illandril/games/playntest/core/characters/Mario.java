package net.illandril.games.playntest.core.characters;

import static playn.core.PlayN.json;
import static playn.core.PlayN.assets;

import net.illandril.games.playntest.core.GameObject;
import net.illandril.games.playntest.core.SpriteSheet;

import org.jbox2d.common.Vec2;

import playn.core.Json.Array;
import playn.core.Json.Object;
import playn.core.ResourceCallback;

public class Mario extends GameObject {
  private float unusedDelta = 0;
  
  public Mario(Vec2 position) {
    super(position);
    SpriteSheet.get("smbsheet");
  }

  @Override
  protected String image() {
    return "smbsheet.gif";
  }

  @Override
  protected int spriteWidth() {
    return 24;
  }

  @Override
  protected int spriteHeight() {
    return 32;
  }

  @Override
  protected int spriteX() {
    return 6;
  }

  @Override
  protected int spriteY() {
    return 196;
  }

  public void update(float delta) {
    unusedDelta += delta;
    String anim = "stand";
    int animNum = Math.round((unusedDelta % 5000) / 1000);
    switch( animNum ) {
      case 0:
        anim = "stand";
        break;
      case 1:
        anim = "walk";
        break;
      case 2:
        anim = "run";
        break;
      case 3:
        anim = "jump";
        break;
      case 4:
        anim = "reverse";
        break;
    }
    SpriteSheet.get("smbsheet").updateLayer(layer, "mario_small", anim, unusedDelta);
  }

  public void paint(float alpha) {

  }
}
