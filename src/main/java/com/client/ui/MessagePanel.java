package com.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.client.listener.SendMessageListener;
import com.client.tools.MessageTool;
/**
 * 消息传输面板，可以用于传输文本消息
 * 包含一个JTextArea和一个JTextField还有发送的JButton
 * 可以在线收发消息
 *
 */
public class MessagePanel extends JPanel {
    
    private static MessagePanel mp = null;
    
    private JScrollPane messagePane=new JScrollPane();
    private JTextArea messageArea=MessageTool.getMessageArea();
    private JTextField sendField=new JTextField(33);
    private JButton sendBtn=new JButton("Send");
    private JPanel sendPanel=new JPanel(new FlowLayout());
    //单例模式
    public static MessagePanel getInstance(){
        if(mp==null){
            mp=new MessagePanel();
        }
        return mp;
    }
    public JTextField getSendField(){
        return sendField;
    }
    private MessagePanel(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("消息面板"));
        setPreferredSize(new Dimension(286,0));
        sendPanel.setLayout(new BorderLayout());
        sendPanel.add(sendField,BorderLayout.CENTER);
        sendPanel.add(sendBtn, BorderLayout.EAST);
        add(messagePane,BorderLayout.CENTER);
        add(sendPanel,BorderLayout.SOUTH);
        messagePane.setViewportView(messageArea);
        // sendField.setSize(100,100);
        // sendField.setPreferredSize(new Dimension(50, 20));
        sendBtn.addActionListener(new SendMessageListener());
    }
    
}
