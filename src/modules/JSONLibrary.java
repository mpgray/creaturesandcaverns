package modules;

import org.json.JSONObject;

public class JSONLibrary {
    public static final String MODULE = "CREATURESANDCAVERNS";

    public JSONLibrary(){

    }

    public static String sendUser(String user){
        JSONObject loginMessage = new JSONObject();
        JSONObject username = new JSONObject();

        loginMessage.put("type", "login");
        username.put("username", user);
        loginMessage.put("message", username);

        return loginMessage.toString();
    }

    public static String joinLobby(){
        JSONObject joinLobby = new JSONObject();
        JSONObject joinCommand = new JSONObject();

        joinLobby.put("type", "application");
        joinCommand.put("module", MODULE);

        return joinLobby.toString();
    }

    public static String startGame(){
        JSONObject startGame = new JSONObject();
        JSONObject startCommand = new JSONObject();

        startGame.put("type", "application");
        startCommand.put("module", MODULE);
        startCommand.put("action", "startNewGame");
        startGame.put("message", startCommand);
        System.out.println(startGame.toString());

        return startGame.toString();
    }

    public static String sendPlayerCharacter(String pc){
        JSONObject sendPC = new JSONObject();
        JSONObject playerCharacter = new JSONObject();

        sendPC.put("type", "application");
        playerCharacter.put("module", MODULE);
        playerCharacter.put("action", "addPlayerCharacter");
        playerCharacter.put("type", pc);
        sendPC.put("message", playerCharacter);
        System.out.println(sendPC.toString());

        return sendPC.toString();
    }

    public static String initiateTurn(String attackerUsername, String targetUsername, int attackRoll, int damageRoll ){

        JSONObject initiateTurn = new JSONObject();

        return initiateTurn.toString();    }
        

}
