import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Hangman {

    // hangman images source https://codegolf.stackexchange.com/questions/135936/ascii-hangman-in-progress
    // hangman logo Image source: https://ascii.co.uk/art/hangman

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /*
        * always on startup show hangman logo
        * set necessary global variables
         * after that read the user.txt, which has information of previous game (file is blank if no data was saved earlier)
            * then save all data to "database >> userSavedData"
         * check if "database >> userSavedData" is empty or not
            * if yes, means there is no previously saved data exist, so start new game
            * if not, means there is previously saved game exist, so sk user if user like to resume saved game or not:
                * if yes, read data from "database >> userSavedData"  and assign to relevant variables
                * if no, start a new game
         * during starting a new game:
            * first read the word.txt file and save data to "Database >> wordList/wordListEasy/wordListMedium/wordListDifficult"
            * ask user to chose difficulty level and chose a random word from Database based on difficulty level
         * at anypoint user have the opportunity to exit the game by entering "exit"
            * if user decided to exit a game, all data related to current game will be saved to user.txt
        * if user has guessed 6 times wrong then user will lose the game.
            * a message will be shown saying user has lost
            * In that scenario user.txt will be made blank by erasing any previously saved data
            * user will be asked if user like to play another round or quit
        * if user guess all letters correctly,
            * a message will be shown saying user has won
            * the user.txt file will also be made blank by erasing any previously saved data
            * user will be asked if user like to play another round or quit
         */

        // print a hangman logo. Number 7 indicate that logo exists in key 7 in the image  HashMap.
        System.out.println("Welcome to \n"  +Database.hangmanImages.get(7));
        // word to be guessed by user
        String wordToGuess = "";
        // to calculate how many times user has guessed wrong
        int wrongGuessCounter = 0;
        // start a saved game or not
        String startFromSaveGame = "no";
        // make a char array to store users correct guesses
        ArrayList<Character> userGuessedChars = new ArrayList<Character>();

        // read user.txt file and saved data to "Database >> userSavedData"
        HangmanFileReader.readUserSavedData();


        // if "Database >> userSavedData" is not empty then start this block
        if (!Database.userSavedData.isEmpty()) {
            System.out.println("Do you like to start from save game? Enter yes or no");
            startFromSaveGame = scanner.nextLine(); // get users choice

            // if user decide to play previously saved game then run this block
            if (startFromSaveGame.equals("yes")) {
                wordToGuess = Database.userSavedData.get(0); // get the word to be guessed
                wrongGuessCounter = Integer.parseInt(Database.userSavedData.get(1)); // wrong guess counter was saved first, therefore array index 0
                userGuessedChars = getSavedUserGuessedChars(); // get already correctly guessed chars
            }
        }


        // if "Database >> userSavedData" is empty then start this block
        // plus if "Database >> userSavedData" is not empty but user decided to start a new game
        if (Database.userSavedData.isEmpty() || startFromSaveGame.equals("no")) {
            HangmanFileReader.readWordFileAndSaveData(); // read the word.txt file and save data to Database
            int difficultyLevel = getDifficultyLevel(); // get user's choice of difficulty level (easy/medium/hard)
            wordToGuess = getRandomWord(difficultyLevel); // get a random word from word list depending on user's choice of difficulty
            userGuessedChars = getDashFilledUserGuessedChar(wordToGuess); // initially fill the "userGuessedChars" with "_"
        }





        // start wile loop to get user's choice
        // break the loop if user guessed 6 times wrong or when user has guessed all correct.
        // if user deliberately type "exit", the loop will break too and quit the game
        while (true) {
            // if user guess a right letter, then will set true inside the checking for loop
            boolean isGuessRight = false;
            // will save user's individual guessed letter
            char guessedChar;
            // print the current guesses plus empty guesses, first time it will be only "_" signs
            printCurrentUserGuesses(userGuessedChars);

            // get the user's input (exit the game or guess a char)
            String userInput = getUserInput();
            if (userInput.equals("exit")) { // if user decided to exit the game
                // save game data
                HangmanFileWriter.saveGameDataToFile(wordToGuess, wrongGuessCounter, userGuessedChars);
                // break the while loop, exit the current game
                break;
            } else { // if user guess a char
                // if user typed a char, then parse string to char
                guessedChar = userInput.charAt(0);
            }


            // check if user has guessed correct or not
            // we set user's guessed char to the corresponding index of "userGuessedChars" array
            // we also set "isGuessRight" to true
            for (int i = 0; i < wordToGuess.length(); i++) {
                char individualChar = wordToGuess.charAt(i);
                int compareGuessedCharWithOriginal = Character.compare(guessedChar, individualChar);
                if (compareGuessedCharWithOriginal == 0) {
                    userGuessedChars.set(i, guessedChar);
                    System.out.println("You have guessed correct!");
                    isGuessRight = true;
                }
            }

            // if user's guess was wrong, increase the value of wrong guess counter, and print results
            wrongGuessCounter = getWrongGuessCounter(wrongGuessCounter, isGuessRight);
            // print the specific hangman image based on how many times user has guessed wrong
            printHangmanImage(wrongGuessCounter);
            System.out.println("--------------------------------------------------------");

            // check if user has guessed all right, if yes print the result
            boolean userGuessedAllChars = hasUserGuessedAllChars(userGuessedChars);
            if (userGuessedAllChars == true) {
                System.out.println("Congratulations! you correctly guessed the word: " + wordToGuess);
                HangmanFileWriter.makeGameDataEmpty();
                break;
            }

            // in case user has guessed 6 times wrong, so user has lost the game. Break the loop and print result
            if (wrongGuessCounter == 6) {
                System.out.println("You lost the game. Correct word was: " + wordToGuess);
                HangmanFileWriter.makeGameDataEmpty();
                break;
            }
        }
    } // end of main

    // check if user has guessed all letter correctly
    static boolean hasUserGuessedAllChars(ArrayList<Character> userGuessedChars){
        for (int i = 0; i < userGuessedChars.size(); i++) {
            char individualChar = userGuessedChars.get(i);
            int dashOrNot = Character.compare(individualChar, '_');
            if (dashOrNot == 0) {
                return false;
            }
        }
        return true;
    }

    // ask and return difficulty level out of easy, medium and hard
    static int getDifficultyLevel(){
        int difficultyLevel = 0;
        System.out.println("Choose difficulty: 1- easy, 2- medium, 3- hard");
        difficultyLevel = Integer.parseInt(scanner.nextLine());
        return difficultyLevel;
    }

    // get already correctly guessed letters from saved data
    //make an array list and return it
    static ArrayList getSavedUserGuessedChars(){
        ArrayList<Character> savedUserGuessedChars = new ArrayList<>();
        String savedUserGuesses = Database.userSavedData.get(2);
        String[] charsAsString = savedUserGuesses.split(",");
        for (int i = 0; i < charsAsString.length; i++) {
            char individualChar = charsAsString[i].charAt(0);
            savedUserGuessedChars.add(individualChar);
        }
        return savedUserGuessedChars;
    }

    // when start a new game, initially fill the userGuessedChars array list with "_" and return it
    static ArrayList  getDashFilledUserGuessedChar(String wordToGuess) {
        ArrayList<Character> userGuessedChars = new ArrayList<>();
        for (int i = 0; i < wordToGuess.length(); i++) {
            userGuessedChars.add('_');
        }
        return userGuessedChars;
    }

    // print the current correctly guessed letters and dashes
    static void printCurrentUserGuesses(ArrayList userGuessedChars){
        for (int i = 0; i < userGuessedChars.size(); i++) {
            System.out.print(userGuessedChars.get(i)  + " ");
        }
        System.out.println(); // just add line break after printing
    }

    // get the user input as exit or a char
    static  String getUserInput(){
        System.out.println("Guess a letter: (or Enter \"exit\" to exit the game)");
        String userInput = scanner.nextLine().toLowerCase();
        return userInput;
    }

    // read word file, save in database, and pick a random word, return the word
    static String getRandomWord(int difficultyLevel){
        String randomWord = "";
        int totalNoOfWordsInWordList;
        int randomIndexFromWordList;
        if (difficultyLevel == 1) {
            totalNoOfWordsInWordList = Database.wordListEasy.size(); // get how many words are there
            randomIndexFromWordList = random.nextInt((totalNoOfWordsInWordList+0)+0); // get a random index from word list
            randomWord = Database.wordListEasy.get(randomIndexFromWordList); // get the word from random index
        } else if (difficultyLevel == 2) {
            totalNoOfWordsInWordList = Database.wordListMedium.size(); // get how many words are there
            randomIndexFromWordList = random.nextInt((totalNoOfWordsInWordList+0)+0); // get a random index from word list
            randomWord = Database.wordListMedium.get(randomIndexFromWordList); // get the word from random index
        } else {
            totalNoOfWordsInWordList = Database.wordListDifficult.size(); // get how many words are there
            randomIndexFromWordList = random.nextInt((totalNoOfWordsInWordList+0)+0); // get a random index from word list
            randomWord = Database.wordListDifficult.get(randomIndexFromWordList); // get the word from random index
        }
        return randomWord;
    }

    // method to calculate wrong guess attempts, print the data, and return the number of wrong guess
    static int getWrongGuessCounter(int wrongGuessCounter, boolean isGuessRight){
        if (isGuessRight == false) {
            wrongGuessCounter = wrongGuessCounter + 1;
            System.out.println("You have guessed wrong");
            System.out.println("You already guessed " + wrongGuessCounter + " times wrong");
        }
        return wrongGuessCounter;
    }

    // method to print hangman image depending on how many mistakes user has made
    public static void printHangmanImage(int falseGuessCounter){
        System.out.println();
        switch (falseGuessCounter){
            case 0:
                System.out.println(Database.hangmanImages.get(0));
                break;
            case 1:
                System.out.println(Database.hangmanImages.get(1));
                break;
            case 2:
                System.out.println(Database.hangmanImages.get(2));
                break;
            case 3:
                System.out.println(Database.hangmanImages.get(3));
                break;
            case 4:
                System.out.println(Database.hangmanImages.get(4));
                break;
            case 5:
                System.out.println(Database.hangmanImages.get(5));
                break;
            case 6:
                System.out.println(Database.hangmanImages.get(6));
                break;
            default: break;
        }
    }
} // end of class
