package com.susanna.client_server.server;
import java.io.*;
import java.util.*;
import java.net.*;

public class Server {

    private static ArrayList<Connection> connections = new ArrayList<Connection>();

    public static void main(String[] args) {
        int port = 1304;
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server started");
            while (true) {
                Socket socket = ss.accept();
                connections.add(new Connection(socket));  //Список клиентов
                System.out.println("We have new client");
            }
        } catch (IOException ex) {
            System.out.println("Error connection");
        }
    }
    public Server() {}

    private static class Connection extends Thread {

        private DataInputStream input;
        private DataOutputStream output;
        private Socket socket;
        private String name = "";

        public Connection(Socket socket) {
            try {
                this.socket = socket;  //ИНИЦИАЛИЗАЦИЯ СОКЕТА(соединение)
                input = new DataInputStream(socket.getInputStream()); //Поток ввода
                output = new DataOutputStream(socket.getOutputStream()); //Поток вывода
                start(); //запуск потока для чтения и отправки сообщений(метод run())
            } catch (IOException ex) {
            }
        }

        public void run() {
            try {
                output.writeUTF("Enter your nick:");
                name = input.readUTF();

                while (true) {
                    String data = input.readUTF();
                    if (data != null) {
                            receiveText(data);
                        }
                }
            } catch (IOException ex) {
            }
        }

        private void receiveText(String text) {
            System.out.println(name + " send text: " + text);
            sendText(text);
        }


        private void sendText(String text) { //отправка сообщений клиентам
            try {
                Iterator<Connection> iter = Server.connections.iterator();
                while (iter.hasNext()) {
                    iter.next().output.writeUTF(name + ": " + text);
                }
            } catch (IOException ex) {
            }
        }

    }
}