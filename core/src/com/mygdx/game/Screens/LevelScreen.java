package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.*;
import com.mygdx.game.Objects.*;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LevelScreen extends BaseScreen {

    Iterator<Zombie> iteratorZ;
    Iterator<ZombieBoss> iteratorZB;
    Iterator<Barrel> iteratorBarrel;
    Iterator<BlastRadius> iteratorBlast;
    Iterator<Bullet> iteratorB;
    long reloadTime = TimeUtils.nanoTime();
    long followTime = TimeUtils.nanoTime();
    public Sound loadSound;
    public Sound grenadeDropSound;
    public static Sound reloadRifleSound;
    public static Sound reloadPistolSound;
    public Sound heartbeatSound;

    private boolean gameOver;
    //public CameraHelper cameraHelper;
    public Level level;
    float mouseX, mouseY, xx, yy;
    public boolean win;
    long hbTime = TimeUtils.nanoTime();
    private BitmapFont font;
    private Label labelLeve1;
    private Label labelScore;
    private Label labelHealth;
    private Label labelFPS;
    private Label label1;
    private Label label2;
    private Label label3;
    private Label label4;
    private Label label5;
    private Label labelBullet1;
    private Label labelBullet2;
    private Label labelGrenadeCount;
    private Label labelMonkeyCount;
    private Label labelHealthkitCount;
    public static Label labelInfo;
    Label.LabelStyle labelStyle;
    Label.LabelStyle fpsStyle;
    public static boolean tp_visible = false;

    Image ws_border;
    int wp_size;
    int wp_position;
    public PixAssets pixAssets;
    public Image healthbarBG;
    public Image healthbarFG;
    long hurtTimer = TimeUtils.nanoTime();
    public Sound explosion;
    public Sound Z1_Sound;
    public Sound wandSound;
    public Sound monkeySound;
    public Sound monkeyBombSound;
    //public Sound wandSound;
    public float monkeyX;
    public float monkeyY;

    public int zMax;
    public int zSpawned;
    public int zCurrent;
    public int currLevel;

    Rectangle recPlayer;
    Rectangle recMysteryBox;
    Rectangle recTree;
    Rectangle recRock;
    Rectangle recZombie;
    Rectangle recZombieBoss;
    ArrayList<Rectangle> recZombieBossList;
    ArrayList<Rectangle> recZombieList;
    boolean mysteryboxEnabled;
    float timeSinceCollision = 0;
    float zRotation;
    public static Countdown countdown;
    Image tp_icon;
    Image grenade_icon;
    Image monkey_icon;
    BaseActor grenade;
    Image healthkit_icon;
    long healTimer = TimeUtils.nanoTime();
    BaseActor pixel;
    BaseActor youWinMessage;

    public void initialize() {
        gameOver = false;

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("game-bg2.png");
        background.setSize(38, 21);
        BaseActor.setWorldBounds(background);
        loadSound = Gdx.audio.newSound(Gdx.files.internal("sound/switch_weapon.ogg"));
        grenadeDropSound = Gdx.audio.newSound(Gdx.files.internal("sound/grenade_drop.ogg"));
        reloadRifleSound = Gdx.audio.newSound(Gdx.files.internal("sound/reload_rifle.ogg"));
        reloadPistolSound = Gdx.audio.newSound(Gdx.files.internal("sound/reload_pistol.ogg"));
        heartbeatSound = Gdx.audio.newSound(Gdx.files.internal("sound/heartbeat.ogg"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sound/explosion.ogg"));
        Z1_Sound = Gdx.audio.newSound(Gdx.files.internal("sound/zombie-4.ogg"));
        //wandSound = Gdx.audio.newSound(Gdx.files.internal("sound/fizzle.ogg"));
        monkeySound = Gdx.audio.newSound(Gdx.files.internal("sound/wanna_play.ogg"));
        monkeyBombSound = Gdx.audio.newSound(Gdx.files.internal("sound/monkey_bomb.ogg"));

        youWinMessage = new BaseActor(0, 0, uiStage);
        youWinMessage.loadTexture("level_cleared.png");
        youWinMessage.centerAtPosition(uiStage.getWidth()/2, uiStage.getHeight()/2+200);
        youWinMessage.setOpacity(0);

        BlastRadius obj = null;
        obj = new BlastRadius(5-3,5-3,mainStage);
        obj.setScale(0.2f);
        obj = new BlastRadius(30-3,6-3,mainStage);
        obj.setScale(0.2f);
        obj = new BlastRadius(18-3,13-3,mainStage);
        obj.setScale(0.2f);

        level = new Level(Constants.LEVEL_04, mainStage);

        currLevel = Constants.START_LEVEL;
        zSpawned = 0;
        zMax = Constants.ZOMBXLEVEL[currLevel-1][0];
        zCurrent = Constants.ZOMBXLEVEL[currLevel-1][1];

        // for debugging
        pixel = new BaseActor(0, 0, mainStage);
        pixel.loadTexture("pixel_black.png");
        pixel.setSize(38, 21);
        pixel.setVisible(false);

        win = false;
        initGUI();
        initWeaponSlot();
        initPhysics ();

        //System.out.println(zMax + ":" + zCurrent);


        //System.out.println(zMax + ":" + zCurrent);

        level.zSpawn(zCurrent,mainStage);
        zSpawned = zCurrent;

        initMiniMap();

        countdown = new Countdown(Gdx.graphics.getWidth()/2-60,Gdx.graphics.getHeight()/2-70,uiStage);

    }

    public void initMiniMap() {
        Pixmap pixmap;
        Image miniMapImage;
        Texture miniMapTexture;
        recZombieList = new ArrayList<Rectangle>();
        recZombieBossList = new ArrayList<Rectangle>();

        pixmap = new Pixmap((int)Constants.WORLD_WIDTH*5, (int)Constants.WORLD_HEIGHT*5, Pixmap.Format.RGBA8888);
        pixmap.setColor(0,0,0,0.5f);
        pixmap.fill();

        miniMapTexture = new Texture(pixmap);
        miniMapImage = new Image(miniMapTexture);
        miniMapImage.setPosition(uiStage.getWidth()-200,uiStage.getHeight()-115);

        recPlayer = new Rectangle(level.player.getX(),level.player.getY(),3,3,Color.WHITE);
        recMysteryBox = new Rectangle(uiStage.getWidth()-200 + level.mb.getX()*5,uiStage.getHeight()-115 + level.mb.getY()*5,3,3,Color.GOLD);

        uiStage.addActor(miniMapImage);
        uiStage.addActor(recMysteryBox);

        for (BaseActor treeActor : level.treeList) {
            Tree tree = (Tree) treeActor;
            recTree = new Rectangle(uiStage.getWidth()-200 + tree.getX()*5,uiStage.getHeight()-115 + tree.getY()*5,3,3,Color.GREEN);
            uiStage.addActor(recTree);
        }

        for (BaseActor rockActor : level.rockList) {
            Rock rock = (Rock) rockActor;
            recRock = new Rectangle(uiStage.getWidth()-200 + rock.getX()*5,uiStage.getHeight()-115 + rock.getY()*5,3,3,Color.GRAY);
            uiStage.addActor(recRock);
        }

        for (BaseActor zombieActor : level.zombieList) {
            Zombie zombie = (Zombie) zombieActor;
            recZombie = new Rectangle(uiStage.getWidth()-200 + zombie.getX()*5,uiStage.getHeight()-115 + zombie.getY()*5,3,3,Color.PINK);
            uiStage.addActor(recZombie);
            recZombieList.add(recZombie);
        }

        for (BaseActor zombieActor : level.zombieBossList) {
            ZombieBoss zombie = (ZombieBoss) zombieActor;
            recZombieBoss = new Rectangle(uiStage.getWidth()-200 + zombie.getX()*5,uiStage.getHeight()-115 + zombie.getY()*5,4,4,Color.RED);
            uiStage.addActor(recZombieBoss);
            recZombieBossList.add(recZombieBoss);
        }

        uiStage.addActor(recPlayer);

    }

    public void initGUI() {
        font = new BitmapFont(Gdx.files.internal("Comic.fnt"),
                Gdx.files.internal("Comic.png"), false);
        font.setColor(Color.WHITE);
        font.getData().setScale(0.35f);

        fpsStyle = new Label.LabelStyle(font,Color.WHITE);

        labelStyle = new Label.LabelStyle(font,Color.WHITE);
        labelInfo = new Label("",labelStyle);
        labelInfo.setPosition(Gdx.graphics.getWidth()/2-30,Gdx.graphics.getHeight()/2-50);
        labelInfo.setFontScale(0.35f);

        labelLeve1 = new Label("Level " + currLevel,labelStyle);
        labelScore = new Label("",labelStyle);
        labelHealth = new Label("",labelStyle);
        labelFPS = new Label("", labelStyle);

        labelLeve1.setPosition(25,Gdx.graphics.getHeight()-40);
        labelScore.setPosition(25,Gdx.graphics.getHeight()-55);
        labelHealth.setPosition(45,40);
        labelFPS.setPosition(Gdx.graphics.getWidth()-75,Gdx.graphics.getHeight()-130);
        labelFPS.setFontScale(0.35f);

        uiStage.addActor(labelFPS);
        uiStage.addActor(labelLeve1);
        uiStage.addActor(labelScore);
        uiStage.addActor(labelInfo);

        pixAssets = new PixAssets();
        healthbarBG = new Image(pixAssets.healthbarBG);
        healthbarBG.setScale(1.5f,2.75f);
        healthbarBG.setX(30);
        healthbarBG.setY(25);

        healthbarFG = new Image(pixAssets.healthbarFG);
        healthbarFG.setScale(1.5f,2.75f);
        healthbarFG.setX(30);
        healthbarFG.setY(25);

        uiStage.addActor(healthbarBG);
        uiStage.addActor(healthbarFG);
        uiStage.addActor(labelHealth);

    }

    public void initWeaponSlot(){
        Image ws_blue = new Image(new Texture (Gdx.files.internal("rs_full_blue.png")));
        Image ws_yellow = new Image(new Texture (Gdx.files.internal("rs_full_yellow.png")));
        Image ws_purple = new Image(new Texture (Gdx.files.internal("rs_full_purple.png")));
        Image ws_orange = new Image(new Texture (Gdx.files.internal("rs_full_orange.png")));
        Image ws_gray = new Image(new Texture (Gdx.files.internal("rs_full_gray.png")));

        ws_border = new Image(new Texture (Gdx.files.internal("rs_white_border.png")));

        wp_size = 60;
        wp_position = 1000;

        ws_border.setSize(wp_size,wp_size);
        ws_blue.setSize(wp_size,wp_size);
        ws_yellow.setSize(wp_size,wp_size);
        ws_purple.setSize(wp_size,wp_size);
        ws_orange.setSize(wp_size,wp_size);
        ws_gray.setSize(wp_size,wp_size);

        ws_border.setPosition(wp_position,45);
        ws_blue.setPosition(wp_position,45);
        ws_yellow.setPosition(wp_position + wp_size*1+2,45);

        ws_orange.setPosition(wp_position + wp_size*3+6,45);
        ws_gray.setPosition(wp_position + wp_size*4+8,45);
        ws_purple.setPosition(wp_position + wp_size*5+10,45);

        uiStage.addActor(ws_blue);
        uiStage.addActor(ws_yellow);
        uiStage.addActor(ws_purple);
        uiStage.addActor(ws_orange);
        uiStage.addActor(ws_gray);
        uiStage.addActor(ws_border);

        //Weapon slot selection
        label1 = new Label("1", labelStyle);
        label2 = new Label("2", labelStyle);
        label3= new Label("3", labelStyle);
        label4 = new Label("4", labelStyle);
        label5 = new Label("5", labelStyle);
        label1.setFontScale(0.25f);
        label2.setFontScale(0.25f);
        label3.setFontScale(0.25f);
        label4.setFontScale(0.25f);
        label5.setFontScale(0.25f);

        label1.setPosition(wp_position+20,105);
        label2.setPosition(wp_position+wp_size*1+22,105);
        label3.setPosition(wp_position+wp_size*3+26,105);
        label4.setPosition(wp_position+wp_size*4+28,105);
        label5.setPosition(wp_position+wp_size*5+30,105);

        uiStage.addActor(label1);
        uiStage.addActor(label2);
        uiStage.addActor(label3);
        uiStage.addActor(label4);
        uiStage.addActor(label5);

        //Bullet count
        labelBullet1 = new Label("", labelStyle);
        labelBullet1.setFontScale(0.2f);
        labelBullet1.setPosition(wp_position+10,25);
        labelBullet2 = new Label("", labelStyle);
        labelBullet2.setFontScale(0.2f);
        labelBullet2.setPosition(wp_position+wp_size+10,25);

        labelGrenadeCount = new Label("", labelStyle);
        labelGrenadeCount.setFontScale(0.2f);
        labelGrenadeCount.setPosition(wp_position+wp_size*3+30,25);
        labelHealthkitCount = new Label("", labelStyle);
        labelHealthkitCount.setFontScale(0.2f);
        labelHealthkitCount.setPosition(wp_position+wp_size*4+30,25);
        labelMonkeyCount = new Label("", labelStyle);
        labelMonkeyCount.setFontScale(0.2f);
        labelMonkeyCount.setPosition(wp_position+wp_size*5+30,25);

        if(level.player.grenadeCount==0)
            labelGrenadeCount.setVisible(false);
        if(level.player.healtkitCount==0)
            labelHealthkitCount.setVisible(false);

        uiStage.addActor(labelBullet1);
        uiStage.addActor(labelBullet2);
        uiStage.addActor(labelGrenadeCount);
        uiStage.addActor(labelHealthkitCount);
        uiStage.addActor(labelMonkeyCount);

        initWeapons();
    }

    public void initWeapons(){
        Image gun1 = new Image(new Texture (Gdx.files.internal("assets-raw/glock.png")));
        Image gun2 = new Image(new Texture (Gdx.files.internal("assets-raw/m249.png")));
        tp_icon = new Image(new Texture (Gdx.files.internal("tp_icon.png")));
        healthkit_icon = new Image(new Texture (Gdx.files.internal("healthkit.png")));
        grenade_icon = new Image(new Texture (Gdx.files.internal("grenade.png")));
        monkey_icon = new Image(new Texture (Gdx.files.internal("monkey_bomb.png")));
        grenade = new BaseActor(-100,-100,mainStage);
        grenade.loadTexture("grenade.png");

        gun1.setSize(60,60);
        gun2.setSize(60,60);
        tp_icon.setSize(60,60);
        healthkit_icon.setSize(healthkit_icon.getWidth()*0.3f,healthkit_icon.getHeight()*0.3f);
        grenade_icon.setSize(grenade_icon.getWidth()*0.15f,grenade_icon.getHeight()*0.15f);
        monkey_icon.setScale(0.2f);
        grenade.setSize(grenade_icon.getWidth()*0.009f,grenade_icon.getHeight()*0.009f);

        gun1.setPosition(wp_position,45);
        gun2.setPosition(wp_position+wp_size,45);
        tp_icon.addAction(Actions.fadeOut(0));

        grenade_icon.setPosition(wp_position+wp_size*3+20,57);
        healthkit_icon.setPosition(wp_position+wp_size*4+14,60);
        tp_icon.setPosition(wp_position+wp_size*5+10,45);
        monkey_icon.setPosition(wp_position+wp_size*4+30,25);

        uiStage.addActor(gun1);
        uiStage.addActor(gun2);
        uiStage.addActor(tp_icon);
        uiStage.addActor(healthkit_icon);
        uiStage.addActor(grenade_icon);
        uiStage.addActor(monkey_icon);
        mainStage.addActor(grenade);

        if(level.player.healtkitCount==0)
            healthkit_icon.setVisible(false);
        if(level.player.grenadeCount==0)
            grenade_icon.setVisible(false);

    }

    public void update(float dt) {
        if(!Constants.gamePaused)
            updateObjects(dt);

        if (level.zombieList.size() < zCurrent && zSpawned < zMax) {
            level.zSpawn(1, mainStage);

            zSpawned++;
            //int i = zMax - zSpawned;
            //System.out.println("Level: " + currLevel + ", left: " + i);
        }

    }

     public void updateObjects(float dt) {

         if(level.player.monkeyCount == 0) {
             labelMonkeyCount.setVisible(false);
             monkey_icon.setVisible(false);
             tp_icon.setPosition(wp_position+wp_size*5+10,45);
         }
         else {
             labelMonkeyCount.setVisible(true);
             monkey_icon.setVisible(true);
             tp_icon.setPosition(-100,-100);
         }

        int fps = Gdx.graphics.getFramesPerSecond();
        recPlayer.setPosition(uiStage.getWidth()-200 + level.player.getX()*5,uiStage.getHeight()-115 + level.player.getY()*5);

        if (level.player.health <= 0) {
            level.player.health = 0;
            Constants.gamePaused = true;
            gameOver = true;
        }

         if (level.player.health < 100 && level.player.health > 0) {
             if (!level.player.isHurt) {
                 if (TimeUtils.timeSinceNanos(healTimer) > 1000000000 * 2) {
                     healTimer = TimeUtils.nanoTime();
                     if (level.player.health >= 100)
                         level.player.health = 100;
                     else {
                         level.player.health += 1;
                         //healthbarFG.setSize(healthbarFG.getWidth() + 1, healthbarFG.getHeight());
                     }
                 }
             }
         }

         healthbarFG.setSize(level.player.health, healthbarFG.getHeight());


        int i = 0;
        int j = 0;
        // Zombie boss
        for (BaseActor zombie : level.zombieBossList) {
            i++;
            for (Rectangle rec : recZombieBossList) {
                j++;
                if(j> level.zombieBossList.size())
                    j = 1;
                if (i == j) {
                    rec.setPosition(uiStage.getWidth() - 200 + zombie.getX() * 5, uiStage.getHeight() - 115 + zombie.getY() * 5);
                }
            }
        }

        // Zombie
        i = 0;
        j = 0;
        for (BaseActor zombie : level.zombieList) {
            i++;
            for (Rectangle rec : recZombieList) {
                j++;
                if(j> level.zombieList.size())
                    j = 1;
                if (i == j) {
                    rec.setPosition(uiStage.getWidth() - 200 + zombie.getX() * 5, uiStage.getHeight() - 115 + zombie.getY() * 5);
                }
            }
        }

        level.player.isHurt = false;
        //cameraHelper.update(dt);
        iteratorZ = level.zombieList.iterator();
        iteratorZB = level.zombieBossList.iterator();
        iteratorBarrel = level.barrelList.iterator();
        iteratorB = level.player.getBulletList().iterator();
        iteratorBlast = level.blastList.iterator();

        labelScore.setText("Score: " + level.player.score);
        labelHealth.setText("" + level.player.health + "/100");
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsStyle.fontColor = Color.GREEN;
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
             fpsStyle.fontColor = Color.YELLOW;
        } else {
            // less than 30 FPS show up in red
              fpsStyle.fontColor = Color.RED;
        }

        if (level.player.gunOption == 1)
            ws_border.setPosition(wp_position, 45);
        else if (level.player.gunOption == 2)
            ws_border.setPosition(wp_position + wp_size*1+2, 45);
        else if (level.player.gunOption == 3)
            ws_border.setPosition(wp_position+wp_size*2+4, 45);
        else
            ws_border.setPosition(wp_position+wp_size*3+6, 45);

        labelFPS.setText("fps: " + fps);
        labelFPS.setStyle(fpsStyle);
        labelBullet1.setText("[" + level.player.gun1Magazines + "] " + level.player.gun1Bullets);
        labelBullet2.setText("[" + level.player.gun2Magazines + "] " + level.player.gun2Bullets);
        labelGrenadeCount.setText(level.player.grenadeCount);
        labelHealthkitCount.setText(level.player.healtkitCount);
         labelMonkeyCount.setText(level.player.monkeyCount);

        mouseX = mouse_position.x;
        mouseY = mouse_position.y;

        xx = mouseX - level.player.getX();
        yy = mouseY - level.player.getY();

        float rotation = (float) Math.toDegrees(Math.atan(yy / xx));

        if (rotation < 0)
            rotation += 360;
        if (xx < 0)
            rotation += 180;

        level.player.setRotation(rotation);

        // Zombie Boss
        for (BaseActor zombieActor : level.zombieBossList) {
            final ZombieBoss zombie = (ZombieBoss) zombieActor;

            if(level.player.isMonkeyActive){
                xx = monkeyX - (zombie.getX());
                yy = monkeyY - (zombie.getY());
            }
            else {
                xx = level.player.getX() - (zombie.getX());
                yy = level.player.getY() - (zombie.getY());
            }

            rotation = (int) Math.toDegrees(Math.atan(yy / xx));
            if (rotation < 0)
                rotation += 360;
            if (xx < 0)
                rotation += 180;

             zRotation = rotation;

            //Zombie speed
            if(currLevel>8)
                zombie.setMaxSpeed(3f);
            else if(currLevel>6)
                zombie.setMaxSpeed(2.5f);
            else if(currLevel>4)
                zombie.setMaxSpeed(2f);
            else if(currLevel>2)
                zombie.setMaxSpeed(1.5f);

            if(!level.player.isTeleporting && !gameOver) {
                zombie.accelerateAtAngle(zRotation);

                if (zombieActor.overlaps(level.player)) {
                    level.player.isHurt = true;
                    int damage = 20;

                    //Damage
                    if(currLevel>9)
                        damage = 50;
                    else if(currLevel>8)
                        damage = 45;
                    else if(currLevel>7)
                        damage = 40;
                    else if(currLevel>6)
                        damage = 35;
                    else if(currLevel>3)
                        damage = 30;

                    if (TimeUtils.timeSinceNanos(hbTime) > 1000000000) {
                        hbTime = TimeUtils.nanoTime();
                        heartbeatSound.play();
                    }

                    if (TimeUtils.timeSinceNanos(hurtTimer) > 1000000000) {
                        hurtTimer = TimeUtils.nanoTime();
                        level.player.health -= damage;
                    }
                }
            }

            for (BaseActor bulletActor : level.player.getBulletList()) {
                Bullet bullet = (Bullet) bulletActor;
                if (bulletActor.overlaps(zombie) && !zombie.dead) {
                    if(level.player.gunOption == 1)
                        zombie.health -= 2;
                    else
                        zombie.health -= 3;
                    level.player.score += 3;
                     if(zombie.health < 0) {
                        zombie.dead = true;
                        Z1_Sound.play(0.75f);
                        level.player.score += zombie.worth;
                        zombie.clearActions();
                        zombie.addAction(Actions.fadeOut(1));
                        zombie.addAction(Actions.after(Actions.removeActor()));
                    }
                    bullet.remove = true;
                }
            }

            for (BlastRadius blastActor : level.blastList) {
                if (blastActor.overlaps(zombie) && !zombie.dead && !zombie.blasted) {
                    zombie.health -= 100;
                    zombie.blasted = true;
                    if (zombie.health <= 0) {
                        zombie.dead = true;
                        Z1_Sound.play(0.75f);
                        level.player.score += zombie.worth;
                        zombie.clearActions();
                        zombie.addAction(Actions.fadeOut(1));
                        zombie.addAction(Actions.after(Actions.removeActor()));
                    }

                    float delay = 1f; // seconds
                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            // Do your work
                            zombie.blasted = false;
                        }
                    }, delay);
                }
            }
        }

        //Zombies
        for (BaseActor zombieActor : level.zombieList) {
            final Zombie zombie = (Zombie) zombieActor;

            if(level.player.isMonkeyActive){
                xx = monkeyX - (zombie.getX());
                yy = monkeyY - (zombie.getY());
            }
            else {
                xx = level.player.getX() - (zombie.getX());
                yy = level.player.getY() - (zombie.getY());
            }

            rotation = (int) Math.toDegrees(Math.atan(yy / xx));

            if (rotation < 0)
                rotation += 360;
            if (xx < 0)
                rotation += 180;

            //if (TimeUtils.timeSinceNanos(followTime) > 100) {
            //    followTime = TimeUtils.nanoTime();
                zRotation = rotation;
            //}

            //Zombie speed
            if(currLevel>9)
                zombie.setMaxSpeed(4f);
            else if(currLevel>8)
                zombie.setMaxSpeed(3f);
            else if(currLevel>6)
                zombie.setMaxSpeed(2.5f);
            else if(currLevel>4)
                zombie.setMaxSpeed(2f);
            else if(currLevel>2)
                zombie.setMaxSpeed(1.5f);

            if(!level.player.isTeleporting && !gameOver) {
                zombie.accelerateAtAngle(zRotation);

                if (zombieActor.overlaps(level.player)) {
                    level.player.isHurt = true;
                    int damage = 5;

                    //Damage
                    if(currLevel>9)
                        damage = 30;
                    else if(currLevel>8)
                        damage = 25;
                    else if(currLevel>7)
                        damage = 20;
                    else if(currLevel>6)
                        damage = 15;
                    else if(currLevel>3)
                        damage = 10;

                    if (TimeUtils.timeSinceNanos(hbTime) > 1000000000) {
                        hbTime = TimeUtils.nanoTime();
                        heartbeatSound.play();
                    }

                    if (TimeUtils.timeSinceNanos(hurtTimer) > 1000000000) {
                        hurtTimer = TimeUtils.nanoTime();
                        level.player.health -= damage;
                    }
                }
            }

            for (BaseActor bulletActor : level.player.getBulletList()) {
                Bullet bullet = (Bullet) bulletActor;
                if (bulletActor.overlaps(zombie) && !zombie.dead) {
                    if(level.player.gunOption == 1)
                        zombie.health -= 1;
                    else
                        zombie.health -= 3;
                    level.player.score += 3;
                    if(zombie.health < 0) {
                        zombie.dead = true;
                        level.player.score += zombie.worth;
                        zombie.clearActions();
                        zombie.addAction(Actions.fadeOut(1));
                        zombie.addAction(Actions.after(Actions.removeActor()));
                    }
                    bullet.remove = true;
                }
            }

            for (BlastRadius blastActor : level.blastList) {
                if (blastActor.overlaps(zombie) && !zombie.dead && !zombie.blasted) {
                    zombie.health -= 20;
                    zombie.blasted = true;
                    if (zombie.health <= 0) {
                        zombie.dead = true;
                        level.player.score += zombie.worth;
                        zombie.clearActions();
                        zombie.addAction(Actions.fadeOut(1));
                        zombie.addAction(Actions.after(Actions.removeActor()));
                    }

                    float delay = 1f; // seconds
                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            // Do your work
                            zombie.blasted = false;
                        }
                    }, delay);

                }
            }
        }

        for (BaseActor barrelActor : level.barrelList) {
            Barrel barrel = (Barrel) barrelActor;
            for ( BaseActor bulletActor : level.player.getBulletList()) {
                Bullet bullet = (Bullet) bulletActor;
                if (bulletActor.overlaps(barrel)) {
                    barrel.damage -= 1;
                    if (barrel.damage <= 0) {
                        Explosion boom = new Explosion(0, 0, mainStage);
                        boom.centerAtActor(barrel);
                        explosion.play(0.45f);
                        labelInfo.setText("Kaboom!");
                        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                        LevelScreen.labelInfo.addAction(Actions.delay(2));
                        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));
                        barrel.remove = true;
                        barrelActor.remove();

                        final BlastRadius blast = new BlastRadius(barrel.getX()-3,barrel.getY()-3,mainStage);
                        //BlastRadius blast2 = new BlastRadius(barrel.getX()-3,barrel.getY()-3,mainStage);
                        //blast2.setScale(0.25f);
                        blast.setColor(Color.RED);
                        level.blastList.add(blast);

                        Vector2 origin = new Vector2();
                        BodyDef bodyDef;
                        Body body;
                        CircleShape circleShape;
                        FixtureDef fixtureDef;

                        bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(blast.position);
                        body = b2dworld.createBody(bodyDef);
                        blast.body = body;
                        origin.x = blast.bounds.width / 2f;
                        origin.y = blast.bounds.height / 2f;
                        circleShape = new CircleShape();
                        circleShape.setPosition(origin);
                        circleShape.setRadius(3f);
                        fixtureDef = new FixtureDef();
                        fixtureDef.shape = circleShape;
                        body.createFixture(fixtureDef);
                        circleShape.dispose();

                        level.player.shakeInProgress = true;
                        ((OrthographicCamera)level.player.getStage().getCamera()).zoom = Constants.CAM_ZOOM*1.5f;

                         float delay = 0.1f; // seconds
                         Timer.schedule(new Timer.Task(){
                             @Override
                             public void run() {
                                 // Do your work
                                 if (!b2dworld.isLocked())
                                     b2dworld.destroyBody(blast.body);
                                 blast.body = null;
                                 blast.setPosition(-100,-100);
                                 blast.addAction(Actions.removeActor());
                               }
                         }, delay);
                    }
                    bullet.remove = true;
                }
            }
        }

         if(timeSinceCollision > 3) {
            if (level.player.overlaps(level.mb)) {
                timeSinceCollision = 0;
                mysteryboxEnabled = true;

                // do collision stuff
                if(level.player.score < 200)
                    labelInfo.setText("($200)");
                else
                    labelInfo.setText("Press E for a mystery item ($200)");
                labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                labelInfo.addAction(Actions.delay(2));
                labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));
                }
            else
                mysteryboxEnabled = false;
         }
         timeSinceCollision += dt;

        //Remove objects
        while(iteratorZ.hasNext()) {
            Zombie zombie = iteratorZ.next();
            if(zombie.dead) {
                iteratorZ.remove(); // Removes the current object.
                if (!b2dworld.isLocked())
                    b2dworld.destroyBody(zombie.body);
                zombie.body = null;
            }
        }
        while(iteratorZB.hasNext()) {
            ZombieBoss zombie = iteratorZB.next();
            if(zombie.dead) {
                iteratorZB.remove(); // Removes the current object.
                if (!b2dworld.isLocked())
                    b2dworld.destroyBody(zombie.body);
                zombie.body = null;
            }
        }
        while(iteratorB.hasNext()) {
            Bullet bullet = iteratorB.next();
            if(bullet.remove) {
                bullet.addAction(Actions.removeActor());
                iteratorB.remove(); // Removes the current object.
            }
        }
        while(iteratorBarrel.hasNext()) {
            Barrel barrel = iteratorBarrel.next();
            if(barrel.remove) {
                iteratorBarrel.remove(); // Removes the current object.
                if (!b2dworld.isLocked())
                    b2dworld.destroyBody(barrel.body);
                barrel.body = null;
            }
        }

        if (!win && level.zombieList.isEmpty() && level.zombieBossList.isEmpty() && zSpawned >= zMax) {
            win = true;
            youWinMessage.addAction(Actions.after(Actions.fadeIn(1)));
            youWinMessage.addAction(Actions.delay(7));
            youWinMessage.addAction(Actions.after(Actions.fadeOut(2)));

            float delay = 9f; // seconds
            countDown2(9);
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    // Do your work
                    if(currLevel==10) {
                        gameOver = true;
                        labelInfo.setText("Thank you for playing!");
                        labelInfo.addAction(Actions.fadeIn(1));
                    }
                    else
                    nextLevel();
                }
            }, delay);
        }


        if (gameOver) {
            BaseActor messageLose = new BaseActor(0, 0, uiStage);
            messageLose.loadTexture("game-over.png");
            messageLose.centerAtPosition(uiStage.getWidth()/2, uiStage.getHeight()/2+200);
            messageLose.setOpacity(0);
            messageLose.addAction(Actions.fadeIn(1));
            //labelInfo.setText("Thank you for playing. Comments: hs2600@gmail.com");
            Constants.gamePaused = true;


//            float delay = 5f; // seconds
//            Timer.schedule(new Timer.Task(){
//                @Override
//                public void run() {
//                    // Do your work
//                    Zombs.setActiveScreen( new MenuScreen() );
//                }
//            }, delay);
        }
    }


    private void nextLevel(){
        win = false;

        currLevel++;
        //System.out.print(currLevel);
        zMax = Constants.ZOMBXLEVEL[currLevel-1][0];
        zCurrent = Constants.ZOMBXLEVEL[currLevel-1][1];
        labelLeve1.setText("Level " + currLevel);

        level.zSpawn(zCurrent, mainStage);
        zSpawned = zCurrent;

        if(grenade_icon.isVisible()) {
            if (level.player.grenadeCount == 0)
                level.player.grenadeCount = 3;
            else if (level.player.grenadeCount < 3)
                level.player.grenadeCount++;
        }

        if(currLevel>1)
            level.zBossSpawn(1, mainStage);
        else if(currLevel>3)
            level.zBossSpawn(2, mainStage);
        else if(currLevel>5)
            level.zBossSpawn(3, mainStage);
        else
            level.zBossSpawn(4, mainStage);

        if(level.barrelList.isEmpty()) {
            BaseActor obj = null;
            obj = new Barrel(5, 5, mainStage);
            level.barrelList.add((Barrel) obj);
            obj = new Barrel(30, 6, mainStage);
            level.barrelList.add((Barrel) obj);
            obj = new Barrel(18, 13, mainStage);
            level.barrelList.add((Barrel) obj);
        }

        Vector2 origin = new Vector2();
        BodyDef bodyDef;
        Body body;
        CircleShape circleShape;
        FixtureDef fixtureDef;

        for (Barrel barrel : level.barrelList) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(barrel.position);
            body = b2dworld.createBody(bodyDef);
            barrel.body = body;
            origin.x = barrel.bounds.width/2;
            origin.y = barrel.bounds.height/2;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.45f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

    }

    private void initPhysics() {

        //b2dworld = new World(new Vector2(0,0),true);

        Vector2 origin = new Vector2();
        BodyDef bodyDef;
        Body body;
        CircleShape circleShape;
        FixtureDef fixtureDef;

        // Player
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(level.player.position);
        body = b2dworld.createBody(bodyDef);
        level.player.body = body;
        origin.x = level.player.bounds.width / 2f;
        origin.y = level.player.bounds.height / 2f + 0.1f;
        circleShape = new CircleShape();
        circleShape.setPosition(origin);
        circleShape.setRadius(0.25f);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef);
        circleShape.dispose();

        // Mystery Box
        PolygonShape polygonShape = new PolygonShape();
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(level.mb.position);
        body = b2dworld.createBody(bodyDef);
        level.mb.body = body;
        origin.x = level.mb.bounds.width/2;
        origin.y = level.mb.bounds.height/2;
        polygonShape.setAsBox(0.25f,0.25f, origin, 0);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        body.createFixture(fixtureDef);
        polygonShape.dispose();

        // Grenade
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(grenade.position);
        body = b2dworld.createBody(bodyDef);
        grenade.body = body;
        origin.x = grenade.bounds.width / 2f + 0.15f;
        origin.y = grenade.bounds.height / 2f + 0.15f;
        circleShape = new CircleShape();
        circleShape.setPosition(origin);
        circleShape.setRadius(0.10f);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef);
        circleShape.dispose();

//
        // Rocks
        for (Rock rock : level.rockList) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            body = b2dworld.createBody(bodyDef);
            rock.body = body;
            origin.x = rock.bounds.width / 2f + 0.25f;
            origin.y = rock.bounds.height / 2f + 0.25f;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.25f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Trees
        for (Tree tree : level.treeList) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(tree.position);
            body = b2dworld.createBody(bodyDef);
            tree.body = body;
            origin.x = tree.bounds.width / 2f + 0.25f;
            origin.y = tree.bounds.height / 2f + 0.25f;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.20f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

        // Zombies
//        for (Zombie zombie : level.zombieList) {
//            bodyDef = new BodyDef();
//            bodyDef.type = BodyDef.BodyType.DynamicBody;
//            bodyDef.position.set(zombie.position);
//            body = b2dworld.createBody(bodyDef);
//            zombie.body = body;
//            origin.x = zombie.bounds.width / 2f + 0.02f;
//            origin.y = zombie.bounds.height / 2f + 0.02f;
//            circleShape = new CircleShape();
//            circleShape.setPosition(origin);
//            circleShape.setRadius(0.25f);
//            fixtureDef = new FixtureDef();
//            fixtureDef.shape = circleShape;
//            body.createFixture(fixtureDef);
//            circleShape.dispose();
//        }

//        // Boss Zombies
//        for (ZombieBoss zombie : level.zombieBossList) {
//            bodyDef = new BodyDef();
//            bodyDef.type = BodyDef.BodyType.DynamicBody;
//            bodyDef.position.set(zombie.position);
//            body = b2dworld.createBody(bodyDef);
//            zombie.body = body;
//            origin.x = zombie.bounds.width / 2f;
//            origin.y = zombie.bounds.height / 2f;
//            circleShape = new CircleShape();
//            circleShape.setPosition(origin);
//            circleShape.setRadius(0.25f);
//            fixtureDef = new FixtureDef();
//            fixtureDef.shape = circleShape;
//            body.createFixture(fixtureDef);
//            circleShape.dispose();
//        }

        // Barrels
        for (Barrel barrel : level.barrelList) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(barrel.position);
            body = b2dworld.createBody(bodyDef);
            barrel.body = body;
            origin.x = barrel.bounds.width/2;
            origin.y = barrel.bounds.height/2;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.45f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();
        }

    }

    // override default InputProcessor method
    public boolean keyDown(int keycode) {
        if ( keycode == Keys.X ) {
            level.player.warp();
        }

        if ( keycode == Keys.R ) {
            if(level.player.gunOption==1) {
                countDown(1);
                reloadPistolSound.play();
            }
            if(level.player.gunOption==2) {
                countDown(4);
                reloadRifleSound.play();
            }
        }

        if ( keycode == Keys.PERIOD ) {
            pixel.setVisible(!pixel.isVisible());
        }

        else if (keycode == Keys.E) {
            if(mysteryboxEnabled && level.player.score > 200){
                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000) {
                    reloadTime = TimeUtils.nanoTime();

                    int randi = MathUtils.random(1, 10);

                    if (level.player.gunOption == 1) {
                        level.player.gun1Magazines += 5;
                        loadSound.play(0.75f);
                        labelInfo.setText("Gun magazine+5");
                    }
                    if (level.player.gunOption == 2) {
                        level.player.gun2Magazines += 1;
                        loadSound.play(0.75f);
                        labelInfo.setText("Rifle magazine+1");
                    }

                    if (currLevel > 3 && randi > 2) {
                        if (randi == 10 && level.player.monkeyCount ==0) {
                            level.player.monkeyCount = 3;
                            monkeySound.play(0.75f);
                            labelInfo.setText("Monkey Bomb!");
                        }
                        else if (randi > 7 && !tp_visible && level.player.monkeyCount ==0) {
                            level.player.teleport_Sound.play(1f);
                            tp_icon.addAction(Actions.fadeIn(3));
                            labelInfo.setText("Teleport!");
                            tp_visible = true;
                        }
                        else if (randi > 5 && level.player.healtkitCount == 0){
                            labelHealthkitCount.setVisible(true);
                            healthkit_icon.setVisible(true);
                            level.player.healtkitCount = 3;
                            labelInfo.setText("Health Kit+3");
                        }
                        else if (randi > 2 && level.player.grenadeCount == 0){
                            labelGrenadeCount.setVisible(true);
                            grenade_icon.setVisible(true);
                            level.player.grenadeCount = 3;
                            labelInfo.setText("Grenades+3");
                            //System.out.println("Grenades!");
                        }

                    }
                        level.player.score -= 200;
                        //System.out.println(randi);
                }
            }
        }

// Gun options
        else if (keycode == Keys.NUM_1) {
            if(level.player.gunOption != 1){
                level.player.gunOption = 1;
                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000) {
                    reloadTime = TimeUtils.nanoTime();
                    loadSound.play(1f,1.5f,0);
                }
            }
        }
        else if (keycode == Keys.NUM_2) {
            if(level.player.gunOption != 2){
                level.player.gunOption = 2;
                if (TimeUtils.timeSinceNanos(reloadTime) > 1000000000) {
                    reloadTime = TimeUtils.nanoTime();
                    loadSound.play(1f);
                }
            }
        }
        else if (keycode == Keys.NUM_3) {
            //System.out.println(level.player.grenadeCount);
            if(level.player.grenadeCount>0) {
                if (TimeUtils.timeSinceNanos(reloadTime) > 2000000000) {
                    reloadTime = TimeUtils.nanoTime();
                    grenadeDropSound.play(0.45f);
                    grenade.setPosition(level.player.getX()-0.25f, level.player.getY()-0.25f);
                    grenade.setVisible(true);
                    grenade.body.setTransform(grenade.getX(), grenade.getY(), 0);
                    float delay = 2f; // seconds
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            // Do your work
                            setExplosion(grenade.getX() - 0.35f, grenade.getY() - 0.35f,true);
                            grenade.setPosition(-100, -100);
                            grenade.setVisible(false);
                            grenade.body.setTransform(grenade.getX(), grenade.getY(), 0);
                        }
                    }, delay);
                    level.player.grenadeCount--;
                }
            }
        }
        else if (keycode == Keys.NUM_4) {
            if(level.player.healtkitCount>0 && level.player.health < 70) {
                if (TimeUtils.timeSinceNanos(reloadTime) > 2000000000) {
                    reloadTime = TimeUtils.nanoTime();
                    countDown(5);
                    float delay = 5f; // seconds
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            // Do your work
                            level.player.health += 50;
                            if (level.player.health > 100)
                                level.player.health = 100;
                        }
                    }, delay);
                    labelInfo.setText("Health Kit!");
                    LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                    LevelScreen.labelInfo.addAction(Actions.delay(2));
                    LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));
                    level.player.healtkitCount--;
                }
            }
        }

        else if (keycode == Keys.NUM_5) {
            if(tp_visible && level.player.monkeyCount == 0) {
                level.player.warp();
            }

            if(!level.player.isMonkeyActive && level.player.monkeyCount > 0) {
                final Monkey monkey = new Monkey(0, 0, mainStage);
                monkey.setPosition(level.player.getX(), level.player.getY() - 0f);

                //monkey.setRotation( level.player.getRotation());
                monkey.setMotionAngle( level.player.getRotation());

                float delay1 = 2f; // seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // Do your work
                        level.player.isMonkeyActive = true;
                        monkeyX = monkey.getX();
                        monkeyY = monkey.getY();//+3.5f
                        countDown2(6);
                        labelInfo.setText("Monkey!");
                        monkeyBombSound.play(1.5f);
                        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
                        LevelScreen.labelInfo.addAction(Actions.delay(2));
                        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));

                        Vector2 origin = new Vector2();
                        BodyDef bodyDef;
                        Body body;
                        CircleShape circleShape;
                        FixtureDef fixtureDef;

                        bodyDef = new BodyDef();
                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(monkeyX,monkeyY);
                        body = b2dworld.createBody(bodyDef);
                        monkey.body = body;
                        origin.x = monkey.bounds.width / 2f;
                        origin.y = monkey.bounds.height / 2f;
                        circleShape = new CircleShape();
                        circleShape.setPosition(origin);
                        circleShape.setRadius(0.15f);
                        fixtureDef = new FixtureDef();
                        fixtureDef.shape = circleShape;
                        body.createFixture(fixtureDef);
                        circleShape.dispose();

                    }
                }, delay1);


                float delay = 8f; // seconds
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // Do your work
                        //setExplosion(monkey.getX() - 0.35f, monkey.getY() - 0.35f);
                        setExplosion(monkey.getX() - 0.25f, monkey.getY(), false);
                        level.player.isMonkeyActive = false;
                        level.player.monkeyCount--;
                        if (!b2dworld.isLocked())
                            b2dworld.destroyBody(monkey.body);
                        monkey.body = null;
                    }
                }, delay);
            }
        }

         return false;
    }

    public static void countDown(int seconds){
        if(!countdown.inProgress) {
            countdown.wait = 0;
            countdown.setVisible(true);
            countdown.inProgress = true;
            countdown.seconds = seconds+1;
        }
    }

    public static void countDown2(int seconds){
        if(!countdown.inProgress) {
            countdown.wait = 0;
            countdown.setVisible(true);
            //countdown.inProgress = true;
            countdown.seconds = seconds+1;
        }
    }


    public void setExplosion(float x, float y, boolean sound){

        Explosion boom = new Explosion(0, 0, mainStage);
        boom.setScale(0.5f);
        //boom.centerAtActor(barrel);
        boom.setPosition(x-0.5f,y-2.5f);
        if(sound)
            explosion.play(0.25f);

        labelInfo.setText("Kaboom!");
        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeIn(0.5f)));
        LevelScreen.labelInfo.addAction(Actions.delay(2));
        LevelScreen.labelInfo.addAction(Actions.after(Actions.fadeOut(0.5f)));

        final BlastRadius blast = new BlastRadius(x-3,y-3,mainStage);
        blast.setScale(0.5f);
        final BlastRadius blast2 = new BlastRadius(x-3,y-3,mainStage);
        blast2.setScale(0.5f);
        blast2.addAction(Actions.fadeOut(5f));
        blast.setColor(Color.RED);
        level.blastList.add(blast);

        Vector2 origin = new Vector2();
        BodyDef bodyDef;
        Body body;
        CircleShape circleShape;
        FixtureDef fixtureDef;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(blast.position);
        body = b2dworld.createBody(bodyDef);
        blast.body = body;
        origin.x = blast.bounds.width / 2f;
        origin.y = blast.bounds.height / 2f;
        circleShape = new CircleShape();
        circleShape.setPosition(origin);
        circleShape.setRadius(1.5f);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        body.createFixture(fixtureDef);
        circleShape.dispose();

        float delay = 0.1f; // seconds
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Do your work
                if (!b2dworld.isLocked())
                    b2dworld.destroyBody(blast.body);
                blast.body = null;
                blast.setPosition(-100,-100);
                blast.addAction(Actions.removeActor());
            }
        }, delay);

    }

}
