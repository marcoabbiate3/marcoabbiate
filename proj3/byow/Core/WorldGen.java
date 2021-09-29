package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class WorldGen {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 30;

    private long SEED;
    private Random RANDOM = new Random(456);
    private List<Position> occupiedTiles = new LinkedList<>();
    private List<Position> occupiedFloor = new LinkedList<>();
    private List<Position> occupiedWall = new LinkedList<>();
    private List<Rectangle> rooms = new LinkedList<>();
    private List<Position> lights = new LinkedList<>();
    private TETile floor = Tileset.FLOOR;
    private TETile wall = Tileset.WALL;
    private Position doorPosition;

    public long getSEED() {
        return this.SEED;
    }

    public Random getRANDOM() {
        return this.RANDOM;
    }

    public List<Position> getOccupiedTiles() {
        return this.occupiedTiles;
    }

    public List<Position> getOccupiedWall() {
        return this.occupiedWall;
    }

    public List<Position> getOccupiedFloor() {
        return this.occupiedFloor;
    }

    public void randomTheme(Avatar avatar) {
        floor = randomTile();
        wall = randomTile();
        avatar.setRandomTheme(floor, wall);
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private TETile randomTile() {
        int tileNum = uniform(RANDOM, 7);
        if (tileNum == 0) {
            return Tileset.GRASS;
        }
        if (tileNum == 1) {
            return Tileset.WATER;
        }
        if (tileNum == 2) {
            return Tileset.SAND;
        }
        if (tileNum == 3) {
            return Tileset.MOUNTAIN;
        }
        if (tileNum == 4) {
            return Tileset.SNOWFLAKE;
        }
        if (tileNum == 5) {
            return Tileset.TREE;
        }
        if (tileNum == 6) {
            return Tileset.FLOWER;
        }
        return Tileset.SAND;
    }

    public void placeRooms(TETile[][] world) {
        int numOfRooms = uniform(RANDOM, 10, 20);
        for (int i = 0; i < numOfRooms; i++) {
            int height = uniform(RANDOM, 4, 8);
            int width = uniform(RANDOM, 4, 8);
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 2) + 1;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 2) + 1;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            if (!checkOverlap(rect, world)) {
                addRectangle(rect, world, floor, wall);
                rooms.add(rect);
            }
        }
    }

    public boolean checkOverlap(Rectangle rectangle, TETile[][] world) {
        List<Position> current = new LinkedList<>();
        current.addAll(rectangle.getPositions());
        current.addAll(rectangle.getWallPositions());
        for (Position p : current) {
            if (occupiedTiles.contains(p)) {
                return true;
            }
        }
        return false;
    }

    public void addRectangle(Rectangle rectangle, TETile[][] world, TETile f, TETile w) {
        List<Position> floorPositions = rectangle.getPositions();
        occupiedTiles.addAll(floorPositions);
        for (Position p: floorPositions) {
            world[p.getX()][p.getY()] = f;
            occupiedFloor.add(p);
        }
        List<Position> wallPositions = rectangle.getWallPositions();
        occupiedTiles.addAll(wallPositions);
        for (Position p: wallPositions) {
            world[p.getX()][p.getY()] = w;
            occupiedWall.add(p);
        }
    }

    public void addHallway(Rectangle rectangle, TETile[][] world, TETile f, TETile w) {
        List<Position> floorPositions = rectangle.getPositions();
        for (Position p : floorPositions) {
            if (world[p.getX()][p.getY()] == Tileset.NOTHING) {
                occupiedTiles.add(p);
                occupiedFloor.add(p);
                world[p.getX()][p.getY()] = f;
            } else if (world[p.getX()][p.getY()] == w) {
                occupiedTiles.add(p);
                occupiedFloor.add(p);
                world[p.getX()][p.getY()] = f;
                occupiedWall.remove(p);
            }
        }

        List<Position> wallPositions = rectangle.getWallPositions();
        for (Position p : wallPositions) {
            if (world[p.getX()][p.getY()] == Tileset.NOTHING) {
                occupiedTiles.add(p);
                occupiedWall.add(p);
                world[p.getX()][p.getY()] = w;
            }
        }
    }

    public void connectRooms(Rectangle rectangle, Rectangle other,
                             TETile[][] world, TERenderer ter) {
        // get coordinates
        List<Position> rectanglePositions = rectangle.getPositions();
        List<Position> otherPositions = other.getPositions();
        int randomNumber = uniform(RANDOM, rectanglePositions.size());
        int otherNumber = uniform(RANDOM, otherPositions.size());
        Position hallwayStart = rectanglePositions.get(randomNumber);
        Position hallwayEnd = otherPositions.get(otherNumber);

        //end is left
        if (hallwayEnd.getX() - hallwayStart.getX() < 0) {
            int width = hallwayStart.getX() - hallwayEnd.getX() + 1;
            Rectangle hallway = new Rectangle(new Position(hallwayEnd.getX(),
                    hallwayStart.getY()), width, 1);
            addHallway(hallway, world, floor, wall);
        }
        //end is right
        if (hallwayEnd.getX() - hallwayStart.getX() > 0) {
            int width = hallwayEnd.getX() - hallwayStart.getX() + 1;
            Rectangle hallway = new Rectangle(new Position(hallwayStart.getX(),
                    hallwayStart.getY()), width, 1);
            addHallway(hallway, world, floor, wall);
        }

        int X = hallwayEnd.getX();
        //get x Position to start
        //end is below
        if (hallwayEnd.getY() - hallwayStart.getY() < 0) {
            int height = hallwayStart.getY() - hallwayEnd.getY() + 1;
            Rectangle hallway = new Rectangle(new Position(X, hallwayEnd.getY()), 1, height);
            addHallway(hallway, world, floor, wall);
        }
        //end is above
        if (hallwayEnd.getY() - hallwayStart.getY() > 0) {
            int height = hallwayEnd.getY() - hallwayStart.getY() + 1;
            Rectangle hallway = new Rectangle(new Position(X, hallwayStart.getY()), 1, height);
            addHallway(hallway, world, floor, wall);
        }
    }

    public void chooseRooms(TETile[][] world, TERenderer ter) {
        List<Rectangle> currentRooms = new LinkedList<>(rooms);

        while (currentRooms.size() > 1) {
            Rectangle first = currentRooms.remove(0);
            Rectangle second = currentRooms.get(0);
            connectRooms(first, second, world, ter);
        }
    }

    public static void fillWithNothingTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] generateRandomWorld(TETile[][] world, TERenderer ter, Long seed) {
        fillWithNothingTiles(world);
        SEED = seed;
        RANDOM = new Random(SEED);
        placeRooms(world);
        chooseRooms(world, ter);
        placeLockedDoor(world);
        world = placeLights(world);
        return world;
    }

    public TETile[][] placeLockedDoor(TETile[][] world) {
        int randomNumber = uniform(RANDOM, occupiedWall.size());
        Position p = occupiedWall.get(randomNumber);
        world[p.getX()][p.getY()] = Tileset.LOCKED_DOOR;
        doorPosition = p;
        return world;
    }

    public TETile[][] placeLights(TETile[][] world) {
        for (Rectangle room : rooms) {
            List<Position> positions = room.getPositions();
            int number = uniform(RANDOM, positions.size());
            Position light = positions.remove(number);
            world[light.getX()][light.getY()] = Tileset.LIGHT;
            room.setLight(light);
        }
        turnOnLights(world);
        return world;
    }

    public double distance(Position light, Position p) {
        return Math.sqrt(Math.pow(light.getX() - p.getX(), 2)
                + Math.pow(light.getY() - p.getY(), 2));
    }

    public TETile[][] turnOnLights(TETile[][] world) {
        for (Rectangle room : rooms) {
            Position light = room.getLight();
            for (Position p : room.getPositions()) {
                double distance = distance(light, p);
                float ratio = (float) (1 / distance);
                int r = Math.round(Color.BLUE.getRed() * ratio);
                int g = Math.round(Color.BLUE.getGreen() * ratio);
                int b = Math.round(Color.BLUE.getBlue() * ratio);
                Color c = new Color(r, g, b);
                world[p.getX()][p.getY()] =  new TETile(floor.character(), floor.getTextColor(), c,
                        floor.description());
            }
        }
        return world;
    }

    public TETile[][] turnOffLights(TETile[][] world) {
        for (Rectangle room : rooms) {
            for (Position p : room.getPositions()) {
                world[p.getX()][p.getY()] = floor;
            }
        }
        return world;
    }

    public TETile[][] moveDoor(TETile[][] world) {
        world[doorPosition.getX()][doorPosition.getY()] = wall;
        world = placeLockedDoor(world);
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(50, 30);

        /*TETile[][] world =  new TETile[WIDTH][HEIGHT];
        fillWithNothingTiles(world);
        WorldGen test = new WorldGen();
        test.placeRooms(world);
        test.chooseRooms(world, ter);*/

        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString("n2912s");
        ter.renderFrame(world);


    }
}
