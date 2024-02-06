package netology;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final String SETTINGS_FILE = "settings.txt";
    private static final String LOG_FILE = "file.log";

    String username;
    private SimpleDateFormat dateFormat;

    public ChatClient() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void start() {
        loadSettings();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to server: " + socket);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println(username);

            Thread receivingThread = new Thread(() -> {
                try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = serverReader.readLine()) != null) {
                        System.out.println(message);
                        logMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receivingThread.start();

            String message;
            while ((message = reader.readLine()) != null) {
                writer.println(message);

                if ("/exit".equals(message)) {
                    break;
                }
            }

            receivingThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() {


        try (BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Load settings from file
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }


    private void logMessage(String message) {
        String timestamp = dateFormat.format(new Date());
        String formattedMessage = "[" + timestamp + "] " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter("file.log", true))) {
            writer.println(formattedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
