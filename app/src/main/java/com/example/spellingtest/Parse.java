package com.example.spellingtest;

// Parse a list into words and sentences.
//
// DATA STRUCTURE
// 1. No sentences: word1 word2 word3
// 2. With some sentences: word1 word2 (sentence for word2) word3 word4 (sentence for word 4)

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum State {
    firstWord, secondWord, inSentence, endSentence
}

public class Parse {
    public static void intoSentences(String[] words, List<String> myWords, List<String> mySentences) {
        State parseState = State.firstWord;
        StringBuilder sentence = null;
        for (String word : words) {
            System.out.print(parseState);
            System.out.print("/");
            switch (parseState) {
                case firstWord:
                case endSentence:
                    myWords.add(word);
                    parseState = State.secondWord;
                    break;
                case secondWord:
                    if (word.startsWith("(")) {
                        sentence = new StringBuilder();
                        sentence.append(word);
                        parseState = State.inSentence;
                    } else {
                        mySentences.add("");
                        myWords.add(word);
                    }
                    break;
                case inSentence:
                    sentence.append(" ");
                    sentence.append(word);
                    if (word.endsWith(")")) {
                        mySentences.add(sentence.toString());
                        parseState = State.endSentence;
                    }
            }
        }
        if (parseState == State.inSentence) {
            mySentences.add(sentence.toString());
        } else if (parseState != State.endSentence) {
            mySentences.add("");
        }
    }


    public static ArrayList[] shuffle(List<String> myWords, List<String> mySentences) {
        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<String> shuffledWords = new ArrayList<>();
        ArrayList<String> shuffledSentences = new ArrayList<>();
        for (int i = 0; i < myWords.size(); i++) {
            order.add(i);
        }
        Collections.shuffle(order);
        for (int i = 0; i < myWords.size(); i++) {
            shuffledWords.add(myWords.get(order.get(i)));
            shuffledSentences.add(mySentences.get(order.get(i)));
        }

        return new ArrayList[]{shuffledWords, shuffledSentences};
    }
}
