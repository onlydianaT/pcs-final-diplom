import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        //Открываем серверный socket, используем try,catch, т.к. socket требует закрытия
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is started");
            //Запускаем вечный цикл
            while (true) {
                //Ожидаем подключения
                //После открытия серверный сокет откроет клиентский сокет
                try (Socket clientSocket = serverSocket.accept();
                     //Создаем поток вывода
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     //Создаем поток ввода
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println("Server started");
                    String word = in.readLine().toLowerCase();
                    Gson gson = new Gson();
                    List<PageEntry> searchList = engine.search(word);
                    String json = gson.toJson(searchList);
                    // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
                    //Записываем ответ в виде json формата
                    out.println(json);
                } catch (IOException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }
    }
}

