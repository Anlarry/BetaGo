package com.client.robot;

import com.client.ui.ChessPoint;


import java.awt.Color;
import java.util.concurrent.ExecutionException;

import javax.jws.Oneway;


public class Evaluater {
    static public int INF = 1000000;
    private final int FIVE = 10000;
    private final int FOUR = 1000;
    private final int semiFOUR = 100;
    private final int THREE = 100;
    private final int semiTHREE = 10;
    private final int TWO = 10;
    private final int semiTWO = 5;
    private final int ONE = 1;
    private final int semiONE = 0;
    private final int DISTENSE_DECEY = 1;

    private int value = 0;
    private Color color;
    private ChessPoint cp[][];
    public Evaluater(ChessPoint cp[][], Color color) {
        this.cp = cp;
        this.color = color;
        for(int i = 0; i < 15; i++) {
            value += evalRow(i);
            value += evalCol(i);
        }
        for(int d = 14; d >= -14; d--) {
            value += evalDiag(d);
        }
        for(int d = 0; d <= 28; d++) {
            value += evalAntiDiag(d);
        }
    }
    public int eval() {
        return value;
    }
    public void step(int x, int y, Color color) {
        int tmpVal = evalRow(x) + evalCol(y) + evalDiag(x-y) + evalAntiDiag(x+y);
        value -= tmpVal;
        int decay = evalDistense(x, y);
        if(this.color == color) {
            decay = -decay;
        }
        value += decay;
        cp[x][y] = new ChessPoint(x, y, color);
        value += evalRow(x) + evalCol(y) + evalDiag(x-y) + evalAntiDiag(x+y);
    }
    public void unstep(int x, int y) {
        int tmpVal = evalRow(x) + evalCol(y) + evalDiag(x-y) + evalAntiDiag(x+y);
        value -= tmpVal;
        Color color = cp[x][y].getCPColor();
        cp[x][y] = null;
        int decay = evalDistense(x, y);
        if(this.color == color)  decay = -decay;
        value -= decay;
        value += evalRow(x) + evalCol(y) + evalDiag(x-y) + evalAntiDiag(x+y);
    }
    private int evalRow(int r) {
        return evalLine(r, 0, 0, 1, r+1, 15);
    }
    private int evalCol(int c) {
        return evalLine(0, c, 1, 0, 15, c+1);
    }
    private int evalDiag(int d) {
        return evalLine(Math.max(d, 0), Math.max(-d, 0), 1, 1, Math.min(14, d+14)+1, Math.min(14, 14-d)+1);
    } 
    private int evalAntiDiag(int d) {
        return evalLine(Math.min(d, 14), Math.max(0, d-14), -1, 1, Math.max(0, d-14)-1, Math.min(14, d)+1);
    }
    private int evalLine(int x, int y, int dx, int dy, int ex, int ey)  {
        int res = 0;
        Color color;
        while(x != ex && y != ey) {
            if(cp[x][y] == null) {
                x += dx;
                y += dy;
                continue;
            }
            int cnt = 0;
            color = cp[x][y].getCPColor();
            int bx = x, by = y;
            for(; x != ex && y != ey && cp[x][y] != null && cp[x][y].getCPColor() == color; x += dx, y += dy) {
                cnt++;
            }
            boolean semi = true;
            boolean close = true;
            if(inRange(bx-dx) && inRange(by-dy) && inRange(x) && inRange(y)) {
                if(cp[bx-dx][by-dy] == null && cp[x][y] == null)
                    semi = false; 
            }
            if(inRange(bx-dx) && inRange(by-dy) && cp[bx-dx][by-dy] == null) {
                close = false;
            }
            if(inRange(x) && inRange(y) && cp[x][y] == null) {
                close = false;
            }
            int curVal = 0;
            switch (cnt) {
                case 1:
                    curVal = semi ? semiONE : ONE;
                    if(close) curVal = 0;
                    break;
                case 2:
                    curVal = semi ? semiTWO : TWO;
                    if(close) curVal = 0;
                    break;
                case 3:
                    curVal = semi ? semiTHREE : THREE;
                    if(close) curVal = 0;
                    break;
                case 4:
                    curVal = semi ? semiFOUR : FOUR;
                    if(close) curVal = 0;
                    break;
                case 5:
                    curVal = FIVE;
                    break;
                default:
                    curVal = FIVE;
                    break;
                    // System.out.println("Bad eval");
            }
            if(color != this.color) 
                curVal = -curVal;
            res += curVal;
        }
        return res;
    }
    private boolean inRange(int x) {
        return x >= 0 && x <= 14;
    }
    private int evalDistense(int x, int y) {
        int decay = 0;
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                if(cp[i][j] != null) {
                    int dis = Math.abs(i-x) + Math.abs(j-y);
                    decay = DISTENSE_DECEY * dis;
                }
            }
        }
        return decay;
    }
}
