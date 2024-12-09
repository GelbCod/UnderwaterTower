package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;



public class Main extends ApplicationAdapter {
	LocalDataBase LDB = new LocalDataBase();
	//общая громкость звука
	private static final float MAIN_VOLUME = 0.2f;
	public static final float SCR_HEIGHT = 900, SCR_WIDTH = 1600;
	public static final int TURRET_COUNT = 4;
	long curTime = System.currentTimeMillis();
	long startMissionTime;
	SpriteBatch batch;

	OrthographicCamera camera;

	public static int contentCount;


	public static Enemy[] enemy = new Enemy[10];
	Turret[] turret = new Turret[TURRET_COUNT];

	public static BitmapFont fontMedium;

	public static int screenCondition = 0;

	ArrayList<Bullet> bullets;

	private float[] turretFireDelay = new float[TURRET_COUNT];
	private Sound one_turret_shoot;
	private Sound hit;
	private Sound alarm;
	private Texture bg;
	Texture foundationImg;
	Texture missionEndScreen;
	Texture explosion0;
	Texture explosion1;
	Texture explosion2;
	Texture explosion3;
	Texture explosion4;
	Texture explosion5;
	Texture explosion6;
	Texture explosion7;

	BitmapFont mainFont;



	//для волн противников
	private float chanceTimer = 0;
	private float waveTimer = 0;
	private float gapBetweenWaves;
	private int chanceOfWave = 0;
	int helper;
	int mouseX;
	int mouseY;
	int explosionTimerHelper = 0;
	int explosionTimerHelperHelper;
	Texture mainMenuScreen;
	Texture failedMissionScreen;
	Texture chooseMission;
	Texture newMissionMark;

	// Функция проверки на столкновения
	boolean checkForCollision(Enemy enemy){
		for (Bullet bullet : bullets){
			if ((enemy.x < bullet.position.x + bullet.width) && (bullet.position.x < enemy.x + enemy.width) && (enemy.y < bullet.position.y + bullet.height) && (bullet.position.y < enemy.y + enemy.height)){
				bullet.deactivate = true;
				return true;
			}
		}

		return false;
	}
	//boolean buttonMoerserIsActive = false;

	Button buttonMainMenuStart;
	Button buttonMainMenuAbout;
	Button buttonMainMenuExit;
	Button buttonAboutExit;
	Button buttonEnterDevMode;
	//Button buttonMoerser;

	NewMissionMark[] missionMark = new NewMissionMark[3];

	@Override
	public void create () {
		mainFont = new BitmapFont(Gdx.files.internal("beastimpact.fnt"));
		explosion0 = new Texture("explosion0.png");
		explosion1 = new Texture("explosion1.png");
		explosion2 = new Texture("explosion2.png");
		explosion3 = new Texture("explosion3.png");
		explosion4 = new Texture("explosion4.png");
		explosion5 = new Texture("explosion5.png");
		explosion6 = new Texture("explosion6.png");
		explosion7 = new Texture("explosion7.png");
		missionEndScreen = new Texture("missionacomp.png");
		foundationImg = new Texture("base.png");
		fontMedium = new BitmapFont();
		batch = new SpriteBatch();
		bg = new Texture("bg.png");
		mainMenuScreen = new Texture("mainmenu.png");
		chooseMission = new Texture("missionchoose.png");
		newMissionMark = new Texture("newmission.png");
		failedMissionScreen = new Texture("faildemission.png");

		gapBetweenWaves = 30; //первая волня через пол-минуты

		buttonAboutExit = new Button(75, 500, 200, 50);
		buttonMainMenuStart = new Button(75, 200, 200, 50);
		buttonEnterDevMode = new Button(SCR_WIDTH-200, SCR_HEIGHT-125, 200, 125);
		//buttonMoerser = new Button(0, 0, 75, 75);
		//buttonMainMenuAbout = new Button(75, 350, 200, 50);
		//buttonAboutExit = new Button(75, 500, 200, 50);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		for (int i = 0; i < missionMark.length; i++) {
			missionMark[i] = new NewMissionMark();
		}
		for (int i = 0; i < missionMark.length; i++) {
			missionMark[i].spawn();
		}
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = new Enemy();
			if (i < 2){
				enemy[i].statusActive = true;
			}
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
		one_turret_shoot = Gdx.audio.newSound(Gdx.files.internal("one_turret_shut.ogg"));
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
		alarm = Gdx.audio.newSound(Gdx.files.internal("alarm.ogg"));
	}

	@Override
	public void render () {
		System.out.println(buttonMainMenuStart.hit(Gdx.input.getX(), Gdx.input.getY()));
		//playAnimationExplosion(0, 0);
		ScreenUtils.clear(1, 1, 1, 1);
		/*camera.position.x += 0.5;
		camera.update();*/
		curTime = System.currentTimeMillis();
		LDB.loadRecords();
		batch.setProjectionMatrix(camera.combined);
		if (screenCondition != 2) {
			camera.position.y = SCR_HEIGHT/2;
			camera.position.x = SCR_WIDTH/2;
			camera.update();
		}
		/*if (screenCondition !=2 ) {
			buttonAboutExit.deltaY = (int) camera.position.y;
			buttonAboutExit.deltaX = (int) camera.position.x;
			buttonMainMenuStart.deltaY = (int) camera.position.y;
			buttonMainMenuStart.deltaX = (int) camera.position.x;
			for (int i = 0; i < missionMark.length; i++) {
				missionMark[i].deltaY = (int) camera.position.y;
				missionMark[i].deltaX = (int) camera.position.x;
			}
		}*/
		//System.out.println(camera.position.x + " " + camera.position.y + " ");
		switch (screenCondition) {
			case 0: if (buttonMainMenuStart.hit(Gdx.input.getX(), Gdx.input.getY()) & Gdx.input.isTouched()) {
				screenCondition = 1;
			} else { batch.begin();

				//System.out.println(Gdx.input.getX() + " " +  Gdx.input.getY() + " " + Gdx.input.isTouched());
				batch.draw(mainMenuScreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				mainFont.draw(batch, "Play", 75, SCR_HEIGHT-200);
				if (explosionTimerHelper >=33) {
					explosionTimerHelperHelper = 0;
					explosionTimerHelper= 0;
				}
				if (explosionTimerHelper < 4) {
					explosionTimerHelperHelper = 0;
				} else if (explosionTimerHelper < 8) {
					explosionTimerHelperHelper = 1;
				}else if (explosionTimerHelper < 12) {
					explosionTimerHelperHelper = 2;
				}else if (explosionTimerHelper < 16) {
					explosionTimerHelperHelper = 3;
				}else if (explosionTimerHelper < 20) {
					explosionTimerHelperHelper = 4;
				}else if (explosionTimerHelper < 24) {
					explosionTimerHelperHelper = 5;
				}else if (explosionTimerHelper < 28) {
					explosionTimerHelperHelper = 6;
				}else if (explosionTimerHelper < 32) {
					explosionTimerHelperHelper = 7;
				}
					/*switch(explosionTimerHelperHelper) {
						case 0:batch.draw(explosion0, 0, 0, 192, 192);
							break;
						case 1:batch.draw(explosion1, 0, 0, 192, 192);
							break;
						case 2:batch.draw(explosion2, 0, 0, 192, 192);
							break;
						case 3:batch.draw(explosion3, 0, 0, 192, 192);
							break;
						case 4:batch.draw(explosion4, 0, 0, 192, 192);
							break;
						case 5:batch.draw(explosion5, 0, 0, 192, 192);
							break;
						case 6:batch.draw(explosion6, 0, 0, 192, 192);
							break;
						case 7:batch.draw(explosion7, 0, 0, 192, 192);
							break;
					}
					explosionTimerHelper++;*/
				batch.end();

			}
				break;
			case 1: batch.begin();
			helper = 1;
			batch.draw(chooseMission, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				for (int i = 0; i < missionMark.length; i++) {
					batch.draw(newMissionMark, missionMark[i].x, missionMark[i].y, 100, 150);
				}
				for (int i = 0; i < missionMark.length; i++) {
					if (missionMark[i].hit(Gdx.input.getX(), (SCR_HEIGHT - Gdx.input.getY()) - 150) & Gdx.input.isTouched()) {
						screenCondition = 2;
					}
				}
			batch.end();
				break;
			case 2:
				//System.out.println(camera.position.x + " " + camera.position.y + " ");
				if (helper == 1) {
					startMissionTime = System.currentTimeMillis();
					helper = 0;
				}
				if (helper == 2) {
					if (Gdx.input.isTouched()) {
						camera.position.y -= mouseY - Gdx.input.getY();
						camera.position.x += mouseX - Gdx.input.getX();
						if (camera.position.y > SCR_HEIGHT/2) {
							camera.position.y = SCR_HEIGHT/2;
						}
						if (camera.position.y < 0) {
							camera.position.y = 0;
						}
						if (camera.position.x < SCR_WIDTH/2) {
							camera.position.x = SCR_WIDTH/2;
						}
						if (camera.position.x > SCR_WIDTH) {
							camera.position.x = SCR_WIDTH;
						}
					}
					mouseY = Gdx.input.getY();
					mouseX = Gdx.input.getX();
					camera.update();
				}
				if (helper == 0) {
					mouseX = Gdx.input.getX();
					mouseY = Gdx.input.getY();
					helper = 2;
				}



				float dt = Gdx.graphics.getDeltaTime();
				if (curTime-startMissionTime >= 200000) {
					screenCondition = 4;
					helper = 0;
				}

				/*if (buttonMoerser.hit(Gdx.input.getX(), Gdx.input.getY()) && Gdx.input.isTouched()) {
					buttonMoerserIsActive = true;
				}
				if (buttonMoerserIsActive && Gdx.input.justTouched()) {
					playAnimationExplosion(Gdx.input.getX(), Gdx.input.getY());
				}*/



				//волны
				if (chanceOfWave < 100) { //максимальное значение шанса волны
					chanceTimer += dt;
					if (chanceTimer > 2){//каждые 2 сек добавляем понемногу шанс волны и скидываем таймер шанса
						chanceOfWave += 2;
						chanceTimer = 0;
					}
				}
				waveTimer += dt;
				if (waveTimer >= gapBetweenWaves){
					waveTimer = 0;
					gapBetweenWaves = MathUtils.random(10, 20); //после первой волны волны идут в через случайное время
					int randomNum = MathUtils.random(0, 100); //определим будет ли волна вообще
					int EnemyAmount = 2; //минимум врагов
					if (randomNum >= chanceOfWave){

						randomNum = MathUtils.random(0, 2);
						switch (randomNum) {
							case 0:
								EnemyAmount = 3; //чтобы хоть как то отличалось от дефолта
								break;
							case 1:
								EnemyAmount = 5; //средняя волна
								break;
							case 2:
								EnemyAmount = 10; //полный ахтунг!!! Включай серену!!!
								alarm.play(MAIN_VOLUME * 0.5f);
								break;
							default:
								EnemyAmount = 2; //согласно условия такого быть не может. Это заготовка на всякий случай.
								break;
						}
					}

					for (int i = 0; i < enemy.length; i++) {
						if (i < EnemyAmount){
							enemy[i].statusActive = true;
						} else {
							enemy[i].statusActive = false;
						}
					}
				}
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

					if (turret[i].fire){
						if (turretFireDelay[i] <= 0){
							bullets.add(new Bullet(turret[i].barrelEndX, turret[i].barrelEndY, turret[i].eX, turret[i].eY));
							one_turret_shoot.play(MAIN_VOLUME);
							turretFireDelay[i] += 1;
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
				//коллизии
				for (Enemy curEnemy : enemy){
					boolean isCollision = checkForCollision(curEnemy);
					if (isCollision){
						hit.play(MAIN_VOLUME);
						/*if (explosionTimerHelper >=33) {
							if (explosionTimerHelper < 4) {
								explosionTimerHelperHelper = 0;
							} else if (explosionTimerHelper < 8) {
								explosionTimerHelperHelper = 1;
							}else if (explosionTimerHelper < 12) {
								explosionTimerHelperHelper = 2;
							}else if (explosionTimerHelper < 16) {
								explosionTimerHelperHelper = 3;
							}else if (explosionTimerHelper < 20) {
								explosionTimerHelperHelper = 4;
							}else if (explosionTimerHelper < 24) {
								explosionTimerHelperHelper = 5;
							}else if (explosionTimerHelper < 28) {
								explosionTimerHelperHelper = 6;
							}else if (explosionTimerHelper < 32) {
								explosionTimerHelperHelper = 7;
							}
							switch(explosionTimerHelperHelper) {
								case 0:batch.draw(explosion0, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 1:batch.draw(explosion1, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 2:batch.draw(explosion2, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 3:batch.draw(explosion3, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 4:batch.draw(explosion4, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 5:batch.draw(explosion5, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 6:batch.draw(explosion6, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
								case 7:batch.draw(explosion7, curEnemy.x+96, curEnemy.y+96, 192, 192);
									break;
							}
							explosionTimerHelper++;
						}*/
						curEnemy.spawn();
						}


				}
				//Удалим пули, которые деактивировались после коллизий
				for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
					Bullet bullet = iterator.next();
					if (bullet.deactivate){
						iterator.remove();
					}
				}
				batch.begin();
				batch.draw(bg, 0, 0);
				for (int i = 0; i < enemy.length; i++) {
					if (enemy[i].statusActive) {
						batch.draw(enemy[i].texture, enemy[i].x, enemy[i].y, enemy[i].width / 2, enemy[i].height / 2, enemy[i].width, enemy[i].height,
								1, 1, enemy[i].rotation, 0, 0, 256, 256, false, false);
					}
				}
				batch.draw(foundationImg, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				for (Bullet bullet : bullets){
					batch.draw(bullet.texture, bullet.position.x, bullet.position.y);

				}
				for (int i = 0; i < turret.length; i++) {
					batch.draw(turret[i].texture, turret[i].turX, turret[i].turY, turret[i].height/2, turret[i].height/2, turret[i].width, turret[i].height, 1, 1, turret[i].a, 0, 0, 48, 32, false, false);
					batch.draw(turret[i].targetDot, turret[i].eX, turret[i].eY);
				}
				batch.end();
				if (Base.health <= 0) {
					screenCondition = 3;
				}
				break;

			case 3:
				batch.begin();
				for (int i = 0; i < enemy.length; i++) {
					enemy[i].spawn();
					enemy[i].statusActive = false;
				}
				batch.draw(failedMissionScreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				batch.end();
				if (Gdx.input.isTouched()) {
					screenCondition = 1;
				}
				break;
			case 4:
				batch.begin();
				batch.draw(missionEndScreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				if (helper == 0) {
					contentCount += 125;
					helper =1;
					LDB.saveRecords();
				}

				for (int i = 0; i < enemy.length; i++) {
					enemy[i].spawn();
					enemy[i].statusActive = false;
				}
				batch.end();
				if (Gdx.input.isTouched()) {
					screenCondition = 1;
				}
		}
		batch.begin();
		fontMedium.draw(batch, "Res:" + contentCount, 50, SCR_HEIGHT-50);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();

		for (Enemy curEnemy : enemy){
			curEnemy.dispose();
		}

		for (Turret curTurret : turret){
			curTurret.dispose();
		}

		for (Bullet bullet : bullets){
			bullet.dispose();
		}

		//Диспозим звуки
		one_turret_shoot.dispose();
		hit.dispose();
	}
}
