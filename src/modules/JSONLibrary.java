package modules;

import org.json.JSONObject;

public class JSONLibrary {
    public static final String module = "CREATURESANDCAVERNS";

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

    public static String startGame(){
        JSONObject startGame = new JSONObject();
        JSONObject startCommand = new JSONObject();

        startGame.put("type", "application");
        startCommand.put("module", module);
        startCommand.put("action", "startNewGame");
        startGame.put("message", startCommand);

        return startGame.toString();
    }

    public static String sendPlayerCharacter(String pc){
        JSONObject sendPC = new JSONObject();
        JSONObject playerCharacter = new JSONObject();

        sendPC.put("type", "application");
        playerCharacter.put("module", module);
        playerCharacter.put("action", "addPlayerCharacter");
        playerCharacter.put("type", pc);
        sendPC.put("message", playerCharacter);

        return sendPC.toString();
    }


}
