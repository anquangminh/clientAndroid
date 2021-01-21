package com.example.clientandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnChat;
    EditText edMessage;
    private String serverUrl = "http://192.168.1.104:3000/";
    private Socket _socket;
    {
        try {
            _socket = IO.socket(serverUrl);
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnChat = findViewById(R.id.btnChat);
        btnLogin = findViewById(R.id.btnLogin);
        edMessage = findViewById(R.id.edMessage);
        _socket.connect();
        _socket.on("receiver_message", onNewMessage);
    }

    public Socket get_socket() {
        return _socket;
    }

    public void chat(View view){
        _socket.emit("", edMessage.getText().toString());
    }

    public void login(View view){
        _socket.emit("user_login", edMessage.getText().toString());
        edMessage.setText("");
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message = data.optString("data");
                    Log.d("Chat", "Oke");
                }
            });
        }
    };
}