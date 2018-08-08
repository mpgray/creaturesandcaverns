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

    public static String sendStartGame(){
        JSONObject startGame = new JSONObject();
        JSONObject startCommand = new JSONObject();

        startGame.put("type", "application");
        startCommand.put("module", MODULE);
        startCommand.put("action", "startNewGame");
        startGame.put("message", startCommand);
        System.out.println(startGame.toString());

        return startGame.toString();
    }

    public static String sendPlayerCharacter(String pc, String username){
        JSONObject sendPC = new JSONObject();
        JSONObject playerCharacter = new JSONObject();

        sendPC.put("type", "application");
        playerCharacter.put("module", MODULE);
        playerCharacter.put("action", "addPlayerCharacter");
        playerCharacter.put("username", username);
        playerCharacter.put("playerType", pc);
        sendPC.put("message", playerCharacter);
        System.out.println(sendPC.toString());

        return sendPC.toString();
    }

    public static String sendInitiateTurn(String attackerUsername, String targetUsername, int attackRoll, int damageRoll ){

        JSONObject sendInitiateTurn = new JSONObject();
        JSONObject initiateTurn = new JSONObject();

        sendInitiateTurn.put("type", "application");
        initiateTurn.put("module", MODULE);
        initiateTurn.put("attacker", attackerUsername);


        sendInitiateTurn.put("message", initiateTurn);



        return sendInitiateTurn.toString();
    }

    public static String sendAddCreature( ){

        JSONObject addCreature = new JSONObject();

        return addCreature.toString();
    }


}
