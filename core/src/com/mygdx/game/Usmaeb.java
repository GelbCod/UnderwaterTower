package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;

public class Usmaeb {
    byte[] chamberNumber = new byte[6];
    void tossingMap() {
        chamberNumber[0] = (byte) MathUtils.random(0, 1);
        if (chamberNumber[0] == 0) {
            chamberNumber[1] = (byte) MathUtils.random(0, 1);
        }
    }
}
