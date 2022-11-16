package ru.gb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

    public static  void main (String... args){
        //ServerSocket - подключение к порту, помещаем в try-catch, т.к. порт может быть занят
        try(ServerSocket serverSocket = new ServerSocket( 8080)){
            System.out.println("Server started!");

            //Входим на страницу http://localhost:8080/
            Socket socket = serverSocket.accept(); //Будит ожидать подключение клиента
            System.out.println("New client connect");


            //Получаемая от клиента информация socket.getInputStream() приходит в виде байтов
            //Её можно преобразовать в читаемы вид через InputStreamReader
            //класс BufferedReader позволяет считывать всю информацию в виде строки, а не символов
            try(BufferedReader input =  new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter output = new PrintWriter(socket.getOutputStream());)
            {

                //input.ready(); // ожидаем прихода информации

                while (!input.ready()); //ждем пока не появится что-то что можно считать
                while (input.ready()){
                    System.out.println(input.readLine()); // выводим пока есть что выводить
                }

                //Ответ (версия протокола) (код 200) (текстовое значение кода)
                //тип страницы и кодировка
                //Обязательно пустую строку в конце (ответ закончен)
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<h1>Мой документ</h1>"); //страница которую мы передаем


            }catch (IOException e){
                e.printStackTrace();
            }


        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
