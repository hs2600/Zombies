package com.mygdx.game;

public class Constants {
    public static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    public static int START_LEVEL = 1;

    //Visible game world is 38 meters wide by 21 meters high
    public static final float VIEWPORT_WIDTH = 15f;

    //Visible game world is 5 meters tall
    public static final float VIEWPORT_HEIGHT = VIEWPORT_WIDTH * 9/16f;

    public static final float CAM_ZOOM = 0.45f;  //0.45f

    public static final float WORLD_WIDTH = 38f; //1710*1380
    public static final float WORLD_HEIGHT = 21.375f;

    // location of description file for texture atlas
    public static final String TEXTURE_ATLAS_OBJECTS = "assets-raw/images/zombies.pack.atlas";

    // Location of image file for level 01
    public static final String LEVEL_01 = "levels/level-01.png";
    public static final String LEVEL_04 = "levels/level-04.png";

    // Player move speed
    public static final float SPR_MOVESPEED = 2.5f;

    public static final float SHOOT_WAIT_TIME = 0.1f;

    public static boolean gamePaused = false;

    public static int ZOMBXLEVEL[][] = {
        {20,5},  {40,5},  {60,5},  {80,5},  {100,20},
        {120,20},{140,20},{160,20},{180,20},{200,50}
    };
    //{20,5},  {40,5},  {60,5},  {80,5},  {100,20},
    //{120,20},{140,20},{160,20},{180,20},{200,50}
    //{5,5},  {10,5},  {10,5},  {10,5},  {10,10},
    //{10,10},  {10,10},  {10,10},  {10,10},  {20,20}

    // GUI Width
    //public static final float VIEWPORT_GUI_WIDTH = 80.0f;
    // GUI Height
    //public static final float VIEWPORT_GUI_HEIGHT = 48.0f;

}
