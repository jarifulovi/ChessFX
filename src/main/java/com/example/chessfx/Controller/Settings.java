package com.example.chessfx.Controller;

import com.example.chessfx.Logic.logic;


// Settings will be created in Menu Controller
// It will be passed to play, computer, options
// In Options it will be modified
// Then when loading menu it will pass the settings
// Menu and Options will set settings when loading each other
// At first Menu will initialize the first settings with default value
public class Settings {

    public int boardType;
    public int player;
    public int gameType;
    public boolean isSound;
    public int duration;

    public Settings(){
        boardType = logic.GREEN_BOARD;
        player = logic.WHITE;
        gameType = logic.ONE_PLAYER;
        isSound = true;
    }

}
