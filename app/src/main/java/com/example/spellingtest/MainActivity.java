package com.example.spellingtest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String SPELL_FILE = "spelling.txt";
    private static final String[] RANDOM_WORDS = {"about", "brass", "warm", "army", "employ", "funny", "anyone", "half", "raise", "bless", "frost", "straight", "everything", "mouth", "garden", "change", "scratch", "clock", "choose", "town", "lunch", "laugh", "agree", "sand", "penny", "age", "holiday", "string", "bought", "crop", "ask", "body", "balloon", "alone", "herself", "adventure", "bear", "crazy", "neither", "bring", "front", "dollar", "elbow", "really", "lose", "grass", "shelf", "elsewhere", "crawl", "window", "library", "move", "blind", "wrist", "prize", "dinner", "health", "touch", "cracker", "disappear", "always", "compare", "stretch", "afternoon", "stick", "crust", "bedsheet", "sound", "important", "birthday", "swim", "something", "himself", "month", "airplane", "guess", "mouse", "neighbor", "flow", "actor", "hundred", "morning", "alike", "driving", "school", "bare", "throw", "whole", "begin", "shopping", "already", "cheese", "newspaper", "also", "grin", "clear", "bent", "cure", "treat", "without", "mark", "piece", "running", "point", "cardboard", "round", "stairs", "breakfast", "thing", "away", "slept", "better", "bedtime", "children", "desk", "brand", "decimal", "cherry", "enjoy", "baseball", "awake", "happen", "kitchen", "almost", "flower", "thousand", "anything", "scream", "circus", "began", "together", "trouble", "behind", "aloud", "angriest", "caught", "ago", "suit", "churn", "bench", "clothing", "match", "brush", "young", "picnic", "doctor", "listen", "spring", "climb", "wrong", "afraid", "forgot", "along", "riding", "worst", "become", "shiny", "hallway"};
    EditText wordList, answer;
    ImageView talkIcon;
    Button editButton, saveButton, testButton, clearButton, cancelButton, randomButton, sayButton, cancelTestButton, clearAnswerButton, checkButton;
    View[] mainScreen, editScreen, testScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MAIN SCREEN
        editButton = findViewById(R.id.editButton);
        testButton = findViewById(R.id.testButton);
        mainScreen = new View[]{editButton, testButton};

        // EDIT SCREEN
        wordList = findViewById(R.id.wordList);
        saveButton = findViewById(R.id.saveButton);
        clearButton = findViewById(R.id.clearButton);
        cancelButton = findViewById(R.id.cancelButton);
        randomButton = findViewById(R.id.randomButton);
        editScreen = new View[]{wordList, saveButton, clearButton, randomButton, cancelButton};

        // TEST SCREEN
        answer = findViewById(R.id.answer);
        sayButton = findViewById(R.id.sayButton);
        clearAnswerButton = findViewById(R.id.clearAnswerButton);
        checkButton = findViewById(R.id.checkButton);
        cancelTestButton = findViewById(R.id.cancelTestButton);
        talkIcon = findViewById(R.id.talkIcon);
        testScreen = new View[]{answer, sayButton, clearAnswerButton, checkButton, cancelTestButton, talkIcon};

        readFileInEditor();
    }

    public void saveTextPad() {
        try {
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(SPELL_FILE, 0));
            out.write(wordList.getText().toString());
            out.close();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t, Toast.LENGTH_LONG).show();
        }
    }

    public void readFileInEditor() {
        try {
            InputStream in = openFileInput(SPELL_FILE);
            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;

                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str).append("\n");
                }
                in.close();
                wordList.setText(buf.toString());
                wordList.setSelection(wordList.getText().length());
            }
        } catch (java.io.FileNotFoundException e) {
            wordList.setText("");
            wordList.setSelection(wordList.getText().length());
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t, Toast.LENGTH_LONG).show();
        }
    }

    public void onSaveClick(View view) {
        saveTextPad();
        hideAndShow(editScreen, mainScreen);
    }

    public void onEditClick(View view) {
        hideAndShow(mainScreen, editScreen);
    }

    private void hideAndShow(View[] hide, View[] show) {
        for (View view : hide) view.setVisibility(View.GONE);
        for (View view : show) view.setVisibility(View.VISIBLE);
    }

    public void onClearClick(View view) {
        wordList.setText("");
    }

    public void onRandomClick(View view) {
        List<String> words = Arrays.asList(RANDOM_WORDS);
        Collections.shuffle(words);
        StringBuilder wordBuilder = new StringBuilder();
        boolean firstWord = true;
        for (String word : words.subList(0, 10)) {
            if (firstWord) {
                firstWord = false;
            } else {
                wordBuilder.append("\n");
            }
            wordBuilder.append(word);
        }
        wordList.setText(wordBuilder.toString());
    }

    public void onCancelClick(View view) {
        hideAndShow(editScreen, mainScreen);
    }

    // TEST SCREEN
    public void onTestClick(View view) {
        hideAndShow(mainScreen, testScreen);
    }
    public void onCheckClick(View view) {
    }

    public void onClearAnswerClick(View view) {
        answer.setText("");
    }

    public void onSayClick(View view) {
    }

    public void onCancelTestClick(View view) {
        hideAndShow(testScreen, mainScreen);
    }
}