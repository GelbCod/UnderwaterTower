package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class NewMissionMark {
    float x, y;
    float width = 100;
    float height = 150;
    void spawn() {
        this.x = MathUtils.random(50, Main.SCR_WIDTH-200);
        this.y = MathUtils.random(50, Main.SCR_HEIGHT-150);
    }
    boolean hit(float tx, float ty){
        return x < tx & tx < x+width & y-height < ty & ty < y;
    }
}
