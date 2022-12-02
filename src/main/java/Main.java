import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        List<PageEntry> object = new ArrayList<>();
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
                    BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
                    List<PageEntry> searchList = engine.search(word);
                    if (searchList.get(0).compareTo(searchList.get(0)) > 0) {
                        List<PageEntry> searchListInterm = new ArrayList<>();
                        searchListInterm.add(searchList.get(0));
                    }
                    while (true) {
                        int count = 0;
                        for (int i = 0; i < searchList.size() - 1; i++) {
                            int compare1 = searchList.get(i + 1).compareTo(searchList.get(i + 1));
                            int compare2 = searchList.get(i).compareTo(searchList.get(i));
                            if (compare1 > compare2) {
                                List<PageEntry> searchListInterm = new ArrayList<>();
                                searchListInterm.add(searchList.get(i + 1));
                                searchListInterm.add(searchList.get(i));
                                searchList.remove(i + 1);
                                searchList.add(i + 1, searchListInterm.get(1));
                                searchList.remove(i);
                                searchList.add(i, searchListInterm.get(0));
                                count++;
                            }
                            if (count != 0 && i == searchList.size()) {
                                i = -1;
                            }
                        }
                        if (count == 0) {
                            break;
                        }
                    }
                    for (int i = 0; i < searchList.size(); i++) {
                        PageEntry page = searchList.get(i);
                        object.add(page);
                    }
                    // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
                    //Записываем ответ в виде json формата
                    String json = new Gson().toJson(object);
                    out.println(json);
                } catch (IOException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }
    }
}

