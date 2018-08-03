import org.json.JSONObject;

import java.io.IOException;

public class ServerGame {
    private Game game;
    private String battleReport;
    private int attackRoll;
    private int[] damageRolls;
    private JSONObject latestRolls;

    public ServerGame(){
        game = new Game();
    }

    private void readJson(JSONObject json){
        try{
            String module = json.get("module").toString();
            String username = json.get("username").toString();
            JSONObject message = new JSONObject(json.get("message"));

            switch(module){
                case "addPlayer" :
                    game.addPlayer(username, message.get("playerType").toString());
                    break;
                case "addRandomMonster" :
                    game.addRandomMonster();
                    break;
                case "fight" :
                    battleReport = game.fight(username, message.get("targetName").toString());
                    latestRolls.put("attackRoll", attackRoll);
                    latestRolls.put("damageRolls", damageRolls);
                    break;
                case "getRolls" :
            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
