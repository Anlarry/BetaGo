package com.client.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.client.gamedata.GameData;
import com.client.tools.IOTool;
import com.client.tools.MessageTool;
import com.client.ui.PlayerPanel;
import com.client.ui.StatusPanel;
/**
 * “不下了”按钮的监听事件
 */
public class EscapeListener implements ActionListener{

    public void actionPerformed(ActionEvent e) {
        if(!GameData.againstRobot && GameData.isConnected==false){
            JOptionPane.showConfirmDialog
            (null,"请先登录！", "登录提示",JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION);
        }
        else if(GameData.hasOpponent==false){
            JOptionPane.showConfirmDialog
            (null,"请先开始游戏！", "游戏未开始",JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION);
        }
        else if(GameData.hasOpponent==true &&!GameData.opponentID.equals("")){
            int choice=JOptionPane.showConfirmDialog
            (null,"战斗中逃跑就是认输，你确定不下了吗？", "退出当前棋局",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(choice==JOptionPane.YES_OPTION){
                GameData.gameOver=true;
                if(!GameData.againstRobot){
                    // 向对方发送认输信息须在弹出信息之前
                    // 以免因输棋恼羞成怒直接关闭客户端，导致对方接收不到信息
                    IOTool.getInstance().getWriter().println("LOSE:"+GameData.myID+"#"+GameData.opponentID);
                }
                MessageTool.getInstance().addMessage("请点击重来结束本局！");
                JOptionPane.showConfirmDialog(null,"你输了",
                         "结果产生",JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION);
                   
                PlayerPanel.getInstance().setEscapeEnabledInvalid();
                StatusPanel.getInstance().setResetStatusValid();
            }
            else if(choice==JOptionPane.NO_OPTION){
                //不做动作
            }
        }
    }    
}
