package com.mygdx.game;

public class GameOver {
    GameOver() {
        for (int i = 0; i < Main.enemy.length; i++) {
            Main.enemy[i].spawn();
            Main.enemy[i].statusActive = false;
        }
    }
}
