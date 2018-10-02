package com.gmail.gpolomicz.coinman;

import com.badlogic.gdx.graphics.Texture;

class Clouds extends Monster {

    static private Texture[] cloud1 = new Texture[]{new Texture("cloud1.png")};
    static private Texture[] cloud2 = new Texture[]{new Texture("cloud2.png")};
    static private Texture[] cloud3 = new Texture[]{new Texture("cloud3.png")};
    static private int cloudCount;

    Clouds(int speed, int positionX, int positionY) {
        super(speed, positionX, positionY);

        if(cloudCount == 0) {
            super.setFlyTextures(cloud1);
            cloudCount++;
        } else if(cloudCount == 1) {
            super.setFlyTextures(cloud2);
            cloudCount++;
        } else {
            super.setFlyTextures(cloud3);
            cloudCount = 0;
        }
    }
}
