package Game;

import GUI.MainWindow.MainWindow;

import java.awt.*;

public class BlockShapes {
    /*
    This class is just an extension of the Block Class. Handles getting block rotations,
    Loading textures
    */


    /* Array of all shapes 7 in total */

    private final int[][][] blockShapes = new int[][][]{
            {{1,0},{1,0},{1,1}}, // L
            {{0,1},{0,1},{1,1}}, // J
            {{1,1,0},{0,1,1}},   // Z
            {{0,1,1},{1,1,0}},   // S
            {{1,1,1},{0,1,0}},   // T
            {{1,1},{1,1}},       // O
            {{1,1,1,1}}         // I
    };

    /* An Array of all the block textures */

    private final Image[] blockTextures = new Image[]{
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block1.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block2.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block3.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block4.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block5.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block6.png"),
            Toolkit.getDefaultToolkit().getImage("src/GUI/Assets/block7.png"),
    };

    /* Returns the block shape from a given index */

    public int [][] getBlockShape(int shapeIndex){
        return blockShapes[shapeIndex];
    }

    /* sends a 3D array of all possible block rotations for the given shape */

    public int[][][] getBlockRotations(int[][] shape){
        int[][][] shapeRotations = new int[4][][];
        for(int i = 0; i < 4; i++){ int r = shape[0].length;
            int c = shape.length;

            shapeRotations[i] = new int[r][c];
            for(int y = 0; y < r; y++){
                for(int x = 0; x < c; x++){
                    shapeRotations[i][y][x] = shape[c - x - 1][y];
                }
            }
            shape = shapeRotations[i];
        }
        return shapeRotations;
    }

    /* Gets the texture of the current shape based on its index */

    public Image getBlockTexture(int shapeIndex){
        if(shapeIndex < 7)
            return blockTextures[shapeIndex];
        else return blockTextures[MainWindow.random.nextInt(7)];
    }

    /* Sends the number of possible shapes. This is always 7 unless the blockShapes[] is added or removed from */

    public int numOfShapes(){return blockShapes.length;}
}