package net.illandril.games.playntest.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import net.illandril.games.playntest.core.PlayNTest;

public class PlayNTestJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("net/illandril/games/playntest/resources");
    PlayN.run(new PlayNTest());
  }
}
