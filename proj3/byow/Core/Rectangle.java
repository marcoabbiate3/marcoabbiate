package byow.Core;
import java.util.LinkedList;
import java.util.List;

public class Rectangle {

    int width;
    int height;
    private Position bottomLeftCorner;
    Position topRightCorner;
    private List<Position> positions = new LinkedList<>();
    private List<Position> wallPositions = new LinkedList<>();
    private String type;
    private Position light;

    public Rectangle(Position bottomLeftCorner, int width, int height) {
        this.bottomLeftCorner = bottomLeftCorner;
        this.topRightCorner = new Position(bottomLeftCorner.getX() + width - 1,
                bottomLeftCorner.getY() + height - 1);
        this.width = width;
        this.height = height;
        if (width < 2 || height < 2) {
            type = "hallway";
        } else {
            type = "Room";
        }
        setPositions();
    }

    public void setLight(Position light) {
        this.light = light;
    }

    public Position getBottomLeftCorner() {
        return bottomLeftCorner;
    }

    public String getType() {
        return this.type;
    }

    public Position getLight() {
        return this.light;
    }

    /**
     * Fill in positions of rectangle and the walls of the rectangle
     */
    private void setPositions() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                int X = bottomLeftCorner.getX() + x;
                int Y = bottomLeftCorner.getY() + y;
                Position pos = new Position(X, Y);
                positions.add(pos);
            }
        }
        // add top and bottom walls
        this.wallPositions = new LinkedList<>();
        Position bottomWall = new Position(bottomLeftCorner.getX() - 1,
                bottomLeftCorner.getY() - 1);
        int yTop = bottomWall.getY() + height + 1;
        for (int x = 0; x < width + 2; x++) {
            int X = bottomWall.getX() + x;
            Position wallPos = new Position(X, bottomWall.getY());
            Position topWallPos = new Position(X, yTop);
            wallPositions.add(wallPos);
            wallPositions.add(topWallPos);
        }
        int xRight = bottomWall.getX() + width + 1;
        for (int y = 0; y < height + 2; y++) {
            int Y = bottomWall.getY() + y;
            Position wallPos = new Position(bottomWall.getX(), Y);
            Position rightWallPos = new Position(xRight, Y);
            wallPositions.add(wallPos);
            wallPositions.add(rightWallPos);
        }
    }

    public List<Position> getSidePositions(int side) {
        List<Position> sidePositions = new LinkedList<>();
        // top
        if (side == 0) {
            int X = bottomLeftCorner.getX();
            int yTop = bottomLeftCorner.getY() + height;
            /*sidePositions.add(new Position(X, yTop));
            sidePositions.add(new Position(X + width - 1, yTop));*/
            for (int x = 0; x < width; x++) {
                Position p = new Position(X + x, yTop);
                sidePositions.add(p);
            }
            //bottom
        } else if (side == 1) {
            int X = bottomLeftCorner.getX();
            /*sidePositions.add(new Position(X, bottomLeftCorner.getY() - 1));
            sidePositions.add(new Position(X + width - 1, bottomLeftCorner.getY() - 1));*/
            for (int x = 0; x < width; x++) {
                Position p = new Position(X + x, bottomLeftCorner.getY() - 1);
                sidePositions.add(p);
            }
            //left
        } else if (side == 2) {
            int Y = bottomLeftCorner.getY();
            /*sidePositions.add(new Position(bottomLeftCorner.getX() - 1, Y));
            sidePositions.add(new Position(bottomLeftCorner.getX() - 1, Y + height - 1));*/
            for (int y = 0; y < height; y++) {
                Position p = new Position(bottomLeftCorner.getX() - 1, Y + y);
                sidePositions.add(p);
            }
            //right
        } else if (side == 3) {
            int Y = bottomLeftCorner.getY();
            int xRight = bottomLeftCorner.getX() + width;
            /*sidePositions.add(new Position(xRight, Y));
            sidePositions.add(new Position(xRight, Y + height - 1));*/
            for (int y = 0; y < height; y++) {
                Position p = new Position(xRight, Y + y);
                sidePositions.add(p);
            }
        }
        return sidePositions;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<Position> getWallPositions() {
        return this.wallPositions;
    }


    public static Position getBottomLeftCornerFromTopRightCorner(Position topRightCorner,
                                                                 int width, int height) {
        int X = topRightCorner.getX() - width + 1;
        int Y = topRightCorner.getY() - height + 1;
        return new Position(X, Y);
    }

    public Rectangle neighbor(Position bottomLeftCorner2, int width2, int height2) {
        Rectangle neighbor = new Rectangle(bottomLeftCorner2, width2, height2);
        return neighbor;
    }
}
