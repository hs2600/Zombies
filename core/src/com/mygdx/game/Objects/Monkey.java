package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.BaseActor;

public class Monkey extends BaseActor {

    public Monkey(float x, float y, Stage s) {
        super(x,y,s);
        loadAnimationFromSheet("monkey.png", 2, 4, 0.045f, true); //0.035f
        setSize(0.55f,getHeight()/getWidth()*0.55f);
        setOrigin(getX(),getHeight());
        setSpeed(5);
        setAcceleration(5);
        setMaxSpeed(5);
        setDeceleration(2.5f);
        //setScale(0.15f);
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        float delay = 8f; // seconds
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Do your work
                if ( isAnimationFinished() )
                    remove();
            }
        }, delay);


    }
}
