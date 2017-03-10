package com.integrityvision;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class is used to find the first longest concatenated word(s),
 * the second-longest concatenated word(s) and the total count
 * of concatenated words in a file.
 *
 * @author Agienko Sergiy <agienko.sergiy@gmail.com>
 *
 */
public class ChallengeSolver {

    // List of longest words that is comprised entirely of shorter words in the file
    private List<String> longestWords;

    // List of second-longest words
    private List <String> secondLongestWords;

    //The total count of concatenated words in the file
    private int concatWordsCount=0;

    //Path to file with words
    private String pathToWordsFile;


    /**
     * Constructor.
     *
     * @param pathToWordsFile (required) valid path to text file with
     * words. The words should be listed one word per line,
     * do not contain spaces, and be all lowercase.
     *
     */
    public ChallengeSolver(String pathToWordsFile) {
        this.longestWords = new ArrayList<>();
        this.secondLongestWords = new ArrayList<>();
        this.pathToWordsFile = pathToWordsFile;
    }


    public static void main(String[] args) {

        ChallengeSolver solver = new ChallengeSolver("words.txt");

        solver.solveTheChallenge();
        solver.printResults();
    }


    /**
     * Populates class fields with data. Actually solves the challenge.
     */
    public void solveTheChallenge(){

        List<String> conatenatedWords =
                getConcatenatedWords(getWordsListFromFile(pathToWordsFile));

        //checking if there are any results
        if(!conatenatedWords.isEmpty()){
            concatWordsCount = conatenatedWords.size();
            String longestWord = conatenatedWords.get(concatWordsCount-1);

            int longestWordLength = longestWord.length();
            int secondLongestWordLength=0;

            //filling the results
            for(int i = conatenatedWords.size()-1;i>=0;i--){
                String word = conatenatedWords.get(i);

                if(word.length()==longestWordLength){
                    longestWords.add(word);
                    conatenatedWords.remove(word);
                }
                else {
                    if(secondLongestWordLength==0){
                        secondLongestWordLength=word.length();
                        secondLongestWords.add(word);
                    }
                    else if (word.length()==secondLongestWordLength){
                        secondLongestWords.add(word);
                    }
                    else {
                        break;
                    }
                }
            }
        }
    }


    /**
     * Outputs results to console.
     */
    public void printResults(){
        if(!longestWords.isEmpty()){

            //print longest word(s)
            if(longestWords.size()>1){
                System.out.println("The longest words are: ");
                for(String word: longestWords){
                    System.out.println(word + " (length: " + word.length() + " letters),");
                }
            }
            else{
                String word = longestWords.get(0);
                System.out.println("The longest concatenated word is " +
                        word + " (length: " + word.length() + " letters),");
            }

            //print second-longest word(s)
            if(!secondLongestWords.isEmpty()){
                if(secondLongestWords.size()>1){
                    System.out.println("The second-longest words are: ");
                    for(String word: secondLongestWords){
                        System.out.println(word + " (length: " + word.length() + " letters),");
                    }
                }
                else{
                    String word = secondLongestWords.get(0);
                    System.out.println("The second-longest concatenated word is " +
                            word + " (length: " + word.length() + " letters),");
                }
            }

            //print total count of all the concatenated words in the file
            System.out.println("The total count of all the concatenated words in the file: " + concatWordsCount);
        }
        else{
            System.out.println("There are no concatenated words in file :(");
        }

    }


    /**
     * Gets concatenated words list from file.
     *
     * @param pathToFile The path to file with words.
     * @return List of concatenated words.
     */
    public static List<String> getWordsListFromFile(String pathToFile){
        List<String> wordsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            for(String word; (word = br.readLine()) != null; ) {
                if(!word.isEmpty()) {
                    wordsList.add(word);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

        return wordsList;
    }


    /**
     * Finds concatenated words in list.
     *
     * @param words The words list to search for concatenated words in.
     * @return List of concatenated words.
     */
    public static List<String> getConcatenatedWords(List<String> words){
        List<String> concatenatedWords = new ArrayList<>();
        Set<String> preWords = new HashSet<>();
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });
        for(String word:words){
            if (isConcatenated(word, preWords)) {
                concatenatedWords.add(word);
            }
            preWords.add(word);
        }
        return concatenatedWords;
    }


    /**
     * Tells if a word is a concatenated word or not.
     *
     * @param  word The word to check if it's a concatenated word or not.
     * @param preWords The set of previously checked words.
     * @return True if the given word is a concatenated word.  False otherwise.
     */
    private static boolean isConcatenated(String word, Set<String> preWords){
        if (preWords.isEmpty()) return false;
        boolean[] concatMarkers = new boolean[word.length() + 1];
        concatMarkers[0] = true;
        for (int i = 1; i <= word.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (!concatMarkers[j]) continue;
                if (preWords.contains(word.substring(j, i))) {
                    concatMarkers[i] = true;
                    break;
                }
            }
        }
        return concatMarkers[word.length()];
    }

}