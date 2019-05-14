package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
    private static final String TAG = CameraHelper.class.getName();

    private BaseActor target;
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;

    private Vector2 position;
    private float zoom;

    private boolean movex = true;
    private boolean movey = true;


    public CameraHelper(){
        position = new Vector2();
        zoom = 1f;
    }

    public void update(float deltaTime){
        if(!hasTarget()) return;
            position.x = target.getX() + target.getOriginX();
            position.y = target.getY() + target.getOriginY();
    }

    public void setPosition(float x, float y){
        this.position.set(x,y);
    }
    public Vector2 getPosition () { return position; }

    public void movexx(boolean x){
        movex = x;
    }
    public void moveyy(boolean y){
        movey = y;
    }
    public float getX () { return position.x; }
    public float getY () { return position.y; }
    public void addZoom (float amount) { setZoom(zoom + amount); }
    public void setZoom (float zoom) {
        this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }
    public float getZoom () { return zoom; }
    public void setTarget (BaseActor target) { this.target = target; }
    public BaseActor getTarget () { return target; }
    public boolean hasTarget () { return target != null; }
    public boolean hasTarget (BaseActor target) {
        return hasTarget() && this.target.equals(target);
    }
    public void applyTo (OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }

}
