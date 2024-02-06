package netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345;
    private static final String SETTINGS_FILE = "ShatJava/srs/settings.txt";
    private static final String LOG_FILE = "file.log";

    private List<ClientHandler> clients;
    private SimpleDateFormat dateFormat;

    public ChatServer() {
        clients = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void start()  {
        try {

            loadSettings();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadSettings() throws IOException {
        File file = null;
        try {
            file = new File("settings.txt");
            if (file.createNewFile()) {
                System.out.println("Settings file created");
            } else {
                System.out.println("File exists");
            }
        } catch (IOException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }


        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    private synchronized void broadcastMessage(String message) {
        String timestamp = dateFormat.format(new Date());
        String formattedMessage = "[" + timestamp + "] " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter("file.log", true))) {
            writer.println(formattedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ClientHandler client : clients) {
            client.sendMessage(formattedMessage);
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);

                String username = reader.readLine();
                System.out.println("New user connected: " + username);

                String message;
                while ((message = reader.readLine()) != null) {
                    broadcastMessage(username + ": " + message);
                }

                clients.remove(this);
                socket.close();
                System.out.println("User disconnected: " + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String message) {
            writer.println(message);
        }
    }
}
