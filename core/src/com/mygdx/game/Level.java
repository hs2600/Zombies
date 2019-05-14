package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Objects.*;

import java.util.ArrayList;

import static com.mygdx.game.Screens.BaseScreen.b2dworld;

public class Level {
    public static final String TAG = Level.class.getName();
    public enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // black
        TREE(0, 255, 0), // green
        ZOMBIE(255, 0, 255), // purple
        ROCK(255, 255, 0), // yellow
        BARREL(255, 0, 0), // red
        PORTAL(0, 0, 255), // blue
        PLAYER_SPAWNPOINT(255, 255, 255); // white

        private int color;
        BLOCK_TYPE (int r, int g, int b) {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor (int color) {
            return this.color == color;
        }

        public int getColor () {
            return color;
        }
    }

    // objects
    public Player player;
    public ArrayList<Zombie> zombieList;
    public ArrayList<ZombieBoss> zombieBossList;
    public ArrayList<Tree> treeList;
    public ArrayList<Rock> rockList;
    public static ArrayList<Barrel> barrelList;
    public static ArrayList<BlastRadius> blastList;
    public static ArrayList<Portal> portalList;
    public BaseActor mb; //Mystery Box

    //public int zombieCount = 200;

    public Level (String filename, Stage stage) {
        init(filename, stage);
    }

    private void init (String filename, Stage stage) {
        // objects
        player = null;
        zombieList = new ArrayList<Zombie>();
        zombieBossList = new ArrayList<ZombieBoss>();
        treeList = new ArrayList<Tree>();
        rockList = new ArrayList<Rock>();
        portalList = new ArrayList<Portal>();
        barrelList = new ArrayList<Barrel>();
        blastList = new ArrayList<BlastRadius>();

        mb = new MysteryBox(24,10,stage);

        // load image file that represents the level data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));

        // scan pixels from top-left to bottom-right
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                BaseActor obj = null;
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is a match

                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {
                    // do nothing
                }
                // rock
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    obj = new Rock(pixelX,pixelY,stage);
                    rockList.add((Rock)obj);
                }
                // Trees
                else if (BLOCK_TYPE.TREE.sameColor(currentPixel)) {
                    obj = new Tree(pixelX,pixelY,stage);
                    treeList.add((Tree)obj);
                }
                // Portals
                else if (BLOCK_TYPE.PORTAL.sameColor(currentPixel)) {
                    obj = new Portal(pixelX,pixelY,stage);
                    portalList.add((Portal)obj);
                }
                // Barrels
                else if (BLOCK_TYPE.BARREL.sameColor(currentPixel)) {
                    obj = new Barrel(pixelX,pixelY,stage);
//                    System.out.println("x: " + pixelX +", y: " + pixelY);
                    barrelList.add((Barrel)obj);
                }
                 //Zombie Boss
//                else if (BLOCK_TYPE.ZOMBIE.sameColor(currentPixel)) {
//                    obj = new ZombieBoss(pixelX,pixelY,stage);
////                    obj.setScale(1.5f, 1.5f);
//                    zombieBossList.add((ZombieBoss) obj);
//                }
                // player spawn point
                else if
                (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    obj = new Player(pixelX,pixelY,stage);
                    obj.setScale(1.25f, 1.25f);
                    player = (Player)obj;
                }
                // unknown object/pixel color
                else {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    //Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                    //        + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
            }
        }

//        for (int i = 0; i < zombieCount; i++) {
//            Zombie obj = null;
//            float randomX = MathUtils.random(Constants.WORLD_WIDTH/2f*-1, Constants.VIEWPORT_WIDTH*2f) + 13;
//            float randomY = MathUtils.random(Constants.WORLD_HEIGHT/2f*-1, Constants.VIEWPORT_HEIGHT*2f) + 7 ;
//            obj = new Zombie(randomX,randomY, stage);
//            obj.setScale(0.65f, 0.65f);
//            zombieList.add(obj);
//        }
        // free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");
    }

    public void zSpawn(int count, Stage stage ){

        for (int i = 0; i < count; i++) {
            Zombie obj = null;
            float randomX = MathUtils.random(Constants.WORLD_WIDTH/2f*-1, Constants.VIEWPORT_WIDTH*2f) + 13;
            float randomY = MathUtils.random(Constants.WORLD_HEIGHT/2f*-1, Constants.VIEWPORT_HEIGHT*2f) + 7 ;

            int randi = MathUtils.random(1, 3);

            if(randi==1)
                obj = new Zombie(randomX, randomY, "Z1.png", stage);
            else if(randi==2)
                obj = new Zombie(randomX, randomY, "Z2.png", stage);
            else
                obj = new Zombie(randomX, randomY, "Z3.png", stage);

            obj.setScale(0.65f, 0.65f);

            //obj.loadTexture("Z4.Png");

            Vector2 origin = new Vector2();
            BodyDef bodyDef;
            Body body;
            CircleShape circleShape;
            FixtureDef fixtureDef;

            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(obj.position);
            body = b2dworld.createBody(bodyDef);
            obj.body = body;
            origin.x = obj.bounds.width / 2f + 0.02f;
            origin.y = obj.bounds.height / 2f + 0.02f;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.25f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();

            zombieList.add(obj);
        }

    }

    public void zBossSpawn(int count, Stage stage ){

        for (int i = 0; i < count; i++) {
            ZombieBoss obj = null;
            float randomX = MathUtils.random(Constants.WORLD_WIDTH/2f*-1, Constants.VIEWPORT_WIDTH*2f) + 13;
            float randomY = MathUtils.random(Constants.WORLD_HEIGHT/2f*-1, Constants.VIEWPORT_HEIGHT*2f) + 7 ;
            obj = new ZombieBoss(randomX,randomY, stage);
            obj.setScale(1.5f, 1.5f);

            Vector2 origin = new Vector2();
            BodyDef bodyDef;
            Body body;
            CircleShape circleShape;
            FixtureDef fixtureDef;

            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(obj.position);
            body = b2dworld.createBody(bodyDef);
            obj.body = body;
            origin.x = obj.bounds.width / 2f + 0.02f;
            origin.y = obj.bounds.height / 2f + 0.02f;
            circleShape = new CircleShape();
            circleShape.setPosition(origin);
            circleShape.setRadius(0.25f);
            fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            body.createFixture(fixtureDef);
            circleShape.dispose();

            zombieBossList.add(obj);
        }

    }
}