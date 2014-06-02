package com.mcwizard111.rpgplus.grid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public interface IGrid {
    public TextureRegion getTexture();
    public String getName();
    public boolean isSolid();
}
