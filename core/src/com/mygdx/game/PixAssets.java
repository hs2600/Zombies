
package com.mygdx.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class PixAssets {

    Pixmap pixmap;

    public Texture healthbarBG = new Texture(pixmap = healthBarBG());
    public Texture healthbarFG = new Texture(pixmap = healthBarFG());
    public Texture gameoverBG = new Texture(pixmap = gameOverBG());
    //public Texture gameoverBG = new Texture(pixmap = gameOverBG());

    public Pixmap createPixmap(float r, float g, float b){

        Pixmap pixmap;
        int width = 140;
        int height = 140;

        pixmap = new Pixmap(width, height, Format.RGBA8888);
        pixmap.setColor(0,0,0,0.3f);
        //pixmap.fill();

        //Pick handle
        pixmap.setColor(0,0,0,1);
        pixmap.fillRectangle(85,5,10,110);

        //Pick
        pixmap.setColor(0.4f,0.4f,0.4f,1);
        pixmap.fillRectangle(75,115,20,20);
        pixmap.fillTriangle(95,115,95,134,110,122);
        pixmap.fillTriangle(95,115,95,134,110,127);
        pixmap.fillRectangle(100,122,10,6);

        //Left hand
        pixmap.setColor(0,0,0,1);
        pixmap.fillCircle(85, 25, 15);
        pixmap.setColor(r,g,b,1);
        pixmap.fillCircle(85, 25, 10);

        //Right hand
        pixmap.setColor(0,0,0,1);
        pixmap.fillCircle(85, 95, 15);
        pixmap.setColor(r,g,b,1);
        pixmap.fillCircle(85, 95, 10);

        //Head
        pixmap.setColor(0,0,0,1.0f);
        pixmap.fillCircle(50, 60, 50);
        pixmap.setColor(r,g,b,1);
        pixmap.fillCircle(50, 60, 43);

        return pixmap;
    }

    public Pixmap gameOverBG(){

        Pixmap pixmap;
        int width = 200;
        int height = 50;

        pixmap = new Pixmap(width, height, Format.RGBA8888);

        //BG
        pixmap.setColor(0,0,0,0.5f);
        pixmap.fill();

        return pixmap;
    }


    public Pixmap healthBarBG(){

        Pixmap pixmap;
        int width = 100;
        int height = 10;

        pixmap = new Pixmap(width, height, Format.RGBA8888);

        //BG
        pixmap.setColor(0,0,0,0.2f);
        pixmap.fill();
        //Empty (red)
        pixmap.setColor(1f,0f,0f,1f);
        pixmap.fillRectangle(0,2,100,6);


        return pixmap;
    }

    public Pixmap healthBarFG(){

        Pixmap pixmap;
        int width = 100;
        int height = 10;

        pixmap = new Pixmap(width, height, Format.RGBA8888);

        //Full (green)
        pixmap.setColor(0.40f,0.80f,0.30f,1f);
        pixmap.fillRectangle(0,2,100,6);

        return pixmap;
    }

}
