package ru.gb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static  void main (String... args){
        //ServerSocket - подключение к порту, помещаем в try-catch, т.к. порт может быть занят
        try(ServerSocket serverSocket = new ServerSocket( 8080)){
            System.out.println("Server started!");

            //Входим на страницу http://localhost:8080/
            Socket socket = serverSocket.accept(); //Будит ожидать подключение клиента
            System.out.println("New client connect");

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
