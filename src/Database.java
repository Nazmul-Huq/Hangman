import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    static ArrayList<String> wordList = new ArrayList<String>(); // save all words
    static ArrayList<String> wordListEasy = new ArrayList<String>(); // save only words with 5 or fewer letters
    static ArrayList<String> wordListMedium = new ArrayList<String>(); // save words between 6-10 letters
    static ArrayList<String> wordListDifficult = new ArrayList<String>(); // save words with more than 10 letters
    static ArrayList<String> userSavedData = new ArrayList<>(); // save user's game data

    // different hangman images are stored
    static HashMap<Integer, String> hangmanImages = new HashMap<>(Map.of(
            0,"  +---+\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            1, "+---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            2, "+---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            3, " +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========\n",
            4, "+---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            5, " +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    " /    |\n" +
                    "      |\n" +
                    "=========",
            6, "+---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    " / \\  |\n" +
                    "      |\n" +
                    "=========",
            7, "| |                                            \n" +
                    "| |__   __ _ _ __   __ _ _ __ ___   __ _ _ __  \n" +
                    "| '_ \\ / _` | '_ \\ / _` | '_ ` _ \\ / _` | '_ \\ \n" +
                    "| | | | (_| | | | | (_| | | | | | | (_| | | | |\n" +
                    "|_| |_|\\__,_|_| |_|\\__, |_| |_| |_|\\__,_|_| |_|\n" +
                    "                    __/ |                      \n" +
                    "                   |___/      "
    ));


}


