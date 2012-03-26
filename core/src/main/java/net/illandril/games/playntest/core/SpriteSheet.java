package net.illandril.games.playntest.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.json;

import java.util.HashMap;
import java.util.Vector;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Json.Array;
import playn.core.Json.Object;
import playn.core.Json.TypedArray;
import playn.core.ResourceCallback;

public class SpriteSheet implements ResourceCallback<String> {
  private static class SpriteArray {
    private final Vector<Sprite> sprites;
    private int displayMilliseconds;

    private SpriteArray() {
      sprites = new Vector<Sprite>();
      displayMilliseconds = 0;
    }

    private void addSprite(Sprite sprite) {
      sprites.add(sprite);
      displayMilliseconds += sprite.displayMilliseconds;
    }
  }

  private static class Sprite {
    private final int height;
    private final int width;
    private final int x;
    private final int y;
    private final float originX;
    private final float originY;
    private final int displayMilliseconds;

    private Sprite(int height, int width, int x, int y, float originX, float originY,
        int displayMilliseconds) {
      this.height = height;
      this.width = width;
      this.x = x;
      this.y = y;
      this.originX = originX;
      this.originY = originY;
      this.displayMilliseconds = displayMilliseconds;
    }
  }

  private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();

  public static SpriteSheet get(String name) {
    if (!spriteSheets.containsKey(name)) {
      spriteSheets.put(name, new SpriteSheet(name));
    }
    return spriteSheets.get(name);
  }

  private HashMap<String, HashMap<String, SpriteArray>> sprites = new HashMap<String, HashMap<String, SpriteArray>>();
  private Image image;

  private SpriteSheet(String name) {
    image = assets().getImage("images/" + name + ".gif");
    assets().getText("images/" + name + ".json", this);
  }

  public void updateLayer(ImageLayer layer, String key, String subKey, float offset) {
    HashMap<String, SpriteArray> spriteArrays = sprites.get(key);
    if (spriteArrays != null) {
      SpriteArray spriteArray = spriteArrays.get(subKey);
      if (spriteArray != null) {
        int index = -1;
        if (spriteArray.sprites.size() == 0) {
          index = 0;
        } else {
          offset = offset % spriteArray.displayMilliseconds;
          while (offset >= 0) {
            index++;
            offset -= spriteArray.sprites.get(index).displayMilliseconds;
          }
        }
        Sprite sprite = spriteArray.sprites.get(index);
        layer.setImage(image);
        layer.setHeight(sprite.height);
        layer.setSize(sprite.width, sprite.height);
        layer.setOrigin(sprite.originX, sprite.originY);
        layer.setSourceRect(sprite.x, sprite.y, sprite.width, sprite.height);
      }
    }
  }

  public void done(String json) {
    Object o = json().parse(json);
    TypedArray<String> keys = o.keys();
    for (String key : keys) {
      HashMap<String, SpriteArray> spriteMap = new HashMap<String, SpriteArray>();
      sprites.put(key, spriteMap);
      Object subObject = o.getObject(key);
      TypedArray<String> subKeys = subObject.keys();
      for (String subKey : subKeys) {
        Object spAO = subObject.getObject(subKey);
        int bw = spAO.getInt("bw");
        int bh = spAO.getInt("bh");
        int bx = spAO.getInt("bx", 0);
        int by = spAO.getInt("by", 0);
        int bt = spAO.getInt("bt", 1000);
        SpriteArray array = new SpriteArray();
        Array a = spAO.getArray("sprites");
        int len = a.length();
        for (int i = 0; i < len; i++) {
          Object spO = a.getObject(i);
          int w = spO.getInt("w", bw);
          int h = spO.getInt("h", bh);
          int x = spO.getInt("x", bx);
          int y = spO.getInt("y", by);
          int t = spO.getInt("t", bt);
          array.addSprite(new Sprite(h, w, x, y, w / 2.0f, h / 2.0f, t));
        }
        spriteMap.put(subKey, array);
      }
    }
  }

  @Override
  public void error(Throwable err) {
    System.out.println(err);
  }
}
