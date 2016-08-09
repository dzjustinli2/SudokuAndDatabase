/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokuanddatabase;

/**
 *
 * @author justin
 */
public class ListOfTodosFromSudokuAndDatabase {

}

//TODO: 1) design "Sudoku" class in such a way that it would be easy to change the grid size to 16 by 16 
/**
 * TODO: 2) class "Sudoku", variable "List<Spot> emptySpots", the variable is
 * given in the class template, but what if it is declared as "HashSet<Spot>
 * emptySpots"? Because hashset can not contain identical element, and each
 * instance of "Spot" is located in a different location on the grid, doesn't
 * this make "Hashset<Spot>" a better choice over "List<Spot>"?
 */
/**
 * TODO: 3) should " Set<Integer> allPossibleValues = new HashSet<>();" be
 * declared in the "Sudoku" class or in the "Spot" inner class? My thought is
 * that even though it is only used in "Spot" class, if I declare it in "Spot"
 * class, then every time I create an instance of "Spot" class, a new instance
 * of "Set<Integer> allPossibleValues" is created. So why not put it in the
 * outer "Sudoku" class, so that all instance of "Spot" class can use the same
 * instance of "allPossibleValues".
 */
/**
 * TODO: 4) magic number 3 is used in calculating variable
 * "startingColumePositionFor3By3Grid", should I come up with a way to get rid
 * of it? But it is something so simple, is it worthwhile get rid of it? but on
 * the other hand, there should not be magic number laying around.
 */
