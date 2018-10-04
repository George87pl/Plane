package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Random;

public class Plane extends ApplicationAdapter {
    private static final String TAG = "GPDEB";

    private static final int CLOUDS_NUMBER = 300; //IF LESS THAN MORE CLOUDS
    private static final int MONSTERS_NUMBER = 100; //IF LESS THAN MORE MONSTERS

    private SpriteBatch batch;
    private Texture background;
    private Texture logo;
    private Texture level1;
    private Texture level2;
    private Texture boss;
    private Texture gameOver;
    private Stage stage;


    private Player player;
    private Random random;

    private ArrayList<Clouds> clouds = new ArrayList<Clouds>();
    private ArrayList<Monster> monster = new ArrayList<Monster>();

    private int cloudSpawn;
    private int monsterSpawn;
    private int gameState;
    private int level;
    private double gameTime = 0;   //6400
    private BitmapFont font;
    private int score;
    private ImageButton repeatButton;
    private Music bossFight;


    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        logo = new Texture("logo.png");
        level1 = new Texture("level1.png");
        level2 = new Texture("level2.png");
        boss = new Texture("boss.png");
        gameOver = new Texture("game_over.png");
        level = 1;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Skin mySkin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));
        repeatButton = new ImageButton(mySkin);

        bossFight = Gdx.audio.newMusic(Gdx.files.internal("bossFight.mp3"));
        bossFight.setLooping(true);

        player = new Player();
        random = new Random();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5);
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1 && gameTime > 3000 && level == 1) {
            level = 2;
            monster.clear();
        }

        if (gameState == 1 && gameTime > 6700 && level == 2) {
            level = 3;
            bossFight.play();
        }

        if (gameState == 1) {
            gameTime++;
            cloudsSpawn();
            if (gameTime > 100) {
                monstersSpawn();
            }
            player.fly();
            playerFire();


        } else if (gameState == 0) { //START GAME
            repeatButton.remove();
            monster.clear();
            player.setPlaneX(Gdx.graphics.getWidth() / 2 - player.getPlaneTexture(0).getWidth() / 2);
            player.setPlaneY(Gdx.graphics.getHeight() / 2 - player.getPlaneTexture(0).getHeight() / 2);
            score = 0;
            level = 1;

            batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() - logo.getHeight() - 50);
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 4) {        //GAME OVER
            player.death(batch);
            gameTime = 0;
            player.setLive(3);
            bossFight.stop();
            font.draw(batch, String.valueOf(score), 50, 100);
            batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
            batch.draw(player.getLives()[player.getLive()], 10, Gdx.graphics.getHeight() - 42);
            batch.draw(logo, 100, 100);

            repeatButton.setSize(130, 135);
            repeatButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("repeat.png"))));
            repeatButton.setPosition(Gdx.graphics.getWidth() - 140, 10);
            repeatButton.addListener(new InputListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    gameState = 0;
                }

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });
            stage.addActor(repeatButton);
            stage.act();
            stage.draw();
        }


        font.draw(batch, String.valueOf(score), 50, 100);
        batch.draw(player.getLives()[player.getLive()], 10, Gdx.graphics.getHeight() - 42);

        if (gameTime < 300 && gameState == 1 && level == 1) {
            batch.draw(level1, Gdx.graphics.getWidth() / 2 - level1.getWidth() / 2, Gdx.graphics.getHeight() - level1.getHeight() - 50);
        } else if (gameTime < 3500 && gameState == 1 && level == 2) {
            batch.draw(level2, Gdx.graphics.getWidth() / 2 - level2.getWidth() / 2, Gdx.graphics.getHeight() - level2.getHeight() - 50);
        } else if (gameTime < 7100 && gameState == 1 && level == 3) {
            batch.draw(boss, Gdx.graphics.getWidth() / 2 - boss.getWidth() / 2, Gdx.graphics.getHeight() - boss.getHeight() - 50);
        }

        if (gameState == 1 && gameTime > 6510) {
            bossFight();
        }

        if (gameState == 1 && gameTime > 6800) {
            batch.draw(monster.get(0).hpbar1, Gdx.graphics.getWidth() / 2 - 300, 50);
            batch.draw(monster.get(0).hpbar2, Gdx.graphics.getWidth() / 2 - 300, 51, monster.get(0).getLives() * 6, 40);
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

            if ((gameTime > 2600 && gameTime < 3500) || (gameTime > 6100 && gameTime < 7000)) {
                //Wait
            } else {
                int randMonster = random.nextInt(100) + (int) (gameTime / 1000);
//                Gdx.app.log("GPDEB", "Random: " + String.valueOf(randMonster) + " gametime: " + String.valueOf((int) gameTime / 100) + " level: " +level);

                if (randMonster > 20) {
                    if (level <= 2) {
                        monster.add(new BrownBird(2, 5, 3, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
                    }
                    if (randMonster > 40 && gameTime > 1000) {
                        if (level <= 2) {
                            monster.add(new RedFly(1, 3, 2, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
                        }
                    }
                    if (level == 2) {
                        if (randMonster > 60) {
                            monster.add(new BlueBird(3, 2, 8, Gdx.graphics.getWidth(), (int) (random.nextFloat() * Gdx.graphics.getHeight())));
                        }
                        if (randMonster > 80 && gameTime > 4000) {
                            monster.add(new LandMonster(1, 2, 8, Gdx.graphics.getWidth(), 0));
                        }
                    }
                }

                monsterSpawn = 0;
            }
        }

        if (gameTime == 6500) {
            monster.clear();
            Gdx.app.log("GPDEB", "BOSS SPAWNED");
            monster.add(new Boss(100, 1, 8, Gdx.graphics.getWidth() + 250, 100));
        }

        if (monster.size() > 0 && gameState == 1) {
            for (int i = 0; i < monster.size(); i++) {

                if (!monster.get(i).isDead()) {
                    monster.get(i).fly();
                    if (monster.get(i) instanceof LandMonster) {
                        ((LandMonster) monster.get(i)).jump();
                    }

                    monster.get(i).draw(batch);

                    if (Intersector.overlaps(player.getPlaneRectangle(), monster.get(i).getRectangle())) {

                        player.setLive(player.getLive() - 1);
                        if (player.getLive() <= 0) {
                            player.getKillSound().play();
                            monster.remove(i);
                            gameState = 4;
                        } else {
                            player.getHit();
                        }
                    }

                    if (level == 3 && gameState == 1) {
                        if (monster.get(i).getAtackRectangle() != null) {
                            if ((Intersector.overlaps(player.getPlaneRectangle(), monster.get(i).getAtackRectangle()))) {
                                player.setLive(player.getLive() - 1);
                                if (player.getLive() <= 0) {
                                    player.getKillSound().play();
                                    monster.remove(i);
                                    gameState = 4;
                                } else {
                                    player.getHit();
                                }
                            }
                        }
                    }

                } else {
                    monster.get(i).draw(batch);
                }
            }
        }
    }

    private void playerFire() {
        if ((Gdx.input.isTouched() || player.getShootDelay() > 0) && gameTime > 50) {
            if (player.getShootDelay() == 0) {
                player.setShootDelay(30);
                player.shoot();

                for (int i = 0; i < monster.size(); i++) {
                    if (Intersector.overlaps(player.getShootRectangle(), monster.get(i).getRectangle())) {
                        score = score + monster.get(i).getHit();
                    }
                }

            } else {
                player.setShootDelay(player.getShootDelay() - 1);
            }
            player.shootDraw(batch);

        } else {
            player.flyDraw(batch);
        }
    }

    private void bossFight() {

        if (gameTime < 6600) {
            ((Boss) monster.get(0)).makeShadow(batch);
        }

        if (gameTime > 7000) {
            ((Boss) monster.get(0)).attack1(batch);
        }

    }

    @Override
    public void dispose() {

    }
}
