package com.gmail.gpolomicz.coinman;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

class LandMonster extends Monster {

    private int gravity;
    private int velocity = 0;
    private int positionY;
    Random random = new Random();

    private static Texture[] flyTextures = new Texture[]{
            new Texture("monster1.png")};

    private static Texture[] deathTexture = new Texture[]{
            new Texture("monster1death.png")};

    LandMonster(int live, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTextures, deathTexture, live, speed, animationSpeed, positionX, positionY);
        Sound killSound = Gdx.audio.newSound(Gdx.files.internal("monsterkill.mp3"));
        super.setKillSound(killSound);
        this.positionY = positionY;
        gravity = 1;
    }

    void jump() {
        if(super.getPositionY() <= 0) {
            velocity = ((random.nextInt(40)) + 1) * (-1);
            velocity = gravity + velocity;
            positionY = positionY - velocity;
            super.setPositionY(positionY);
        } else {
            positionY = super.getPositionY();
            velocity = gravity + velocity;
            positionY = positionY - velocity;
            super.setPositionY(positionY);
        }
    }
}
