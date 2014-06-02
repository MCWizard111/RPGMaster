package com.mcwizard111.rpgplus.enums;

import com.mcwizard111.rpgplus.levels.Level01;
import com.mcwizard111.rpgplus.levels.Level02;

/**
 * Created by mcwizard111 on 5/31/2014.
 */
public enum Level {
    LEVEL01("Level 1", new Level01()),
    LEVEL02("Level 2", new Level02());

    private final String levelName;
    private final com.mcwizard111.rpgplus.levels.Level level;

    private Level(String levelName, com.mcwizard111.rpgplus.levels.Level level) {
        this.levelName = levelName;
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }
}
