import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStream;

public class Client {
    private final int port;
    private final String host;

    private Client(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public static Client connectToServer(int port) {
        String host = "127.0.0.1";
        return new Client(port, host);
    }

    public void run() {
        System.out.println("Write 'stop' to quit server");

        try(Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            try (scanner; printWriter; InputStream input = socket.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                while (true) {
                    String command = scanner.nextLine();
                    printWriter.write(command);
                    printWriter.write(System.lineSeparator());
                    printWriter.flush();

                    if(command.equalsIgnoreCase("stop")) {
                        return;
                    }

                    String line = reader.readLine();
                    System.out.println("Server: " + line);
                }
            }
        } catch (NoSuchElementException nsee) {
            System.out.println("Connection dropped");
        } catch (IOException ioe) {
            System.out.printf("Can't connect to %s:%s %n", host, port);
            ioe.printStackTrace();
        }

    }
}
