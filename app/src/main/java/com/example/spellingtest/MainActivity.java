package com.example.spellingtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import alex.common.AlexFile;
import alex.common.AlexView;
import alex.common.voice.AlexVoice;
import alex.common.voice.PositivePhrases;

public class MainActivity extends AppCompatActivity {

    private static final String SPELL_FILE = "spelling.txt";
    private static final String[] RANDOM_WORDS = {"about", "brass", "warm", "army", "employ", "funny", "anyone", "half", "raise", "bless", "frost", "straight", "everything", "mouth", "garden", "change", "scratch", "clock", "choose", "town", "lunch", "laugh", "agree", "sand", "penny", "age", "holiday", "string", "bought", "crop", "ask", "body", "balloon", "alone", "herself", "adventure", "bear", "crazy", "neither", "bring", "front", "dollar", "elbow", "really", "lose", "grass", "shelf", "elsewhere", "crawl", "window", "library", "move", "blind", "wrist", "prize", "dinner", "health", "touch", "cracker", "disappear", "always", "compare", "stretch", "afternoon", "stick", "crust", "bedsheet", "sound", "important", "birthday", "swim", "something", "himself", "month", "airplane", "guess", "mouse", "neighbor", "flow", "actor", "hundred", "morning", "alike", "driving", "school", "bare", "throw", "whole", "begin", "shopping", "already", "cheese", "newspaper", "also", "grin", "clear", "bent", "cure", "treat", "without", "mark", "piece", "running", "point", "cardboard", "round", "stairs", "breakfast", "thing", "away", "slept", "better", "bedtime", "children", "desk", "brand", "decimal", "cherry", "enjoy", "baseball", "awake", "happen", "kitchen", "almost", "flower", "thousand", "anything", "scream", "circus", "began", "together", "trouble", "behind", "aloud", "angriest", "caught", "ago", "suit", "churn", "bench", "clothing", "match", "brush", "young", "picnic", "doctor", "listen", "spring", "climb", "wrong", "afraid", "forgot", "along", "riding", "worst", "become", "shiny", "hallway"};
    static List<String> myWords;
    static List<String> mySentences;
    static String myWord, mySentence, previousWord;
    static int myWordIndex;
    static Random random;
    EditText wordList, answer;
    ImageView talkIcon, bee;
    TextView questionCount;
    Button editButton, saveButton, testButton, clearButton, cancelButton, randomButton, sayButton, cancelTestButton, clearAnswerButton, cheatButton, sentenceButton;
    View[] mainScreen, editScreen, testScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();

        // MAIN SCREEN
        editButton = findViewById(R.id.editButton);
        testButton = findViewById(R.id.testButton);
        bee = findViewById(R.id.bee);
        mainScreen = new View[]{editButton, testButton, bee};

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
        cancelTestButton = findViewById(R.id.cancelTestButton);
        talkIcon = findViewById(R.id.talkIcon);
        cheatButton = findViewById(R.id.cheatButton);
        questionCount = findViewById(R.id.questionCount);
        sentenceButton = findViewById(R.id.sentenceButton);
        testScreen = new View[]{answer, sayButton, clearAnswerButton, cancelTestButton, talkIcon, cheatButton, questionCount, sentenceButton};
        readFileInEditor();

        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String currentWord = s.toString();
                char c = ' ';
                String lastCharacter = "";
                if (!currentWord.isEmpty()) {
                    if (previousWord.isEmpty() && currentWord.length() == 1) {
                        c = currentWord.charAt(0);
                    } else if (!previousWord.isEmpty() && currentWord.length() - previousWord.length() == 1) {
                        if (currentWord.startsWith(previousWord)) {
                            c = currentWord.charAt(currentWord.length() - 1);
                        }
                    }
                    if (c != ' ') {
                        if (c == '\'') {
                            lastCharacter = "apostrophe. ";
                        } else {
                            lastCharacter = "" + c + ". ";
                        }
                        AlexVoice.say(lastCharacter);
                    }
                }
                checkWord(lastCharacter);
                previousWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void saveTextPad() {
        AlexFile.saveString(this, SPELL_FILE, wordList.getText().toString());
    }

    public void readFileInEditor() {
        wordList.setText(AlexFile.readAsString(this, SPELL_FILE));
        wordList.setSelection(wordList.getText().length());
    }

    public void onSaveClick(View view) {
        saveTextPad();
        AlexView.hideAndShow(editScreen, mainScreen);
    }

    public void onEditClick(View view) {
        AlexView.hideAndShow(mainScreen, editScreen);
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
        AlexView.hideAndShow(editScreen, mainScreen);
    }

    // TEST SCREEN
    public void onTestClick(View view) {
        String[] words;
        words = wordList.getText().toString().split("\\s+");
        if (words.length == 0) {
            words = RANDOM_WORDS;
        }
        myWords = new ArrayList<>();
        mySentences = new ArrayList<>();
        Parse.intoSentences(words, myWords, mySentences);
        ArrayList[] shuffled = Parse.shuffle(myWords, mySentences);
        myWords = shuffled[0];
        mySentences = shuffled[1];
        // Collections.shuffle(myWords);
        myWordIndex = 0;
        getWord();
        AlexView.hideAndShow(mainScreen, testScreen);
    }

    private void getWord() {
        myWord = myWords.get(myWordIndex);
        mySentence = mySentences.get(myWordIndex);
        answer.setText("");
        String text = (myWordIndex + 1) + "/" + myWords.size();
        questionCount.setText(text);
        AlexVoice.say("spell " + myWord);
        sentenceButton.setVisibility(mySentence.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void checkWord(String lastCharacter) {
        String answerText = answer.getText().toString().trim();
        if (answerText.equals(myWord)) {
            saySomethingGood(lastCharacter);
            nextWord();
        }
    }

    private void saySomethingGood(String lastCharacter) {
        String goodText = lastCharacter + " " + PositivePhrases.getRandom(random);
        AlexVoice.say(goodText);
    }

    private void nextWord() {
        myWordIndex = (myWordIndex + 1) % myWords.size();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::getWord, 3500);
    }

    public void onClearAnswerClick(View view) {
        answer.setText("");
    }

    public void onSayClick(View view) {
        AlexVoice.say(myWord);
    }

    public void onCancelTestClick(View view) {
        AlexView.hideAndShow(testScreen, mainScreen);
    }

    public void onCheatClick(View view) {
        StringBuilder word = new StringBuilder();
        for (char c : myWord.toCharArray()) {
            if (c == '\'') {
                word.append("apostrophe");
            } else {
                word.append(c);
            }
            word.append(". ");
        }
        AlexVoice.say(word.toString());
    }

    public void onSentenceClick(View view) {
        AlexVoice.say(mySentence);
    }

    @Override
    protected void onPause() {
        AlexVoice.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        AlexVoice.start(getApplicationContext());
        super.onResume();
    }
}