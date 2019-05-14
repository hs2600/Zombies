package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseActor;

public class Barrel extends BaseActor {
    public boolean remove;
    public int damage = 10;

    public Barrel(float x, float y, Stage s) {
        super(x,y,s);
        remove = false;

        loadTexture("barrel.png");
        setSize(1,getHeight()/getWidth());
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setOrigin(getWidth()/2,getHeight()/2);
        setBoundaryPolygon(8, getWidth(), getHeight());
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
    }
}
