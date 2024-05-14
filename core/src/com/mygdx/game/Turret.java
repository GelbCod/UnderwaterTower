package com.mygdx.game;

import static java.lang.Math.abs;

import com.badlogic.gdx.math.MathUtils;

public class Turret {
    float a;
    float curA = 0;
    int turX;
    int turY;
    float eX;
    float eY;
    float width = 48, height = 32;

    float barrelX, barrelY;
    float burrelLenghr = width - height/2;
    boolean fire;

    float spaceToNearestEnemy = 0;

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

        barrelX = turX + this.width;
        barrelY = turY + this.height/2;
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
        /*eX = Main.enemy[iNearestEnemy].x;
        eY = Main.enemy[iNearestEnemy].y;*/
        fire = false;
        float rotation = MathUtils.atan((Main.enemy[iNearestEnemy].width + turX-Main.enemy[iNearestEnemy].x)/(Main.enemy[iNearestEnemy].height + Main.enemy[iNearestEnemy].y-turY))*MathUtils.radiansToDegrees;
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

        /*barrelX = turX + (float)(burrelLenghr*Math.sin(Math.toRadians(curA)));
        barrelY = turY + (float)(burrelLenghr*Math.cos(Math.toRadians(curA)));*/



        if (Math.abs(90 + this.a - rotation) < 0.5){
            fire = true;
        }

    }
}
