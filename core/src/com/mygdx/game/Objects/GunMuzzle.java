package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class GunMuzzle extends BaseActor {

    public GunMuzzle(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("flash_gun.png");
        setSize(0.85f,getHeight()/getWidth()*0.85f);
        //setOrigin(getWidth()*0.25f,getHeight()/2);
        setOrigin(getWidth(),getHeight()/2);
        Action pulse = Actions.sequence(
        Actions.fadeIn(0.03f),
        Actions.fadeOut(0.03f)
        );
        //addAction( Actions.forever(pulse) );
    }
}
