package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseActor;

public class MysteryBox extends BaseActor {

    public MysteryBox(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("mystery_box.png");
        setSize(1,getHeight()/getWidth());
        //setOrigin(getX()-2,getY()-2);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setBoundaryPolygon(4, getWidth()+0.5f,getHeight()+0.5f);

    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
    }
}
