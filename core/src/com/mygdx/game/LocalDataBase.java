package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LocalDataBase {
    void saveRecords(){
        Preferences prefs = Gdx.app.getPreferences("UnderwaterPrefs");
        prefs.putInteger("content", Main.contentCount);
        prefs.flush();
    }

    void loadRecords(){
        Preferences prefs = Gdx.app.getPreferences("UnderwaterPrefs");
        Base.contentCount = prefs.getInteger("content");
    }
}
