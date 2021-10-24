import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HangmanFileReader {

    static void readWordFileAndSaveData(){

        // read the Word file and save data to "Database >> wordList/wordListEasy/wordListMedium/wordListDifficult"
        try{
            File wordFile = new File("src/word.txt");  // create a file object
            Scanner wordFileScanner = new Scanner(wordFile); // set a scanner for the Word file
            while (wordFileScanner.hasNextLine()){
                String individualWord =wordFileScanner.nextLine(); // get each word (in this case each line)
                addWordToDatabase(individualWord); // add the word to the database
            }
        } catch (FileNotFoundException e){
            System.out.println("File could not found");
        }

    }

    // add the scanned word to the "Database >> wordList/wordListEasy/wordListMedium/wordListDifficult"
    static void addWordToDatabase(String individualWord){
        Database.wordList.add(individualWord);
        if (individualWord.length() > 10) {
            Database.wordListDifficult.add(individualWord);
        } else if (individualWord.length() <= 5) {
            Database.wordListEasy.add(individualWord);
        } else {
            Database.wordListMedium.add(individualWord);
        }
    }



    // method will read user.txt file and save data to "Database >> userSavedData""
    static void readUserSavedData() {
        try {
            File userFile = new File("src/user.txt");
            Scanner userScanner = new Scanner(userFile);
            while (userScanner.hasNextLine()) {
                String individualLine = userScanner.nextLine();
                Database.userSavedData.add(individualLine);
            }
        } catch (FileNotFoundException e){
            System.out.println("User file could not found");
        }
    }

} // end of class
