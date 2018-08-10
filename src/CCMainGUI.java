import modules.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public class CCMainGUI extends JFrame implements ActionListener {
    private static final int serverPort = 8989;
    static final String MODULE = "CREATURESANDCAVERNS";

    private Game game = new Game();
    private BufferedReader in;
    private PrintWriter out;


    private JFrame frame = new JFrame("Caverns and Creatures");
    private JLayeredPane contentPane = new JLayeredPane();
    private JTextArea chatFieldTXT;
    private JLabel scoreBoardLBL = new JLabel();
    private JLabel imgBackground = new JLabel();
    private JLabel topBackground = new JLabel();
    private JLabel dragonIconLBL = new JLabel();
    private JLabel player1LBL = new JLabel();
    private JLabel player2LBL = new JLabel();
    private JLabel player3LBL = new JLabel();
    private JLabel creature1LBL= new JLabel();
    private ImageIcon player1Icon, player2Icon, player3Icon;
    private JScrollPane scrollChatTxt;
    private JTextField submitFieldTXT = new JTextField(75);
    private JButton sendButton = new JButton("Send");
    private JButton attackButton = new JButton("Roll Attack", createImageIcon("sword.png"));
    private JButton rollButton = new JButton("Roll", createImageIcon("d20-blank.png"));
    private JButton addCreatureButton = new JButton("Add Creature");
    private JButton startGameButton;
    private JButton initiateTurnButton = new JButton("End Turn");;
    private JComboBox<String> targetComboBox = new JComboBox<>();;
    private Actor playerActor;
    private int attackRoll = 0, damageRoll = 0;
    private String username, playerCharacter, target;
    private boolean playerDeath = false;
    private boolean playerTurn = false;

    public CCMainGUI() {
        setTitle("Caverns and Creatures");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(10, 10, 905, 700);
        setIconImage((createImageIcon("dragonicon.png")).getImage());
        contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        contentPane.setBackground(new Color( 216,234,240));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setOpaque(true);

        imgBackground.setOpaque(true);

        imgBackground.setIcon(createImageIcon("dark_field.jpg"));
        topBackground.setIcon(createImageIcon("dragonbackground.png"));
        dragonIconLBL.setIcon(createImageIcon("dragonicon.png"));
        scoreBoardLBL.setForeground(Color.WHITE);

        topBackground.setBounds(0, 0, 905, 140);
        imgBackground.setBounds(0, 0, 905, 700);
        dragonIconLBL.setBounds(840, 102, 50, 50);
        scoreBoardLBL.setBounds(0, 0, 905, 140);

        contentPane.add(topBackground,JLayeredPane.PALETTE_LAYER);
        contentPane.add(imgBackground,JLayeredPane.DEFAULT_LAYER);
        contentPane.add(dragonIconLBL,JLayeredPane.MODAL_LAYER);

        displayChat();
        displayGameBoard();
        createGameControls();

    }
    private void displayRoll(int roll){
        rollButton.setFont(new Font("Arial", Font.BOLD, 22));
        rollButton.setText(Integer.toString(roll));
    }
    private void displayGameBoard(){
        attackButton.addActionListener(evt -> {
            if(attackRoll == 0) {
                attackRoll = playerActor.rollAttack();
                displayRoll(attackRoll);
                chatFieldTXT.append("Attack Roll: " + attackRoll + "\n");
                attackButton.setText("Roll Damage");

            }
            else {
                damageRoll = playerActor.rollDamage();
                chatFieldTXT.append("Damage Roll: " + damageRoll + "\n");
                displayRoll(damageRoll);
                attackButton.setEnabled(false);
                attackButton.setVisible(false);
                attackButton.setText("Roll Attack");
                player1Icon.getImage().flush(); //causes animation to continue
                attackRoll = 0;

            }
        });
        rollButton.addActionListener(evt -> {
            Die d20 = new Die(20);
            int roll = d20.rollDie();
            chatFieldTXT.append("You rolled a " + roll + "!\n");
            displayRoll(roll);
        });
        addCreatureButton.addActionListener(evt -> {
            sendJson(JSONLibrary.sendAddCreature());
        });

        targetComboBox.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            if(!cb.getSelectedItem().equals("--Target--")){
                target = (String)cb.getSelectedItem();
                chatFieldTXT.append("Target : " + target + "\n");
            }
        });

        initiateTurnButton.addActionListener(e-> {
            if (!attackButton.isEnabled() && !targetComboBox.getSelectedItem().equals("--Target--")) {
                sendJson(JSONLibrary.sendInitiateTurn(username, target, attackRoll, damageRoll));
                if (!attackButton.isEnabled()) {
                    sendJson(JSONLibrary.sendInitiateTurn(username, target, attackRoll, damageRoll));
                    initiateTurnButton.setEnabled(false);
                    playerTurn = false;
                } else {
                    chatFieldTXT.append("You must roll attack and damage and select a target to initiate combat.\n");
                }
            }
        });

        contentPane.add(targetComboBox,JLayeredPane.MODAL_LAYER);
        contentPane.add(initiateTurnButton,JLayeredPane.MODAL_LAYER);

        targetComboBox.addItem("--Target--");


        scoreBoardLBL.setOpaque(false);
        player1LBL.setOpaque(false);
        rollButton.setOpaque(false);
        targetComboBox.setVisible(false);
        initiateTurnButton.setVisible(false);

        attackButton.setEnabled(true);
        attackButton.setContentAreaFilled(false);
        attackButton.setBorder(BorderFactory.createEmptyBorder());
        attackButton.setForeground(new Color( 161,81,55));
        attackButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        attackButton.setHorizontalTextPosition(AbstractButton.CENTER);
        rollButton.setEnabled(true);
        rollButton.setBorder(BorderFactory.createEmptyBorder());
        rollButton.setContentAreaFilled(false);
        rollButton.setBorderPainted(false);
        rollButton.setForeground(new Color( 255,255,255));
        rollButton.setFont(new Font("Arial", Font.PLAIN, 38));
        rollButton.setVerticalTextPosition(AbstractButton.CENTER);
        rollButton.setHorizontalTextPosition(AbstractButton.CENTER);
        player1LBL.setVerticalTextPosition(AbstractButton.CENTER);
        player1LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player1LBL.setForeground(Color.WHITE);
        player2LBL.setVerticalTextPosition(AbstractButton.BOTTOM);
        player2LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player2LBL.setForeground(Color.WHITE);
        player3LBL.setVerticalTextPosition(AbstractButton.BOTTOM);
        player3LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player3LBL.setForeground(Color.WHITE);
        addCreatureButton.setEnabled(true);

        attackButton.setBounds(0,249,100,100);
        rollButton.setBounds(0,140,100,109);
        addCreatureButton.setBounds(100,492,175,23);
        player1LBL.setBounds(440,250,240,160);
        player2LBL.setBounds(540,275,240,160);
        player3LBL.setBounds(640,300,240,160);
        creature1LBL.setBounds(200,275,240,160);
        targetComboBox.setBounds(105, 225, 300, 25);
        initiateTurnButton.setBounds(0, 415, 100, 100);


        contentPane.add(attackButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(rollButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(addCreatureButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(player1LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player2LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player3LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(creature1LBL,JLayeredPane.MODAL_LAYER);
    }
    private void displayChat(){
        chatFieldTXT = new JTextArea(20, 75);
        scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        submitFieldTXT.setBounds(5, 627, 795, 25);
        sendButton.setBounds(800, 627, 84, 23);
        scrollChatTxt.setBounds(5,515,880,110);

        chatFieldTXT.setEditable(false);
        sendButton.addActionListener(this);
        submitFieldTXT.addActionListener(this);
        sendButton.setEnabled(true);

        contentPane.add(scoreBoardLBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(scrollChatTxt,JLayeredPane.MODAL_LAYER);
        contentPane.add(submitFieldTXT,JLayeredPane.MODAL_LAYER);
        contentPane.add(sendButton,JLayeredPane.MODAL_LAYER);
    }
    private void createGameControls() {
        startGameButton = new JButton("Start Game");

        targetComboBox = new JComboBox();
        targetComboBox.addItem("--Target--");
        initiateTurnButton = new JButton("Initiate Turn");


        targetComboBox.setVisible(false);
        initiateTurnButton.setVisible(false);

        startGameButton.addActionListener(e->{
            sendJson(JSONLibrary.sendStartGame());
            startGameGuiVisibility();
        });

        startGameButton.setBounds(105, 175, 300, 25);

        contentPane.add(startGameButton,JLayeredPane.MODAL_LAYER);

    }

    private void startGameGuiVisibility() {
        startGameButton.setEnabled(false);
        attackButton.setVisible(true);
        attackButton.setEnabled(false);
        targetComboBox.setVisible(true);
        targetComboBox.setEnabled(false);
        initiateTurnButton.setVisible(true);
        initiateTurnButton.setEnabled(false);
    }

    private void setupGame(){
        //Just a test//
        game.addRandomMonster();
        //String[] nameActors = game.getNames();
        //String[] actorStats = game.getScoreboard();
        //displayScoreboard(nameActors,actorStats);

        //populate Actor test
        //player1LBL.setText(username);
        player1Icon = createImageIcon(playerActor.getType()+".gif");
        player2Icon = createImageIcon("Mage.gif");
        player3Icon = createImageIcon("Rogue.gif");
        player1LBL.setIcon(createImageIcon(playerActor.getType()+".gif"));
        player2LBL.setIcon(player2Icon); //hard coded but you get the idea
        player3LBL.setIcon(player3Icon); //hard coded but you get the idea
        creature1LBL.setIcon(createImageIcon("Dragon.gif"));

    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CCMainGUI.class.getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
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
        scoreBoardLBL.setText(scoreboard);
    }
    private void sendJson(String json){
        out.println(json);
        out.flush();
    }

    public void run() {

        boolean isConnected = connectToServer();
        username = getUser();
        sendJson(JSONLibrary.sendUser(username));

        playerCharacter = getPlayerCharacter();
        sendJson(JSONLibrary.sendPlayerCharacter(playerCharacter, username));

        setupGame();

        Runnable gameOn = () -> {
            JSONObject json;
            String serverMsg;
            while(true){
                try{
                    serverMsg = in.readLine();
                    System.out.println(serverMsg);
                    json = new JSONObject(serverMsg);
                    messageHandler(json);
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }; new Thread(gameOn).start();
    }

    private void messageHandler(JSONObject json){
        if(json.get("type").equals("acknowledge")){
            return;
        }
        if(json.get("module") != null || MODULE.equals(json.get("module").toString())) {
            String action = json.get("action").toString();
            switch (action) {
                case "startGame"        :       startGameGuiVisibility();
                    break;
                case "battleReport"     :       updateBattleReport(json.get("battleReport").toString());
                    break;
                case "scoreBoard"       :       updateScoreboard(json);
                    break;
                case "playerDeath"      :       youAreDead();
                    break;
                case "yourTurn"         :       yourTurn();
                    break;
                case "gameOver"         :       gameOver(json.get("winner").toString());
                    break;
                case "targetNames"      :       updateComboTargetBox(json);
                    break;
            }
        }
    }

    private void updateBattleReport(String battleReport) {
        chatFieldTXT.append("Battle Report: " + battleReport + "\n");
        submitFieldTXT.selectAll();
        chatFieldTXT.setCaretPosition(chatFieldTXT.getDocument().getLength());
    }

    private void updateScoreboard(JSONObject json) {
        JSONArray p = json.getJSONArray("playerNames");
        JSONArray c = json.getJSONArray("colorActorStats");

        String[] playerNames = new String[p.length()];
        String[] colorActorStats = new String[c.length()];

        for(int i = 0; i < p.length(); i++){
            playerNames[i] = p.getString(i);
        }
        for(int i = 0; i < c.length(); i++){
            colorActorStats[i] = c.getString(i);
        }

        displayScoreboard(playerNames, colorActorStats);
    }

    private void youAreDead() {
        //TODO DISPLAY YOU ARE DEAD PROMPT
    }

    private void yourTurn(){
        //TODO DISPLAY YOUR TURN PROMPT
        targetComboBox.setEnabled(true);
        initiateTurnButton.setEnabled(true);
        attackButton.setEnabled(true);
    }

    private void gameOver(String winner){
        if(username.equalsIgnoreCase(winner)){
            //TODO VICTORY GUI MESSAGE
        } else {
            // TODO DEFEAT GUI MESSAGE
        }
    }

    private void updateComboTargetBox(JSONObject json){
        JSONArray jsonArray = json.getJSONArray("targetNames");
        for(int i = 0; i < jsonArray.length(); i++){
            if(((DefaultComboBoxModel)targetComboBox.getModel()).getIndexOf(jsonArray.get(i)) == -1){
                targetComboBox.addItem(jsonArray.getString(i));
            }
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