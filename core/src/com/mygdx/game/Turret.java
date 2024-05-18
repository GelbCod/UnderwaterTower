package com.mygdx.game;

import static java.lang.Math.abs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    float barrelEndX, barrelEndY;

    float  barrelLenght = width - height/2;

    boolean fire;

    Texture texture;
    Texture targetDot;

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

        texture = new Texture("turret" + turretNumber + ".png");
        targetDot = new Texture("targetdot" + turretNumber + ".png");

        barrelX = turX + this.height/2;
        barrelY = turY + this.height/2;
        setBarrelEnd();

    }

    int getINearestEnemy() {
        fire = false;
        double j = 1600;
        double curJ;
        int nearestEnemyNumber = 0;
        for (int i = 0; i < Main.enemy.length; i++) {
            if (Main.enemy[i].x < 0 | Main.enemy[i].x > Gdx.graphics.getWidth() | Main.enemy[i].y < 0 | Main.enemy[i].y > Gdx.graphics.getHeight()) {continue;} //игнорируем врагов за пределами экрана

            if (Main.enemy[i].statusActive) {
                curJ = Math.sqrt(Math.pow((double) (turX - Main.enemy[i].x), 2) + Math.pow((double) (turY - Main.enemy[i].y), 2));
                if (j > curJ) {
                    j = curJ;
                    nearestEnemyNumber = i;
                    fire = true;
                }
            }

        }

        //Определим угол между врагом и туррелью. Угол самого врага при этом никакой роли не играет!
        float centerEnemyX = Main.enemy[nearestEnemyNumber].x + Main.enemy[nearestEnemyNumber].lenghtDiag;
        float centerEnemyY = Main.enemy[nearestEnemyNumber].y + Main.enemy[nearestEnemyNumber].lenghtDiag;

        float angleTurretEnemy = MathUtils.atan((barrelEndY - centerEnemyY)/(barrelEndX - centerEnemyX));
        //Определим текущую точку прицеливания туррели
        //плюсы минусы как-то нелогично расположены, но именно этот вариант рабочий
        //никакие другие нормального результата не дают!
        if (angleTurretEnemy > 0){ //противник слева
            eX = centerEnemyX + Main.enemy[nearestEnemyNumber].lenghtDiag * MathUtils.cos(angleTurretEnemy);
            eY = centerEnemyY + Main.enemy[nearestEnemyNumber].lenghtDiag * MathUtils.sin(angleTurretEnemy);
        }else{
            eX = centerEnemyX - Main.enemy[nearestEnemyNumber].lenghtDiag * MathUtils.cos(angleTurretEnemy);
            eY = centerEnemyY - Main.enemy[nearestEnemyNumber].lenghtDiag * MathUtils.sin(angleTurretEnemy);
        }

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


        //Ищем дульный срез, откуда будут вылетать пули
        setBarrelEnd();


        if (Math.abs(90 + this.a - rotation) < 0.5){
            fire = true;
        }
    }

    public void dispose(){
        texture.dispose();
        targetDot.dispose();
    }
}
