package modules;

import org.json.JSONObject;

public class MyHandler extends Handler {

    public MyHandler(String portString) {
        super(portString);
    }

    @Override
    protected void handle(JSONObject message) {
        if(message.has("module")){
            System.out.println(message.toString());
        }
    }

    public static void main(String[] args) {
        String portString = "8990";
        new Thread(new MyHandler(portString)).start();
    }
}
