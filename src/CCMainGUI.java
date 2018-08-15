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

    private BufferedReader in;
    private PrintWriter out;

    private JLayeredPane contentPane;
    private JTextArea chatFieldTXT;
    private JLabel scoreBoardLBL, imgBackground, topBackground, dragonIconLBL, messageAlert, player1LBL, player2LBL, player3LBL, creature1LBL;
    private ImageIcon player1Icon, player2Icon, player3Icon;
    private JScrollPane scrollChatTxt;
    private JTextField submitFieldTXT;
    private JButton sendButton, attackButton, rollButton, addCreatureButton, startGameButton, initiateTurnButton;
    private DefaultComboBoxModel<String> cbModel;
    private JComboBox<String> targetComboBox;
    private Actor playerActor;
    private int attackRoll, damageRoll;
    private String username, playerCharacter, target;
    private boolean playerDeath, playerTurn;

    public CCMainGUI(){
        attackRoll = 0;
        damageRoll = 0;
        playerDeath = false;
        playerTurn = false;

        createContentPane();
        createChat();
        displayGameBoard();

        this.setTitle("Caverns and Creatures");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(10, 10, 905, 700);
        this.setIconImage((createImageIcon("dragonicon.png")).getImage());
        this.setContentPane(contentPane);
    }

    private void createContentPane() {
        contentPane = new JLayeredPane();
        contentPane.setBorder(new EmptyBorder(0, 5, 5, 5));
        contentPane.setBackground(new Color( 216,234,240));
        contentPane.setLayout(null);
        contentPane.setOpaque(true);

        imgBackground = new JLabel();
        imgBackground.setIcon(createImageIcon("dark_field.jpg"));
        imgBackground.setOpaque(true);
        imgBackground.setBounds(0, 0, 905, 700);

        topBackground = new JLabel();
        topBackground.setIcon(createImageIcon("dragonbackground.png"));
        topBackground.setBounds(0, 0, 905, 140);

        dragonIconLBL = new JLabel();
        dragonIconLBL.setIcon(createImageIcon("dragonicon.png"));
        dragonIconLBL.setBounds(840, 102, 50, 50);

        scoreBoardLBL = new JLabel();
        scoreBoardLBL.setForeground(Color.WHITE);
        scoreBoardLBL.setBounds(0, 0, 905, 140);
        scoreBoardLBL.setOpaque(false);

        messageAlert = new JLabel();
        messageAlert.setFont(new Font("Arial", Font.BOLD, 32));
        messageAlert.setForeground(Color.RED);
        messageAlert.setBounds(300,200,600,100);

        player1LBL = new JLabel();
        player2LBL = new JLabel();
        player3LBL = new JLabel();
        creature1LBL = new JLabel();

        contentPane.add(player1LBL);
        contentPane.add(player2LBL);
        contentPane.add(player3LBL);
        contentPane.add(creature1LBL);

        contentPane.add(topBackground,JLayeredPane.PALETTE_LAYER);
        contentPane.add(imgBackground,JLayeredPane.DEFAULT_LAYER);
        contentPane.add(dragonIconLBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(messageAlert,JLayeredPane.MODAL_LAYER);
        contentPane.add(scoreBoardLBL,JLayeredPane.MODAL_LAYER);
    }

    private void createChat(){
        chatFieldTXT = new JTextArea(20, 75);
        chatFieldTXT.setEditable(false);

        scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollChatTxt.setBounds(5,515,880,110);

        submitFieldTXT = new JTextField(75);
        submitFieldTXT.setBounds(5, 627, 795, 25);
        submitFieldTXT.addActionListener(this);

        sendButton = new JButton("Send");
        sendButton.setEnabled(true);
        sendButton.setBounds(800, 627, 84, 23);
        sendButton.addActionListener(this);

        contentPane.add(scrollChatTxt,JLayeredPane.MODAL_LAYER);
        contentPane.add(submitFieldTXT,JLayeredPane.MODAL_LAYER);
        contentPane.add(sendButton,JLayeredPane.MODAL_LAYER);
    }

    private void displayRoll(int roll){
        rollButton.setFont(new Font("Arial", Font.BOLD, 22));
        rollButton.setText(Integer.toString(roll));
    }

    private void displayGameBoard(){

        createInitiateTurnButton();

        createStartGameButton();

        createAttackButton();

        createRollButton();

        createPlayerLabels();

        createTargetComboBox();

        createAddCreatureButton();

        contentPane.add(startGameButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(attackButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(rollButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(addCreatureButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(targetComboBox,JLayeredPane.MODAL_LAYER);
        contentPane.add(initiateTurnButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(player1LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player2LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player3LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(creature1LBL,JLayeredPane.MODAL_LAYER);

    }

    private void createInitiateTurnButton() {
        initiateTurnButton = new JButton("Initiate Turn");
        initiateTurnButton.setBounds(360, 492, 175, 23);
        initiateTurnButton.addActionListener(e-> {
            if(!playerTurn){
                chatFieldTXT.append("It is not your turn.\n");
            }else if(!attackButton.isEnabled() && !targetComboBox.getSelectedItem().equals("--Target--")) {
                sendJson(JSONLibrary.sendInitiateTurn(username, target, attackRoll, damageRoll));
                playerTurn = false;
                attackRoll = 0;
                messageAlert.setText("");
                } else {
                    chatFieldTXT.append("You must roll attack and damage and select a target to initiate combat.\n");
                }
        });
    }

    private void createStartGameButton() {
        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(105, 175, 300, 25);
        startGameButton.addActionListener(e->{
            sendJson(JSONLibrary.sendStartGame());
            startGameGuiVisibility();
        });
    }

    private void createAddCreatureButton() {
        addCreatureButton = new JButton("Add Creature");
        addCreatureButton.setVisible(false);
        addCreatureButton.setBounds(5,492,175,23);
        addCreatureButton.addActionListener(evt -> {
            sendJson(JSONLibrary.sendAddCreature());
        });
    }

    private void createTargetComboBox() {
        cbModel = new DefaultComboBoxModel<>();
        cbModel.addElement("--Target--");
        targetComboBox = new JComboBox<>(cbModel);
        targetComboBox.setBounds(180, 492, 175, 23);
        targetComboBox.addActionListener(e->{
            JComboBox cb = (JComboBox)e.getSource();
            if(!cb.getSelectedItem().equals("--Target--")){
                target = (String)cb.getSelectedItem();
                chatFieldTXT.append("Target : " + target + "\n");
            }
        });
    }

    private void createPlayerLabels() {
        creature1LBL = new JLabel();
        creature1LBL.setBounds(200,275,240,160);

        player1LBL = new JLabel();
        player1LBL.setVerticalTextPosition(AbstractButton.CENTER);
        player1LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player1LBL.setForeground(Color.WHITE);
        player1LBL.setBounds(440,250,240,160);
        player1LBL.setOpaque(false);

        player2LBL = new JLabel();
        player2LBL.setVerticalTextPosition(AbstractButton.BOTTOM);
        player2LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player2LBL.setForeground(Color.WHITE);
        player2LBL.setBounds(540,275,240,160);

        player3LBL = new JLabel();
        player3LBL.setVerticalTextPosition(AbstractButton.BOTTOM);
        player3LBL.setHorizontalTextPosition(AbstractButton.CENTER);
        player3LBL.setForeground(Color.WHITE);
        player3LBL.setBounds(640,300,240,160);
    }

    private void createRollButton() {
        rollButton = new JButton("Roll", createImageIcon("d20-blank.png"));
        rollButton.setEnabled(true);
        rollButton.setBorder(BorderFactory.createEmptyBorder());
        rollButton.setContentAreaFilled(false);
        rollButton.setBorderPainted(false);
        rollButton.setForeground(new Color( 255,255,255));
        rollButton.setFont(new Font("Arial", Font.PLAIN, 38));
        rollButton.setVerticalTextPosition(AbstractButton.CENTER);
        rollButton.setHorizontalTextPosition(AbstractButton.CENTER);
        rollButton.setBounds(0,140,100,109);
        rollButton.setOpaque(false);
        rollButton.addActionListener(evt -> {
            Die d20 = new Die(20);
            int roll = d20.rollDie();
            chatFieldTXT.append("You rolled a " + roll + "!\n");
            displayRoll(roll);
        });
    }

    private void createAttackButton() {
        attackButton = new JButton("Roll Attack", createImageIcon("sword.png"));
        attackButton.setEnabled(true);
        attackButton.setContentAreaFilled(false);
        attackButton.setBorder(BorderFactory.createEmptyBorder());
        attackButton.setForeground(new Color( 161,81,55));
        attackButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        attackButton.setHorizontalTextPosition(AbstractButton.CENTER);
        attackButton.setBounds(0,249,100,100);
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
                attackButton.setText("Roll Attack");
                player1Icon.getImage().flush(); //causes animation to continue
            }
        });
    }

    private void startGameGuiVisibility() {
        startGameButton.setEnabled(false);
        initiateTurnButton.setEnabled(false);
        attackButton.setEnabled(false);
        addCreatureButton.setEnabled(false);
    }

    private void createGifs(){
        player1Icon = createImageIcon(playerActor.getType()+".gif");
        player2Icon = createImageIcon("Mage.gif");
        player3Icon = createImageIcon("Rogue.gif");
        player1LBL.setIcon(createImageIcon(playerActor.getType()+".gif"));
        player2LBL.setIcon(player2Icon); //hard coded but you get the idea
        player3LBL.setIcon(player3Icon); //hard coded but you get the idea
        creature1LBL.setIcon(createImageIcon("Dragon.gif"));

    }

    private void replayGifs(){
        player1Icon.getImage().flush(); //causes animation to continue
        player2Icon.getImage().flush();
        player3Icon.getImage().flush();
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

        Runnable gameOn = () -> {
            JSONObject json;
            String serverMsg;
            String prevMsg = "Previous Message";
            while(true){
                try{
                    serverMsg = in.readLine();
                    if(!serverMsg.equalsIgnoreCase(prevMsg)){
                        prevMsg = serverMsg;
                        json = new JSONObject(serverMsg);
                        messageHandler(json);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }; new Thread(gameOn).start();
    }

    private void messageHandler(JSONObject json){
        if(json.has("type")){
            if(json.optString("type").equals("acknowledge")){
                return;
            }
            if(json.optString("type").equals("chat")){
                addToChatArea(json.get("message").toString(), json.opt("fromUser").toString());
                System.out.println();
                return;
            }
        }

        JSONObject message = new JSONObject(json.optJSONObject("message").toString());
        if(message.has("message")){
            message = new JSONObject(message.optJSONObject("message").toString());
        }
        System.out.println("Extracted Message: " + message);

        if(message.has("module") && message.opt("module").equals(MODULE)) {
            String action = message.opt("gameAction").toString(); //change all of these to opt
            switch (action) {
                case "startGame"        :       startGameGuiVisibility(); createGifs();
                    System.out.println("Game Started.");
                    break;
                case "battleReport"     :       updateBattleReport(message.get("battleReport").toString());
                    System.out.println("Battle Report Updated");
                    break;
                case "scoreBoard"       :       updateScoreboard(message);
                    System.out.println("Scoreboard Updated");
                    break;
                case "playerDeath"      :       youAreDead();
                    System.out.println("You are dead.");
                    break;
                case "yourTurn"         :       yourTurn();
                    System.out.println("Your Turn.");
                    break;
                case "gameOver"         :       gameOver(message.get("winner").toString());
                    System.out.println("Game Over.");
                    break;
                case "targetNames"      :       updateComboTargetBox(message);
                    System.out.println("Target List Updated.");
                    break;
                case "addedCreature"    :       chatFieldTXT.append(message.get("creatureName").toString() + " was added to the game.\n");
                    System.out.println("Creature added.");
                    break;
            }
        }
    }

    private void addToChatArea(String message, String fromUser) {
        chatFieldTXT.append(fromUser + ": " + message + "\n");
        submitFieldTXT.selectAll();
        chatFieldTXT.setCaretPosition(chatFieldTXT.getDocument().getLength());
    }

    private void updateBattleReport(String battleReport) {
        replayGifs();
        chatFieldTXT.append("Battle Report: " + battleReport + "\n");
        submitFieldTXT.selectAll();
        chatFieldTXT.setCaretPosition(chatFieldTXT.getDocument().getLength());
    }

    //TODO make sure arrays are being instantiated properly
    private void updateScoreboard(JSONObject message) {
        JSONArray p = message.getJSONArray("playerNames");
        JSONArray c = message.getJSONArray("colorActorStats");

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
        messageAlert.setText("You are dead");
        playerDeath = true;
    }

    private void yourTurn(){
        if(playerDeath){
            sendJson(JSONLibrary.sendPassTurn());
            return;
        }
        messageAlert.setText("Your Turn");
        playerTurn = true;
        targetComboBox.setEnabled(true);
        initiateTurnButton.setEnabled(true);
        attackButton.setEnabled(true);
    }

    private void gameOver(String winner){
        if(username.equalsIgnoreCase(winner)){
            messageAlert.setText("You have Won!");
        } else {
            messageAlert.setText("You have been Defeated");
        }
    }

    private void updateComboTargetBox(JSONObject json){
        JSONArray jsonArray = json.getJSONArray("targetNames");

        for(int i = 0; i < jsonArray.length(); i++){
            if(cbModel.getIndexOf(jsonArray.getString(i)) == -1){
                cbModel.addElement(jsonArray.getString(i));
            }
        }
        targetComboBox.setModel(cbModel);
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