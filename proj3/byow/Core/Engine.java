package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.ReplayInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 50;
    private static final int HEIGHT = 30;

    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private WorldGen test = new WorldGen();
    private Font titleFont = new Font("Monaco", Font.BOLD, 30);
    private Font smallFont = new Font("Monaco", Font.BOLD, 15);
    private static final int TILE_SIZE = 16;
    private Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
    private Font loreFont = new Font("TimesRoman", Font.ITALIC, 20);
    private Font winFont = new Font("Serif", Font.BOLD, 60);
    private StringBuilder replayStringBuilder = new StringBuilder();
    private TETile avatarType = Tileset.AVATAR;
    private Avatar avatar = new Avatar();
    private TETile floor = Tileset.FLOOR;
    private TETile wall = Tileset.WALL;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputKeyboard = new KeyboardInputSource();
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        displayMenu();
        world = parseInput(inputKeyboard);
        world = playGame(inputKeyboard);
    }
    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        InputSource inputString = new StringInputDevice(input);
        world = parseInput(inputString);
        world = playGame(inputString);
        //ter.initialize(WIDTH, HEIGHT + 2);
        //ter.renderFrame(world);

        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        return world;
    }

    /**
     * Parse the input seed if in the form N###S or get seed from loaded file
     * After retrieving the seed, get the world by calling generateRandomWorld with the seed, th
     * @param inputSource
     * @return
     */
    public TETile[][] parseInput(InputSource inputSource) {
        setUpPersistence();
        long seedNumber = 0;
        char c = Character.toUpperCase(inputSource.getNextKey());
        if (c == 'N') {
            StringBuilder sb = new StringBuilder();
            boolean parsing = true;
            if (inputSource.getInt() == 0) {
                displayMenu();
                drawFrame("Seed: ", WIDTH / 2, HEIGHT / 4, titleFont);
            }
            while (parsing) {
                if (inputSource.possibleNextInput()) {
                    char c2 = Character.toUpperCase(inputSource.getNextKey());
                    if (c2 == 'S') {
                        parsing = false;
                    } else {
                        sb.append(c2);
                        if (inputSource.getInt() == 0) {
                            displayMenu();
                            drawFrame("Seed: " + sb.toString(), WIDTH / 2, HEIGHT / 4, titleFont);
                        }
                    }
                }
            }
            String s = sb.toString();
            seedNumber = Long.parseLong(s);
            if (inputSource.getInt() == 0) {
                ter.initialize(WIDTH, HEIGHT + 2);
            }
            world = test.generateRandomWorld(world, ter, seedNumber);
            world = avatar.initiateAvatar(world, test);
        }
        if (c == 'L') {
            String s = readFile("lastWorld");
            if (s.equals("")) {
                return null;
            }
            seedNumber = Long.parseLong(s);
            if (inputSource.getInt() == 0) {
                ter.initialize(WIDTH, HEIGHT + 2);
            }
            world = test.generateRandomWorld(world, ter, seedNumber);
            String position = readFile(s);
            String[] posArray = position.split(",");
            Position p = new Position(Integer.parseInt(posArray[0]), Integer.parseInt(posArray[1]));
            world = avatar.loadAvatar(world, p);
        }
        if (c == 'R') {
            //get seed from saved file
            String s = readFile("lastWorld");
            if (s.equals("")) {
                return null;
            }
            seedNumber = Long.parseLong(s);
            ter.initialize(WIDTH, HEIGHT + 2);
            world = test.generateRandomWorld(world, ter, seedNumber);
            world = avatar.initiateAvatar(world, test);
            String replayString = readFile("replayWorld");
            InputSource inputReplay = new ReplayInputSource(replayString);
            world = playGame(inputReplay);
        }
        if (c == 'T') {
            randomTheme(inputSource);
        }
        if (c == 'C') {
            chooseAvatar(inputSource);
        }
        if (c == 'Q') {
            return null;
        }
        if (c == '?') {
            displayLore(inputSource);
        }
        if (inputSource.getInt() == 0) {
            ter.renderFrame(world);
        }
        //TO DO: initialize HUD, allow for winning if reaching lockedDOOR
        return world;
    }

    public void displayLore(InputSource inputSource) {
        StdDraw.clear(Color.BLACK);
        drawFrame("Hello! If you are now reading this, you "
                + "will soon be embarking on a quest", WIDTH / 2, 3 * HEIGHT / 4, loreFont);
        drawFrame("In the land of darkness, you have stumbled "
                + "across rooms of light", WIDTH / 2, 3 * HEIGHT / 4 - 1, loreFont);
        drawFrame("Be careful though, as each misstep will cause "
                + "this light to fade darker...", WIDTH / 2, 3 * HEIGHT / 4 - 2, loreFont);
        drawFrame("Darker and darker...", WIDTH / 2, 3 * HEIGHT / 4 - 3, loreFont);
        drawFrame("To return to your home is simple, however",
                WIDTH / 2, 3 * HEIGHT / 4 - 4, loreFont);
        drawFrame("All you need to do is find the golden door",
                WIDTH / 2, 3 * HEIGHT / 4 - 5, loreFont);
        drawFrame("It’s won’t be as simple as you think though",
                WIDTH / 2, 3 * HEIGHT / 4 - 6, loreFont);
        drawFrame("For this door likes to play tricks", WIDTH / 2,
                3 * HEIGHT / 4 - 7, loreFont);
        drawFrame("Exit (X)", WIDTH / 2, HEIGHT / 2 - 4, loreFont);
        char c = Character.toUpperCase(inputSource.getNextKey());
        if (c == 'X') {
            displayMenu();
            parseInput(inputSource);
        }
    }

    public void chooseAvatar(InputSource inputSource) {
        //menu
        StdDraw.clear(Color.BLACK);
        Color color = null;
        //StdDraw.enableDoubleBuffering();
        drawFrame("Choose a color", WIDTH / 2, 3 * HEIGHT / 4, titleFont);
        drawFrame("Red (R)", WIDTH / 2, HEIGHT / 2 + 1, smallFont);
        drawFrame("Orange (O)", WIDTH / 2, HEIGHT / 2, smallFont);
        drawFrame("Yellow (Y)", WIDTH / 2, HEIGHT / 2 - 1, smallFont);
        drawFrame("Green (G)", WIDTH / 2, HEIGHT / 2 - 2, smallFont);
        drawFrame("Blue (B)", WIDTH / 2, HEIGHT / 2 - 3, smallFont);
        drawFrame("Purple (P)", WIDTH / 2, HEIGHT / 2 - 4, smallFont);
        char c = Character.toUpperCase(inputSource.getNextKey());
        if (c == 'R') {
            color = Color.RED;
        } else if (c == 'O') {
            color = Color.orange;
        } else if (c == 'Y') {
            color = Color.yellow;
        } else if (c == 'G') {
            color = Color.green;
        } else if (c == 'B') {
            color = Color.blue;
        } else if (c == 'P') {
            color = Color.magenta;
        }
        avatar = new Avatar(color);
        //call parse input
        displayMenu();
        parseInput(inputSource);
    }

    public void randomTheme(InputSource inputSource) {
        test.randomTheme(avatar);
        displayMenu();
        parseInput(inputSource);
    }

    /**
     * Create loadWorld File if it doesn't exist
     */
    public static void setUpPersistence() {
        try {
            File lastWorld = new File("lastWorld.txt");
            if (lastWorld.createNewFile()) {
                //System.out.println("File created: lastWorld");
                //write to File
                try {
                    FileWriter myWriter = new FileWriter("lastWorld.txt");
                    myWriter.write("");
                    myWriter.close();
                    //System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    //System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            } /*else {
                //System.out.println("File already exists.");
            }*/
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileName, String contents) {
        try {
            File savedWorld = new File(fileName + ".txt");
            if (savedWorld.createNewFile()) {
                System.out.println("File created: " + savedWorld.getName());
            }
            /*else {
                //System.out.println("File already exists.");
            }*/
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //write to File
        try {
            FileWriter myWriter = new FileWriter(fileName + ".txt");
            myWriter.write(contents);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public String readFile(String fileName) {
        StringBuilder data = new StringBuilder();
        try {
            File myObj = new File(fileName + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data.append(myReader.nextLine());
                //System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data.toString();
    }

    /**
     * After getting the world from ParseInput, pass in the inputSource,
     * continue taking letters until there are none left or you :Q to
     * quit the game if the inputSource is an inputKeyboard, need to
     * draw the world to frame as you go
     * @param inputSource
     * @return
     */
    public TETile[][] playGame(InputSource inputSource) {
        boolean playing = true;
        while (playing) {
            if (inputSource.getInt() == 0) {
                int X = (int) Math.floor(StdDraw.mouseX());
                int Y = (int) Math.floor(StdDraw.mouseY());
                StdDraw.pause(50);
                int X2 = (int) Math.floor(StdDraw.mouseX());
                int Y2 = (int) Math.floor(StdDraw.mouseY());
                boolean mouseMoved = !(X == X2 && Y == Y2);
                if ((X2 >= WIDTH || Y2 >= HEIGHT)) {
                    ter.renderFrame(world);
                } else if (mouseMoved) {
                    TETile tile = world[X2][Y2];
                    ter.renderFrame(world);
                    drawFrame(tile.description(), 4, HEIGHT + 1, font);
                    drawFrame("Mistake made: " + avatar.getLose(), 12, HEIGHT + 1, font);
                }
            }
            if (inputSource.getInt() == 1 && !inputSource.possibleNextInput()) {
                playing = false;
            }
            if (inputSource.possibleNextInput()) {
                char c = Character.toUpperCase(inputSource.getNextKey());
                replayStringBuilder.append(c);
                if (c == ':') {
                    if (Character.toUpperCase(inputSource.getNextKey()) == 'Q') {
                        replayStringBuilder.append(c);
                        playing = false;
                        saveWorld();
                        if (inputSource.getInt() == 0) {
                            System.exit(0);
                        }
                    }
                } else if (c == 'L') {
                    world = test.turnOffLights(world);
                    world = avatar.loadAvatar(world, avatar.getPosition());
                    ter.renderFrame(world);
                } else if (c == 'K') {
                    world = test.turnOnLights(world);
                    world = avatar.loadAvatar(world, avatar.getPosition());
                    ter.renderFrame(world);
                } else {
                    world = avatar.moveAvatar(world, c, test);
                    playing = avatar.getWon();
                    if (!playing) {
                        displayWon();
                        break;
                    }
                    if (avatar.getLose() > 10) {
                        displayLose();
                        break;
                    }
                    if (inputSource.getInt() == 0) {
                        ter.renderFrame(world);
                        int X2 = (int) Math.floor(StdDraw.mouseX());
                        int Y2 = (int) Math.floor(StdDraw.mouseY());
                        if (X2 < WIDTH && Y2 < HEIGHT) {
                            TETile tile = world[X2][Y2];
                            drawFrame(tile.description(), 4, HEIGHT + 1, font);
                            drawFrame("Mistake made: " + avatar.getLose(), 12, HEIGHT + 1, font);

                        }
                    }
                }
            }
        }
        return world;
    }

    public void displayWon() {
        StdDraw.clear(Color.BLACK);
        drawFrame("CONGRATS, YOU WON!", WIDTH / 2, HEIGHT / 2, winFont);
    }

    public void displayLose() {
        StdDraw.clear(Color.BLACK);
        drawFrame("Sorry, you lost", WIDTH / 2, HEIGHT / 2 + 2, winFont);
        drawFrame("Better luck next time.", WIDTH / 2, HEIGHT / 2 - 3, winFont);
    }

    /**
     * if :Q pressed, save current state of world in a file
     */
    public void saveWorld() {
        //what needs to be saved?
        //can access all info from seed, series of key presses
        //or instead, seed, and location of the avatar when finished
        //write to a file in a local directory, call it the seed number
        //inside store avatar position with a comma in btw??
        //TO DO: also state of randomNumberGenerator

        String seed = Long.toString(test.getSEED());
        Position avatarPosition = avatar.getPosition();
        String xPosition = String.valueOf(avatarPosition.getX());
        String yPosition = String.valueOf(avatarPosition.getY());
        //create File
        writeFile(seed, xPosition + "," + yPosition);
        //also write seed to lastWorld
        writeFile("lastWorld", seed);
        String replayString = replayStringBuilder.toString();
        writeFile("replayWorld", replayString);
    }

    /**
     * Displays start menu if typing from a keyboard
     */
    public void displayMenu() {
        StdDraw.clear(Color.BLACK);
        //StdDraw.enableDoubleBuffering();
        drawFrame("CS61B: THE GAME", WIDTH / 2, 3 * HEIGHT / 4, titleFont);
        drawFrame("New Game (N)", WIDTH / 2, HEIGHT / 2 + 1, smallFont);
        drawFrame("Load Game (L)", WIDTH / 2, HEIGHT / 2, smallFont);
        drawFrame("Replay Game (R)", WIDTH / 2, HEIGHT / 2 - 1, smallFont);
        drawFrame("Randomize Theme (T)", WIDTH / 2, HEIGHT / 2 - 2, smallFont);
        drawFrame("Change Avatar Appearance (C)", WIDTH / 2, HEIGHT / 2 - 3, smallFont);
        drawFrame("Quit (Q)", WIDTH / 2, HEIGHT / 2 - 4, smallFont);
        drawFrame("Lore (?)", WIDTH / 2, HEIGHT / 2 - 5, smallFont);
    }


    public void drawFrame(String s, int x, int y, Font f) {
        //StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(f);
        StdDraw.text(x, y, s);
        StdDraw.show();

    }
}
