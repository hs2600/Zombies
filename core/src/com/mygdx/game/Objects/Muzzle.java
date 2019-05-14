package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Muzzle extends BaseActor {

    public Muzzle(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("flash_rifle.png");
        //loadAnimationFromSheet("muzzle_flare.png", 3, 1, 0.2f, true);
        setSize(1,getHeight()/getWidth());
        //setOrigin(getWidth()*0.25f,getHeight()/2);
        setOrigin(getWidth()*0.25f,getHeight()/2);
        Action pulse = Actions.sequence(
        Actions.fadeIn(0.03f),
        Actions.fadeOut(0.03f)
        );
        addAction( Actions.forever(pulse) );
    }
}
