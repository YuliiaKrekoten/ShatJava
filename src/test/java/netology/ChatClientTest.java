package netology;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.fail;

class ChatClientTest {
    @Test
    public void testStart_SuccessfulConnection() {
        // Create a mock server socket
        ServerSocket serverSocket = Mockito.mock(ServerSocket.class);
        Socket socket = Mockito.mock(Socket.class);
        BufferedReader serverReader = Mockito.mock(BufferedReader.class);
        PrintWriter writer = Mockito.mock(PrintWriter.class);

        try {
            // Mock the behavior of server socket and socket
            Mockito.when(serverSocket.accept()).thenReturn(socket);
            Mockito.when(socket.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
            Mockito.when(socket.getOutputStream()).thenReturn(Mockito.mock(OutputStream.class));

            // Mock the behavior of server reader and writer
            Mockito.when(serverReader.readLine()).thenReturn("Welcome to the chat!");

            // Create a new ChatClient instance
            ChatClient chatClient = new ChatClient();
            chatClient.start();

            // Verify that the connection to the server was successful
            Mockito.verify(socket).getOutputStream();
            Mockito.verify(writer).println(chatClient.username);
            Mockito.verify(socket).getInputStream();
            Mockito.verify(serverReader).readLine();

        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

    }

    @Test
    public void testStart_SendMessage() {
        // Create a mock socket
        Socket socket = Mockito.mock(Socket.class);
        BufferedReader serverReader = Mockito.mock(BufferedReader.class);
        PrintWriter writer = Mockito.mock(PrintWriter.class);

        try {
            // Mock the behavior of socket
            PowerMockito.whenNew(Socket.class).withAnyArguments().thenReturn(socket);
            Mockito.when(socket.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
            Mockito.when(socket.getOutputStream()).thenReturn(Mockito.mock(OutputStream.class));

            // Mock the behavior of server reader and writer
            Mockito.when(serverReader.readLine()).thenReturn("Welcome to the chat!");

            // Create a new ChatClient instance
            ChatClient chatClient = new ChatClient();
            chatClient.start();

            // Send a message to the server
            Mockito.verify(writer).println(chatClient.username);
            Mockito.verify(writer).println("Hello Server!");

        } catch (IOException e) {
            fail("Exception should not be thrown");
        } catch (InterruptedException e) {
            fail("Thread interruption error");
        } catch (Exception e) {
            fail("Error mocking Socket creation");
        }
    }
}