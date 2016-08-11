package sudokuanddatabase;

import java.lang.reflect.Array;
import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {

    private static final int SIZE = 9;
    private int[][] grid;
    /**
     * TODO: 3) should " Set<Integer> allPossibleValues = new HashSet<>();" be
     * declared in the "Sudoku" class or in the "Spot" inner class? My thought
     * is that even though it is only used in "Spot" class, if I declare it in
     * "Spot" class, then every time I create an instance of "Spot" class, a new
     * instance of "Set<Integer> allPossibleValues" is created. So why not put
     * it in the outer "Sudoku" class, so that all instance of "Spot" class can
     * use the same instance of "allPossibleValues".
     */
    private Set<Integer> allPossibleValues;
    private List<Spot> emptySpots;
    private int numberOfSolutions;
    private double timeElapsed;
    private int[][] firstSolution;

    private int numberOfTimesRecursiveSolveMethodIsCalled = 0;

    class Spot {

        int xCoordinate, yCoordinate;
        Set<Integer> assignableValues;
        int assignableValueSize;

        Spot(int x, int y) {
            xCoordinate = x;
            yCoordinate = y;
            assignableValues = calculateAssignableValues();
            assignableValueSize = getNumberOfAssignableValues();
        }

        private Set<Integer> calculateAssignableValues() {

            Set<Integer> potentiallyAssignableValues = new HashSet<>(allPossibleValues);

            Set<Integer> nonAssignableValues = calculateNonAssignableValues();

            potentiallyAssignableValues.removeAll(nonAssignableValues);

            return potentiallyAssignableValues;

        }

        private Set<Integer> calculateNonAssignableValues() {
            Set<Integer> nonAssignableValues = new HashSet<>();

            //add all numbers from the same row as this particular instance
            //of "Spot" to "nonAssignableValues". 
            for (int i = 0; i < SIZE; i++) {
                if (grid[xCoordinate][i] != 0) {
                    nonAssignableValues.add(grid[xCoordinate][i]);
                }
            }

            //add all numbers from the same colume as this particular instance
            //of "Spot" to "nonAssignableValues". 
            for (int i = 0; i < SIZE; i++) {
                if (grid[i][yCoordinate] != 0) {
                    nonAssignableValues.add(grid[i][yCoordinate]);
                }
            }

            //add all number from the same 3 by 3 grid (a sudoku puzzle 9 by 9 grid
            //is made up of 9 of the 3 by 3 grid) to "nonAssignableValues"
            int smallerGridWidthAndHeight = 3;
            //if "xCoordinate" has value between 0 and 2, then dividing it by 3 
            //will return 0, then times by 3 will still equal to 0. therefore
            //for "Spot" that are located from row 0 to 2, its smaller grid 
            //starting position is 0. Same principle apply for "startingColumePositionFor3By3Grid".
            int startingRowPositionFor3By3Grid = (xCoordinate / 3) * 3;
            /**
             * TODO: 4) magic number 3 is used in calculating variable
             * "startingColumePositionFor3By3Grid", should I come up with a way
             * to get rid of it? But it is something so simple, is it worthwhile
             * get rid of it? but on the other hand, there should not be magic
             * number laying around.
             */
            int startingColumePositionFor3By3Grid = (yCoordinate / 3) * 3;
            for (int i = 0; i < smallerGridWidthAndHeight; i++) {
                for (int j = 0; j < smallerGridWidthAndHeight; j++) {
                    if (grid[i + startingRowPositionFor3By3Grid][j + startingColumePositionFor3By3Grid] != 0) {
                        nonAssignableValues.add(grid[i + startingRowPositionFor3By3Grid][j + startingColumePositionFor3By3Grid]);
                    }
                }
            }

            return nonAssignableValues;
        }

        private int getNumberOfAssignableValues() {
            return assignableValues.size();
        }

        @Override
        public String toString() {
            return "Spot location: row " + xCoordinate + ", colume " + yCoordinate;
        }
    }

    /**
     * this is the initializer that perform the basic common setup, this
     * initializer should be called by other initializer that belong to the same
     * class
     */
    private Sudoku() {
        allPossibleValues = new HashSet<>();

        for (int i = 1; i <= SIZE; i++) {
            allPossibleValues.add(i);
        }

        grid = new int[SIZE][SIZE];
        firstSolution = new int[SIZE][SIZE];

        emptySpots = new ArrayList<>();

        numberOfSolutions = 0;
    }

    /**
     * Sets up based on the given ints.
     *
     * @param ints
     */
    public Sudoku(int[][] ints) {
        this();

        for (int[] int1 : ints) {
            System.arraycopy(ints, 0, grid, 0, ints.length);
        }
    }

    private void findEmptySpotsAndOrderByNumberOfAssignableValues() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    Spot newEmptySpot = new Spot(i, j);
                    emptySpots.add(newEmptySpot);
                }
            }
        }

        emptySpots.sort((Spot s1, Spot s2) -> s1.assignableValueSize - s2.assignableValueSize);
    }

    /**
     * Solves the puzzle, invoking the underlying recursive search.
     */
    void solve() {
        findEmptySpotsAndOrderByNumberOfAssignableValues();
        double startTime = System.currentTimeMillis();
        recursiveSolve(0);
        timeElapsed = System.currentTimeMillis() - startTime;
    }

    private void recursiveSolve(int index) {

        numberOfTimesRecursiveSolveMethodIsCalled += 1;
        System.out.println("number of times recursive method called: " + numberOfTimesRecursiveSolveMethodIsCalled);

        if (index == emptySpots.size()) {
            if (numberOfSolutions == 0) {
                for (int i = 0; i < SIZE; i++) {
                    System.arraycopy(grid[i], 0, firstSolution[i], 0, grid[i].length);
                }
            }
            numberOfSolutions += 1;
            return;
        }

        Spot currentSpot = emptySpots.get(index);
        System.out.println("number of solutions: " + numberOfSolutions);
        System.out.println("currentSpot: " + emptySpots.indexOf(currentSpot) + " " + currentSpot.toString());
        Set<Integer> assignableValues = currentSpot.calculateAssignableValues();
        System.out.println("assignable value: " + assignableValues);
        System.out.println("");

        // when "currentSpot.assignableValues" is a empty "List<Integer>", 
        //the execution will not enter "for (int assignableValue : currentSpot.assignableValues) {}",
        //therefore "for (int assignableValue : currentSpot.assignableValues) {}" can act as 
        //the break condition for the recursive algorithm. 
        for (int assignableValue : assignableValues) {
            grid[currentSpot.xCoordinate][currentSpot.yCoordinate] = assignableValue;
            recursiveSolve(index + 1);
            grid[currentSpot.xCoordinate][currentSpot.yCoordinate] = 0;
        }

    }

    public String getSolutionText() {
        String solutionText = "";
        if (numberOfSolutions == 0) {
            solutionText = "no solution for the given sudoku puzzle.";
        } else {
            for (int[] row : firstSolution) {
                solutionText += (Arrays.toString(row) + "\n");
            }
        }
        return solutionText;
    }

    public double getElapsed() {
        return timeElapsed;
    }

    @Override
    public String toString() {
        String originalGrid = "";
        for (int[] row : grid) {
            originalGrid += (Arrays.toString(row)) + "\n";
        }
        return originalGrid;
    }

    // Provided -- the deliverable main().
    // You can edit to do easier cases, but turn in
    // solving hardGrid.
    public static void main(String[] args) {
        Sudoku sudoku;
        sudoku = new Sudoku(hardGrid);

        System.out.println(sudoku); // print the raw problem
        sudoku.solve();
        System.out.println("solutions:" + sudoku.numberOfSolutions);
        System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
        System.out.println(sudoku.getSolutionText());

    }

    // Provided grid data for main/testing
    // The instance variable strategy is up to you.
    // Provided easy 1 6 grid
    // (can paste this text into the GUI too)
    public static final int[][] easyGrid = Sudoku.stringsToGrid(
            "1 6 4 0 0 0 0 0 2",
            "2 0 0 4 0 3 9 1 0",
            "0 0 5 0 8 0 4 0 7",
            "0 9 0 0 0 6 5 0 0",
            "5 0 0 1 0 2 0 0 8",
            "0 0 8 9 0 0 0 3 0",
            "8 0 9 0 4 0 2 0 0",
            "0 7 3 5 0 9 0 0 1",
            "4 0 0 0 0 0 6 7 9");

    // Provided medium 5 3 grid
    public static final int[][] mediumGrid = Sudoku.stringsToGrid(
            "530070000",
            "600195000",
            "098000060",
            "800060003",
            "400803001",
            "700020006",
            "060000280",
            "000419005",
            "000080079");

//    // Provided hard 3 7 grid
//    // 1 solution this way, 6 solutions if the 7 is changed to 0
//    public static final int[][] hardGrid = Sudoku.stringsToGrid(
//            "3 7 0 0 0 0 0 8 0",
//            "0 0 1 0 9 3 0 0 0",
//            "0 4 0 7 8 0 0 0 3",
//            "0 9 3 8 0 0 0 1 2",
//            "0 0 0 0 4 0 0 0 0",
//            "5 2 0 0 0 6 7 9 0",
//            "6 0 0 0 2 1 0 4 0",
//            "0 0 0 5 3 0 9 0 0",
//            "0 3 0 0 0 0 0 5 1");
//    
//    
//    
//    
//    
    public static final int[][] hardGrid = Sudoku.stringsToGrid(
            "3 0 0 0 0 0 0 8 0",
            "0 0 1 0 9 3 0 0 0",
            "0 4 0 0 8 0 0 0 3",
            "0 9 3 8 0 0 0 1 2",
            "0 0 0 0 4 0 0 0 0",
            "5 2 0 0 0 6 0 9 0",
            "6 0 0 0 2 1 0 4 0",
            "0 0 0 5 3 0 9 0 0",
            "0 3 0 0 0 0 0 5 1");

    public static final int PART = 3;  // size of each 3x3 part
    public static final int MAX_SOLUTIONS = 100;

    // Provided various static utility methods to
    // convert data formats to int[][] grid.
    /**
     * Returns a 2-d grid parsed from strings, one string per row. The "..." is
     * a Java 5 feature that essentially makes "rows" a String[] array.
     * (provided utility)
     *
     * @param rows array of row strings
     * @return grid
     */
    public static int[][] stringsToGrid(String... rows) {
        int[][] result = new int[rows.length][];
        for (int row = 0; row < rows.length; row++) {
            result[row] = stringToInts(rows[row]);
        }
        return result;
    }

    /**
     * Given a single string containing 81 numbers, returns a 9x9 grid. Skips
     * all the non-numbers in the text. (provided utility)
     *
     * @param text string of 81 numbers
     * @return grid
     */
    public static int[][] textToGrid(String text) {
        int[] nums = stringToInts(text);
        if (nums.length != SIZE * SIZE) {
            throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
        }

        int[][] result = new int[SIZE][SIZE];
        int count = 0;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = nums[count];
                count++;
            }
        }
        return result;
    }

    /**
     * Given a string containing digits, like "1 23 4", returns an int[] of
     * those digits {1 2 3 4}. (provided utility)
     *
     * @param string string containing ints
     * @return array of ints
     */
    public static int[] stringToInts(String string) {
        int[] a = new int[string.length()];
        int found = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                a[found] = Integer.parseInt(string.substring(i, i + 1));
                found++;
            }
        }
        int[] result = new int[found];
        System.arraycopy(a, 0, result, 0, found);
        return result;
    }
}
