import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8989;
    private static final String HOST = null;

    public static void main(String[] args) throws ParseException {
        //Открываем клиентский socket, используем try,catch, т.к. socket требует закрытия
        try (Socket clientSocket = new Socket(HOST, PORT);
             //Создаем поток вывода, flush - очистка буфера
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             //Создаем поток ввода
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String smth = in.readLine();
            System.out.println("Input word");
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            out.println(word);
            String fileIn = in.readLine();
            JSONParser parser = new JSONParser();
            try {
                Object object = parser.parse(fileIn);
                JSONArray jsonObject = (JSONArray) object;
                String jsonText = JSONValue.toJSONString(object);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                System.out.println(gson.toJson(object).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}