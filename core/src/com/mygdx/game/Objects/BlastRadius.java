package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseActor;

public class BlastRadius extends BaseActor {
    public boolean remove;


    public BlastRadius(float x, float y, Stage s) {
        super(x,y,s);
        remove = false;

        loadTexture("blast_radius.png");
        //loadTexture("pixel.png");
        setSize(7,7);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setOrigin(getWidth()/2,getHeight()/2);
        setBoundaryPolygon(8, getWidth(), getHeight());
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
    }
}
