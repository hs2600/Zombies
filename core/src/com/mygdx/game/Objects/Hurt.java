package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Hurt extends BaseActor {

    public Hurt(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("hurt.png");
        setSize(1,getHeight()/getWidth());
        setOrigin(getWidth()*0.25f,getHeight()/2);
        Action pulse = Actions.sequence(
        Actions.scaleTo(1.05f, 1.05f, 0.5f), Actions.scaleTo(0.95f, 0.95f, 0.5f) );
        addAction( Actions.forever(pulse) );
    }
}
