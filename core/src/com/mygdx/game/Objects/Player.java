package com.mygdx.game.Objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Constants;
import com.mygdx.game.Level;
import com.mygdx.game.Screens.LevelScreen;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mygdx.game.Constants.SHOOT_WAIT_TIME;

public class Player extends BaseActor {
    private TextureRegion regPlayer;
    private Muzzle muzzle;
    private GunMuzzle gunMuzzle;
    private Hurt hurt;
    public int health;
    ArrayList<Bullet> bulletList;
    float shootTimer;
    public boolean isHurt;
    public boolean isTeleporting;
    public boolean isMonkeyActive = false;
    public int score;
    public Sound machinegunSound;
    public Sound handgunSound;
    //public Sound loadSound;
    public Sound dryClipSound;
    public Sound teleport_Sound;

    //public Sound heartbeatSound;
    long fireTime = TimeUtils.nanoTime();
    long reloadTime = TimeUtils.nanoTime();
    public int gunOption = 1;
    BaseActor gun1;
    BaseActor gun2;
    Iterator<Portal> iteratorP;
    public int gun1Bullets = 10;
    public int gun1Magazines = 15;
    public int gun2Bullets = 120;
    public int gun2Magazines = 0;
    public int grenadeCount = 0;
    public int healtkitCount = 0;
    public int monkeyCount = 0;

    public void initObjects(){
        machinegunSound = Gdx.audio.newSound(Gdx.files.internal("sound/MachineGun.ogg"));
        handgunSound = Gdx.audio.newSound(Gdx.files.internal("sound/smith_wesson.ogg"));
        dryClipSound = Gdx.audio.newSound(Gdx.files.internal("sound/dry_clip.ogg"));
        teleport_Sound = Gdx.audio.newSound(Gdx.files.internal("sound/teleport.ogg"));
    }

    public Player(float x, float y, Stage s) {
        super(x,y,s);

        gun1 = new BaseActor(0,0,s);
        gun1.loadTexture("player_gun1.png");
        gun1.setSize(1,gun1.getHeight()/gun1.getWidth());
        addActor(gun1);

        gun2 = new BaseActor(0,0,s);
        gun2.loadTexture("player_gun2.png");
        gun2.setSize(1,gun1.getHeight()/gun1.getWidth());
        addActor(gun2);

        gun2.setVisible(false);

        loadTexture("p.png");
        //gun1.setVisible(false);
        setAcceleration(5);
        setMaxSpeed(10);
        setDeceleration(5);
        setSize(1,getHeight()/getWidth());
        setOrigin(getWidth()*0.25f,getHeight()/2);
        // Bounding box for collision detection
        bounds.set(getX(), getY(), getWidth()*0.45f, getHeight()*0.65f);
        muzzle = new Muzzle(0,0, s);
        muzzle.setPosition(muzzle.getWidth()-0.35f, getHeight()/2 - muzzle.getHeight()/2-0.035f );
        muzzle.setScale(0.4f,0.4f);
        muzzle.setVisible(false);

        gunMuzzle = new GunMuzzle(0,0, s);
        gunMuzzle.setPosition(gunMuzzle.getWidth()-0.6f, getHeight()/2 - gunMuzzle.getHeight()/2-0.07f );
        gunMuzzle.setScale(0.4f,0.4f);
        gunMuzzle.setVisible(false);

        hurt = new Hurt(0,0, s);
        hurt.centerAtPosition( getWidth()/2, getHeight()/2 );
        health = 100;
        bulletList = new ArrayList<Bullet>();
        addActor(hurt);
        hurt.addAction(Actions.fadeOut(0.1f));
        addActor(muzzle);
        addActor(gunMuzzle);
        isHurt = false;
        //hurt.setVisible(false);
        setBoundaryPolygon(8, getWidth()/2,getHeight());
        initObjects();
    }

    public void act(float dt) {
        if(Constants.gamePaused)
            return;

        super.act( dt );

        //muzzle.setVisible(false);
        velocityVec.set(0,0);
        muzzle.setVisible(false);
        gunMuzzle.setVisible(false);

        if(!isTeleporting) {

            if (Gdx.input.isKeyPressed(Keys.A) && getX() > 0)
                velocityVec.x = -Constants.SPR_MOVESPEED;
            //accelerateAtAngle(180);
            if (Gdx.input.isKeyPressed(Keys.D) && getX() + getWidth() < worldBounds.width)
                velocityVec.x = Constants.SPR_MOVESPEED;
            if (Gdx.input.isKeyPressed(Keys.W) && getY() + getHeight() < worldBounds.height)
                velocityVec.y = Constants.SPR_MOVESPEED;
            if (Gdx.input.isKeyPressed(Keys.S) && getY() > 0)
                velocityVec.y = -Constants.SPR_MOVESPEED;
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
            //Shooting code
            if(!LevelScreen.countdown.inProgress) {
                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000) {
                    if (gunOption == 1) {
                        if (gun1Bullets == 0) {
                            if(gun1Magazines > 0) {
                                LevelScreen.countDown(1);
                                LevelScreen.reloadPistolSound.play();
                                gun1Magazines--;
                                gun1Bullets=10;
                            }
                            else{
                                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000)
                                    reloadTime = TimeUtils.nanoTime();
                                dryClipSound.play();
                                LevelScreen.labelInfo.setText("Out of bullets...");
                                LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                                LevelScreen.labelInfo.addAction(Actions.delay(1));
                                LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));
                            }
                        } else {
                            //Shooting code
                            if (TimeUtils.timeSinceNanos(fireTime) > 210000000) {
                                fireTime = TimeUtils.nanoTime();
                                handgunSound.play(0.25f, 1f, 0);
                            }

                            shootTimer += dt;
                            if (shootTimer >= SHOOT_WAIT_TIME * 2) {
                                shootTimer = 0;
                                gunMuzzle.setVisible(true);
                                shoot();
                                gun1Bullets -= 1;
                            }
                        }

                    } else if (gunOption == 2) {
                        if (gun2Bullets == 0) {
                            if(gun2Magazines > 0) {
                                LevelScreen.countDown(4);
                                LevelScreen.reloadRifleSound.play();
                                gun2Magazines--;
                                gun2Bullets=120;
                            }
                            else{
                                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000)
                                    reloadTime = TimeUtils.nanoTime();
                                dryClipSound.play();
                                LevelScreen.labelInfo.setText("Out of bullets...");
                                LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                                LevelScreen.labelInfo.addAction(Actions.delay(1));
                                LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));
                            }
                        } else {
                            //Shooting code
                            if (TimeUtils.timeSinceNanos(fireTime) > 305000000) { //305000000, 406666666.7
                                fireTime = TimeUtils.nanoTime();
                                machinegunSound.play(0.25f);
                            }

                            shootTimer += dt;
                            if (shootTimer >= SHOOT_WAIT_TIME) {
                                shootTimer = 0;
                                muzzle.setVisible(true);
                                shoot();
                                gun2Bullets -= 1;
                            }
                        }
                    }
                }
            }


//            if (TimeUtils.timeSinceNanos(fireTime) > 305000000) {
//                fireTime = TimeUtils.nanoTime();
//                machinegunSound.play(0.25f);
//            }
//            shootTimer += dt;
//            if (shootTimer >= Constants.SHOOT_WAIT_TIME) {
//                shootTimer = 0;
//                shoot();
//            }
        }

        applyPhysics(dt);
        setAnimationPaused( !isMoving() );

        boundToWorld();

//        if(shakeInProgress)
//                ((OrthographicCamera) getStage().getCamera()).zoom -= 0.001;
//        else
            alignCamera();

        if (body != null) {
        body.setLinearVelocity(velocityVec.x,velocityVec.y);
        }

        if (health <= 0)
            hurt.setVisible(false);

        if (isHurt)
            hurt.addAction(Actions.fadeIn(0.1f));
        else
            hurt.addAction(Actions.fadeOut(2));

        if(gunOption == 3 || gunOption == 4)
            return;

        gun1.setVisible(false);
        gun2.setVisible(false);

        if(gunOption ==1)
            gun1.setVisible(true);
        if(gunOption ==2)
            gun2.setVisible(true);
    }

    public void warp() {
        if ( getStage() == null)
        return;

        if(isTeleporting || LevelScreen.tp_visible == false)
            return;

        teleport_Sound.play();

        isTeleporting = true;
        Warp warp1 = new Warp(0,0, this.getStage());
//        warp1.centerAtActor(this);
        warp1.setPosition(getX() + getWidth()/2 - 1f, getY() + getHeight()/2 - 0.75f);
        gun2.addAction( Actions.after( Actions.fadeOut(1.5f) ) );

        final Warp warp2 = new Warp(0,0, this.getStage());
        //warp2.setPosition(MathUtils.random(4,getStage().getCamera().viewportWidth-4), MathUtils.random(3,getStage().getCamera().viewportHeight-3));

        //iteratorP = Level.portalList.iterator();
        iteratorP = Level.portalList.iterator();

        int i = MathUtils.random(1,Level.portalList.size());
        int j = 1;

        while(iteratorP.hasNext()) {
            Portal portal = iteratorP.next();
            if (j == i)
                warp2.setPosition(portal.getX()-0.25f, portal.getY()-0.25f);
            j++;
        }

        LevelScreen.labelInfo.setText("Beam me up, Scotty");
        LevelScreen.labelInfo.addAction( Actions.after( Actions.fadeIn(0.5f) ) );
        float delay = 1.5f; // seconds
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Do your work
                //setPosition(MathUtils.random(35), MathUtils.random(19));
                //setPosition(MathUtils.random(4,getStage().getCamera().viewportWidth-4), MathUtils.random(3,getStage().getCamera().viewportHeight-3));
                //warp2.setPosition(getX() + getWidth()/2 - 1f, getY() + getHeight()/2 - 0.75f);
                //setPosition(warp2.getX()+0.5f, warp2.getY()+0.5f);
                body.setTransform(warp2.getX()+0.5f, warp2.getY()+0.5f,0);
                gun2.addAction( Actions.after( Actions.fadeIn(1.5f) ) );
                LevelScreen.labelInfo.addAction( Actions.after( Actions.fadeOut(1.5f) ) );
            }
        }, delay);

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Do your work
                isTeleporting = false;
            }
        }, delay*2);

    }

    public void shoot() {
    if ( getStage() == null )
        return;
    Bullet bullet = new Bullet(0,0, this.getStage());

    float randOffset;
    randOffset = MathUtils.random(-0.5f,-1f)+0.75f;
    bullet.setPosition(getX() + getWidth()/2 - 0.25f - randOffset, getY() + getHeight()/2 - 0.1f - randOffset);

    bullet.setRotation( this.getRotation() );
    bullet.setMotionAngle( this.getRotation() );
    bulletList.add(bullet);
    }

    public ArrayList<Bullet> getBulletList() {
        return bulletList;
    }

}
