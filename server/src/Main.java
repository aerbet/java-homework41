

public class Main {
    public static void main(String[] args) {

//        Server server = new Server(8089);
//        server.run();

        // паттерн фабрика
        Server.bindToServer(8089).run();

    }
}