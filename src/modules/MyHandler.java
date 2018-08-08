package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {


    static final String MODULE = "CREATURESANDCAVERNS";
    private Game game = new Game();

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.opt("module") != null || MODULE.equals(message.getString("module"))){
            String action = message.getString("action");
            switch(action){
                case "addPlayerCharacter"        :   addPlayer(message);
                case "startGame"              :   startGame();

            }
//            for(message.has("action")){
//
//            }

        }

    }

    private void addPlayer(JSONObject message) {
        String username = message.getString("username");
        String playerType = message.getString("playerType");

        game.addPlayer(username, playerType);
    }

    private void startGame() {
        game.startGame();
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new MyHandler(portString)).start();
    }
}
