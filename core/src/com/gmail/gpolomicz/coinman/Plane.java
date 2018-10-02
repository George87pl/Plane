package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Plane extends ApplicationAdapter {
    private static final String TAG = "GPDEB";

    private static final int CLOUDS_NUMBER = 500; //IF LESS THAN MORE CLOUDS
    private static final int MONSTERS_NUMBER = 100; //IF LESS THAN MORE MONSTERS

    private SpriteBatch batch;
    private Texture background;
    private Player player;
    private Random random;

    private ArrayList<Clouds> clouds = new ArrayList<Clouds>();
    private ArrayList<BrownBird> brownBird = new ArrayList<BrownBird>();
    private ArrayList<RedFly> redFly = new ArrayList<RedFly>();

    private int cloudSpawn;
    private int monsterSpawn;
    private int gameState = 1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        player = new Player();
        random = new Random();
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            cloudsSpawn();
            monstersSpawn();
            player.fly(batch);

        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }
        batch.end();
    }

    private void cloudsSpawn() {
        if (cloudSpawn < CLOUDS_NUMBER) {
            cloudSpawn++;
        } else {
            clouds.add(new Clouds(1, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
            cloudSpawn = 0;
        }
        if (clouds.size() > 0) {
            for (int i = 0; i < clouds.size(); i++) {
                clouds.get(i).fly();
                clouds.get(i).draw(batch);
            }
        }
    }

    private void monstersSpawn() {
        if (monsterSpawn < MONSTERS_NUMBER) {
            monsterSpawn++;
        } else {
            brownBird.add(new BrownBird(4, 3, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
            redFly.add(new RedFly(3, 2, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
            monsterSpawn = 0;
        }
        if (brownBird.size() > 0) {
            for (int i = 0; i < brownBird.size(); i++) {
                brownBird.get(i).fly();
                brownBird.get(i).draw(batch);
            }
        }
        if (redFly.size() > 0) {
            for (int i = 0; i < redFly.size(); i++) {
                redFly.get(i).fly();
                redFly.get(i).draw(batch);
            }
        }
    }

    @Override
    public void dispose() {

    }
}
