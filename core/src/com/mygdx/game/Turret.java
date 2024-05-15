package com.mygdx.game;

import static java.lang.Math.abs;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Turret {
    float a;
    float curA = 0;
    int turX;
    int turY;
    float eX;
    float eY;
    float width = 48, height = 32;

    float barrelX, barrelY;
    float barrelEndX, barrelEndY;

    float  barrelLenght = width - height/2;

    boolean fire;

    void setBarrelEnd(){
        barrelEndX = barrelX + barrelLenght*MathUtils.cosDeg(a);
        barrelEndY = barrelY + barrelLenght*MathUtils.sinDeg(a);
    }

    void spawn(int turretNumber) {
        fire = false;
        switch (turretNumber) {
            case 0: turX = (int) Main.SCR_WIDTH/5;
            turY = (int) Main.SCR_HEIGHT-100;
            break;
            case 1: turX = (int) Main.SCR_WIDTH/5*4;
            turY = (int) Main.SCR_HEIGHT-100;
            break;
            case 2: turX = (int) Main.SCR_WIDTH/7*3;
            turY = (int) Main.SCR_HEIGHT-250;
            break;
            case 3: turX = (int) Main.SCR_WIDTH/7*4;
            turY = (int) Main.SCR_HEIGHT-250;
            break;
        }

        barrelX = turX + this.height/2;
        barrelY = turY + this.height/2;
        setBarrelEnd();

    }

    int getINearestEnemy() {
        double j = 1600;
        double curJ;
        int nearestEnemyNumber = 0;
        for (int i = 0; i < Main.enemy.length; i++) {
            curJ = Math.sqrt(Math.pow((double) (turX-Main.enemy[i].x), 2)+Math.pow((double) (turY-Main.enemy[i].y), 2));
            if (j > curJ) {
                j = curJ;
                nearestEnemyNumber = i;
            }
        }
        eX = Main.enemy[nearestEnemyNumber].x + Main.enemy[nearestEnemyNumber].width; //они нам нужны для направления новых пуль
        eY = Main.enemy[nearestEnemyNumber].y + Main.enemy[nearestEnemyNumber].height;
        return nearestEnemyNumber;
    }

    void lookToEnemy(int iNearestEnemy) {

        fire = false;

        float rotation = MathUtils.atan((barrelX - eX)/(eY - barrelY))*MathUtils.radiansToDegrees;

        if (rotation > curA) {
            curA += 0.5;

        } else if(rotation < curA) {
            curA -= 0.5;

        } else if (abs(rotation - curA) < 2){
            curA = rotation;

        }
        this.a = curA-90;

        //длина от центра вращения до дульного среза

        /*barrelX = turX + this.width + (float)(burrelLenghr*Math.sin(Math.toRadians(curA)));
        barrelY = turY + this.height/2 + (float)(burrelLenghr*Math.cos(Math.toRadians(curA)));*/

        /*barrelX = turX + (float)(burrelDiag*Math.cos(Math.toRadians(this.a) + curA2));
        barrelY = turY + (float)(burrelDiag*Math.sin(Math.toRadians(this.a) + curA2));*/



        setBarrelEnd();

        if (Math.abs(90 + this.a - rotation) < 0.5){
            fire = true;
        }

    }
}
