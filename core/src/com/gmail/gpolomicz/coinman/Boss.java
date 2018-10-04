package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

class Boss extends Monster {

    private Texture shadow = new Texture("bossShadow.png");
    private Texture beforeAtack = new Texture("beforeAtack.png");
    private Sprite sprite;
    private int shadowFrame;
    private Sound bossShadow;
    private Sound atack1Sound;
    private Rectangle rectangle;
    private Rectangle atackRectangle;
    private boolean shadowSound = false;

    private int flyAnimationFrame;
    private int flyAnimationState;
    private int attackAnimationFrame;
    private int attackAnimationState;
    private int atack1Time;
    private boolean setup = true;
    private boolean forward = true;
    private boolean up;
    private Random random;
    private int deathTime;

    private static Texture[] attack1Textures = new Texture[]{
            new Texture("lightning.png"),
            new Texture("lightning_1.png"),
            new Texture("lightning_2.png"),
            new Texture("lightning_3.png")};

    private Texture bossEye = new Texture("boss_eye.png");

    private static Texture[] flyTextures = new Texture[]{
            new Texture("boss1.png"),
            new Texture("boss2.png"),
            new Texture("boss3.png"),
            new Texture("boss4.png")};

    static private Texture[] deathTextures = new Texture[]{new Texture("bird1death.png")};

    Boss(int lives, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTextures, deathTextures, lives, speed, animationSpeed, positionX, positionY);
        bossShadow = Gdx.audio.newSound(Gdx.files.internal("bossShadow.wav"));
        atack1Sound = Gdx.audio.newSound(Gdx.files.internal("atack1sound.wav"));
        sprite = new Sprite(attack1Textures[0]);
        random = new Random();
    }

    void makeShadow(SpriteBatch batch) {
        if (!shadowSound) {
            bossShadow.play();
            shadowSound = true;
        }
        shadowFrame += 50;
        batch.draw(shadow, 0 - shadow.getWidth() + shadowFrame, Gdx.graphics.getHeight() / 2 - shadow.getHeight() / 2);
    }

    @Override
    void fly() {
        if (flyAnimationFrame < 7) {
            flyAnimationFrame++;
        } else {
            flyAnimationFrame = 0;
            if (flyAnimationState < flyTextures.length - 1) {
                flyAnimationState++;
            } else {
                flyAnimationState = 0;
            }
        }

        if (setup) {
            if (super.getPositionX() > Gdx.graphics.getWidth() / 2) {
                super.setPositionX(getPositionX() - 1);
            } else {
                setup = false;
                forward = false;
                up = random.nextBoolean();
            }
        } else {
            if (!forward) {
                if (super.getPositionX() < Gdx.graphics.getWidth() - flyTextures[0].getWidth() / 2) {
                    super.setPositionX(getPositionX() + 1);
                } else {
                    forward = true;
                }
            } else {
                if (super.getPositionX() > Gdx.graphics.getWidth() / 2) {
                    super.setPositionX(getPositionX() - 1);
                } else {
                    forward = false;
                }
            }

            if (!up) {
                if (super.getPositionY() > (-1 *(flyTextures[0].getHeight() / 2)) + 150) {
                    super.setPositionY(getPositionY() - 1);
                } else {
                    up = true;
                }
            } else {
                if (super.getPositionY() < (Gdx.graphics.getHeight() - flyTextures[0].getHeight() / 2) - 100) {
                    super.setPositionY(getPositionY() + 1);
                } else {
                    up = false;
                }
            }
        }
        super.setFlyAnimationState(flyAnimationState);
    }

    @Override
    void draw(SpriteBatch batch) {
        if(!super.isDead()) {
            if(super.isHit()) {
                if(super.getHitTime() > 0) {
                    if(super.getHitTime() > 20 || (super.getHitTime() < 15 && super.getHitTime() > 10) || super.getHitTime() < 5) {
                        batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                        batch.setColor(Color.RED);
                        batch.draw(bossEye, getPositionX()+81, getPositionY()+172);
                        batch.setColor(Color.WHITE);
                    } else {
                        batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                    }
                    super.setHitTime(super.getHitTime()-1);
                } else {
                    super.setHit(false);
                    batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                }

            }else {
                batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
            }
            rectangle = new Rectangle(getPositionX()+81, getPositionY()+172, bossEye.getWidth(), bossEye.getHeight());
            super.setRectangle(rectangle);
        } else {
            if(deathTime > 0 ) {
                death();
                batch.draw(deathTextures[getDeathAnimationState()], getPositionX(), getPositionY());
                deathTime--;
            }
        }
    }

    void attack1(SpriteBatch batch) {

        if (atack1Time > 0) {
            if (attackAnimationFrame < 5) {
                attackAnimationFrame++;
            } else {
                attackAnimationFrame = 0;
                if (attackAnimationState < attack1Textures.length - 1) {
                    attackAnimationState++;
                } else {
                    attackAnimationState = 0;
                }
            }

            if (atack1Time > 100) {
                if (atack1Time > 180 || (atack1Time < 160 && atack1Time > 140) || atack1Time < 120) {
                    sprite.setPosition(getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 125);

                    if (atack1Time == 159) {
                        sprite.flip(false, true);
                    }
                    sprite.draw(batch);
                }
                if (atack1Time == 120) {
                    atack1Sound.play();
                }
            } else {
                batch.draw(attack1Textures[attackAnimationState], getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 125);
                atackRectangle = new Rectangle(getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 175, attack1Textures[0].getWidth(), attack1Textures[0].getHeight() - 100);
                setAtackRectangle(atackRectangle);
            }

            atack1Time--;
        } else {
            atack1Time = 200;
            setAtackRectangle(null);
        }
    }

}
