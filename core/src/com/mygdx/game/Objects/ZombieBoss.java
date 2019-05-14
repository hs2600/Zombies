package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class ZombieBoss extends BaseActor {
    public int health = 540;
    public int worth = 1000;
    public boolean dead;
    public boolean blasted = false;

    public ZombieBoss(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("Z4.png");
        setSize(1f,getHeight()/getWidth());
        setOrigin(getWidth()/2,getHeight()/2);
        setAcceleration(10);
        setMaxSpeed(1);
        setDeceleration(10);
        dead = false;
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setBoundaryPolygon(8, getWidth(), getHeight());
    }

    public void act(float dt) {
        super.act( dt );
        applyPhysics(dt);
        if ( getSpeed() > 0 )
            setRotation( getMotionAngle() );
        if (body != null)
            body.setLinearVelocity(velocityVec.x,velocityVec.y);
    }

    public boolean isDead()
    {
        return dead;
    }
    public void dead() {
        dead = true;
        clearActions();
        addAction( Actions.fadeOut(1) );
        addAction( Actions.after( Actions.removeActor() ) );
    }

}
