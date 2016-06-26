package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private static HashSet<String> wordSet = new HashSet<>();
    private static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private static int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            String sortedWord = sortString(word);

            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(word);
                lettersToWord.put(sortedWord, arrayList);
            }

            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(word);
                sizeToWords.put(word.length(), arrayList);
            }
        }
    }

    public String sortString(String a) {
        char[] chars = a.toCharArray();
        Arrays.sort(chars);
        String string = new String(chars);
        return string;
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(base)) {
            System.out.print("Word contains exact base string.");
            return false;
        }
        if (!wordSet.contains(word)) {
            System.out.print("Word is not present in dictionary.");
            return false;
        }
        return true;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            String abc = sortString(word + c);
            if (lettersToWord.containsKey(abc)) {
                ArrayList<String> anagram = lettersToWord.get(abc);
                for (String s : anagram) {
                    if (isGoodWord(s, word)) {
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> list = sizeToWords.get(wordLength);

        while (list.size() < 1){
            wordLength++;
            list = sizeToWords.get(wordLength);
        }

        int i = Math.abs(random.nextInt() % list.size());
        String answer = new String();
        int counter = 0;

        for ( ; ; i++, counter++) {
            String word = list.get(i);
            String sortedword = sortString(word);
            ArrayList<String> sortedwordList = lettersToWord.get(sortedword);
            if (sortedwordList.size() >= MIN_NUM_ANAGRAMS) {
                answer = word;
                break;
            }

            if (i == list.size() - 1) {
                i = 0;
            }

            if (counter == list.size()){
                if (wordLength < MAX_WORD_LENGTH){
                    wordLength++;
                }
                list = sizeToWords.get(wordLength);
                i = Math.abs(random.nextInt() % list.size());
                counter = 0;
            }
        }

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return answer;
    }
}
