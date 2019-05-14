package com.mygdx.game.Objects.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
//import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.BaseActor;
import com.mygdx.game.Constants;
import com.mygdx.game.Objects.*;

public class Rifle extends BaseActor {
    private Muzzle muzzle;
    public Sound fireSound;
    public Sound dryClipSound;

    //float shootTimer;
    long fireTime = TimeUtils.nanoTime();
    long reloadTime = TimeUtils.nanoTime();

    public int bulletCount = 100;
    public int magazineCount = 3;
    public int magazineSize = 100;

    public void initObjects(){
        fireSound = Gdx.audio.newSound(Gdx.files.internal("sound/MachineGun.ogg"));
        dryClipSound = Gdx.audio.newSound(Gdx.files.internal("sound/dry_clip.ogg"));
    }

    public Rifle(float x, float y, Stage s) {
        super(x,y,s);

        loadTexture("player_gun1.png");
//        setVisible(false);
        setSize(1,getHeight()/getWidth());
        setOrigin(getWidth()*0.25f,getHeight()/2);
        muzzle = new Muzzle(0,0, s);
        muzzle.setPosition(muzzle.getWidth()-0.35f, getHeight()/2 - muzzle.getHeight()/2-0.035f );
        muzzle.setScale(0.4f,0.4f);
        muzzle.setVisible(false);

        initObjects();
    }

    public void act(float dt) {
        super.act( dt );
        muzzle.setVisible(false);
       setAnimationPaused( !isMoving() );
    }
}
