package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.B2dModel;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Constants;

import javax.swing.text.View;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected Stage mainStage;
    protected Stage uiStage;
    public static World b2dworld;
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    //Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer(true,true,true,true,true,true);

    public Vector3 mouse_position = new Vector3(0,0,0);
    Camera cam;

    public BaseScreen() {
        mainStage = new Stage();
        uiStage = new Stage();
        initialize();
        //b2dworld = new World(new Vector2(0,0),true);
    }

    public abstract void initialize();
    public abstract void update(float dt);

    public void render(float dt) {
        //if(!Constants.gamePaused) {
            cam = mainStage.getCamera();

            uiStage.act(dt);
            mainStage.act(dt);
            update(dt);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            mainStage.draw();
            uiStage.draw();

            mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(mouse_position);

            b2dworld.step(dt, 8, 3);
            if (Constants.DEBUG_DRAW_BOX2D_WORLD)
                debugRenderer.render(b2dworld, cam.combined);
        //}
    }

    // methods required by Screen interface
    public void resize(int width, int height) { }
    public void pause() {
        Constants.gamePaused = true;
    }
    public void resume() {
        Constants.gamePaused = false;
    }
    public void dispose() { }
    public void show() {
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor( im );
        im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
    }

    public void hide() {
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage);
    }

    // methods required by InputProcessor interface
    public boolean keyDown(int keycode) { return false; }
    public boolean keyUp(int keycode) { return false; }
    public boolean keyTyped(char c) { return false; }
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    public boolean scrolled(int amount) { return false; }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

}
