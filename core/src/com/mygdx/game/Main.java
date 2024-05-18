package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;



public class Main extends ApplicationAdapter {
	LocalDataBase LDB = new LocalDataBase();
	//общая громкость звука
	private static final float MAIN_VOLUME = 0.2f;
	public static final float SCR_HEIGHT = 900, SCR_WIDTH = 1600;
	public static final int TURRETT_COUNT = 4;
	long curTime = System.currentTimeMillis();
	long startMissionTime;
	SpriteBatch batch;

	OrthographicCamera camera;

	public static int contentCount;


	public static Enemy[] enemy = new Enemy[10];
	Turret[] turret = new Turret[TURRETT_COUNT];

	public static BitmapFont fontMedium;

	public static int screenCondition = 0;

	ArrayList<Bullet> bullets;

	private float[] turretFireDelay = new float[TURRETT_COUNT];
	private Sound one_turret_shut;
	private Sound hit;
	private Sound alarm;
	private Texture bg;
	Texture foundationImg;
	Texture missionEndScreen;

	//для волн противников
	private float chanceTimer = 0;
	private float waveTimer = 0;
	private float gapBetweenWaves;
	private int chanceOfWave = 0;
	int helper;

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

	Button buttonMainMenuStart;
	Button buttonMainMenuAbout;
	Button buttonMainMenuExit;
	Button buttonAboutExit;

	NewMissionMark[] missionMark = new NewMissionMark[3];

	@Override
	public void create () {
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
		buttonMainMenuAbout = new Button(75, 350, 200, 50);
		buttonAboutExit = new Button(75, 500, 200, 50);

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
		one_turret_shut = Gdx.audio.newSound(Gdx.files.internal("one_turret_shut.ogg"));
		hit = Gdx.audio.newSound(Gdx.files.internal("hit.ogg"));
		alarm = Gdx.audio.newSound(Gdx.files.internal("alarm.ogg"));
	}

	@Override
	public void render () {
		curTime = System.currentTimeMillis();
		LDB.loadRecords();
		batch.setProjectionMatrix(camera.combined);
		switch (screenCondition) {
			case 0: if (buttonMainMenuStart.hit(Gdx.input.getX(), Gdx.input.getY()) & Gdx.input.isTouched()) {
				screenCondition = 1;
			} else { batch.begin();
				//ScreenUtils.clear(1, 1, 1, 1);
				System.out.println(Gdx.input.getX() + " " +  Gdx.input.getY() + " " + Gdx.input.isTouched());
				batch.draw(mainMenuScreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				fontMedium.draw(batch, "Play", 75, SCR_HEIGHT-200);
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
				if (helper == 1) {
					startMissionTime = System.currentTimeMillis();
					helper = 0;
				}
				float dt = Gdx.graphics.getDeltaTime();
				if (curTime-startMissionTime >= 300000) {
					screenCondition = 1;
				}



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
							one_turret_shut.play(MAIN_VOLUME);
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
				//коллизии
				for (Enemy curEnemy : enemy){
					boolean isCollision = checkForCollision(curEnemy);
					if (isCollision){
						curEnemy.spawn();
						hit.play(MAIN_VOLUME);
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
			/*case 4:
				batch.begin();
				batch.draw(missionEndScreen, 0, 0, SCR_WIDTH, SCR_HEIGHT);
				if (helper == 0) {
					contentCount += 125;
					helper =1;
				}

				for (int i = 0; i < enemy.length; i++) {
					enemy[i].spawn();
					enemy[i].statusActive = false;
				}
				batch.end();
				if (Gdx.input.isTouched()) {
					screenCondition = 1;
				}*/
				LDB.saveRecords();
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
		one_turret_shut.dispose();
		hit.dispose();
	}
}
