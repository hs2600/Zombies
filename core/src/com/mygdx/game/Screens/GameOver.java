package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;
import com.mygdx.game.Zombs;

public class GameOver extends BaseScreen {
    public void initialize() {
        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("GameStartBG.png");
        background.setSize(1600, 900);

        BaseActor title = new BaseActor(0, 0, mainStage);
        title.loadTexture("game-over.png");
        title.centerAtPosition(800, 500);
        title.moveBy(0, 100);
        b2dworld = new World(new Vector2(0,0),true);
    }
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.R))
            Zombs.setActiveScreen( new MenuScreen() );
    }
}
