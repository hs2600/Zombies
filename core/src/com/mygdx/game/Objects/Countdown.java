package com.mygdx.game.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.BaseActor;

public class Countdown extends BaseActor {

    Label labelCountdown;
    Label.LabelStyle labelStyle;
    BitmapFont font;
    public int seconds = 0;
    public long wait = 0;
    public boolean inProgress = false;

    public Countdown(float x, float y, Stage s) {
        super(x,y,s);
        //loadTexture("work_circle.png");
        loadTexture("circle_dotted.png");
        setSize(40,getHeight()/getWidth()*40);
        setOrigin(getWidth()/2,getHeight()/2);
        Action pulse = Actions.sequence(
        Actions.scaleTo(1.15f, 1.15f, 0.5f), Actions.scaleTo(0.85f, 0.85f, 0.5f) );
        addAction( Actions.forever(pulse) );

        font = new BitmapFont(Gdx.files.internal("Comic.fnt"),
                Gdx.files.internal("Comic.png"), false);
        font.setColor(Color.WHITE);
        //font.getData().setScale(0.35f);

        labelStyle = new Label.LabelStyle(font,Color.WHITE);
        labelCountdown = new Label("" + seconds,labelStyle);
        labelCountdown.setPosition(13,-14);
        //labelInfo.setPosition(Gdx.graphics.getWidth()/2-30,Gdx.graphics.getHeight()/2-50);
        labelCountdown.setFontScale(0.5f);
        addActor(labelCountdown);
    }

    public void act(float dt) {
        super.act( dt );

        if (TimeUtils.timeSinceNanos(wait) > 1000000000) {
            wait = TimeUtils.nanoTime();
            if(seconds > 0)
                seconds--;
        }

        labelCountdown.setText("" + seconds);

        if(seconds == 0) {
            addAction(Actions.fadeOut(0.5f));
            inProgress = false;
            setVisible(false);
        }
        else
            addAction(Actions.fadeIn(0.5f));
    }
}
