package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {

    static final String MODULE = "CREATURESANDCAVERNS";

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.opt("module") != null || MODULE.equals(message.getString("module"))){
            for(message.has("action")){
                
            }

        }

    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new MyHandler(portString)).start();
    }
}
