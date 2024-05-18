package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
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
    float targetX, targetY;

    float lenghtDiag;
    boolean statusActive = false;
    int attackCountHelper = 61;

    Texture texture;

    void spawn() {
        //if (statusActive) {
        height = width = MathUtils.random(45, 100);

        //длина диагонали к середине верхней части врага - для расчета места прицеливания
        lenghtDiag = height / 2;

        int randomNum = MathUtils.random(0, 3);
        switch (randomNum) {

            case 0:
                x = MathUtils.random(-width, Main.SCR_WIDTH / 2 - 150);
                y = MathUtils.random(-150, -110);
                break;

            case 1:
                x = MathUtils.random(Main.SCR_WIDTH / 2 + 150, Main.SCR_WIDTH);
                y = MathUtils.random(-150, -110);
                break;

            case 2:
                x = -width;
                y = MathUtils.random(0, Main.SCR_HEIGHT / 2);
                break;

            case 3:
                x = SCR_WIDTH;
                y = MathUtils.random(0, Main.SCR_HEIGHT / 2);
                break;
        }

        texture = new Texture("enemy" + randomNum + ".png");

        targetX = MathUtils.random(Main.SCR_WIDTH / 7 * 2, Main.SCR_WIDTH / 7 * 3.5f);
        targetY = MathUtils.random(Main.SCR_HEIGHT - 300, Main.SCR_HEIGHT - 200);

        v = MathUtils.random(1f, 2f);
        a = MathUtils.atan((x - targetX) / (y - targetY));
        rotation = -a * MathUtils.radiansToDegrees;
        vx = MathUtils.sin(a) * v;
        vy = MathUtils.cos(a) * v;
        //}
    }
    void moveToBase(float vx, float vy, float x, float y) {
        if (statusActive) {
            x += vx;
            y += vy;
            this.x = x;
            this.y = y;
            //не было условия по оси Y, поэтому враги посередине экрана дергались, атакую сразу
            if (x >= Main.SCR_WIDTH/7*3-75 & x <= Main.SCR_WIDTH/7*4+75 & y > 200) {
                attack();
            }
        }
    }

    void attack() {
        //statusActive = false;
        if (attackCountHelper <= 60) {
            x -= 2 * vx;
            y -= 2 * vy;
            attackCountHelper++;
        } else if (attackCountHelper <= 70) {
            x += 5 * vx;
            y += 5 * vy;
            attackCountHelper++;
        } else {
            attackCountHelper = 0;

        }
        Base.health -= 2/30F;
    }

    public void dispose(){
        texture.dispose();
    }
}
