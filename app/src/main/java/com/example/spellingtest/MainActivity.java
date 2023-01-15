package com.example.spellingtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String SPELL_FILE = "spelling.txt";
    private static final String[] RANDOM_WORDS = {"about", "brass", "warm", "army", "employ", "funny", "anyone", "half", "raise", "bless", "frost", "straight", "everything", "mouth", "garden", "change", "scratch", "clock", "choose", "town", "lunch", "laugh", "agree", "sand", "penny", "age", "holiday", "string", "bought", "crop", "ask", "body", "balloon", "alone", "herself", "adventure", "bear", "crazy", "neither", "bring", "front", "dollar", "elbow", "really", "lose", "grass", "shelf", "elsewhere", "crawl", "window", "library", "move", "blind", "wrist", "prize", "dinner", "health", "touch", "cracker", "disappear", "always", "compare", "stretch", "afternoon", "stick", "crust", "bedsheet", "sound", "important", "birthday", "swim", "something", "himself", "month", "airplane", "guess", "mouse", "neighbor", "flow", "actor", "hundred", "morning", "alike", "driving", "school", "bare", "throw", "whole", "begin", "shopping", "already", "cheese", "newspaper", "also", "grin", "clear", "bent", "cure", "treat", "without", "mark", "piece", "running", "point", "cardboard", "round", "stairs", "breakfast", "thing", "away", "slept", "better", "bedtime", "children", "desk", "brand", "decimal", "cherry", "enjoy", "baseball", "awake", "happen", "kitchen", "almost", "flower", "thousand", "anything", "scream", "circus", "began", "together", "trouble", "behind", "aloud", "angriest", "caught", "ago", "suit", "churn", "bench", "clothing", "match", "brush", "young", "picnic", "doctor", "listen", "spring", "climb", "wrong", "afraid", "forgot", "along", "riding", "worst", "become", "shiny", "hallway"};
    private static final String[] POSITIVE_PHRASES = {"All Right!", "Exactly right", "Excellent!", "Exceptional", "Fabulous!", "Fantastic!", "Sensational!", "Wonderful!", "Outstanding!", "That's it!", "Just right!", "Unbelievable", "Way to go!", "Simply superb", "Stupendous!", "Magnificent", "Marvelous!", "First class job", "First class work", "Good for you!", "That's great", "Good going!", "Good thinking", "Right on!", "Better than ever!", "I'm impressed!", "You're one of a kind", "You've got it now.", "You've mastered it!", "What an improvement!", "You always amaze me", "You are fantastic", "You are learning a lot", "You are learning fast", "You are so good", "You did it!", "You did that very well", "You don't miss a thing", "You got it right!", "You hit the target", "I'm very proud of you", "Keep up the great work!", "Nothing can stop you now", "Now you've figured it out", "You make it look easy", "You haven't missed a thing", "You did that all by yourself", "That's really nice work!", "You're doing beautifully!", "You are very good at that", "That's the way to do it", "It's perfect!", "Nice going!", "That's right!", "Well done", "I'm speechless!", "Great work", "Keep it up!", "You got it!", "Not bad at all!", "That's the way!", "Now you have it", "I knew you could do it!", "Great improvement!", "That's it exactly", "That's the best ever", "That's the way to do it", "Couldn't have done it better myself", "Tremendous job", "You're doing well", "You're learning fast", "That's the right way to do it", "You're doing a great job", "Your studying really paid off", "You must have been practicing", "You're on the right track now", "You're getting better every day", "I've never seen anyone do it better", "It looks like you've put a lot of work into this", "Now that's what I call a great job", "That's the right way to do it", "You certainly did well today."};
    static TextToSpeech t1;
    static List<String> myWords;
    static String myWord, previousWord;
    static int myWordIndex;
    static Random random;
    EditText wordList, answer;
    ImageView talkIcon, bee;
    TextView questionCount;
    Button editButton, saveButton, testButton, clearButton, cancelButton, randomButton, sayButton, cancelTestButton, clearAnswerButton, cheatButton;
    View[] mainScreen, editScreen, testScreen;

    static void say(String toSpeak) {
        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        random = new Random();
        t1 = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.US);
            }
        });

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
        testScreen = new View[]{answer, sayButton, clearAnswerButton, cancelTestButton, talkIcon, cheatButton, questionCount};
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
                        lastCharacter = "" + c + ". ";
                        say(lastCharacter);
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
        String[] words;
        words = wordList.getText().toString().split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("\\W", "");
        }
        if (words.length == 0) {
            words = RANDOM_WORDS;
        }
        myWords = Arrays.asList(words);
        Collections.shuffle(myWords);
        myWordIndex = 0;
        getWord();
        hideAndShow(mainScreen, testScreen);
    }

    private void getWord() {
        myWord = myWords.get(myWordIndex);
        answer.setText("");
        String text = (myWordIndex + 1) + "/" + myWords.size();
        questionCount.setText(text);
        say("spell " + myWord);
    }

    private void checkWord(String lastCharacter) {
        String answerText = answer.getText().toString().trim();
        if (answerText.equals(myWord)) {
            saySomethingGood(lastCharacter);
            nextWord();
        }
    }

    private void saySomethingGood(String lastCharacter) {
        String goodText = lastCharacter + " " + POSITIVE_PHRASES[random.nextInt(POSITIVE_PHRASES.length)];
        say(goodText);
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
        say(myWord);
    }

    public void onCancelTestClick(View view) {
        hideAndShow(testScreen, mainScreen);
    }

    public void onCheatClick(View view) {
        StringBuilder word = new StringBuilder();
        for (char c : myWord.toCharArray()) {
            word.append(c);
            word.append(". ");
        }
        say(word.toString());
    }

}