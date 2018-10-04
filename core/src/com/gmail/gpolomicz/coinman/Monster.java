package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

class Monster {

    private int lives;
    private Texture[] flyTextures;
    private Texture[] deathTextures;
    Texture hpbar1 = new Texture("bosshp1.png");
    Texture hpbar2 = new Texture("bosshp2.png");;
    private Sound killSound;
    private int speed;
    private int flyAnimationFrameCount;
    private int flyAnimationSpeed;
    private int flyAnimationState;
    private int deathAnimationState;
    private int positionX;
    private int positionY;
    private Rectangle rectangle;
    private Rectangle atackRectangle;
    private boolean isDead = false;
    private int deathTime;
    private boolean isHit = false;
    private int hitTime;

    Monster(Texture[] flyTextures, Texture[] deathTextures, int lives, int speed, int animationSpeed, int positionX, int positionY) {
        this.flyTextures = flyTextures;
        this.deathTextures = deathTextures;
        this.lives = lives;
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

    void death() {

        if (flyAnimationFrameCount < 10) {
            flyAnimationFrameCount++;
        } else {
            flyAnimationFrameCount = 0;
            if (deathAnimationState < deathTextures.length - 1) {
                deathAnimationState++;
            } else {
                deathAnimationState = 0;
            }
        }
    }

    int getHit() {
        lives--;
        if(lives == 0) {
            isDead = true;
            killSound.play();
            deathTime = 100;
            return 1;
        } else {
            isHit = true;
            hitTime = 25;
            return 0;
        }
    }

    void draw (SpriteBatch batch){
        if(!isDead) {
            if(isHit) {
                if(hitTime > 0) {
                    if(hitTime > 20 || (hitTime < 15 && hitTime > 10) || hitTime < 5) {
                        batch.setColor(Color.RED);
                        batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                        batch.setColor(Color.WHITE);
                    } else {
                        batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                    }
                    hitTime--;
                } else {
                    isHit = false;
                    batch.draw(flyTextures[flyAnimationState], positionX, positionY);
                }

            }else {
                batch.draw(flyTextures[flyAnimationState], positionX, positionY);
            }
            rectangle = new Rectangle(positionX, positionY, flyTextures[flyAnimationState].getWidth(), flyTextures[flyAnimationState].getHeight());
        } else {
            if(deathTime > 0 ) {
                death();
                batch.draw(deathTextures[deathAnimationState], positionX, positionY);
                deathTime--;
            }
        }
    }

    Rectangle getRectangle() {
        return rectangle;
    }

    void setFlyTextures(Texture[] flyTextures) {
        this.flyTextures = flyTextures;
    }

    public int getPositionX() {
        return positionX;
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

    int getPositionY() {
        return positionY;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isDead() {
        return isDead;
    }

    public int getDeathTime() {
        return deathTime;
    }

    public void setDeathTime(int deathTime) {
        this.deathTime = deathTime;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public int getHitTime() {
        return hitTime;
    }

    public void setHitTime(int hitTime) {
        this.hitTime = hitTime;
    }

    public int getDeathAnimationState() {
        return deathAnimationState;
    }

    public void setKillSound(Sound killSound) {
        this.killSound = killSound;
    }

    public int getLives() {
        return lives;
    }

    public void setAtackRectangle(Rectangle atackRectangle) {
        this.atackRectangle = atackRectangle;
    }

    public Rectangle getAtackRectangle() {
        return atackRectangle;
    }
}
