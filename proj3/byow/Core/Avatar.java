package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static byow.Core.RandomUtils.uniform;

public class Avatar {

    private Position position;
    private TETile current; //tile that avatar is on
    private Color color;
    private TETile avatarType = Tileset.AVATAR;
    private TETile floor = Tileset.FLOOR;
    private TETile wall = Tileset.WALL;
    private TETile lockedDoor = Tileset.LOCKED_DOOR;
    private int count = 0;
    private boolean notWon = true;
    private int lose = 0;


    public int getLose() {
        return lose;
    }

    public boolean getWon() {
        return notWon;
    }

    public Position getPosition() {
        return position;
    }

    public Avatar() {
    }

    public Avatar(Color color) {
        avatarType = new TETile(Tileset.AVATAR, color);
    }

    public void setRandomTheme(TETile f, TETile w) {
        floor = f;
        wall = w;
    }

    /**
     * Places avatar at random spot on the world
     * @param world
     * @param worldGen
     * @return
     */
    public TETile[][] initiateAvatar(TETile[][] world, WorldGen worldGen) {
        List<Position> possiblePositions = worldGen.getOccupiedFloor();
        Random r = worldGen.getRANDOM();
        int randomNumber = uniform(r, possiblePositions.size());
        Position p = possiblePositions.get(randomNumber);
        this.position = p;
        current = world[p.getX()][p.getY()];
        world[p.getX()][p.getY()] = avatarType;
        return world;
    }

    /**
     * Loads avatar to given position
     */
    public TETile[][] loadAvatar(TETile[][] world, Position p) {
        if (world[p.getX()][p.getY()] != avatarType) {
            current = world[p.getX()][p.getY()];
        }
        world[p.getX()][p.getY()] = avatarType;
        position = p;
        return world;
    }


    /**
     * Moves avatar on the world if possible
     * @param world
     * @param c
     * @return
     */
    public TETile[][] moveAvatar(TETile[][] world, Character c, WorldGen worldGen) {
        c = c.toString().toUpperCase().toCharArray()[0];
        // add checks to see if finished game
        if (c.equals('W')) { //specifying character?
            if (world[position.getX()][position.getY() + 1].
                    description().equals(floor.description())
                    || world[position.getX()][position.getY() + 1].
                    description().equals("*light*")) {
                world[position.getX()][position.getY()] = current;
                position = new Position(position.getX(), position.getY() + 1);
                current = world[position.getX()][position.getY()];
                world[position.getX()][position.getY()] = avatarType;
            } else if (world[position.getX()][position.getY() + 1].
                    description().equals(lockedDoor.description())) {
                checkLockedDoor(world, worldGen, position.getX(), position.getY() + 1);
            } else {
                world = darkenWorld(world);
                lose += 1;
            }
        } else if (c.equals('A')) {
            if (world[position.getX() - 1][position.getY()].
                    description().equals(floor.description())
                    || world[position.getX() - 1][position.getY()].
                    description().equals("*light*")) {
                world[position.getX()][position.getY()] = current;
                position = new Position(position.getX() - 1, position.getY());
                current = world[position.getX()][position.getY()];
                world[position.getX()][position.getY()] = avatarType;
            } else if (world[position.getX() - 1][position.getY()].
                    description().equals(lockedDoor.description())) {
                checkLockedDoor(world, worldGen, position.getX() - 1, position.getY());
            } else {
                world = darkenWorld(world);
                lose += 1;
            }
        } else if (c.equals('S')) {
            if (world[position.getX()][position.getY() - 1].
                    description().equals(floor.description())
                    || world[position.getX()][position.getY() - 1].
                    description().equals("*light*")) {
                world[position.getX()][position.getY()] = current;
                position = new Position(position.getX(), position.getY() - 1);
                current = world[position.getX()][position.getY()];
                world[position.getX()][position.getY()] = avatarType;
            } else if (world[position.getX()][position.getY() - 1].
                    description().equals(lockedDoor.description())) {
                checkLockedDoor(world, worldGen, position.getX(), position.getY() - 1);
            } else {
                world = darkenWorld(world);
                lose += 1;
            }
        } else if (c.equals('D')) {
            if (world[position.getX() + 1][position.getY()]
                    .description().equals(floor.description())
                    || world[position.getX() + 1][position.getY()].
                    description().equals("*light*")) {
                world[position.getX()][position.getY()] = current;
                position = new Position(position.getX() + 1, position.getY());
                current = world[position.getX()][position.getY()];
                world[position.getX()][position.getY()] = avatarType;
            } else if (world[position.getX() + 1][position.getY()].
                    description().equals(lockedDoor.description())) {
                checkLockedDoor(world, worldGen, position.getX() + 1, position.getY());
            } else {
                world = darkenWorld(world);
                lose += 1;
            }
        }
        return world;
    }

    public TETile[][] checkLockedDoor(TETile[][] world, WorldGen worldGen, int x, int y) {
        if (count < 3) {
            world = worldGen.moveDoor(world);
            count += 1;
        } else {
            world[x][y] = Tileset.UNLOCKED_DOOR;
            notWon = false;
        }
        return world;
    }

    public TETile[][] darkenWorld(TETile[][] world) {
        int height = world[0].length;
        int width = world.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                TETile tile = world[x][y];
                if (tile == avatarType) {
                    continue;
                }
                char c = tile.character();
                Color textColor = tile.getTextColor();
                Color backgroundColor = tile.getBackgroundColor();
                String description = tile.description();
                float ratio = (float) 7 / 9;
                int r = Math.round(backgroundColor.getRed() * ratio);
                int g = Math.round(backgroundColor.getGreen() * ratio);
                int b = Math.round(backgroundColor.getBlue() * ratio);

                Color col = new Color(r, g, b);
                int rText = Math.round(textColor.getRed() * ratio);
                int gText = Math.round(textColor.getGreen() * ratio);
                int bText = Math.round(textColor.getBlue() * ratio);
                Color textCol = new Color(rText, gText, bText);

                world[x][y] =  new TETile(c, textCol, col, description);
            }
        }
        return world;
    }

}
