package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	public static final float SCR_HEIGHT = 900, SCR_WIDTH = 1600;
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;

	public static Enemy[] Enemy = new Enemy[15];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < Enemy.length; i++) {
			Enemy[i] = new Enemy();
		}
		for (int i = 0; i < Enemy.length; i++) {
			Enemy[i].spawn();
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.setProjectionMatrix(camera.combined);
		for (int i = 0; i < Enemy.length; i++) {
			Enemy[i].moveToBase(Enemy[i].vx, Enemy[i].vy, Enemy[i].x, Enemy[i].y);
		}
		batch.begin();

		for (int i = 0; i < Enemy.length; i++) {
			batch.draw(img, Enemy[i].x, Enemy[i].y, Enemy[i].width, Enemy[i].height);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
