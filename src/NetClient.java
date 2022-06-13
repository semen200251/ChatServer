import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NetClient extends JFrame implements KeyListener {

    final String serverIP = "127.0.0.1";
    final int serverPort = 1234;

    JTextArea textArea;
    JScrollPane scrollPane;
    InputStreamReader in;
    PrintWriter out;

    NetClient() {
        // ������� ����

        super("Simple Chat client");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ��������� �� ���� ��������� ����
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);

        // �������������� � �������
        connect();

    }

    void connect() {
        try {
            Socket socket = new Socket(serverIP, serverPort);
            in = new InputStreamReader(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            textArea.addKeyListener(this);
        } catch (IOException e) {
            textArea.setForeground(Color.RED);
            textArea.append("Server " + serverIP + " port " + serverPort + " " + "" + "NOT AVAILABLE");
            e.printStackTrace();
        }
        new Thread() {
            // � ��������� ������
            // ��������� ������� �� �������
            public void run() {
                while (true) {
                    try {
                        addCharToTextArea((char) (in.read()));
                    } catch (IOException e) {
                        textArea.setForeground(Color.RED);
                        textArea.append("\nCONNECTION ERROR");
                        e.printStackTrace();
                        return;
                    }
                }
            };
        }.start();

    }

    public static void main(String[] args) {
        new NetClient().setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent arg0) {}
    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {
        // ���������� ������������ ������ � ���� � �� �����
        out.print(arg0.getKeyChar());
        out.flush();

        System.out.print((int)(arg0.getKeyChar()));
        addCharToTextArea(arg0.getKeyChar());
    }

    void addCharToTextArea(char c) {
        textArea.append(c + "");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}
