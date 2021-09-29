package byow.InputDemo;

/**
 * Created by hug.
 */

public class ReplayInputSource implements InputSource {
    private String input;
    private int index;

    public ReplayInputSource(String s) {
        index = 0;
        input = s;
    }

    public char getNextKey() {
        char returnChar = input.charAt(index);
        index += 1;
        return returnChar;
    }

    public boolean possibleNextInput() {
        return index < input.length();
    }

    @Override
    public int getInt() {
        return 0;
    }
}

