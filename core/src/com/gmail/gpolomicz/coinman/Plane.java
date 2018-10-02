package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;


public class Plane extends ApplicationAdapter {
    private static final String TAG = "GPDEB";

    private SpriteBatch batch;
    private Texture background;

    private Texture[] cloud;
    private ArrayList<Integer> cloudXs = new ArrayList<Integer>();
    private ArrayList<Integer> cloudYs = new ArrayList<Integer>();
    private int cloudCount;

    private Texture[] plane;
    private Texture planeDead;
    private int flyAnimSpeed;
    private int flyAnimState;
    private int planeX;
    private int planeY;
    private Texture[] shoot;
    private int shootAnimSpeed;
    private int shootAnimState;
    private Sound shootSound;
    private int shootDelay;
    private Rectangle planeRectangle;

    private Texture[] bird1;
    private Texture birdIconDeath;
    private ArrayList<Integer> bird1Xs = new ArrayList<Integer>();
    private ArrayList<Integer> bird1Ys = new ArrayList<Integer>();
    private ArrayList<Integer> bird1DeadXs = new ArrayList<Integer>();
    private ArrayList<Integer> bird1DeadYs = new ArrayList<Integer>();
    private int birdDeath;
    private int bird1Count;
    private int bird1AnimSpeed;
    private int bird1AnimState;
    private ArrayList<Rectangle> bird1Rectagles = new ArrayList<Rectangle>();

    private Texture[] bird2;
    private ArrayList<Integer> bird2Xs = new ArrayList<Integer>();
    private ArrayList<Integer> bird2Ys = new ArrayList<Integer>();
    private int bird2Count;
    private int bird2AnimSpeed;
    private int bird2AnimState;
    private boolean downUp;
    private ArrayList<Rectangle> bird2Rectagles = new ArrayList<Rectangle>();

    private Random random;
    private int gameState = 1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");

        cloud = new Texture[3];
        cloud[0] = new Texture("cloud1.png");
        cloud[1] = new Texture("cloud2.png");
        cloud[2] = new Texture("cloud3.png");

        plane = new Texture[2];
        plane[0] = new Texture("fly1.png");
        plane[1] = new Texture("fly2.png");
        planeDead = new Texture("dead.png");
        planeX = Gdx.graphics.getWidth() / 2 - (plane[flyAnimState].getWidth() / 2);
        planeY = Gdx.graphics.getHeight() / 2 - (plane[flyAnimState].getHeight() / 2);
        shoot = new Texture[5];
        shoot[0] = new Texture("shoot1.png");
        shoot[1] = new Texture("shoot2.png");
        shoot[2] = new Texture("shoot3.png");
        shoot[3] = new Texture("shoot4.png");
        shoot[4] = new Texture("shoot5.png");
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
        shootDelay = 40;

        bird1 = new Texture[8];
        bird1[0] = new Texture("bird1_1.png");
        bird1[1] = new Texture("bird1_2.png");
        bird1[2] = new Texture("bird1_3.png");
        bird1[3] = new Texture("bird1_4.png");
        bird1[4] = new Texture("bird1_5.png");
        bird1[5] = new Texture("bird1_6.png");
        bird1[6] = new Texture("bird1_7.png");
        bird1[7] = new Texture("bird1_8.png");

        birdIconDeath = new Texture("bird1death.png");

        bird2 = new Texture[2];
        bird2[0] = new Texture("bird2_1.png");
        bird2[1] = new Texture("bird2_2.png");
        downUp = true;

        random = new Random();
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {
            // CLOUDS
            if (cloudCount < random.nextInt(10000) + 500) {
                cloudCount++;
            } else {
                makeCloud();
                cloudCount = 0;
            }
            for (int i = 0; i < cloudXs.size(); i++) {
                if (i % 3 == 0) {
                    batch.draw(cloud[0], cloudXs.get(i), cloudYs.get(i));
                } else if (i % 3 == 1) {
                    batch.draw(cloud[1], cloudXs.get(i), cloudYs.get(i));
                } else if (i % 3 == 2) {
                    batch.draw(cloud[2], cloudXs.get(i), cloudYs.get(i));
                }
                cloudXs.set(i, cloudXs.get(i) - 1);
            }
            // CLOUDS END

            //PLANE
            if (flyAnimSpeed < 4) {
                flyAnimSpeed++;
            } else {
                flyAnimSpeed = 0;
                if (flyAnimState < 1) {
                    flyAnimState++;
                } else {
                    flyAnimState = 0;
                }
            }

            if (shootAnimSpeed < 10) {
                shootAnimSpeed++;
            } else {
                shootAnimSpeed = 0;
                if (shootAnimState < 4) {
                    shootAnimState++;
                } else {
                    shootAnimState = 0;
                }
            }

            planeRectangle = new Rectangle(planeX + 50, planeY + 50, plane[flyAnimState].getWidth() - 100, plane[flyAnimState].getHeight() - 100);

            if (Gdx.input.getAccelerometerX() > 0.5) {
                planeY = planeY - ((int) Gdx.input.getAccelerometerX() + 2);
            } else if (Gdx.input.getAccelerometerX() < -0.5) {
                planeY = planeY - ((int) Gdx.input.getAccelerometerX() - 2);
            }

            if (Gdx.input.getAccelerometerY() > 0.5) {
                planeX = planeX + ((int) Gdx.input.getAccelerometerY() + 2);
            } else if (Gdx.input.getAccelerometerY() < -0.5) {
                planeX = planeX + ((int) Gdx.input.getAccelerometerY() - 2);
            }

            if (planeX + plane[flyAnimState].getWidth() >= Gdx.graphics.getWidth()) {
                planeX = Gdx.graphics.getWidth() - plane[flyAnimState].getWidth();
            }
            if (planeX <= 0) {
                planeX = 0;
            }
            if (planeY + plane[flyAnimState].getHeight() >= Gdx.graphics.getHeight()) {
                planeY = Gdx.graphics.getHeight() - plane[flyAnimState].getHeight();
            }
            if (planeY <= 0) {
                planeY = 0;
            }
            //PLANE END

            //BIRDS
            if (bird1AnimSpeed < 4) {
                bird1AnimSpeed++;
            } else {
                bird1AnimSpeed = 0;
                if (bird1AnimState < 1) {
                    bird1AnimState++;
                } else {
                    bird1AnimState = 0;
                }
            }

            if (bird1Count < random.nextInt(700) + 300) {
                bird1Count++;
            } else {
                makeBird1();
                bird1Count = 0;
            }

            bird1Rectagles.clear();
            for (int i = 0; i < bird1Xs.size(); i++) {
                batch.draw(bird1[bird1AnimState], bird1Xs.get(i), bird1Ys.get(i));
                bird1Xs.set(i, bird1Xs.get(i) - 5);
                bird1Rectagles.add(new Rectangle(bird1Xs.get(i), bird1Ys.get(i), bird1[bird1AnimState].getWidth(), bird1[bird1AnimState].getWidth()));
            }

            if (bird2AnimSpeed < 3) {
                bird2AnimSpeed++;
            } else {
                bird2AnimSpeed = 0;
                if (bird2AnimState < 1) {
                    bird2AnimState++;
                } else {
                    bird2AnimState = 0;
                }
            }

            if (bird2Count < random.nextInt(1000) + 1000) {
                bird2Count++;
            } else {
                makeBird2();
                bird2Count = 0;
            }

            bird2Rectagles.clear();
            for (int i = 0; i < bird2Xs.size(); i++) {
                batch.draw(bird2[bird2AnimState], bird2Xs.get(i), bird2Ys.get(i));
                if (downUp) {
                    bird2Ys.set(i, bird2Ys.get(i) + 2);
                    if (bird2Ys.get(i) >= Gdx.graphics.getHeight() - bird2[bird2AnimState].getHeight()) {
                        downUp = !downUp;
                    }
                } else {
                    bird2Ys.set(i, bird2Ys.get(i) - 2);
                    if (bird2Ys.get(i) <= 0) {
                        downUp = !downUp;
                    }
                }

                bird2Xs.set(i, bird2Xs.get(i) - 2);
                bird2Rectagles.add(new Rectangle(bird2Xs.get(i), bird2Ys.get(i), bird2[bird1AnimState].getWidth(), bird2[bird1AnimState].getWidth()));
            }

            //BIRDS END
        } else if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 2) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
                planeY = Gdx.graphics.getHeight() / 2;
                bird1Ys.clear();
                bird1Xs.clear();
                bird1Rectagles.clear();
                bird1Count = 0;
                bird2Ys.clear();
                bird2Xs.clear();
                bird2Rectagles.clear();
                bird2Count = 0;
            }
        }

        if (gameState == 2) {
            batch.draw(planeDead, planeX, planeY);

        } else {
            if (Gdx.input.isTouched()) {
                batch.draw(shoot[flyAnimState], planeX, planeY);

                for (int i = 0; i < bird1Ys.size(); i++) {
                    if (planeY > bird1Ys.get(i)) {
                        if (planeY - bird1Ys.get(i) < 50) {
                            bird1DeadXs.add(bird1Xs.get(i));
                            bird1DeadYs.add(bird1Ys.get(i));
                            bird1Ys.remove(i);
                            bird1Xs.remove(i);
                            birdDeath = 1000;
                        }
                    } else if (bird1Ys.get(i) - planeY < 50) {
                        bird1DeadXs.add(bird1Xs.get(i));
                        bird1DeadYs.add(bird1Ys.get(i));
                        bird1Ys.remove(i);
                        bird1Xs.remove(i);
                        birdDeath = 100;
                    }
                }

                if (shootDelay == 40) {
                    shootSound.play(0.3f);
                    shootDelay = 0;
                } else {
                    shootDelay++;
                }
            } else {
                batch.draw(plane[flyAnimState], planeX, planeY);
                shootDelay = 40;
            }

            Gdx.app.log(TAG, String.valueOf(birdDeath));

            if(birdDeath > 0) {
                for(int i=0; i<bird1DeadXs.size(); i++) {
                    batch.draw(birdIconDeath, bird1DeadXs.get(i), bird1DeadYs.get(i));
                }
                birdDeath--;
            } else {
                if(bird1DeadXs.size() > 0) {
                    bird1DeadXs.remove(0);
                    bird1DeadYs.remove(0);
                }
            }

            for (int i = 0; i < bird1Rectagles.size(); i++) {
                if (Intersector.overlaps(planeRectangle, bird1Rectagles.get(i))) {

                    bird1Rectagles.remove(i);
                    bird1Xs.remove(i);
                    bird1Ys.remove(i);
                    gameState = 2;
                    break;
                }
            }
        }


        for (int i = 0; i < bird2Rectagles.size(); i++) {
            if (Intersector.overlaps(planeRectangle, bird2Rectagles.get(i))) {

                bird2Rectagles.remove(i);
                bird2Xs.remove(i);
                bird2Ys.remove(i);
                gameState = 2;
                break;
            }
        }

        batch.end();
    }

    private void makeCloud() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        cloudYs.add((int) height);
        cloudXs.add(Gdx.graphics.getWidth());
    }

    private void makeBird1() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bird1Ys.add((int) height);
        bird1Xs.add(Gdx.graphics.getWidth());
    }

    private void makeBird2() {
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bird2Ys.add((int) height);
        bird2Xs.add(Gdx.graphics.getWidth());
    }

    @Override
    public void dispose() {

    }
}
