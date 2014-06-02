package com.mcwizard111.rpgplus.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public class AirGrid extends AbstractGrid {
    private final Texture texture;

    public AirGrid() {
        texture = new Texture(Gdx.files.internal("blocks/stone.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public String getName() {
        return "AIR";
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public TextureRegion getTexture() {
        return new TextureRegion(texture);
    }
}
