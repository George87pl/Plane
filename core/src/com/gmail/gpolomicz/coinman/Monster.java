package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class Monster {

    private Texture[] flyTextures;
    private int speed;
    private int flyAnimationFrameCount;
    private int flyAnimationSpeed;
    private int flyAnimationState;
    private int positionX;
    private int positionY;
    private Rectangle rectangle;

    Monster(Texture[] flyTextures, int speed, int animationSpeed, int positionX, int positionY) {
        this.flyTextures = flyTextures;
        this.speed = speed;
        this.positionX = positionX;
        this.flyAnimationSpeed = animationSpeed;

        if(positionY + flyTextures[flyAnimationState].getHeight() > Gdx.graphics.getHeight()) {
            this.positionY = Gdx.graphics.getHeight() - flyTextures[flyAnimationState].getHeight();
        } else {
            this.positionY = positionY;
        }
    }

    Monster(int speed, int positionX, int positionY) {
        this.speed = speed;
        this.positionX = positionX;
        this.positionY = positionY;
        this.flyAnimationSpeed = 10000;
    }

    void fly() {
        if (flyAnimationFrameCount < flyAnimationSpeed) {
            flyAnimationFrameCount++;
        } else {
            flyAnimationFrameCount = 0;
            if (flyAnimationState < flyTextures.length - 1) {
                flyAnimationState++;
            } else {
                flyAnimationState = 0;
            }
        }
        this.positionX = positionX - speed;
    }

    void draw (SpriteBatch batch){
        batch.draw(flyTextures[flyAnimationState], positionX, positionY);
        rectangle = new Rectangle(positionX, positionY, flyTextures[flyAnimationState].getWidth(), flyTextures[flyAnimationState].getWidth());
        //Gdx.app.log("GPDEB", "PositionX: " +positionX);
    }

    Rectangle getRectangle() {
        return rectangle;
    }

    void setFlyTextures(Texture[] flyTextures) {
        this.flyTextures = flyTextures;
    }

    void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    void setFlyAnimationState(int flyAnimationState) {
        this.flyAnimationState = flyAnimationState;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }
}
