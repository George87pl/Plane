package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.graphics.Texture;

class BrownBird extends Monster {

    private static Texture[] flyTexture = new Texture[]{
        new Texture("bird1_1.png"),
                new Texture("bird1_2.png"),
                new Texture("bird1_3.png"),
                new Texture("bird1_4.png"),
                new Texture("bird1_5.png"),
                new Texture("bird1_6.png"),
                new Texture("bird1_7.png"),
                new Texture("bird1_8.png")};

    static private Texture[] deathTexture = new Texture[]{ new Texture("bird1death.png")};

    BrownBird(int lives, int speed, int animationSpeed, int positionX, int positionY) {
        super(flyTexture, deathTexture, lives, speed, animationSpeed, positionX, positionY);
    }
}
