package com.client.robot;

import com.client.gamedata.GameData;
import com.client.ui.ChessBoard;
import com.client.ui.ChessPoint;
import java.awt.Color;


public class Robot {
    static int searchDepth = 3;

    private ChessPoint[][] cp = new ChessPoint[15][15];
    private Color myColor = GameData.isBlack ? Color.WHITE : Color.BLACK;
    private ChessPoint nextPoint = new ChessPoint(8, 8, GameData.isBlack ? Color.WHITE : Color.BLACK);
    private Evaluater evaluater;


    public ChessPoint go() {
        int chessNum = 0;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                cp[i][j] = ChessBoard.getInstance().getChessPoint(i, j);
                if(cp[i][j] != null) chessNum += 1;
            }
        }
        if(chessNum == 0 && myColor == Color.BLACK) {
            return nextPoint;
        }
        evaluater = new Evaluater(cp, this.myColor);
        
        return nextPoint;
    }
    private int alphaBeta() {
        
    }   
}
