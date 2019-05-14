package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    // singleton: prevent instantiation from other classes
    private Assets () {}

    public AssetPlayer player;
    public AssetZombies zombies;
    public AssetLevelDecoration levelDecoration;

    public void init (AssetManager assetManager) {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,
                TextureAtlas.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: "
                + assetManager.getAssetNames().size);
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);
    //}

//    TextureAtlas atlas = new TextureAtlas(Constants.TEXTURE_ATLAS_OBJECTS);

    TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
    // enable texture filtering for pixel smoothing
     for (Texture t : atlas.getTextures()) {
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }

    // create game resource objects
    player = new AssetPlayer(atlas);
    zombies = new AssetZombies(atlas);
    levelDecoration = new AssetLevelDecoration(atlas);

    }

    @Override
    public void dispose () {
        assetManager.dispose();
    }

    //@Override
    public void error (String filename, Class type,
                       Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '"
                + filename + "'", (Exception)throwable);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" +
                asset.fileName + "'", (Exception)throwable);
    }

    public class AssetPlayer {
        public final AtlasRegion player;
        public final AtlasRegion player_gun1;
        public final AtlasRegion player_gun2;

        public AssetPlayer (TextureAtlas atlas){
            player = atlas.findRegion("player");
            player_gun1 = atlas.findRegion("player_gun1");
            player_gun2 = atlas.findRegion("player_gun2");
        }
    }

    public class AssetZombies {
        public final AtlasRegion zombie1;
        public final AtlasRegion zombie2;
        public final AtlasRegion zombie3;
        public final AtlasRegion zombie4;

        public AssetZombies (TextureAtlas atlas) {
            zombie1 = atlas.findRegion("z1");
            zombie2 = atlas.findRegion("z2");
            zombie3 = atlas.findRegion("z3");
            zombie4 = atlas.findRegion("z4");
        }
    }

    public class AssetLevelDecoration {
        public final AtlasRegion rock;
        public final AtlasRegion tree;
        public final AtlasRegion background;
        public final AtlasRegion bullet;
        public final AtlasRegion handgunBullet;
        public final AtlasRegion gun1;
        public final AtlasRegion gun2;
        public final AtlasRegion handgunFlash;
        public final AtlasRegion mystery_box;

        public AssetLevelDecoration (TextureAtlas atlas) {
            rock = atlas.findRegion("rock");
            tree = atlas.findRegion("tree");
            background = atlas.findRegion("background");
            bullet = atlas.findRegion("artillery_trail4");
            handgunBullet = atlas.findRegion("artillery_trail3");
            gun1 = atlas.findRegion("glock");
            gun2 = atlas.findRegion("m249");
            handgunFlash = atlas.findRegion("gun_flash");
            mystery_box = atlas.findRegion("mystery_box");
        }
    }
}
