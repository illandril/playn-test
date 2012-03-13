package net.illandril.games.playntest.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import net.illandril.games.playntest.core.PlayNTest;

public class PlayNTestHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("playntest/");
    PlayN.run(new PlayNTest());
  }
}
