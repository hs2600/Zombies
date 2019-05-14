package com.mygdx.game.Objects;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Bullet extends BaseActor {
    public boolean remove;

    public Bullet(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("artillery_trail4.png");
        setSize(1,getHeight()/getWidth());
        //setOrigin(getWidth()*0.25f,getHeight()/2);
        setOrigin(getX(),getHeight());
        bounds.set(getX(), getY(), getWidth(), getHeight());
        addAction( Actions.delay(0.5f) );
        //addAction( Actions.after( Actions.fadeOut(10.5f) ) );
        addAction( Actions.after( Actions.removeActor() ) );
        setSpeed(10);
        //setMaxSpeed(10);
        //setDeceleration(10);
        setBoundaryPolygon(8, getWidth()/2,getHeight());
    }

    public void act(float dt) {
        super.act( dt );
        applyPhysics(dt);
//        if (body != null)
//           body.setLinearVelocity(velocityVec.x,velocityVec.y);
    }
}
