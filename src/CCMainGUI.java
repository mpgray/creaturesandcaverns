
import modules.Actor;
import modules.ActorPresets;
import modules.Game;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;

public class CCMainGUI extends JFrame implements ActionListener {
    private static final int serverPort = 8989;
    private Game game = new Game();
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame = new JFrame("Caverns and Creatures");
    private JPanel contentPane = new JPanel();
    private JTextArea chatFieldTXT = new JTextArea(20, 75);
    private JLabel scoreBoardLBL = new JLabel();
    private JScrollPane scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JTextField submitFieldTXT = new JTextField(75);
    private JButton sendButton = new JButton("Send");
    private JButton attackButton = new JButton("Attack");
    private JButton addCreatureButton = new JButton("Add Creature");
    private String username;
    private String playerCharacter;


    public CCMainGUI() {
        setTitle("Caverns and Creatures");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 905, 700);

        contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        scoreBoardLBL.setOpaque(true);

        submitFieldTXT.addActionListener(this);
        attackButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatFieldTXT.append("Attack");
            }
        });
        addCreatureButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                game.addRandomMonster();
                scoreBoardLBL.updateUI();
            }
        });

        sendButton.addActionListener(this);
        sendButton.setEnabled(true);
        attackButton.setEnabled(true);
        addCreatureButton.setEnabled(true);
        chatFieldTXT.setEditable(false);

        scoreBoardLBL.setBounds(0, 0, 905, 140);
        attackButton.setBounds(5,175,84,23);
        addCreatureButton.setBounds(5,200,175,23);
        submitFieldTXT.setBounds(5, 627, 795, 25);
        sendButton.setBounds(800, 627, 84, 23);
        scrollChatTxt.setBounds(5,515,880,110);


        contentPane.add(scoreBoardLBL);
        contentPane.add(scrollChatTxt);
        contentPane.add(submitFieldTXT);
        contentPane.add(sendButton);
        contentPane.add(attackButton);
        contentPane.add(addCreatureButton);

    }

    private String getUser() {
        String username = JOptionPane.showInputDialog(
                contentPane,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
        chatFieldTXT.append("Your username is now " + username + "\n");
        return username;
    }

    private void sendUser(String user){
        JSONObject loginMessage = new JSONObject();
        JSONObject username = new JSONObject();

        loginMessage.put("type", "login");
        username.put("username", user);
        loginMessage.put("message", username);

        out.println(loginMessage.toString());
    }

    private String getPlayerCharacter(){
        String[] options = new String[]{"Fighter", "Rogue", "Mage"};
        int response = JOptionPane.showOptionDialog(contentPane, "Choose Your Character!",
                "Player Character Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);
        switch(response){
            case 0 : return "Fighter";
            case 1 : return "Rogue";
            case 2 : return "Mage";
        }
        return null;
    }

    private void sendPlayerCharacter(String playerCharacter){
        JSONObject characterMessage = new JSONObject();
        JSONObject character = new JSONObject();

        characterMessage.put("type", "application");
        character.put("player", playerCharacter);
        characterMessage.put("message", character);
        System.out.println(playerCharacter);
        out.println(playerCharacter);
    }

    private String getServerAddress() {
        String serverName = JOptionPane.showInputDialog(contentPane,
                "Server name or IP", "ec2-18-207-150-67.compute-1.amazonaws.com");
        return  serverName;
    }

    private boolean connectToServer(){
        String serverAddress = getServerAddress();
        Socket socket = null;

        try {
            socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
            chatFieldTXT.append("Could not connect to " + serverAddress + ". Continuing anyway for testing. \n");
            return false;
        }
        chatFieldTXT.append("Connected to " + serverAddress + " successfully.\n");
        return true;
    }

    private String displayScoreBoard(Game game) {
        String scoreBoard = "<HTML><TABLE ALIGN=TOP BORDER=0  cellspacing=2 cellpadding=2><TR>";
        for(String actorName: game.getNames()){
            scoreBoard += "<TH><H2>" + actorName + "</H2></TH>";
        }
        scoreBoard += "</TR><TR>";
        int count = 0;
        for(String anActor: game.getScoreBoard()){
            if(count % 2 == 0){
                scoreBoard += "<TD BGCOLOR=" + anActor + ">";
            }
            else {
                scoreBoard += anActor + "</TD>";
            }
            count++;
        }
        scoreBoard += "</TR></TABLE></HTML>";
        return scoreBoard;
    }


    public void run() {


        boolean isConnected = connectToServer();
        username = getUser();
        sendUser(username);

        playerCharacter = getPlayerCharacter();
        game.addPlayer(username, playerCharacter);

        String JSONtestApp= "{\"type\": \"application\", \"message\": {\"module\": \"test\"}}";
        out.println(JSONtestApp);
        System.out.println(JSONtestApp);

        sendPlayerCharacter(playerCharacter);
        //Just a test//
        ActorPresets actorPresets = new ActorPresets();
        Actor player1 = actorPresets.playerPresets.get(playerCharacter);
        game.addPlayer(username, player1.getType());
        game.addRandomMonster();

        //for titles of UI
        for(String usernames: game.getNames()){
            chatFieldTXT.append(usernames + "\n");
        }

        // Process all messages from server, according to the protocol.
        while (true) {

            scoreBoardLBL.setText(displayScoreBoard(game));

            //THIS IS WHERE THE SERVER COMMUNICATES WITH THE UI!!!!!!!!!!!!!
            //Put Handler here...
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String text = submitFieldTXT.getText();
        chatFieldTXT.append(username + ": " + text + "\n");
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