package com.mygdx.game.Objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Tree extends BaseActor {
    public boolean remove;

    public Tree(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("tree2.png");
        setSize(1,getHeight()/getWidth());
        bounds.set(getX(), getY(), getWidth()*0.45f, getHeight()*0.45f);
        setOrigin(getWidth()/2,getHeight()/2);
        //float random = MathUtils.random(30);
        //addAction( Actions.forever( Actions.rotateBy(30 + random, 1) ) );
        //setSpeed(5 + random);
        //setMaxSpeed(5 + random);
        //setDeceleration(0);
        //setMotionAngle( MathUtils.random(360) );
        remove = false;
        setBoundaryPolygon(8, getWidth(), getHeight());
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        //wrapAroundWorld();
    }
}
