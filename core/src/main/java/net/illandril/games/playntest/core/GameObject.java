package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import org.jbox2d.common.Vec2;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public abstract class GameObject implements ResourceCallback<Image> {

  private Vec2 position;
  private float angle;
  public final ImageLayer layer;

  protected GameObject(Vec2 position) {
    this.position = position;
    this.angle = angle;
    Image i = assets().getImage("images/" + image());
    i.addCallback(this);
    layer = graphics().createImageLayer(i);
    layer.setOrigin(spriteWidth() / 2, spriteHeight() / 2);
    layer.setWidth(spriteWidth());
    layer.setHeight(spriteHeight());
    layer.setSourceRect(spriteX(), spriteY(), spriteWidth(), spriteHeight());
    layer.setTranslation(position.x, position.y);
    layer.setRotation(angle);
  }
  
  public void setPosition(float x, float y) {
    position.set(x, y);
    layer.setTranslation(position.x, position.y);
  }

  public void setPosition(Vec2 pos) {
    setPosition(pos.x, pos.y);
  }
  
  protected abstract String image();
  protected abstract int spriteWidth();
  protected abstract int spriteHeight();
  protected abstract int spriteX();
  protected abstract int spriteY();

  public void update(float delta) {

  }

  public void paint(float alpha) {

  }

  @Override
  public void done(Image image) {
    // since the image is loaded, we can use its width and height
//    layer.setWidth(image.width());
//    layer.setHeight(image.height());
//    layer.setOrigin(image.width() / 2f, image.height() / 2f);
//    layer.setScale(getWidth() / image.width(), getHeight() / image.height());
//    layer.setTranslation(x, y);
//    layer.setRotation(angle);
//    initPostLoad(peaWorld);
  }

  @Override
  public void error(Throwable err) {
    PlayN.log().error("Error loading image: " + err.getMessage());
  }
}
