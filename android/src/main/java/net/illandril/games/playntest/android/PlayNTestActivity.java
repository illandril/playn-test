package net.illandril.games.playntest.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import net.illandril.games.playntest.core.PlayNTest;

public class PlayNTestActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("net/illandril/games/playntest/resources");
    PlayN.run(new PlayNTest());
  }
}
