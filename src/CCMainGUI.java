import modules.Actor;
import modules.ActorPresets;
import modules.JSONLibrary;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class CCMainGUI extends JFrame implements ActionListener {
    private static final int serverPort = 8989;
    private JPanel contentPane;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea chatFieldTXT;
    private JLabel scoreboardlbl;
    private JScrollPane scrollChatTxt;
    private JTextField submitFieldTXT;
    private JButton sendButton, startGameButton, addCreatureButton, attackButton, damageButton, initiateTurnButton;
    private JComboBox<String> playerComboBox;
    private String username, playerCharacter, target;
    private Actor playerActor;
    private int attackRoll, damageRoll;
    private boolean playerDeath = false;
    private boolean playerTurn = false;

    public CCMainGUI() {
        setTitle("Caverns and Creatures");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 905, 700);

        contentPane = new JPanel();
        chatFieldTXT = new JTextArea(20, 75);
        scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scoreboardlbl = new JLabel();
        submitFieldTXT = new JTextField(75);

        contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        scoreboardlbl.setOpaque(true);

        createGameControls();

        createChatArea();
    }

    private void createChatArea() {
        sendButton = new JButton("Send");
        submitFieldTXT.addActionListener(this);

        sendButton.addActionListener(this);
        sendButton.setEnabled(true);
        chatFieldTXT.setEditable(false);
        scoreboardlbl.setBounds(0, 0, 880, 140);

        submitFieldTXT.setBounds(5, 627, 795, 25);
        sendButton.setBounds(800, 627, 84, 23);
        scrollChatTxt.setBounds(5,515,880,110);

        contentPane.add(scoreboardlbl);
        contentPane.add(scrollChatTxt);
        contentPane.add(submitFieldTXT);
        contentPane.add(sendButton);
    }

    private void createGameControls() {
        startGameButton = new JButton("Start Game");
        addCreatureButton = new JButton("Add Creature");
        attackButton = new JButton("Roll Attack");
        damageButton = new JButton("Roll Damage");
        playerComboBox = new JComboBox<>();
        playerComboBox.addItem("--Target--");
        initiateTurnButton = new JButton("Initiate Turn");

        addCreatureButton.setVisible(false);
        attackButton.setVisible(false);
        damageButton.setVisible(false);
        playerComboBox.setVisible(false);
        initiateTurnButton.setVisible(false);

        startGameButton.addActionListener(e-> {
            sendJson(JSONLibrary.sendStartGame());
            startGameButton.setVisible(false);
        });

        startGameButton.addActionListener(e->{
            sendJson(JSONLibrary.sendStartGame());
            startGameButton.setEnabled(false);
            attackButton.setVisible(true);
            damageButton.setVisible(true);
            playerComboBox.setVisible(true);
            initiateTurnButton.setVisible(true);
        });

        attackButton.addActionListener(e->{
            attackRoll = playerActor.rollAttack();
            chatFieldTXT.append("Attack Roll: " + attackRoll + "\n");
            attackButton.setEnabled(false);
        });

        damageButton.addActionListener(e->{
            damageRoll = playerActor.rollDamage();
            chatFieldTXT.append("Damage Roll: " + damageRoll + "\n");
            damageButton.setEnabled(false);
        });

        playerComboBox.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            if(!cb.getSelectedItem().equals("--Target--")){
                target = (String)cb.getSelectedItem();
                chatFieldTXT.append("Target : " + target + "\n");
            }
        });

        initiateTurnButton.addActionListener(e-> {
            if (!attackButton.isEnabled() && !damageButton.isEnabled() && !playerComboBox.getSelectedItem().equals("--Target--")) {
                    sendJson(JSONLibrary.sendInitiateTurn(username, target, attackRoll, damageRoll));
                if (!attackButton.isEnabled() && !damageButton.isEnabled()) {
                    sendJson(JSONLibrary.sendInitiateTurn(username, target, attackRoll, damageRoll));
                    initiateTurnButton.setEnabled(false);
                    playerTurn = false;
                } else {
                    chatFieldTXT.append("You must roll attack and damage and select a target to initiate combat.\n");
                }
            }
        });

        addCreatureButton.addActionListener(e->{
            sendJson(JSONLibrary.sendAddCreature());
        });

        startGameButton.setBounds(5, 175, 300, 25);
        addCreatureButton.setBounds(155, 175, 300, 25);
        attackButton.setBounds(5,200,150,25);
        damageButton.setBounds(155, 200, 150, 25);
        playerComboBox.setBounds(5, 225, 300, 25);
        initiateTurnButton.setBounds(5, 250, 300, 25);

        contentPane.add(startGameButton);
        contentPane.add(addCreatureButton);
        contentPane.add(attackButton);
        contentPane.add(damageButton);
        contentPane.add(playerComboBox);
        contentPane.add(initiateTurnButton);
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

    private void sendJson(String json){
        out.println(json);
        out.flush();
    }

    private String getPlayerCharacter(){
        String[] options = new String[]{"Fighter", "Rogue", "Mage"};
        int response = JOptionPane.showOptionDialog(contentPane, "Choose Your Character!",
                "Player Character Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);
        switch(response){
            case 0 : playerActor = new ActorPresets().playerPresets.get("Fighter");
                return "Fighter";
            case 1 : playerActor = new ActorPresets().playerPresets.get("Rogue");
                return "Rogue";
            case 2 : playerActor = new ActorPresets().playerPresets.get("Mage");
                return "Mage";
        }
        return null;
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

    //Takes arrays of player names and player scoreboards from game running on server
    private void displayScoreboard(String[] playerNames, String[] colorActorStats) {
        String scoreboard = "<HTML><TABLE ALIGN=TOP BORDER=0  cellspacing=2 cellpadding=2><TR>";
        for(String actorName: playerNames){
            scoreboard += "<TH><H2>" + actorName + "</H2></TH>";
        }
        scoreboard += "</TR><TR>";
        int count = 0;
        for(String anActor: colorActorStats){
            if(count % 2 == 0){
                scoreboard += "<TD BGCOLOR=" + anActor + ">";
            }
            else {
                scoreboard += anActor + "</TD>";
            }
            count++;
        }
        scoreboard += "</TR></TABLE></HTML>";
        scoreboardlbl.setText(scoreboard);
    }

    public void run() {
        boolean isConnected = connectToServer();
        username = getUser();
        sendJson(JSONLibrary.sendUser(username));

        playerCharacter = getPlayerCharacter();
        sendJson(JSONLibrary.sendPlayerCharacter(playerCharacter, username));

        // Process all messages from server, according to the protocol.
        while (true) {
            //THIS IS WHERE THE SERVER COMMUNICATES WITH THE UI!!!!!!!!!!!!!
            //Put Handler here...
            //TODO use readServerMessage method.
        }
    }

    private void readServerMessage(JSONObject json){
        String action = json.getString("action");

        switch(action){
            case "startGame"        :   //TODO
                break;
            case "battleReport"     :   //TODO
                break;
            case "scoreboard"       :   //TODO
                break;
            case "playerDeath"      :   //TODO
                break;
            case "yourTurn"         :   //TODO
                break;
            case "gameOver"         :   //TODO
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = submitFieldTXT.getText();
        chatFieldTXT.append(username + ": " + text + "\n");
        submitFieldTXT.selectAll();
        chatFieldTXT.setCaretPosition(chatFieldTXT.getDocument().getLength());
        sendJson(JSONLibrary.sendChatMessage(text));
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