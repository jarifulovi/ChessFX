package com.example.chessfx.Controller;

import com.example.chessfx.Logic.Abstract.logic;


// Settings will be created in Menu Controller
// It will be passed to play, computer, options, about and gameOverUI

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
