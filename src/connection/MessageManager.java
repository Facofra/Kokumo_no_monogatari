package connection;

import com.google.gson.Gson;

public class MessageManager {
    static Gson gson = new Gson();

    public MessageManager(Gson gson) {
        this.gson = gson;
    }

    public static String toJson(Message message){
        return gson.toJson(message);
    }

    public static Message jsonToMessage(String json){
        return gson.fromJson(json,Message.class);
    }
}
