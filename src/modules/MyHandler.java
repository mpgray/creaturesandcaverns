package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {

    static final String module = "creaturesAndCaverns";

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.getString("module").contains(module)){
        System.out.println(message);
        }

    }

    public void startGame(){
        /* While loop waiting for player to join,
        * when player joins  a message is sent to client(s) that says "username" has joined the game
        * ---What is the condition for the game to start? For now, we can say when the game has three players----
        * Send message from server to client to update gui - contains player information*/
    }

    public void addCreature(){
        //setInitiative();

    }

    public void setInitiative(){

    }

    public void updateBattleReport(){

    }

    public void sendBattleReport(){

    }

    public void updateScoreCard(){

    }

    public void sendScoreCard(){

    }


    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new MyHandler(portString)).start();
    }
}
