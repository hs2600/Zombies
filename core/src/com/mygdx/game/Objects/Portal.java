package com.mygdx.game.Objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.game.BaseActor;

public class Portal extends BaseActor {
    public boolean remove;

    public Portal(float x, float y, Stage s) {
        super(x,y,s);
        loadTexture("portal2.png");
        setSize(1f,getHeight()/getWidth());
        setScale(1.5f);
        setOrigin(getWidth()/2,getHeight()/2);
        float random = MathUtils.random(90);
        addAction( Actions.forever( Actions.rotateBy(-60 + random*-1, 1) ) );
        remove = false;
        //setBoundaryPolygon(8, getWidth(), getHeight());
        getColor().a = 0.75f;

        Action pulse = Actions.sequence(
                Actions.fadeOut(1f),
                Actions.fadeIn(5f)
        );
        //addAction( Actions.forever(pulse) );

    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        //wrapAroundWorld();
    }
}
