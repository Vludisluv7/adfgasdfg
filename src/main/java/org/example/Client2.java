package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client2 extends JFrame implements ActionListener, KeyListener {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public Client2(String serverAddress, int serverPort) {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

            JPanel panel = new JPanel();
            panel.setLayout(null);

            chatArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(chatArea);
            scrollPane.setBounds(10, 10, 380, 300);
            chatArea.setEditable(false);
            panel.add(scrollPane);

            messageField = new JTextField();
            messageField.setBounds(10, 320, 300, 30);
            messageField.addKeyListener(this);
            panel.add(messageField);

            sendButton = new JButton("Send");
            sendButton.setBounds(320, 320, 70, 30);
            sendButton.addActionListener(this);
            panel.add(sendButton);

            add(panel);

            setResizable(false);
            setSize(410, 400);
            setVisible(true);

            receiveMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage() {
        String message = messageField.getText();
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
            messageField.setText("");
            chatArea.append("You: " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            String serverMessage;
            while (true) {
                serverMessage = inputStream.readUTF();
                chatArea.append("Server: " +
                        serverMessage + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            sendMessage();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        Client client = new Client("localhost", 12345);
    }}