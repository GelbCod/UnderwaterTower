package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {
	public static final float SCR_HEIGHT = 900, SCR_WIDTH = 1600;
	SpriteBatch batch;
	Texture img;
	Texture imgTurret;
	OrthographicCamera camera;

	public static Enemy[] enemy = new Enemy[5];
	Turret[] turret = new Turret[4];
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		imgTurret = new Texture("turret.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = new Enemy();
		}
		for (int i = 0; i < turret.length; i++) {
			turret[i] = new Turret();
		}
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].spawn();
		}
		for (int i = 0; i < turret.length; i++) {
			turret[i].spawn(i);
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.setProjectionMatrix(camera.combined);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].moveToBase(enemy[i].vx, enemy[i].vy, enemy[i].x, enemy[i].y);
		}
		for (int i = 0; i < turret.length; i++) {
			turret[i].lookToEnemy(turret[i].getINearestEnemy());
		}
		batch.begin();

		for (int i = 0; i < enemy.length; i++) {
			batch.draw(img, enemy[i].x, enemy[i].y, enemy[i].width/2, enemy[i].height/2, enemy[i].width, enemy[i].height,
			1, 1, enemy[i].rotation, 0, 0, 256, 256, false, false);
		}
		for (int i = 0; i < turret.length; i++) {
			batch.draw(imgTurret, turret[i].turX, turret[i].turY, turret[i].width/2, turret[i].height/2, turret[i].width, turret[i].height, 1, 1, turret[i].a, 0, 0, 48, 32, false, false);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
