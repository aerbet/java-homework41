import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Server {
    private final int port;

    private Server(int port) {
        this.port = port;
    }

    public static Server bindToServer(int port) {
        return new Server(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            try(Socket clientSocket = server.accept()) {
                handle(clientSocket);
            }  catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (IOException ioe) {
            System.out.printf("Could not listen on port: %s", port);
            ioe.printStackTrace();
        }
    }

    private void handle(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter printWriter = new PrintWriter(output);

        try (Scanner scanner = new Scanner(inputStreamReader)) {
            while (true) {
                String message = scanner.nextLine().trim();
                System.out.printf("Got message: %s%n", message);

                StringBuilder builder = new StringBuilder();
                String reversedMessage = builder.append(message).reverse().toString();
                printWriter.print(reversedMessage);
                printWriter.write(System.lineSeparator());
                printWriter.flush();

                System.out.println("Reversing message...");
                if (message.equalsIgnoreCase("stop")) {
                    System.out.println("Stopping server...");
                    return;
                }
            }
        } catch (NoSuchElementException nsee) {
            System.out.println("Client disconnected");
        }

    }
}
