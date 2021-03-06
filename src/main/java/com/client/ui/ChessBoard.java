package com.client.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.client.gamedata.GameData;
import com.client.net.Resolver;
import com.client.tools.IOTool;
import com.client.tools.MessageTool;


/**
 * 棋盘类 绘制出棋盘和棋子
 */
public class ChessBoard extends JPanel implements MouseListener {
    private static ChessBoard chessBoard = null;

    private static final int MARGINX = 78; // 棋盘与面板 左 外框的间距
    private static final int MARGINY = 54; // 棋盘与面板 上 外框的间距
    private static final int GRID_MARGIN = 35; // 棋盘栅格之间的间距
    private static final int ROWS = 14; // 棋盘的行数-1
    private static final int COLS = 14; // 棋盘的列数-1
    private static final int SUCCESS_COUNT = 5; // 获胜需要的连续棋子数

    private int currentX = 0; // 刚下的棋子的坐标
    private int currentY = 0;

    // private Color currentChessColor = Color.black; // 当前棋子的颜色
    public static int chessCount = 0; // 棋子的数量
    public static ChessPoint[] cp = new ChessPoint[(ROWS + 1) * (COLS + 1)]; // 棋盘队列

    // 获取对象
    public static ChessBoard getInstance() {
        if (chessBoard == null) 
            chessBoard = new ChessBoard();
        return chessBoard;
    }

    public void clearCheessBoard() {
        ChessPoint[] cp = new ChessPoint[(ROWS + 1) * (COLS + 1)];
    }

    // 构造函数绑定监听器
    public ChessBoard() {
        // this.setBackground(Color.gray);

        addMouseListener(this);
        addMouseMotionListener(new MouseMotionListener() {
            // 获取移动鼠标的坐标，将其规范化
            public void mouseMoved(MouseEvent e) {
                if (GameData.hasOpponent == false || GameData.gameOver
                        || GameData.myTurn == false) {
                    return;
                }
                int x = (e.getX() - MARGINX + GRID_MARGIN / 2) / GRID_MARGIN; // 坐标转化的处理，邻接点处理
                int y = (e.getY() - MARGINY + GRID_MARGIN / 2) / GRID_MARGIN;
                if (x < 0 || x > COLS || y < 0 || y > ROWS || GameData.gameOver || getChessPoint(x, y) != null)
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                else
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                // System.out.println("横坐标是："+x+",纵坐标是:"+y);
            }

            public void mouseDragged(MouseEvent arg0) {

            }
        });
    }

    // 绘制函数，每次调用的时候重绘整个棋盘，根据棋子列表重绘棋子
    // 相当于添加了新的棋子
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // 暂时不知道为什么要这样写

        Toolkit tool = this.getToolkit();
        Image image = tool.getImage("./Background/image5.jpg");
        g.drawImage(image, 0, 0, chessBoard.getWidth(), chessBoard.getHeight(), 0, 0, image.getWidth(this),
                image.getHeight(this), this);

        g.setColor(Color.BLACK);
        // 画横线
        for (int i = 0; i <= COLS; i++) {
            g.drawLine(MARGINX, MARGINY + i * GRID_MARGIN, MARGINX + ROWS * GRID_MARGIN, MARGINY + i * GRID_MARGIN);
        }
        // 画竖线
        for (int i = 0; i <= ROWS; i++) {
            g.drawLine(MARGINX + i * GRID_MARGIN, MARGINY, MARGINX + i * GRID_MARGIN, MARGINY + COLS * GRID_MARGIN);
        }
        // 根据棋子列表绘制棋子
        for (int i = 0; i < chessCount; i++) {
            int x0 = cp[i].getCPX();
            int y0 = cp[i].getCPY();
            int x = MARGINX + x0 * GRID_MARGIN; // 计算真实坐标
            int y = MARGINY + y0 * GRID_MARGIN;

            g.setColor(cp[i].getCPColor());
            Graphics2D g2d = (Graphics2D) g;
            Ellipse2D e = new Ellipse2D.Float(x - GRID_MARGIN / 3, y - GRID_MARGIN / 3, GRID_MARGIN * 2 / 3,
                    GRID_MARGIN * 2 / 3);
            g2d.fill(e);

        }
    }

    // 下棋函数
    public void mousePressed(MouseEvent e) {
        if (GameData.hasOpponent == false || GameData.gameOver
                || GameData.myTurn == false) {
            return;
        }
        currentX = (e.getX() - MARGINX + GRID_MARGIN / 2) / GRID_MARGIN; // 坐标规范化，加上网格距离的一半是为了
        currentY = (e.getY() - MARGINY + GRID_MARGIN / 2) / GRID_MARGIN; // 临近化处理
        // 如果不出界或者不冲突就执行下面的
        if (currentX < 0 || currentX > COLS || currentY < 0 || currentY > ROWS
                || getChessPoint(currentX, currentY) != null)
            return;
        // 发送我方结束和棋子位置，锁定我方落子
        else {
            cp[chessCount++] = new ChessPoint(currentX, currentY, GameData.myColor);
            GameData.myTurn = false;
            repaint();
            String message = "YOURTURN:" + GameData.myID + "#" + GameData.opponentID + "#" + currentX + "#" + currentY + "#";
            if (isWin(GameData.myColor, currentX, currentY)) {
                GameData.gameOver = true;
                // 发送胜利消息
                if(!GameData.againstRobot) {
                    IOTool.getInstance().getWriter().println(message);
                    IOTool.getInstance().getWriter().println("WIN:" + GameData.myID + "#" + GameData.opponentID);
                }
                MessageTool.getInstance().addMessage("请点击重来结束本局！");
                JOptionPane.showConfirmDialog(null, "你赢了！", "胜利", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
                // 重来按钮可用
                StatusPanel.getInstance().setResetStatusValid();
                PlayerPanel.getInstance().setEscapeEnabledInvalid();

            }
            else if (GameData.againstRobot){
                message = "ROBOTTURN:" + GameData.myID + "#" + GameData.opponentID + "#" + currentX + "#" + currentY + "#";
                new Resolver(message).resolve();
                System.out.println(message);
            }
            else {
                IOTool.getInstance().getWriter().println(message);
                System.out.println(message);      
            }    
        }
    }

    // 遍历棋子列表，查找是否有坐标为x，y的棋子
    public ChessPoint getChessPoint(int x, int y) {
        for (int i = 0; i < chessCount; i++) {
            if (cp[i].getCPX() == x && cp[i].getCPY() == y) {
                // System.out.println("not null");
                return cp[i];
            }
        }
        return null;
    }

    // 判断胜利的核心函数
    // 主要思想是以当前所下的棋子为中心，进行左右上下以及斜向的搜索
    public boolean isWin(Color targetColor, int X, int Y) {

        int continueChess = 1; // 连续的棋子数量，当前的棋子不算
        // 向左搜索
        for (int pointerX = X - 1; pointerX >= 0; pointerX--) {
            ChessPoint p = getChessPoint(pointerX, Y);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }
        }
        // 向右搜索
        for (int pointerX = X + 1; pointerX <= COLS; pointerX++) {
            ChessPoint p = getChessPoint(pointerX, Y);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件的棋子
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }

        }
        // 判断是否出现连续子
        if (continueChess >= SUCCESS_COUNT) {
            return true;
        } else {
            continueChess = 1;
        }

        // 上下搜索判断连续子
        for (int pointerY = Y - 1; pointerY >= 0; pointerY--) {
            ChessPoint p = getChessPoint(X, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }
        }
        for (int pointerY = Y + 1; pointerY <= ROWS; pointerY++) {
            ChessPoint p = getChessPoint(X, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }
        }
        if (continueChess >= SUCCESS_COUNT) {
            return true;
        } else {
            continueChess = 1;
        }
        // 左上右下搜索判断连续子
        for (int pointerX = X - 1, pointerY = Y - 1; pointerX >= 0
                && pointerY >= 0; pointerX--, pointerY--) {
            ChessPoint p = getChessPoint(pointerX, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }
        }
        for (int pointerX = X + 1, pointerY = Y + 1; pointerX <= COLS
                && pointerY <= ROWS; pointerX++, pointerY++) {
            ChessPoint p = getChessPoint(pointerX, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                // System.out.println("no");
                break;
            }
        }
        if (continueChess >= SUCCESS_COUNT) {
            return true;
        } else {
            continueChess = 1;
        }
        // 右上左下搜索判断连续子
        for (int pointerX = X + 1, pointerY = Y - 1; pointerX <= COLS
                && pointerY >= 0; pointerX++, pointerY--) {
            ChessPoint p = getChessPoint(pointerX, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                break;
            }
        }
        for (int pointerX = X - 1, pointerY = Y + 1; pointerX >= 0
                && pointerY <= ROWS; pointerX--, pointerY++) {
            ChessPoint p = getChessPoint(pointerX, pointerY);
            if (p != null && p.getCPColor() == targetColor) { // 搜索满足条件
                continueChess++;
            } else {
                break;
            }
        }
        if (continueChess >= SUCCESS_COUNT) {
            return true;
        } else {
            continueChess = 1;
            return false;
        }

    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

}
