import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class CCMainGUI extends JFrame implements ActionListener {
    private static final int serverPort = 8989;

    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Caverns and Creatures");
    private JPanel contentPane = new JPanel();
    private JTextArea chatFieldTXT = new JTextArea(20, 75);;
    private JScrollPane scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JTextField submitFieldTXT = new JTextField(75);
    private JButton sendButton = new JButton("Send");

    public CCMainGUI() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 805, 650);

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);


        submitFieldTXT.addActionListener(this);

        sendButton.addActionListener(this);
        sendButton.setEnabled(true);


        submitFieldTXT.setBounds(5, 577, 695, 25);
        sendButton.setBounds(700, 577, 89, 23);
        scrollChatTxt.setBounds(5,465,780,110);
        chatFieldTXT.setEditable(false);

        contentPane.add(scrollChatTxt);
        contentPane.add(submitFieldTXT);
        contentPane.add(sendButton);

    }


    private String getUser() {
        return JOptionPane.showInputDialog(
                contentPane,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    private String getServerAddress() {
        String serverName = JOptionPane.showInputDialog(contentPane,
                "Server name or IP", "ec2-18-207-150-67.compute-1.amazonaws.com");
        return  serverName;
    }

    public void run() {
        System.out.println("Hi");
        // Make connection and initialize streams
        String serverAddress = getServerAddress();
        Socket socket = null;
        try {
            socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Process all messages from server, according to the protocol.
            while (true) {
                String line = in.readLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getUser());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    submitFieldTXT.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    chatFieldTXT.append(line.substring(8) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = submitFieldTXT.getText();
        chatFieldTXT.append(": " + text + "\n");
        submitFieldTXT.selectAll();
        chatFieldTXT.setCaretPosition(chatFieldTXT.getDocument().getLength());
    }

    public static void main(String[] args) throws IOException {
        try {
            CCMainGUI frame = new CCMainGUI();
            frame.setVisible(true);
            frame.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
