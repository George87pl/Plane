package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class BlueBird extends Monster {

    private int positionX;
    private int positionY;
    private int flyAnimationSpeed;
    private int flyAnimationFrame;
    private int flyAnimationState;
    private int speed;
    private Sprite[] sprite;
    private Rectangle rectangle;

    private boolean goBack = false;
    private boolean isFlipped = false;

    private static Texture[] flyTextures = new Texture[]{
            new Texture("bird3_1.png"),
            new Texture("bird3_2.png"),
            new Texture("bird3_3.png")};

    private static Texture[] deathTexture = new Texture[]{
            new Texture("bird3death1.png"),
            new Texture("bird3death2.png")};

    BlueBird(int live, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTextures, deathTexture, live, speed, animationSpeed, positionX, positionY);

        this.positionX = positionX;
        this.positionY = positionY;

        if(positionY + flyTextures[flyAnimationState].getHeight() > Gdx.graphics.getHeight()) {
            this.positionY = Gdx.graphics.getHeight() - flyTextures[flyAnimationState].getHeight();
        } else {
            this.positionY = positionY;
        }

        this.flyAnimationSpeed = animationSpeed;
        this.speed = speed;
        sprite = new Sprite[]{new Sprite(flyTextures[0]), new Sprite(flyTextures[1]), new Sprite(flyTextures[2])};
    }

    @Override
    void fly() {
        if (flyAnimationFrame < flyAnimationSpeed) {
            flyAnimationFrame++;
        } else {
            flyAnimationFrame = 0;
            if (flyAnimationState < flyTextures.length - 1) {
                flyAnimationState++;
            } else {
                flyAnimationState = 0;
            }
        }

        if (!goBack) {
            this.positionX = positionX - speed;
            if (positionX < 0) {
                goBack = true;
            }
            super.setPositionX(positionX);
            super.setFlyAnimationState(flyAnimationState);
        } else {
            this.positionX = positionX + speed;
        }
    }

    @Override
    void draw(SpriteBatch batch) {

        if(!super.isDead()) {
            if(super.isHit()) {
                if(super.getHitTime() > 0) {
                    if(super.getHitTime() > 20 || (super.getHitTime() < 15 && super.getHitTime() > 10) || super.getHitTime() < 5) {
                        batch.setColor(Color.RED);
                        batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                        batch.setColor(Color.WHITE);
                    } else {
                        batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                    }
                    super.setHitTime(getHitTime()-1);
                } else {
                    super.setHit(false);
                }

            }else {
                if(!goBack) {
                    batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                    rectangle = new Rectangle(positionX, positionY, flyTextures[flyAnimationState].getWidth(), flyTextures[flyAnimationState].getWidth());
                    super.setRectangle(rectangle);
                } else {

                    if(!isFlipped) {
                        sprite[0].flip(true, false);
                        sprite[1].flip(true, false);
                        sprite[2].flip(true, false);
                        isFlipped = true;
                    }

                    sprite[flyAnimationState].setPosition(positionX, positionY);
                    sprite[flyAnimationState].draw(batch);
                    rectangle = new Rectangle(positionX, positionY, flyTextures[flyAnimationState].getWidth(), flyTextures[flyAnimationState].getWidth());
                    super.setRectangle(rectangle);
                    super.setPositionX(positionX);
                    super.setPositionX(positionY);
                }
            }
            rectangle = new Rectangle(positionX, positionY, flyTextures[flyAnimationState].getWidth(), flyTextures[flyAnimationState].getWidth());
        } else {
            if(super.getDeathTime() > 0 ) {
                super.death();
                batch.draw(deathTexture[super.getDeathAnimationState()], positionX, positionY);
                super.setDeathTime(super.getDeathTime()-1);
            }
        }
    }
}
