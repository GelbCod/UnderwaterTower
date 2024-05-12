package com.mygdx.game;

import static java.lang.Math.abs;

import com.badlogic.gdx.math.MathUtils;

public class Turret {
    float a;
    float curA;
    int turX;
    int turY;
    float eX;
    float eY;
    float spaceToNearestEnemy = 0;

    void spawn(int turretNumber) {
        switch (turretNumber) {
            case 1: turX = (int) Main.SCR_WIDTH/5*2;
            turY = (int) Main.SCR_HEIGHT-100;
            break;
            case 2: turX = (int) Main.SCR_WIDTH/5*4;
            turY = (int) Main.SCR_HEIGHT-100;
            break;
            case 3: turX = (int) Main.SCR_WIDTH/7*3;
            turY = (int) Main.SCR_HEIGHT-250;
            break;
            case 4: turX = (int) Main.SCR_WIDTH/7*5;
            turY = (int) Main.SCR_HEIGHT-250;
            break;
        }
    }

    void getINearestEnemy() {
        for (int i = 0; i < Main.Enemy.length; i++) {

        }
    }

    void lookToEnemy() {
        a = MathUtils.atan((turX-eX)/(eY-turY))*MathUtils.radiansToDegrees;
        if (a > curA) {
            curA += 1;
        } else if(a < curA) {
            curA -= 1;
        } else if (abs(a - curA) < 1){
            curA = a;
        }
    }
}
