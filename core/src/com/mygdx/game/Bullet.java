package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    Vector2 position;
    Vector2 velosity;
    Vector2 direction;
    int damage;
    Texture texture;

    public Bullet(float x, float y, float targetX, float targetY) {
        texture = new Texture("bullet.png");
        position = new Vector2(x, y);
        direction = new Vector2(targetX, targetY);
        velosity = new Vector2(targetX - x, targetY - y).nor();

        damage = 10;
    }

    public void Update(float dt){
        //position.add(velosity); //1/dt
        position.mulAdd(velosity, 15); //скорость. По идее надо увязать с dt, чтобы не зависела от fps
    }

    public void dispose(){
        texture.dispose();
    }

}
