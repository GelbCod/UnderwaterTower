package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    Vector2 position;
    Vector2 velosity;
    int damage;
    Texture texture;

    float width, height;

    boolean deactivate;

    public Bullet(float x, float y, float targetX, float targetY) {
        texture = new Texture("bullet.png");

        width = texture.getWidth();
        height = texture.getHeight();

        position = new Vector2(x, y);
        velosity = new Vector2(targetX - x, targetY - y).nor();

        deactivate = false;

        damage = 10;
    }

    public void Update(float dt){
        position.mulAdd(velosity, 15); //скорость. По идее надо увязать с dt, чтобы не зависела от fps
    }

    public void dispose(){
        texture.dispose();
    }

}
