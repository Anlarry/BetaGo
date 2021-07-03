package com.client.listener;

/**
 * 客户端列表监听器 双击选择玩家
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;

import com.client.gamedata.GameData;
import com.client.net.Resolver;
import com.client.tools.IOTool;
import com.client.tools.ListTool;

public class ListListener extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {

        JList list = ListTool.getPlayerList();
        int index = list.locationToIndex(e.getPoint());
        GameData.chosenOpponentID=(String) list.getModel().getElementAt(index); // 获取内容
        System.out.println(GameData.chosenOpponentID);
        
        if (e.getClickCount() == 2 && !ListTool.getPlayerList().isSelectionEmpty()) {
            //游戏没有结束，不能匹配玩家
            if (!GameData.opponentID.equals("")&&GameData.gameOver==false){
                JOptionPane.showConfirmDialog(null, "当前正在游戏，不能匹配新的玩家！", "游戏中请勿匹配", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
            //当局游戏结束，未点击重来
            else if (!GameData.opponentID.equals("")&&GameData.gameOver==true){
                JOptionPane.showConfirmDialog(null, "请点击重来结束当局！", "当局结束", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
            // 不能和自己对战
            else if (GameData.chosenOpponentID.equals(GameData.myID)) {
                JOptionPane.showConfirmDialog(null, "不能和自己对战！", "对战错误", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
            // 挑战 Robot
            else if (GameData.chosenOpponentID.equals(GameData.robotID)){
                int choice=JOptionPane.showConfirmDialog(null,"是否选择先手（执黑棋）？",
                     "请选择先后手",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                String color = (choice == 0) ? "black" : "white";
                String message = "ROBOT:" + color;
                new Resolver(message).resolve();
            }
            // 选择在线玩家对战
            else if (GameData.isConnected == true) {
                String message = "CHALL:" + GameData.myID + "#" + GameData.chosenOpponentID;
                JOptionPane.showConfirmDialog(null, "已经发送对战请求！", "请求发送成功", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.DEFAULT_OPTION);
                IOTool.getInstance().getWriter().println(message); // 发送对战请求
            }
            // 未登录
            else {
                JOptionPane.showConfirmDialog(null, "请先登录再挑战！", "登录提示", JOptionPane.DEFAULT_OPTION,
                JOptionPane.DEFAULT_OPTION);
            }

        }
    }
}
