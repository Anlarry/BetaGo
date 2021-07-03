package com.client.gamedata;

import java.awt.Color;

import com.client.ui.ChessBoard;

/**
 * 存储游戏数据
 */
public class GameData {

    public static final int FrameHeight = 750;
    public static final int FrameWidth = 1270;
    
    public static final int port = 6666;
    public static String serverIP = "127.0.0.1"; 

    public static final String robotID = "Robot"; // 机器人的 ID
    
    public static boolean isConnected = false; // 是否连接的标志
    public static boolean hasOpponent = false; //是否有对手，判定游戏是否正在进行
    
    
    public static boolean gameOver=false;    //控制游戏流程的变量
    public static boolean isWinner=false;
    public static boolean isLoser=false;    //这个变量没有用到
    
    public static boolean isBlack=false;    //我方棋子颜色
    public static boolean myTurn=false;
    public static Color myColor= Color.blue;
    
    
    public static String myID="";
    public static String opponentID="";        //    对手的 ID ；机器人对手的 ID 是 Robot
    
    public static String chosenOpponentID=""; //列表中已经选择的 ID

    public static boolean againstRobot = false;    //对手是机器人
    
    public static void Reset(){
        GameData.hasOpponent = false;
        GameData.gameOver=false;
        GameData.isWinner=false;
        GameData.isLoser=false;
        GameData.chosenOpponentID="";
        GameData.opponentID="";
        GameData.myTurn=false;
        GameData.againstRobot = false;
        ChessBoard.chessCount=0;
        ChessBoard.getInstance().clearCheessBoard();
        ChessBoard.getInstance().repaint();
    }
}
