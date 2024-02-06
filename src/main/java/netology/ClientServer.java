package netology;

import java.util.Scanner;

public class ClientServer {
    public static void main(String[] args) {
        ChatClient client = new ChatClient();

         Scanner scanner = new Scanner(System.in);
         System.out.print("Enter your username: ");
         client.username = scanner.nextLine();

        client.start();
    }

}
