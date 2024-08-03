package telran;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyServerAppl {

    private static final String FILE_NAME = "employeesTest.data";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        Company company = new CompanyMapsImpl();
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try {
                ((Persistable)company).restore(FILE_NAME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Protocol protocol = new CompanyProtocol(company);
        TcpServer tcpServer = new TcpServer(protocol, PORT);

        Thread serverThread = new Thread(() -> tcpServer.run());
        serverThread.start();

        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            String command;
            while (!(command = consoleReader.readLine()).equalsIgnoreCase("shutdown")) {
                System.out.println("Введите 'shutdown' для остановки сервера.");
            }
            tcpServer.shutdown();
            serverThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ((Persistable)company).save(FILE_NAME);
        }
    }
}