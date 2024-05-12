package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class Enemy {
    int SCR_WIDTH = (int )Main.SCR_WIDTH;
    int SCR_HEIGHT = (int )Main.SCR_HEIGHT;
    float v = 0; //Вектор
    float a = 0; //Альфа Угол
    float rotation = 0; //Угол поворота
    float x = 0, y = 0;
    float vx = 2, vy;
    float width, height;

    boolean statusActive = true;

    void spawn() {
        height = width = MathUtils.random(45, 100);
        switch (MathUtils.random(0, 3)) {

            case 0: x = MathUtils.random(-width, Main.SCR_WIDTH/2-150);
            y = MathUtils.random(-150, -110);
            break;

            case 1: x = MathUtils.random(Main.SCR_WIDTH/2+150, Main.SCR_WIDTH);
            y = MathUtils.random(-150, -110);
            break;

            case 2: x = -width;
            y = MathUtils.random(0, Main.SCR_HEIGHT/2);
            break;

            case 3: x = SCR_WIDTH;
            y = MathUtils.random(0, Main.SCR_HEIGHT/2);
            break;
        }
        v = MathUtils.random(2f, 5f);
        a = MathUtils.atan((x-Base.xB)/(y-Base.yB));
        rotation = a*MathUtils.radiansToDegrees;
        vx = MathUtils.sin(a)*v;
        vy = MathUtils.cos(a)*v;
    }
    void moveToBase(float vx, float vy, float x, float y) {
        if (statusActive) {
            x += vx;
            y += vy;
            this.x = x;
            this.y = y;
            if (y >= Main.SCR_HEIGHT/3*2) {
                attack();
            }
        }
    }

    void attack() {
        spawn();
    }
}
