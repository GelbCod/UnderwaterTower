package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class NewMissionMark {
    float x, y;
    float width = 100;
    float height = 150;
    int deltaX = 0, deltaY = 0;
    void spawn() {
        this.x = MathUtils.random(50, Main.SCR_WIDTH-200);
        this.y = MathUtils.random(50, Main.SCR_HEIGHT-150);
    }
    boolean hit(float tx, float ty){
        return x+deltaX < tx & tx < x+deltaX+width & y+deltaY-height < ty & ty < y+deltaY;
    }
}
