package com.hihasan.khalikoi.driver.help;

import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hihasan.khalikoi.driver.R;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class ChatActivity extends AppCompatActivity
{
    private ListView listView;
    private View btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_chat);

        chatMessages = new ArrayList<>();

        listView = findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        editText = findViewById(R.id.msg_type);

        //set ListView adapter first
        adapter = new MessageAdapter(this, R.layout.item_chat_left, chatMessages);
        listView.setAdapter(adapter);

        final AIConfiguration config = new AIConfiguration("8eddf4e5e6284f8185d4936a9a5e4242",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIDataService aiDataService = new AIDataService(ChatActivity.this , config);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(ChatActivity.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    String msg = editText.getText().toString();
                    ChatMessage chatMessage = new ChatMessage(msg.toString(), true);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    isMine = !isMine;

                    final AIRequest aiRequest = new AIRequest();
                    aiRequest.setQuery(msg);

                    new AsyncTask<AIRequest, Void, AIResponse>() {
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {
                                String res = aiResponse.getResult().getFulfillment().getSpeech();

                                ChatMessage chatMessage = new ChatMessage(res, false);
                                chatMessages.add(chatMessage);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }.execute(aiRequest);

                }
            }
        });
    }
}
