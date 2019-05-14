package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseActor;

public class Rock extends BaseActor {

    public Rock(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("rock2.png");
        //bounds.set(getX(), getY(), getWidth(), getHeight());
        setSize(1,getHeight()/getWidth());
        bounds.set(getX(), getY(), getWidth()*0.55f, getHeight()*0.55f);
        setOrigin(getWidth()/2,getHeight()/2);
        setBoundaryPolygon(8, getWidth(), getHeight());
    }
}
