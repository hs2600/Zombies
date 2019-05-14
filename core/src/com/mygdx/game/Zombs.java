package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.mygdx.game.Screens.GameOver;
import com.mygdx.game.Screens.LevelScreen;
import com.mygdx.game.Screens.MenuScreen;

public class Zombs extends BaseGame {
    public void create() {
        super.create();
        // Load assets
        Assets.instance.init(new AssetManager());
        //setActiveScreen( new LevelScreen() );
        setActiveScreen( new MenuScreen() );
    }
}
