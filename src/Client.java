import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer chatServer;
    String properName;
    String anotherName;
    public Client(Socket socket, ChatServer chatServer){
        this.socket = socket;
        this.chatServer=chatServer;
        // запускаем поток
        Thread thread = new Thread(this);
        thread.start();
    }

    public void receive(String message){
        out.println(message);
    }
    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Welcome to chat!\n Write your name please:");
            properName=in.nextLine();
            out.println("Who would you like to talk to?");
            anotherName=in.nextLine();
            out.println("If you want to switch person say Switch and than write another name");
            String input = in.nextLine();
            while (!input.equals("bye")) {
                if(input.equals("switch")){
                    anotherName=in.nextLine();
                }else {
                    chatServer.sendAll(input, properName, anotherName);
                }
                input = in.nextLine();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
