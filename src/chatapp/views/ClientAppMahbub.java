package chatapp.views;

import chatapp.utils.Utils;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientAppMahbub extends Application implements EventHandler {

    private static TextArea chatBox;
    private static TextField message;
    private static Button sendBtn;
    private static DataInputStream dis;
    private static DataOutputStream dos;
    public static final String USER_NAME = "Mahboob";

    public ClientAppMahbub() {
        chatBox = new TextArea();
        chatBox.setPrefHeight(Utils.CHAT_BOX_HEIGHT);
        message = new TextField();
        message.setMinWidth(Utils.INPUT_TEXT_FIELD_WEIGHT);
        sendBtn = new Button("SEND");
        sendBtn.setOnAction(this::handle);
    }

    // application starting point
    @Override
    public void start(Stage primaryStage) throws Exception {
        // vertical box and add chat txt area
        VBox root = new VBox();
        root.setPrefSize(Utils.UI_WIDTH, Utils.UI_HEIGHT);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(message, sendBtn);
        root.getChildren().addAll(chatBox, hBox);

        // show stage/app
        primaryStage.setTitle(USER_NAME);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // create client far from main application thread
        Thread thread = new Thread(() -> {
            try {
                createClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
    }

    private static void createClient() throws IOException {
        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, Utils.SERVER_PORT_NUM);

        // obtaining input and out streams
        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());

        // push user name
        dos.writeUTF(USER_NAME + "#" + "::" + "#" + ClientAppAli.USER_NAME);
        addText(USER_NAME + " Connected to the server at: " + Utils.getCurrentDateTime());

        // readMessage thread
        Thread readMessage = new Thread(() -> {
            while (true) {
                try {
                    // read the message sent to this client
                    String msg = dis.readUTF();
                    addText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        readMessage.start();
    }

    // add text in server chat box
    private static void addText(String text) {
        String oldTxt = chatBox.getText();
        if(chatBox.getText().equals("")){
            chatBox.setText(text);
        }else {
            chatBox.setText(oldTxt + "\n\n" + text);
        }
    }

    @Override
    public void handle(Event event) {
        // sendMessage thread
        Thread sendMessage = new Thread(() -> {
            // read the message to deliver.
            try {
                // write on the output stream
                addText(USER_NAME + ": " + message.getText());
                String msg = USER_NAME + "#" + message.getText() + "#" + ClientAppAli.USER_NAME;
                message.setText("");
                dos.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendMessage.start();
    }
}
