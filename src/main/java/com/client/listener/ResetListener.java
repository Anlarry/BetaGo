package com.client.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.client.gamedata.GameData;
import com.client.ui.StatusPanel;
/**
 * 重来按钮的监听事件
 *
 */
public class ResetListener implements ActionListener{

    public void actionPerformed(ActionEvent e) {
        if (!GameData.gameOver) {
            JOptionPane.showConfirmDialog(null, "游戏还未结束！", "提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.DEFAULT_OPTION);
        }
        else {
            GameData.Reset();
            StatusPanel.getInstance().setResetStatusInValid();
            JOptionPane.showConfirmDialog(null, "现在可以重新和其他玩家游玩啦！", "已经和对方断开连接", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.DEFAULT_OPTION);
        }
    }
    
}
