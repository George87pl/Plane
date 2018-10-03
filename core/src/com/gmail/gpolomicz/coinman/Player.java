package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class Player {

    private static final int FLY_ANIMATION_SPEED = 5;
    private static final int SHOOT_ANIMATION_SPEED = 1;

    private Texture[] plane = new Texture[]{new Texture("fly1.png"), new Texture("fly2.png")};
    private Texture planeDead = planeDead = new Texture("dead.png");
    private Texture[] shoot = new Texture[]{
            new Texture("shoot1.png"),
            new Texture("shoot2.png"),
            new Texture("shoot3.png"),
            new Texture("shoot4.png"),
            new Texture("shoot5.png")};

    private int flyAnimationFrame;
    private int flyAnimationState;
    private int shootAnimationFrame;
    private int shootAnimationState;
    private int planeX;
    private int planeY;

    private Sound shootSound;
    private int shootDelay;
    private Rectangle planeRectangle;

    Player() {
        planeX = Gdx.graphics.getWidth() / 2 - (plane[flyAnimationState].getWidth() / 2);
        planeY = Gdx.graphics.getHeight() / 2 - (plane[flyAnimationState].getHeight() / 2);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
        shootDelay = 0;
    }

    void fly() {

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

        if (planeX + plane[flyAnimationState].getWidth() >= Gdx.graphics.getWidth()) {
            planeX = Gdx.graphics.getWidth() - plane[flyAnimationState].getWidth();
        }

        if (planeX <= 0) {
            planeX = 0;
        }

        if (planeY + plane[flyAnimationState].getHeight() >= Gdx.graphics.getHeight()) {
            planeY = Gdx.graphics.getHeight() - plane[flyAnimationState].getHeight();
        }

        if (planeY <= 0) {
            planeY = 0;
        }
    }

    void shoot() {
        shootSound.play(0.2f);
    }

    void shootDraw (SpriteBatch batch) {

        if (shootAnimationFrame < SHOOT_ANIMATION_SPEED) {
            shootAnimationFrame++;
        } else {
            shootAnimationFrame = 0;
            if (shootAnimationState < 4) {
                shootAnimationState++;
            } else {
                shootAnimationState = 0;
            }
        }
        batch.draw(shoot[shootAnimationState], planeX, planeY);
        planeRectangle = new Rectangle(planeX + 100, planeY + 100, plane[flyAnimationState].getWidth() - 150, plane[flyAnimationState].getHeight() - 150);

    }

    void flyDraw (SpriteBatch batch) {

        if (flyAnimationFrame < FLY_ANIMATION_SPEED) {
            flyAnimationFrame++;
        } else {
            flyAnimationFrame = 0;
            if (flyAnimationState < plane.length - 1) {
                flyAnimationState++;
            } else {
                flyAnimationState = 0;
            }
        }

        batch.draw(plane[flyAnimationState], planeX, planeY);
        planeRectangle = new Rectangle(planeX + 100, planeY + 100, plane[flyAnimationState].getWidth() - 150, plane[flyAnimationState].getHeight() - 150);
    }

    void death(SpriteBatch batch) {
        batch.draw(planeDead, planeX, planeY);
    }

    public Rectangle getPlaneRectangle() {
        return planeRectangle;
    }

    public void setShootDelay(int shootDelay) {
        this.shootDelay = shootDelay;
    }

    public int getShootDelay() {
        return shootDelay;
    }

    public int getPlaneX() {
        return planeX;
    }

    public int getPlaneY() {
        return planeY;
    }

    public void setPlaneX(int planeX) {
        this.planeX = planeX;
    }

    public void setPlaneY(int planeY) {
        this.planeY = planeY;
    }

    public Texture getPlaneTexture(int i) {
        return plane[i];
    }
}
