
import modules.Actor;
import modules.ActorPresets;
import modules.Die;
import modules.Game;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static java.lang.Thread.sleep;

public class CCMainGUI extends JFrame implements ActionListener {
    private static final int serverPort = 8989;

    private Game game = new Game();
    private BufferedReader in;
    private PrintWriter out;


    private JFrame frame = new JFrame("Caverns and Creatures");
    private JLayeredPane contentPane = new JLayeredPane();
    private JTextArea chatFieldTXT = new JTextArea(20, 75);
    private JLabel scoreBoardLBL = new JLabel();
    private JLabel imgBackground = new JLabel();
    private JLabel topBackground = new JLabel();
    private JLabel dragonIconLBL = new JLabel();
    private JLabel player1LBL = new JLabel();
    private JLabel player2LBL = new JLabel();
    private JLabel player3LBL = new JLabel();
    private JLabel creature1LBL= new JLabel();
    private JScrollPane scrollChatTxt = new JScrollPane(chatFieldTXT,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JTextField submitFieldTXT = new JTextField(75);
    private JButton sendButton = new JButton("Send");
    private JButton attackButton = new JButton("Attack", createImageIcon("sword.png"));
    private JButton rollButton = new JButton("Roll", createImageIcon("d20-blank.png"));
    private JButton addCreatureButton = new JButton("Add Creature");
    private String username;
    private String playerCharacter;

    ExecutorService executor = Executors.newFixedThreadPool(2);
    ReentrantLock lock = new ReentrantLock();

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
        scoreBoardLBL.setOpaque(false);
        player1LBL.setOpaque(false);
        rollButton.setOpaque(false);

        submitFieldTXT.addActionListener(this);
        attackButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatFieldTXT.append("Attack\n");
            }
        });
        rollButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Die d20 = new Die(20);
                int roll = d20.rollDie();
                chatFieldTXT.append("You rolled a " + roll + "!\n");
                rollButton.setFont(new Font("Arial", Font.BOLD, 20));
                rollButton.setText(Integer.toString(roll));
            }
        });
        addCreatureButton.addActionListener(evt -> executor.submit(() -> {
            lock.lock();
            try {
                game.addRandomMonster();
                scoreBoardLBL.setText(displayScoreBoard(game));
                scoreBoardLBL.updateUI();
            }finally {
                lock.unlock();
            }
        }));

        imgBackground.setIcon(createImageIcon("dark_field.jpg"));
        topBackground.setIcon(createImageIcon("dragonbackground.png"));
        dragonIconLBL.setIcon(createImageIcon("dragonicon.png"));
        scoreBoardLBL.setForeground(Color.WHITE);
        sendButton.addActionListener(this);
        sendButton.setEnabled(true);
        attackButton.setEnabled(true);
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
        chatFieldTXT.setEditable(false);

        topBackground.setBounds(0, 0, 905, 140);
        imgBackground.setBounds(0, 0, 905, 700);
        dragonIconLBL.setBounds(840, 102, 50, 50);
        scoreBoardLBL.setBounds(0, 0, 905, 140);
        attackButton.setBounds(0,140,100,100);
        rollButton.setBounds(0,240,100,109);
        addCreatureButton.setBounds(5,492,175,23);
        submitFieldTXT.setBounds(5, 627, 795, 25);
        sendButton.setBounds(800, 627, 84, 23);
        scrollChatTxt.setBounds(5,515,880,110);
        player1LBL.setBounds(440,250,240,160);
        player2LBL.setBounds(540,275,240,160);
        player3LBL.setBounds(640,300,240,160);
        creature1LBL.setBounds(200,275,240,160);


        contentPane.add(topBackground,JLayeredPane.PALETTE_LAYER);
        contentPane.add(imgBackground,JLayeredPane.DEFAULT_LAYER);
        contentPane.add(dragonIconLBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(scoreBoardLBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(scrollChatTxt,JLayeredPane.MODAL_LAYER);
        contentPane.add(submitFieldTXT,JLayeredPane.MODAL_LAYER);
        contentPane.add(sendButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(attackButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(rollButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(addCreatureButton,JLayeredPane.MODAL_LAYER);
        contentPane.add(player1LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player2LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(player3LBL,JLayeredPane.MODAL_LAYER);
        contentPane.add(creature1LBL,JLayeredPane.MODAL_LAYER);
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
            scoreBoard += "<TH BGCOLOR=BLACK><H2>" + actorName + "</H2></TH>";
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

        scoreBoardLBL.setText(displayScoreBoard(game));

        //populate Actor test
        //player1LBL.setText(username);
        player1LBL.setIcon(createImageIcon(player1.getType()+".gif"));
        player2LBL.setIcon(createImageIcon("Mage.gif")); //hard coded but you get the idea
        player3LBL.setIcon(createImageIcon("Rogue.gif")); //hard coded but you get the idea
        creature1LBL.setIcon(createImageIcon("Dragon.gif"));

        //for titles of UI
        for(String usernames: game.getNames()){
            chatFieldTXT.append(usernames + "\n");
        }

        // Process all messages from server, according to the protocol.
        while (true) {

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