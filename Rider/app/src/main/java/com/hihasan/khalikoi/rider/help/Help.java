package com.hihasan.khalikoi.rider.help;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.Result;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.hihasan.khalikoi.rider.R;

import java.util.Map;

public class Help extends AppCompatActivity implements AIListener
{
    private Button listenButton, chat;
    private TextView resultTextView;
    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        listenButton = (Button) findViewById(R.id.listen);
        chat = (Button) findViewById(R.id.chat);
        resultTextView = (TextView) findViewById(R.id.result);
// ab5acd11f35b41ac8e1e969694d9a732
        final AIConfiguration aiConfiguration = new AIConfiguration("8eddf4e5e6284f8185d4936a9a5e4242", AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, aiConfiguration);
        aiService.setListener(this);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Help.this, ChatActivity.class);
                startActivity(i);
            }
        });

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiService.startListening();
            }
        });

    }


    @Override
    public void onResult(ai.api.model.AIResponse result) {

        Result res = result.getResult();

        // Get parameters

        String parameterString = "";
        if(res.getParameters() != null && !res.getParameters().isEmpty()){
            for (final Map.Entry<String, JsonElement> entry : res.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        resultTextView.setText(res.getFulfillment().getSpeech());

    }

    @Override
    public void onError(ai.api.model.AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
