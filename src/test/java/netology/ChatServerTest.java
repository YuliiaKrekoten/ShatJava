package netology;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ChatServerTest {

    @Test
    public void testLoadSettings() throws IOException, IOException {

        File tempFile = File.createTempFile("settings", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write("Setting 1: Value 1");
        writer.newLine();
        writer.write("Setting 2: Value 2");
        writer.close();

        ChatServer chatServer = new ChatServer();

        chatServer.loadSettings();

        tempFile.delete();
    }

}