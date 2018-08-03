import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

public class JSONTest {

    public static void main(String[] args){
        Actor fighter1 = new ActorPresets().playerPresets.get("Fighter");


        JSONObject jObj = new JSONObject(fighter1);

        System.out.println(jObj);

        String healthBar = jObj.get("currentHitPoints") + "/" + jObj.get("maxHitPoints");

        System.out.println("Actor Health: " + healthBar);




    }
}
