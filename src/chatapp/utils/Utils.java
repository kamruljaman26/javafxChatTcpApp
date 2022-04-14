package chatapp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    // ip address and port number
    public static final String IP_ADDRESS = "";
    public static final int SERVER_PORT_NUM = 7878;
    public static final double CHAT_BOX_HEIGHT = 370;
    public static final double UI_WIDTH = 500;
    public static final double UI_HEIGHT = 400;
    public static final double INPUT_TEXT_FIELD_WEIGHT = 430;

    // return current date time as a formatted string
    public static String getCurrentDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
