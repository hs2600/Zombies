package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.Screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public abstract class BaseGame extends Game {
    public static LabelStyle labelStyle;

    private static BaseGame game;
    public BaseGame() {
        game = this;
    }
    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }
    public void create() {
        labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        // prepare for multiple classes/stages to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor( im );
    }
}
