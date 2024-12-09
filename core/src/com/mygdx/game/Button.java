package com.mygdx.game;

public class Button {
    float x, y;
    float width, height;
    int deltaX = 0, deltaY = 0;
    public Button(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    boolean hit(float tx, float ty){
        return x+deltaX < tx & tx < x+deltaX+width & y+deltaY-height < ty & ty < y+deltaY;
    }
}
