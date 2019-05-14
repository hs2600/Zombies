package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;
import com.mygdx.game.Zombs;

public class MenuScreen extends BaseScreen {
    public void initialize() {
        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("game-bg3.png");
        background.setSize(1600, 900);
//        BaseActor title = new BaseActor(0, 0, mainStage);
//        title.loadTexture("game-title.png");
//        title.centerAtPosition(400, 300);
//        title.moveBy(0, 100);
        BaseActor start = new BaseActor(0, 0, mainStage);
        start.loadTexture("s2start.png");
        start.centerAtPosition(800, 550);

        Action pulse = Actions.sequence(
        Actions.scaleTo(1.05f, 1.05f, 0.5f), Actions.scaleTo(0.95f, 0.95f, 0.5f) );
        start.addAction( Actions.forever(pulse) );

        b2dworld = new World(new Vector2(0,0),true);
    }
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Keys.S))
            Zombs.setActiveScreen( new LevelScreen() );
    }
}
