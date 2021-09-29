package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.*;

import static byow.Core.RandomUtils.uniform;

public class TestWorldGeneration {
    private final int WIDTH = 50;
    private final int HEIGHT = 30;

    private long SEED = 283;
    private Random RANDOM;

    private List<Position> occupiedTiles = new LinkedList<>();

    public boolean checkOverlap2(Rectangle r, TETile[][] world) {
        List<Position> positions = new LinkedList<>();
        List<Position> pos = r.getPositions();
        List<Position> wallPos = r.getWallPositions();
        positions.addAll(pos);
        positions.addAll(wallPos);

        for (Position p : positions) {
            if (occupiedTiles.contains(p)) {
                System.out.println("oops");
                return true;
            }
        }
        return false;
    }

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithNothingTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public boolean check(Rectangle r, TETile[][] world) {
        Position p = r.getBottomLeftCorner();
        //bottom left is valid, need to check upper left, upper right, lower right
        int yTop = p.getY() + r.height;
        int xRight = p.getX() + r.width;
        return (yTop < HEIGHT && xRight < WIDTH && p.getY() > 0 && p.getX() > 0);

    }

    public boolean checkOverlap(Rectangle currentRectangle, Rectangle r, TETile[][] world) {
        // before tried adding all four corners and checking overlap there
        // problematic because bigger rooms overlap not in the corners!
        List<Position> positions = new LinkedList<>();
        List<Position> pos = r.getPositions();
        List<Position> wallPos = r.getWallPositions();
        positions.addAll(pos);
        positions.addAll(wallPos);
        // need to allow for overlap of current neighbor??
        List<Position> currentPositions = new LinkedList<>();
        List<Position> currentPos = currentRectangle.getPositions();
        List<Position> currentWallPos = currentRectangle.getWallPositions();
        currentPositions.addAll(currentPos);
        currentPositions.addAll(currentWallPos);
        List<Position> occupiedPositions = occupiedTiles;
        occupiedPositions.removeAll(currentPositions);
        for (Position p : positions) {
            if (occupiedPositions.contains(p)) {
                return true;
            }
            /*if (world[p.getX()][p.getY()] != Tileset.NOTHING)
                return true;*/
        }
        return false;
    }

    public void addTwoRectangles(TETile[][] world) {
        Position position = new Position(10, 10);
        Rectangle rectangle = new Rectangle(position, 5, 6);
        addRectangle(rectangle, world, Tileset.WATER, Tileset.GRASS);
        Position position2 = new Position(20, 20);
        Rectangle rectangle2 = new Rectangle(position2, 5, 6);
        addRectangle(rectangle2, world, Tileset.WATER, Tileset.GRASS);
    }

    public void generateRandomWorld(TETile[][] world, TERenderer ter, Long seed) {
        SEED = seed;
        RANDOM = new Random(SEED);
        int numOfRectangles = 5; //uniform(RANDOM, 9, 11);
        //make random first rectangle on the map
        int height = uniform(RANDOM, 1, 6);
        int width = uniform(RANDOM, 1, 6);
        int bottomLeftX = uniform(RANDOM, WIDTH / 2 - 8, WIDTH / 2);
        int bottomLeftY = uniform(RANDOM, HEIGHT / 2 -  5, HEIGHT / 2);
        Position bottomLeftPosition = new Position(bottomLeftX, bottomLeftY);
        Rectangle rect = new Rectangle(bottomLeftPosition, width, height);
        addRectangle2(rect, world, Tileset.WATER, Tileset.WALL);
        //ter.renderFrame(world);
        recursive2(rect, world, numOfRectangles, ter);
    }


    public void recursive2(Rectangle r, TETile[][] world, int numOfRectangles, TERenderer ter) {
        numOfRectangles -= 1;
        if (numOfRectangles == 0) {
            return;
        }

        //randomly gen # of neighbors
        int numberOfNeighbors = uniform(RANDOM, 2, 4); //uniform(RANDOM, 1, 3);
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
            boolean neighborCheck = checkNeighbor(r, neighbor, world);
            if (neighborCheck) {
                addRectangle2(neighbor, world, Tileset.WATER, Tileset.WALL);
                recursive2(neighbor, world, numOfRectangles, ter);
                ter.renderFrame(world);
            }
        }
    }


    public Rectangle getNeighbor(Rectangle r, int side) {
        Rectangle neighbor = null;
        //check orientation
        if (side == 0) {
            neighbor = generateTopNeighbor(r, side);
        } else if (side == 1) {
            neighbor = generateBottomNeighbor(r, side);
        } else if (side == 2) {
            neighbor = generateLeftNeighbor(r, side);
        } else if (side == 3) {
            neighbor = generateRightNeighbor(r, side);
        }
        return neighbor;
    }

    public Rectangle generateLeftNeighbor(Rectangle rectangle, int side) {
        Position topRightCorner = getNeighborPosition(rectangle, side);
        int height;
        int width;
        if (rectangle.getType().equals("Room")) {
            height = uniform(RANDOM, 1, 2);
            width = uniform(RANDOM, 7, 10);
        } else {
            height = uniform(RANDOM, 1, 7);
            width = uniform(RANDOM, 4, 7);
        }
        Position bottomLeftCorner =
                Rectangle.getBottomLeftCornerFromTopRightCorner(topRightCorner, width, height);
        return rectangle.neighbor(bottomLeftCorner, width, height);
    }

    public Rectangle generateRightNeighbor(Rectangle rectangle, int side) {
        Position bottomLeftCorner = getNeighborPosition(rectangle, side);
        int height;
        int width;
        if (rectangle.getType().equals("Room")) {
            height = uniform(RANDOM, 1, 2);
            width = uniform(RANDOM, 7, 10);
        } else {
            height = uniform(RANDOM, 1, 7);
            width = uniform(RANDOM, 4, 7);
        }
        return rectangle.neighbor(bottomLeftCorner, width, height);
    }


    public Rectangle generateTopNeighbor(Rectangle rectangle, int side) {
        Position bottomLeftCorner = getNeighborPosition(rectangle, side);
        int width;
        int height;
        if (rectangle.getType().equals("Room")) {
            width = uniform(RANDOM, 1, 2);
            height = uniform(RANDOM, 7, 10);
        } else {
            width = uniform(RANDOM, 1, 7);
            height = uniform(RANDOM, 4, 7);
        }
        return rectangle.neighbor(bottomLeftCorner, width, height);
    }

    public Rectangle generateBottomNeighbor(Rectangle rectangle, int side) {
        Position topRightCorner = getNeighborPosition(rectangle, side);
        int width;
        int height;
        if (rectangle.getType().equals("Room")) {
            width = uniform(RANDOM, 1, 2);
            height = uniform(RANDOM, 7, 10);
        } else {
            width = uniform(RANDOM, 1, 7);
            height = uniform(RANDOM, 4, 7);
        }
        Position bottomLeftCorner =
                Rectangle.getBottomLeftCornerFromTopRightCorner(topRightCorner, width, height);
        return rectangle.neighbor(bottomLeftCorner, width, height);
    }


    public Position getNeighborPosition(Rectangle rect, int side) {
        List<Position> neighborPositions = rect.getSidePositions(side);
        //randomly select from the set
        int randomNumber = uniform(RANDOM, neighborPositions.size());
        return neighborPositions.get(randomNumber);
    }

    /**
     * Checks to see if neighbor will go off the map
     */
    public boolean checkNeighbor(Rectangle current, Rectangle neighbor, TETile[][] world) {
        // if adding neighbors below or above, need to check other corners,
        // also check if you would write over existing occupied tiles
        Position p = neighbor.getBottomLeftCorner();
        //bottom left is valid, need to check upper left, upper right, lower right
        int yTop = p.getY() + neighbor.height;
        int xRight = p.getX() + neighbor.width;
        if (checkOverlap(current, neighbor, world)) {
            return false;
        }
        return (yTop < HEIGHT && xRight < WIDTH && p.getY() > 0 && p.getX() > 0);

    }


    public void addRectangle(Rectangle rectangle, TETile[][] world, TETile tile, TETile wall) {
        // don't think we need occupied tiles, can just see if tile at given
        // position is NOTHING
        List<Position> positions = rectangle.getPositions();
        for (Position p : positions) {
            world[p.getX()][p.getY()] = tile;
            occupiedTiles.add(p);
        }
        List<Position> wallPositions = rectangle.getWallPositions();
        for (Position w : wallPositions) {
            world[w.getX()][w.getY()] = wall;
            occupiedTiles.add(w);
        }
    }

    public void addRectangle2(Rectangle rectangle, TETile[][] world, TETile tile, TETile wall) {
        List<Position> positions = rectangle.getPositions();
        for (Position p : positions) {
            occupiedTiles.add(p);
            if (world[p.getX()][p.getY()] == wall || world[p.getX()][p.getY()] == Tileset.NOTHING) {
                world[p.getX()][p.getY()] = tile;
            }
        }
        List<Position> wallPositions = rectangle.getWallPositions();
        for (Position w : wallPositions) {
            occupiedTiles.add(w);
            if (world[w.getX()][w.getY()] == Tileset.NOTHING) {
                world[w.getX()][w.getY()] = wall;
            }
        }
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(50, 30);

        Engine engine = new Engine();
        TETile[][] world = engine.interactWithInputString("n2912s");
        //like the seed 2912
        TestWorldGeneration test = new TestWorldGeneration();
        test.generateRandomWorld(world, ter, Long.parseLong("14512"));

        ter.renderFrame(world);
    }
}
