/*
package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

import static byow.Core.RandomUtils.uniform;

public class TestWorld2 {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static Set<Position> occupiedTiles = new HashSet<>();

    public static void generateWorld(TETile[][] world) {
        int numOfRectangles = 500; //uniform(RANDOM, 9, 11);
        //make random first rectangle on the map
        int height = uniform(RANDOM, 1, 6);
        int width = uniform(RANDOM, 1, 6);
        int bottomLeftX = uniform(RANDOM, 0, 5);
        int bottomLeftY = uniform(RANDOM, 0, 5);
        Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
        Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
        addRectangle(rect, world, Tileset.WATER, Tileset.WALL);
        recursive(rect, world, numOfRectangles);
    }

    public static void recursive(Rectangle r, TETile[][] world, int numOfRectangles) {
        numOfRectangles -= 1;
        if (numOfRectangles == 0) {
            return;
        }

        //randomly gen # of neighbors
        int numberOfNeighbors = 4;
        //randomly get sides of neighbors
        //allow for 3 neighbors
        List<Integer> nums = new LinkedList<>();
        for (int k = 0; k < 4; k++) {
            nums.add(k);
        }
        for (int i = 0; i < numberOfNeighbors; i++) {
            int randomNeighborNumber = uniform(RANDOM, nums.size());
            int neighborNumber = nums.remove(randomNeighborNumber);

            Rectangle neighbor = getNeighbor(r, neighborNumber);
            //check if adding each neighbor would run off the board
            boolean neighborCheck = check(r, neighbor, world);
            if (neighborCheck) {
                addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);
                recursive(neighbor, world, numOfRectangles);

            }
        }
    }

    public static boolean check(Rectangle rectangle, Rectangle neighbor, TETile[][] world) {
        Position p = rectangle.bottomLeftCorner;
        int YTop = p.getY() + rectangle.height;
        int XRight = p.getX() + rectangle.width;
        return (YTop < HEIGHT && XRight < WIDTH && p.getY() > 0 && p.getX() > 0);

    }

    public static Rectangle getNeighbor(Rectangle rectangle, int side) {
        Rectangle neighbor = null;
        //check orientation
        */
/*if (side == 0) {
            neighbor = Neighbor(rectangle, side);
        } else if (side == 1) {
            neighbor = generateBottomNeighbor(r, side);
        } else if (side == 2) {
            neighbor = generateLeftNeighbor(r, side);
        } else if (side == 3) {
            neighbor = generateRightNeighbor(r, side);
        }*//*

        return neighbor;
    }

    public static void addRectangle(Rectangle rectangle, TETile[][] world,
    TETile tile, TETile wall) {
        Set<Position> positions = rectangle.getPositions();
        for (Position p : positions) {
            if (world[p.getX()][p.getY()] == wall || world[p.getX()][p.getY()] == Tileset.NOTHING) {
                occupiedTiles.add(p);
                world[p.getX()][p.getY()] = tile;
            }
        }
        Set<Position> wallPositions = rectangle.getWallPositions();
        for (Position w : wallPositions) {
            if (world[w.getX()][w.getY()] == Tileset.NOTHING) {
                occupiedTiles.add(w);
                world[w.getX()][w.getY()] = wall;
            }
        }
    }

    /*
    public void add2rectangles(TETile[][] world) {
        Position position = new Position(10, 10);
        Rectangle rectangle = new Rectangle(position, 5, 6);
        addRectangle2(rectangle, world, Tileset.WATER, Tileset.GRASS);
        Position position2 = new Position(11, 11);
        Rectangle rectangle2 = new Rectangle(position2, 5, 6);
        addRectangle2(rectangle2, world, Tileset.WATER, Tileset.GRASS);
        Position position3 = new Position(12, 12);
        Rectangle rectangle3 = new Rectangle(position3, 5, 6);
        addRectangle2(rectangle3, world, Tileset.WATER, Tileset.GRASS);
        addRectangle2(rectangle3, world, Tileset.WATER, Tileset.GRASS);
    }
*/

/** Picks a RANDOM tile with a 33% change of being
 *  a wall, 33% chance of being a flower, and 33%
 *  chance of being empty space.
 */
/*    private TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }*/

/*
    public void testRightNeighbor(TETile[][] world) {
        //create random rectangle, try to add right neighbor
        Position p = new Position(10, 10);
        Rectangle r = new Rectangle(p, 4, 5);
        addRectangle(r, world, Tileset.WATER, Tileset.WALL);
        Rectangle r2 = getNeighbor(r, 3);
        addRectangle(r2, world, Tileset.WATER, Tileset.WALL);
        */
/*Rectangle neighbor = r.rightNeighbor(new Position(20 , 20), 2, 3);
        addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);*//*

    }
*/

/*    public static void testLeftNeighbor(TETile[][] world) {
        //create random rectangle, try to add right neighbor
        Position p = new Position(10, 10);
        Rectangle r = new Rectangle(p, 4, 5);
        addRectangle(r, world, Tileset.WATER, Tileset.WALL);
        Rectangle r2 = getNeighbor(r, 2);
        addRectangle(r2, world, Tileset.WATER, Tileset.WALL);
        *//*Rectangle neighbor = r.leftNeighbor(new Position(20 , 20), 2, 3);
        addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);*//*
    }

    public static void testTopNeighbor(TETile[][] world) {
        //create random rectangle, try to add right neighbor
        Position p = new Position(10, 10);
        Rectangle r = new Rectangle(p, 4, 5);
        addRectangle(r, world, Tileset.WATER, Tileset.WALL);
        Rectangle r2 = getNeighbor(r, 0);
        System.out.println(r2.bottomLeftCorner.getX() + " " + r2.bottomLeftCorner.getY());
        System.out.println(r2.width + " " + r2.height);
        addRectangle(r2, world, Tileset.WATER, Tileset.WALL);
        *//*Rectangle neighbor = r.topNeighbor(new Position(20 , 20), 2, 3);
        addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);*//*
    }

    public static void testBottomNeighbor(TETile[][] world) {
        //create random rectangle, try to add right neighbor
        Position p = new Position(10, 10);
        Rectangle r = new Rectangle(p, 4, 5);
        addRectangle(r, world, Tileset.WATER, Tileset.WALL);
        Rectangle r2 = getNeighbor(r, 1);
        addRectangle(r2, world, Tileset.WATER, Tileset.WALL);
        *//*Rectangle neighbor = r.bottomNeighbor(new Position(20 , 20), 2, 3);
        addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);*//*
    }

        /*public static void recursive(Rectangle r, TETile[][] world, int numOfRectangles) {
        numOfRectangles -= 1;
        if (numOfRectangles == 0) {
            return;
        }

        //randomly gen # of neighbors
        int numberOfNeighbors = 2; //uniform(RANDOM, 2, 4); // uniform(RANDOM, 1, 4);
        //randomly get sides of neighbors
        //allow for 3 neighbors
        List<Integer> nums = new LinkedList<>();
        for (int k = 0; k < 4; k++) {
            nums.add(k);
        }
        //TODO: changes based on how it shuffles, not good, needs to be pseudorandom
        // but deterministic
        for (int i = 0; i < numberOfNeighbors; i++) {
            int randomNeighborNumber = uniform(RANDOM, nums.size());
            int neighborNumber = nums.remove(randomNeighborNumber);

            Rectangle neighbor = getNeighbor(r, neighborNumber);
            //check if adding each neighbor would run off the board
            boolean neighborCheck = checkNeighbor(r, neighbor, world);
            if (neighborCheck) {
                addRectangle(neighbor, world, Tileset.WATER, Tileset.WALL);
                recursive(neighbor, world, numOfRectangles);
            }
        }
    }

    /*    public static void testAbove(TETile[][] world) {
        Position bLP = new Position(5, 5);
        Position bLP2 = new Position(8, 9);
        Rectangle r1 = new Rectangle(bLP, 4, 3);
        Rectangle r2 = new Rectangle(bLP2, 3, 3);
        addRectangle(r1, world, Tileset.WATER, Tileset.WALL);
        System.out.println(r1.getPositions());
        System.out.println(r1.getWallPositions());
        System.out.println(occupiedTiles);
        //System.out.println(checkOverlap(r2, world));
        addRectangle(r2, world, Tileset.WATER, Tileset.WALL);
    }

    /*    public void fillWithRandomRectanglesThenWalls(TETile[][] world) {
        int numRectangles = uniform(RANDOM, 20, 40);
        for (int i = 0; i < numRectangles; i++) {
            int height = uniform(RANDOM, 1, 6);
            int width = uniform(RANDOM, 4, 6);
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 3) + 2;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 3) + 2;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            TETile tile = Tileset.WATER;
            TETile wall = Tileset.WATER;
            addRectangle(rect, world, tile, wall);
        }
        addWalls(world);
    }*/

/*    public static void addWalls(TETile[][] world) {
        Set<Position> wallPositions = new HashSet<>();
        for (Position p : occupiedTiles) {
            Position left = new Position(p.getX() - 1, p.getY());
            Position right = new Position(p.getX() + 1, p.getY());
            Position up = new Position(p.getX(), p.getY() + 1);
            Position down = new Position(p.getX(), p.getY() - 1);
            Position upLeft = new Position(p.getX() - 1, p.getY() + 1);
            Position downLeft = new Position(p.getX() - 1, p.getY() - 1);
            Position upRight = new Position(p.getX() + 1, p.getY() + 1);
            Position downRight = new Position(p.getX() + 1, p.getY() - 1);
            wallPositions.add(left);
            wallPositions.add(right);
            wallPositions.add(up);
            wallPositions.add(down);
            wallPositions.add(upLeft);
            wallPositions.add(downLeft);
            wallPositions.add(upRight);
            wallPositions.add(downRight);
            for (Position w : wallPositions) {
                if (world[w.getX()][w.getY()] == Tileset.NOTHING) {
                    world[w.getX()][w.getY()] = Tileset.WALL;
                }
            }
        }
    }*/

/*
    public void fillWithRandomRectangles(TETile[][] world) {
        int numRectangles = uniform(RANDOM, 20, 40);
        for (int i = 0; i < numRectangles; i++) {
            int height = uniform(RANDOM, 1, 6);
            int width = uniform(RANDOM, 4, 6);
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 2) + 1;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 2) + 1;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            TETile tile = Tileset.WATER;
            TETile wall = Tileset.WALL;
            addRectangle(rect, world, tile, wall);
        }
    }
*/

/*    public void fillWithRandomRectanglesNoOverlap(TETile[][] world, TERenderer ter) {
        int numRooms = 80;//uniform(RANDOM, 20, 40);
        for (int i = 0; i < numRooms; i++) {
            int height = uniform(RANDOM, 3, 8);
            int width = uniform(RANDOM, 4, 8);
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 2) + 1;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 2) + 1;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            TETile tile = Tileset.WATER;
            TETile wall = Tileset.WALL;
            if (!checkOverlap2(rect, world)) {
                addRectangle(rect, world, tile, wall);
            }
        }
        int numHallways = 70; //uniform(RANDOM, 20, 40);
        for (int i = 0; i < numHallways; i++) {
            int width;
            int height;
            int hallwayNumber = uniform(RANDOM, 1, 3);
            if (hallwayNumber == 1) {
                height = uniform(RANDOM, 1, 3);
                width = uniform(RANDOM, 8, 12);
            } else {
                height = uniform(RANDOM, 8, 12);
                width = uniform(RANDOM, 1, 3);
            }
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 2) + 1;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 2) + 1;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            TETile tile = Tileset.WATER;
            TETile wall = Tileset.WALL;
            if (check(rect, world) && checkOverlap2(rect, world)) {
                addRectangle2(rect, world, tile, wall);
            }
        }
    }*/

/*    public void placeRooms(TETile[][] world) {
        int numOfRooms = uniform(RANDOM, 10, 20);
        for (int i = 0; i < numOfRooms; i++) {
            int height = uniform(RANDOM, 3, 7);
            int width = uniform(RANDOM, 3, 7);
            int bottomLeftX = RANDOM.nextInt(WIDTH - width - 2) + 1;
            int bottomLeftY = RANDOM.nextInt(HEIGHT - height - 2) + 1;
            Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
            Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
            TETile tile = Tileset.WATER;
            TETile wall = Tileset.WALL;
            if (!checkOverlap2(rect, world)) {
                //addRectangle2(rect, world, tile, wall);
            }
        }
    }

}*/
