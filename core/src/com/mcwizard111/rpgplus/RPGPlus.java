package com.mcwizard111.rpgplus;

import com.badlogic.gdx.Game;
import com.mcwizard111.rpgplus.enums.Level;
import com.mcwizard111.rpgplus.screens.LevelScreen;

public class RPGPlus extends Game {
    @Override
    public void create() {
        setScreen(new LevelScreen(Level.LEVEL01));
    }
}
