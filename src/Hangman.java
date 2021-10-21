import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Hangman {

    // hangman images source https://codegolf.stackexchange.com/questions/135936/ascii-hangman-in-progress
    // hangman logo Image source: https://ascii.co.uk/art/hangman

    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println(Database.hangmanImages.get(7)); // just print a good hangman logo. Number 7 indicate logo exist in key 7 in the image  HashMap.
        String userSavedData = "";
        int wrongGuessCounter = 0; // to calculate how many times user has guessed wrong
        int correctGuessCounter = 0; // to calculate if user has guessed all right
        userSavedData = getUserSavedData(); // read user.txt file and get the saved value

        if (!userSavedData.isEmpty()) {
            System.out.println("Do you like to start from save game? Enter yes or no");
            String startFromSaveGame = scanner.nextLine();
            if (startFromSaveGame.equals("yes")) {
                String[] userData = userSavedData.split(";"); //
                wrongGuessCounter = Integer.parseInt(userData[0]); // wrong guess counter was saved first, therefore array index 0
                correctGuessCounter = Integer.parseInt(userData[1]); // correct guess counter was saved as second value, therefore array index 1
                String savedGuessedData = userData[2]; // letter guess data was saved as third value, therefore array index 2
            }
            System.out.println(userSavedData);
        } else {
            System.out.println("no user data");
        }


/*


            System.out.println("Saved wrong guess " + wrongGuessCounter);

            System.out.println("Saved correct guess " + correctGuessCounter);

            String[] chars = savedGuessedData.split(",");



            for (int i = 0; i < chars.length; i++) {
                System.out.print(chars[i] + " ");
            }




 */
        // read the Word file and make an arrayList
        try{
            File wordFile = new File("files/word.txt");  // create a file object
            Scanner wordFileScanner = new Scanner(wordFile); // set a scanner for the Word file
            while (wordFileScanner.hasNextLine()){
                String individualWord =wordFileScanner.nextLine();
                Database.wordList.add(individualWord);
            }
            System.out.println(Database.wordList); // just to check if scanner has worked perfectly, can be deleted later ?????
        } catch (FileNotFoundException e){
            System.out.println("File could not found");
        }


        String wordToGuess = getRandomWordFromList(); // get a random word from word list
        System.out.println(wordToGuess); // print the word to guess, can be deleted ?????
        char[] charsInWordToGuess =  wordToGuess.toCharArray(); // make an array with individual char from word to guess

        // get the length of the "charsInWordToGuess", will also use to define the size of "userGuessedChars" array and fill with "_" sign
        int sizeOfCharsInWordToGuess = charsInWordToGuess.length;

        // make a char array to store users correct guesses and initially fill with "_" sign
        char[] userGuessedChars = new char[sizeOfCharsInWordToGuess];
        for (int i = 0; i < sizeOfCharsInWordToGuess; i++) {
            userGuessedChars[i] = '_';
        }



        // start wile loop to get user's choice
        // break the loop if user guessed 6 times wrong / when user has guessed all correct.
        // if user deliberately type "exit", the loop will break too
        while (true){
            boolean isGuessRight = false;// if user guess right letter, then will set true inside the checking for loop

            // ask to guess a letter
            // print the current "userGuessedChars", which is in the first time filled with only "_" signs
            // get the user's guess
            System.out.println("Guess a letter: (or Enter \"exit\" to exit the game)");
            for (int i = 0; i < userGuessedChars.length; i++) {
                System.out.print(userGuessedChars[i] + " ");
            }
            String userInput = scanner.nextLine().toLowerCase();
            if (userInput.equals("exit")) {
                //ask if user like to save the current game or not
                System.out.println("Do you like to save the current game? type yes or no");
                String saveGameOrNot = scanner.nextLine().toLowerCase();
                if (saveGameOrNot.equals("yes")) {
                    try {
                        FileWriter myWriter = new FileWriter("files/user.txt");
                        myWriter.write(wrongGuessCounter + ";");
                        myWriter.write(correctGuessCounter + ";");
                        for (int i = 0; i < userGuessedChars.length; i++) {
                            myWriter.write(userGuessedChars[i] + ",");
                        }
                        myWriter.close();
                        System.out.println("Successfully saved to the file.");
                        break;
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }
            }
            char guessedChar = userInput.charAt(0);

            // check if user has guessed correct or not by checking if users guessed letter exist in the "charsInWordToGuess" or not
            // if yes, we check in which index that letter exists in "charsInWordToGuess" array
            // we set user's guessed char to the corresponding index of "userGuessedChars" array
            // we also set "isGuessRight" to true
            for (int i = 0; i < sizeOfCharsInWordToGuess; i++) {
                char individualCharInGuessedWordChars = charsInWordToGuess[i];
                int compareOneTwo = Character.compare(guessedChar, individualCharInGuessedWordChars);
                if (compareOneTwo == 0) {
                    userGuessedChars[i] = guessedChar;
                    System.out.println("You have guessed correct!");
                    isGuessRight = true;
                    correctGuessCounter = correctGuessCounter + 1;
                }
            }

            wrongGuessCounter = getWrongGuessCounter(wrongGuessCounter, isGuessRight); // increase the value of wrong guess counter, and print results if user's guess was wrong
            printHangmanImage(wrongGuessCounter); // print the specific hangman image based on how many times user has guessed wrong
            System.out.println("--------------------------------------------------------"); // print to make output nice

            // in case user has guessed 6 times wrong, so user has lost the game. Break the loop and print result
            if (wrongGuessCounter == 6) {
                System.out.println("You guessed 6 times wrong, so you lost the game");
                System.out.println("Correct word was: " + wordToGuess);
                break;
            }

            // check if user has guessed all right, if yes print the result
            if (correctGuessCounter == userGuessedChars.length) {
                System.out.println("Congratulations! you correctly guessed the word: " + wordToGuess);
                break;
            }
        }
    } // end of main






    // method to calculate wrong guess attempts, print the data, and return the number of wrong guess
    static int getWrongGuessCounter(int wrongGuessCounter, boolean isGuessRight){
        if (isGuessRight == false) {
            wrongGuessCounter = wrongGuessCounter + 1;
            System.out.println("You have guessed wrong");
            System.out.println("You already guessed " + wrongGuessCounter + " times wrong");
        }
        return wrongGuessCounter;
    }

    // will get a random word from database>>wordList and return as string
    public static String getRandomWordFromList(){
       int totalNoOfWordsInWordList = Database.wordList.size(); // get how many words are there
       int randomIndexFromWordList = random.nextInt((totalNoOfWordsInWordList+0)+0); // get a random index from word list
       String randomWord = Database.wordList.get(randomIndexFromWordList); // get the word from random index
       return randomWord;
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

    // method will read user.txt file and return the value, if file is empty then will return empty string
    static String getUserSavedData() {
        String userSavedData = "";
        try {
            File userFile = new File("files/user.txt");
            Scanner userScanner = new Scanner(userFile);
            while (userScanner.hasNextLine()) {
                userSavedData = userScanner.nextLine();
            }
        } catch (FileNotFoundException e){
            System.out.println("User file could not found");
        }
        return userSavedData;
    }

} // end of class
