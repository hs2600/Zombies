package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Warp extends BaseActor {
    public Warp(float x, float y, Stage s) {
        super(x,y,s);
//        loadAnimationFromSheet("portal.png", 4, 8, 0.05f, true);
        //loadTexture("teleport.png");
        loadAnimationFromSheet("teleport2.png", 2, 2, 0.222f, true);
        setSize(1.5f,getHeight()/getWidth()*1.5f);
        //setOrigin(getWidth()+2f,getHeight()/2);
        addAction( Actions.delay(4) );
        //addAction(Actions.color(Color.BROWN));
        addAction( Actions.after( Actions.fadeOut(1f) ) );
        addAction( Actions.after( Actions.removeActor() ) );
    }
}
