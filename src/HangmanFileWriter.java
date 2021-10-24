import java.io.IOException;
import java.util.ArrayList;

public class HangmanFileWriter {

    // save game data to user.txt file
    static void saveGameDataToFile(String wordToGuess, int wrongGuessCounter, ArrayList userGuessedChars){
        try {
            java.io.FileWriter hangmanFileWriter = new java.io.FileWriter("src/user.txt");
            hangmanFileWriter.write(wordToGuess + "\n");
            hangmanFileWriter.write(wrongGuessCounter + "\n");
            for (int i = 0; i < userGuessedChars.size(); i++) {
                hangmanFileWriter.write(userGuessedChars.get(i) + ",");
            }
            hangmanFileWriter.close();
            System.out.println("Successfully saved game data to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //make the user.txt file empty
    static void makeGameDataEmpty(){
        try {
            java.io.FileWriter hangmanFileWriter = new java.io.FileWriter("src/user.txt");
            hangmanFileWriter.write( "");
            hangmanFileWriter.close();
            System.out.println("User file is empty now");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
