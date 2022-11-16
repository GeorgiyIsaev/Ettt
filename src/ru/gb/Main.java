package ru.gb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static  void main (String... args){
        //ServerSocket - подключение к порту, помещаем в try-catch, т.к. порт может быть занят
        try(ServerSocket serverSocket = new ServerSocket( 8080)){
            System.out.println("Server started!");

            while (true) {

                //Входим на страницу http://localhost:8080/
                Socket socket = serverSocket.accept(); //Будит ожидать подключение клиента
                System.out.println("New client connect");

                //Что бы каждый клиент работал со своим потоком
                new Thread(() -> handleRequest(socket)).start();            }


        } catch (IOException e){
            e.printStackTrace();
        }



    }
    private static void handleRequest(Socket socket) {
        //Получаемая от клиента информация socket.getInputStream() приходит в виде байтов
        //Её можно преобразовать в читаемы вид через InputStreamReader
        //класс BufferedReader позволяет считывать всю информацию в виде строки, а не символов
        try (BufferedReader input = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(socket.getOutputStream());) {

            //input.ready(); // ожидаем прихода информации

            while (!input.ready()) ; //ждем пока не появится что-то что можно считать

            //Создадим переменую для хранения запрашиваемого пользователем адреса
            String firstLine = input.readLine();
            String[] parts = firstLine.split(" ");
            System.out.println(firstLine); // выводим пока есть что выводить
            System.out.println(" parts[1]: " + parts[1]); // выводим пока есть что выводить


            while (input.ready()) {
                System.out.println(input.readLine()); // выводим пока есть что выводить
            }


            parts[1] = parts[1].substring(0);
            Path path = Paths.get("C:\\REPOSITORY\\GB.JavaWeb.WebSerwer\\www\\", parts[1]);
            //Path path = Paths.get(".", parts[1]);
            if (!Files.exists(path)) {
                output.println("HTTP/1.1 404 NOT_FOUND");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<h1>Файл " + parts[1] + " не найден</h1>"); //страница которую мы передаем
                output.flush();

            }
            else {
                //Ответ (версия протокола) (код 200) (текстовое значение кода)
                //тип страницы и кодировка
                //Обязательно пустую строку в конце (ответ закончен)
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();

                Files.newBufferedReader(path).transferTo(output); //страница которую мы передаем
                //output.println("<h1>Мой документ</h1>"); //страница которую мы передаем
                output.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
