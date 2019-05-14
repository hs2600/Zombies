package com.mygdx.game.Objects;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.BaseActor;

public class Explosion extends BaseActor {

    public Explosion(float x, float y, Stage s) {
        super(x,y,s);
        loadAnimationFromSheet("explosion5.png", 5, 5, 0.035f, false);
        setSize(4,getHeight()/getWidth()*4);
        setOrigin(getX(),getHeight());
    }
    public void act(float dt) {
        super.act(dt);
        if ( isAnimationFinished() )
        remove();
    }
}
