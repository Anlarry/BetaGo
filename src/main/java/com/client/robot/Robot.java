package com.client.robot;

import com.client.gamedata.GameData;
import com.client.ui.ChessBoard;
import com.client.ui.ChessPoint;

import javafx.scene.effect.ColorAdjust;

import java.awt.Color;


public class Robot {
    private int searchDepth = 3;

    private ChessPoint[][] cp = new ChessPoint[15][15];
    private Color myColor = GameData.isBlack ? Color.WHITE : Color.BLACK;
    private Color oppColor = GameData.isBlack ? Color.BLACK : Color.WHITE;
    private ChessPoint nextPoint = new ChessPoint(7, 7, myColor);
    private Evaluater evaluater;

    public Robot() {}
    public Robot(int searchDepth) {
        this.searchDepth = searchDepth;
    }
    public Robot(Color myColor, int searchDepth) {
        this.myColor = myColor;
        this.oppColor = myColor == Color.BLACK ? Color.WHITE : Color.BLACK;
        this.searchDepth = searchDepth;
    }
    public ChessPoint go() {
        int chessNum = 0;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                cp[i][j] = ChessBoard.getInstance().getChessPoint(i, j);
                if(cp[i][j] != null) chessNum += 1;
            }
        }
        if(chessNum == 0 && myColor == Color.BLACK) {
            return  new ChessPoint(7, 7, myColor);
        }
        evaluater = new Evaluater(cp, this.myColor);
        alphaBeta(searchDepth, -Evaluater.INF, Evaluater.INF, true);
        return nextPoint;
    }
    public ChessPoint go(ChessPoint[][] cp) {
        int chessNum = 0;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                this.cp[i][j] = cp[i][j];
                if(this.cp[i][j] != null) chessNum ++;
            }
        }
        if(chessNum == 0 && myColor == Color.BLACK) {
            return new ChessPoint(7, 7, myColor);
        }
        evaluater = new Evaluater(this.cp, this.myColor);
        alphaBeta(searchDepth, -Evaluater.INF, Evaluater.INF, true);
        return nextPoint;
    }
    private int alphaBeta(int depth, int alpha, int beta, boolean maxPlayer) {
        if(depth == 0) {
            return evaluater.eval();
        }
        boolean terminal = true;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(cp[i][j] == null)
                    terminal = false;
            }
        }
        if(terminal) {
            return evaluater.eval();
        }
        if(maxPlayer) {
            int value = -Evaluater.INF;
            for(int i = 0; i < 15; i++) {
                for(int j = 0; j < 15; j++) {
                    if(cp[i][j] != null) continue;
                    evaluater.step(i, j, myColor);
                    int tmp = alphaBeta(depth-1, alpha, beta, false);
                    evaluater.unstep(i, j);
                    if(tmp > value) {
                        value = tmp;
                        if(depth == searchDepth) {
                            nextPoint.setCPX(i);
                            nextPoint.setCPY(j);
                        }
                    }
                    if(value >= beta) 
                        break;
                    alpha = Math.max(alpha, value);
                }
            }
            return value;
        }
        else {
            int value = Evaluater.INF;
            for(int i = 0; i < 15; i++) {
                for(int j = 0; j < 15; j++) {
                    if(cp[i][j] != null) continue;
                    evaluater.step(i, j, oppColor);
                    value = Math.min(value, alphaBeta(depth-1, alpha, beta, true));
                    evaluater.unstep(i, j);
                    if(value <= alpha) 
                        break;
                    beta = Math.min(beta, value);
                }
            }
            return value;
        }
    }       
}
