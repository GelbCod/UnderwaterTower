package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class Main extends ApplicationAdapter {
	public static final float SCR_HEIGHT = 900, SCR_WIDTH = 1600;
	public static final int TURRETT_COUNT = 4;
	SpriteBatch batch;
	Texture img;
	Texture imgTurret;
	OrthographicCamera camera;

	public static Enemy[] enemy = new Enemy[5];
	Turret[] turret = new Turret[TURRETT_COUNT];

	ArrayList<Bullet> bullets;

	//private float fireDelay;
	private float[] turretFireDelay = new float[TURRETT_COUNT];

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


		bullets = new ArrayList<Bullet>();



	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);

		float dt = Gdx.graphics.getDeltaTime();

		for (int i = 0; i < turretFireDelay.length; i++){
			turretFireDelay[i] -= dt;
		}



		batch.setProjectionMatrix(camera.combined);
		for (int i = 0; i < enemy.length; i++) {
			enemy[i].moveToBase(enemy[i].vx, enemy[i].vy, enemy[i].x, enemy[i].y);
		}
		for (int i = 0; i < turret.length; i++) {

			int indexEnemy = turret[i].getINearestEnemy();
			turret[i].lookToEnemy(indexEnemy);



//почему то стреляет только первая туррель - самая левая которая хм...теперь две 1 и 3, 2 и 4 патроной не завезли )))
			if (turret[i].fire){
				if (turretFireDelay[i] <= 0){
					bullets.add(new Bullet(turret[i].turX + (turret[i].height/2), turret[i].turY + (turret[i].height/2), turret[i].eX, turret[i].eY));
					//bullets.add(new Bullet(turret[i].barrelX, turret[i].barrelY, turret[i].eX, turret[i].eY));
					turretFireDelay[i] += 0.2;
				}
			} else {
				turretFireDelay[i] = 0;
			}



		}



		for (Bullet bullet : bullets){
			bullet.Update(dt);
		}
		//Текущий размер экрана можно получить так
		//Gdx.graphics.getWidth();
		//Gdx.graphics.getHeight();
		for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
			Bullet bullet = iterator.next();
			if (bullet.position.x <= 0 | bullet.position.x >= SCR_WIDTH | bullet.position.y <= 0 | bullet.position.y >= SCR_HEIGHT){
				iterator.remove();
			}
		}

		batch.begin();

		for (int i = 0; i < enemy.length; i++) {
			batch.draw(img, enemy[i].x, enemy[i].y, enemy[i].width/2, enemy[i].height/2, enemy[i].width, enemy[i].height,
			1, 1, enemy[i].rotation, 0, 0, 256, 256, false, false);
		}

		for (Bullet bullet : bullets){
			batch.draw(bullet.texture, bullet.position.x, bullet.position.y);
		}

		for (int i = 0; i < turret.length; i++) {
			batch.draw(imgTurret, turret[i].turX, turret[i].turY, turret[i].height/2, turret[i].height/2, turret[i].width, turret[i].height, 1, 1, turret[i].a, 0, 0, 48, 32, false, false);
		}







		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		imgTurret.dispose();

		for (Bullet bullet : bullets){
			bullet.dispose();
		}
	}
}
