package com.example.mi_smart_reply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseSmartReply;
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestion;
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
private TextView response;
private Button send;
private EditText input;
List<FirebaseTextMessage > conversation = new ArrayList<FirebaseTextMessage >();
FirebaseSmartReply smartReply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        response = findViewById(R.id.Response);
        send = findViewById(R.id.Send);
        input = findViewById(R.id.input);

        FirebaseApp.initializeApp(MainActivity.this);


        smartReply = FirebaseNaturalLanguage.getInstance().getSmartReply();
        conversation.add(FirebaseTextMessage.createForLocalUser("it is so funny", System.currentTimeMillis()));

        smartReply.suggestReplies(conversation)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult result) {
                        if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                            // The conversation's language isn't supported, so the
                            // the result doesn't contain any suggestions.
                         Log.e("TAG=", "Language is not English");
                        } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            // Task completed successfully
                            // ...
                            for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                String replyText = suggestion.getText();
                                Log.e("reply = " , replyText);
                                response.setText(replyText);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...

                        Log.e("TAG", "on Failuee");
                    }
                });

        conversation.remove(0);
        send.setOnClickListener(myListener);
    }


    private View.OnClickListener myListener = new View.OnClickListener() {
        public void onClick(View v) {
            String fromInput;
            fromInput = input.getText().toString();
            conversation.add(FirebaseTextMessage.createForLocalUser(fromInput, System.currentTimeMillis()));

            smartReply.suggestReplies(conversation)
                    .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                        @Override
                        public void onSuccess(SmartReplySuggestionResult result) {
                            if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                                // The conversation's language isn't supported, so the
                                // the result doesn't contain any suggestions.
                                Log.e("TAG=", "Language is not English");
                            } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                                // Task completed successfully
                                // ...
                                String replyText = "";
                                // create instance of Random class
                                Random rand = new Random();

                                List<String> suggestions = new ArrayList<String>();
                                int rand_index;
                                for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                                    replyText = suggestion.getText();
                                    suggestions.add(replyText);
                                    Log.e("reply= " , replyText);
                                }
                                rand_index = rand.nextInt(result.getSuggestions().size());
                                response.setText(suggestions.get(rand_index));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...

                            Log.e("TAG", "on Failuee");
                        }
                    });

            conversation.remove(0);
            Log.e("Button is clicked","hehe");
        }
    };
}
