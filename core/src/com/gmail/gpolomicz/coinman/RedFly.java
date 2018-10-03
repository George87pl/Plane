package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

class RedFly extends Monster {

    private boolean downUp;
    private int flyX;
    private int flyY;
    private int speed;
    private int animationSpeed;
    private int flyAnimationFrameCount;
    private int flyAnimationState;

    private static Texture[] flyTextures = new Texture[]{
            new Texture("bird2_1.png"),
            new Texture("bird2_2.png")};

    private static Texture[] deathTexture = new Texture[]{
            new Texture("bird2death1.png"),
            new Texture("bird2death2.png")};

    RedFly(int lives, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTextures, deathTexture, lives, speed, animationSpeed, positionX, positionY);
        flyY = positionY;
        flyX = positionX;
        this.speed = speed;
        this.animationSpeed = animationSpeed;
        Random random = new Random();
        downUp = random.nextBoolean();
    }

    @Override
    void fly() {
        if (flyAnimationFrameCount < animationSpeed) {
            flyAnimationFrameCount++;
        } else {
            flyAnimationFrameCount = 0;
            if (flyAnimationState < flyTextures.length - 1) {
                flyAnimationState++;
            } else {
                flyAnimationState = 0;
            }
            super.setFlyAnimationState(flyAnimationState);
        }

        flyX = flyX - speed;
        super.setPositionX(flyX);

        if (downUp) {
            flyY = flyY + 2;
            super.setPositionY(flyY);
            if (flyY >= Gdx.graphics.getHeight() - flyTextures[0].getHeight()) {
                downUp = !downUp;
            }
        } else {
            flyY = flyY - 2;
            super.setPositionY(flyY);
            if (flyY <= 0) {
                downUp = !downUp;
            }
        }
    }
}

