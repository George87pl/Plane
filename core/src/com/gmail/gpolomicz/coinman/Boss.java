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
    private Texture youWin = new Texture("win.png");
    private Sprite sprite;
    private Sprite sprite2;
    private int shadowFrame;
    private Sound bossShadow;
    private Sound atack1Sound;
    private Sound atack2Sound;
    private Rectangle rectangle;
    private Rectangle eyeRectangle;
    private Rectangle atackRectangle;
    private boolean shadowSound = false;

    private int flyAnimationFrame;
    private int flyAnimationState;
    private int attackAnimationFrame;
    private int attackAnimationState;
    private int attack1Time;
    private int attack2Time;
    private boolean setup = true;
    private boolean forward = true;
    private boolean up;
    private Random random;
    private boolean attack1Execute;
    private boolean attack2Execute;
    private int attack2PlayerX = -1;
    private boolean attack1Ready = true;
    private boolean attack2Ready = true;
    private int attack1Break;
    private int attack2Break;
    private boolean enrage = false;
    private boolean bossKill = false;
    boolean win = false;

    private static Texture[] attack1Textures = new Texture[]{
            new Texture("lightning.png"),
            new Texture("lightning_1.png"),
            new Texture("lightning_2.png"),
            new Texture("lightning_3.png")};

    private static Texture[] attack2Textures = new Texture[]{
            new Texture("lightning1_b.png"),
            new Texture("lightning2_b.png"),
            new Texture("lightning3_b.png"),
            new Texture("lightning4_b.png")};

    private static Texture[] closingEyeTextures = new Texture[]{
            new Texture("eye1.png"),
            new Texture("eye2.png")};

    private Texture bossEye = new Texture("boss_eye.png");

    private static Texture[] flyTextures = new Texture[]{
            new Texture("boss1.png"),
            new Texture("boss2.png"),
            new Texture("boss3.png"),
            new Texture("boss4.png")};

    private static Texture[] deathTextures = new Texture[]{
            new Texture("frame-1.png"),
            new Texture("frame-2.png")};

    Boss(int lives, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTextures, deathTextures, lives, speed, animationSpeed, positionX, positionY);
        bossShadow = Gdx.audio.newSound(Gdx.files.internal("bossShadow.wav"));
        atack1Sound = Gdx.audio.newSound(Gdx.files.internal("atack1sound.wav"));
        atack2Sound = Gdx.audio.newSound(Gdx.files.internal("attack2sound.wav"));
        Sound killSound = Gdx.audio.newSound(Gdx.files.internal("bossfall.wav"));
        super.setKillSound(killSound);
        super.setDeathTimeSet(500);
        sprite = new Sprite(attack1Textures[0]);
        sprite2 = new Sprite(attack2Textures[0]);
        random = new Random();
        attack1Time = 150;
        attack2Time = 300;
        attack1Break = 0;
        attack2Break = 0;
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
                if (super.getPositionY() > (-1 * (flyTextures[0].getHeight() / 2)) + 150) {
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
        if (!super.isDead()) {
            if (super.isHit()) {
                if (super.getHitTime() > 0) {
                    if (super.getHitTime() > 20 || (super.getHitTime() < 15 && super.getHitTime() > 10) || super.getHitTime() < 5) {
                        batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                        batch.setColor(Color.RED);
                        batch.draw(bossEye, getPositionX() + 81, getPositionY() + 172);
                        batch.setColor(Color.WHITE);
                    } else {
                        batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                    }
                    super.setHitTime(super.getHitTime() - 1);
                } else {
                    super.setHit(false);
                    batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                }

            } else {
                batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
            }

            if(super.getLives() < 25) {
                enrage = true;
                if (super.getHitTime() > 20 || (super.getHitTime() < 15 && super.getHitTime() > 10) || super.getHitTime() < 5) {
                    batch.draw(flyTextures[flyAnimationState], getPositionX(), getPositionY());
                    batch.setColor(Color.RED);
                    batch.draw(bossEye, getPositionX() + 81, getPositionY() + 172);
                    batch.setColor(Color.WHITE);
                } else {
                    batch.setColor(Color.WHITE);
                    batch.draw(bossEye, getPositionX() + 81, getPositionY() + 172);
                    batch.setColor(Color.RED);
                    batch.setColor(Color.WHITE);
                }
                eyeRectangle = new Rectangle(getPositionX() + 81, getPositionY() + 192, bossEye.getWidth(), bossEye.getHeight() - 40);   // +- 20
            }
            else {
                if (attack2Time > 0 && attack2Time < 299) {
                    if (attack2Time > 294) {
                        batch.draw(closingEyeTextures[0], getPositionX() + 73, getPositionY() + 164);
                        eyeRectangle = new Rectangle(getPositionX() + 81, getPositionY() + 192, bossEye.getWidth(), bossEye.getHeight() - 40);   // +- 20
                    } else {
                        batch.draw(closingEyeTextures[1], getPositionX() + 73, getPositionY() + 164);
                        eyeRectangle = new Rectangle(-100, -100, 0, 0);
                    }
                } else {
                    eyeRectangle = new Rectangle(getPositionX() + 81, getPositionY() + 192, bossEye.getWidth(), bossEye.getHeight() - 40);   // +- 20
                }
            }

            rectangle = new Rectangle(getPositionX() + 81, getPositionY(), flyTextures[0].getWidth(), flyTextures[0].getHeight());
            super.setRectangle(rectangle);

        } else {
            if (getDeathTime() > 0) {
                death();
                batch.draw(deathTextures[getDeathAnimationState()], getPositionX(), getPositionY());
                setDeathTime(getDeathTime()-1);
                bossKill = true;
            }
            if(getDeathTime() == 0) {
                win = true;
            }
        }
    }

    void attack1(SpriteBatch batch) {

        if (!bossKill) {
            if (attack1Time > 0) {
                attack1Execute = true;
                attack1Ready = false;

                attackAnimation();

                if (attack1Time > 80) {

                    if (attack1Time == 120) {
                        sprite.flip(false, true);
                    }

                    if (attack1Time > 130 || (attack1Time < 110 && attack1Time > 90)) {
                        sprite.setPosition(getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 125);
                        sprite.draw(batch);
                    }

                    if (attack1Time == 100) {
                        atack1Sound.play();
                    }
                } else {
                    batch.draw(attack1Textures[attackAnimationState], getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 125);
                    atackRectangle = new Rectangle(getPositionX() + 120 - beforeAtack.getWidth(), getPositionY() + 175, attack1Textures[0].getWidth(), attack1Textures[0].getHeight() - 100);
                    setAtackRectangle(atackRectangle);
                }

                attack1Time--;
            } else {
                attack1Time = 150;
                attack1Execute = false;
                attackAnimationFrame = 0;
                attack1Ready = true;
                setAtackRectangle(null);
                attack1Break = random.nextInt(150) + 50;
            }
        }
    }
    void attack2(SpriteBatch batch, int playerX) {

        if(!bossKill) {
            if (attack2Time > 0) {
                attack2Execute = true;
                attack2Ready = false;

                attackAnimation();

                if (attack2PlayerX == -1) {
                    attack2PlayerX = playerX;
                }

                if (attack2Time > 80) {

                    if (attack2Time == 170 || attack2Time == 130 || attack2Time == 90) {
                        sprite2.flip(false, true);
                    }

                    if (attack2Time < 200) {
                        if (attack2Time > 180 || (attack2Time < 160 && attack2Time > 140) || (attack2Time < 120 && attack2Time > 100)) {
                            sprite2.setPosition(attack2PlayerX - 22, 0);
                            sprite2.draw(batch);
                        }
                    }

                    if (attack2Time == 100) {
                        atack2Sound.play();
                    }

                } else {
                    batch.draw(attack2Textures[attackAnimationState], attack2PlayerX - 22, 0);
                    atackRectangle = new Rectangle(attack2PlayerX + 28, 0, attack2Textures[0].getWidth() - 100, attack2Textures[0].getHeight());
                    setAtackRectangle(atackRectangle);
                }

                attack2Time--;
            } else {
                attack2Time = 300;
                attack2Execute = false;
                attackAnimationFrame = 0;
                attack2PlayerX = -1;
                attack2Ready = true;
                setAtackRectangle(null);
                attack2Break = random.nextInt(150) + 50;
            }
        }
    }

    private void attackAnimation() {
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
    }

    public boolean isAttack1Execute() {
        return attack1Execute;
    }

    public void setAttack1Execute(boolean attack1Execute) {
        this.attack1Execute = attack1Execute;
    }

    public boolean isAttack2Execute() {
        return attack2Execute;
    }

    public void setAttack2Execute(boolean attack2Execute) {
        this.attack2Execute = attack2Execute;
    }

    public Rectangle getEyeRectangle() {
        return eyeRectangle;
    }

    public boolean isAttack1Ready() {
        return attack1Ready;
    }

    public boolean isAttack2Ready() {
        return attack2Ready;
    }

    public int getAttack1Break() {
        return attack1Break;
    }

    public void setAttack1Break(int attackBreak) {
        this.attack1Break = attackBreak;
    }

    public int getAttack2Break() {
        return attack2Break;
    }

    public void setAttack2Break(int attackBreak) {
        this.attack2Break = attackBreak;
    }

    public boolean isEnrage() {
        return enrage;
    }
}
