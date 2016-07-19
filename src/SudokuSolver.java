import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public final class SudokuSolver {
	
	private JFrame jframe;
	
	public SudokuSolver() {
		jframe = new JFrame();
		jframe.setTitle("Sudoku");
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
		    {
		        System.exit(1);
		    }
		});
		jframe.setSize(500,500);
	}
	
	private void drawBoard(Cell[][] board)
	{
		JPanel jpanel = new JPanel(new GridLayout(9, 9));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		for(int row = 1;row <= 9; row++)
			for(int col = 1;col <= 9; col++)
			{
				//If a particular cell is empty, i.e 0 leave it empty
				JLabel valueLabel = new JLabel(board[row][col].getValue()==0 ? "" : String.valueOf(board[row][col].getValue()),SwingConstants.CENTER);
				valueLabel.setBorder(border);
				jpanel.add(valueLabel);
			}

		jframe.setLocationRelativeTo(null);
		jframe.add(jpanel);
		jframe.setVisible(true);
	}

	private boolean isCompleted(Cell[][] board) {
		
		// Iterate through all Cells, if a 0 is found then it is incomplete
		for (int i = 1; i <= 9; i++)
			for (int j = 1; j <= 9; j++)
				if (board[i][j].getValue() == 0)
					return false;
		return true;
	}

	public boolean checkValidity(Cell[][] board)
	{
		/*
		 * flaggerArray used to keep track of values present in each cell's row, column and 3*3 grid.
		 * by using it's index to track possible numbers.
		 *  
		 *  0 -> Not present
		 *  1 -> Present
		 */
		
		int[] flaggerArray;
		
		//Iterate through rows and columns to find a duplicate
		flaggerArray = new int[10];
		for(int i = 1;i <= 9;i++)
		{
			for(int j = 1;j <= 9;j++)
				if(board[i][j].getValue() !=  0)
				{
					if(flaggerArray[board[i][j].getValue()] == 1)
					{
						//Duplicate present in the same row
						return false;
					}
					else
					{
						flaggerArray[board[i][j].getValue()] = 1;
					}
				}
			
			//New flaggerArray for each row
			flaggerArray = new int[10];
		}
		
		//Iterate through its grid to find a duplicate
		ArrayList<Integer> possibleInitialValues = new ArrayList<>();
		possibleInitialValues.add(1);
		possibleInitialValues.add(4);
		possibleInitialValues.add(7);
		flaggerArray = new int[10];
		for(int gridInitialX : possibleInitialValues)
			for(int gridInitialY : possibleInitialValues)
			{
				flaggerArray = new int[10];
				for(int i = gridInitialX;i < gridInitialX + 3;i++)
					for(int j = gridInitialY;j < gridInitialY + 3;j++)
					{
						if(board[i][j].getValue() !=  0)
						{
							if(flaggerArray[board[i][j].getValue()] == 1)
							{
								return false;
							}
						}
						else
						{
							flaggerArray[board[i][j].getValue()] = 1;
						}
					}
			}
		return true;	
	}

	public boolean solveSudoku(Cell[][] board)
	{

		
		drawBoard(board);
		if(isCompleted(board) && checkValidity(board))
		{
			//Output board and exit
			drawBoard(board);
			System.out.println("Solved successfully!\n");
			for(int i = 1;i <= 9;i++)
			{
				for(int j = 1;j <= 9;j++)
					System.out.print(board[i][j].getValue() + "\t");
				System.out.println();
			}
			return true;
		}
		else
		{
			//Compute possibilities for each cell, also find the cell with minimum possibilities
			int minimumPossibilities = 10;
			Cell cellWithMinimumPossibilities = null;
			for(int i = 1;i <= 9;i++)
				for(int j = 1;j <= 9;j++)
					if(board[i][j].getValue() == 0)
					{
						board[i][j].calculatePossibilities(board);
						if(minimumPossibilities > board[i][j].getNumberOfPossibilities())
						{
							minimumPossibilities = board[i][j].getNumberOfPossibilities();
							cellWithMinimumPossibilities = board[i][j];
						}			
					}
			
			//There exists atleast one possibility in any one cell
			if(cellWithMinimumPossibilities != null && minimumPossibilities != 0)
			{
				//Try each one recursively
				for(int possibility : cellWithMinimumPossibilities.possibleEntries)
				{
					int previousValue = cellWithMinimumPossibilities.getValue();
				    cellWithMinimumPossibilities.setValue(possibility);
				    if (solveSudoku(board)) {
				        return true;
					}
				    
				    //Backtrack when fail(return false)
				    cellWithMinimumPossibilities.setValue(previousValue);
				}
			}
			return false;
		}

	}

	public static void main(String[] args) {
		
		Cell[][] board = new Cell[10][10];
		
		//Read numbers from the "input.txt" file
		try {
			Scanner inputBoardScanner = new Scanner(new File("C:\\Users\\venee\\workspace\\Sudoku Solver\\src\\input.txt"));
			
			//i,j indices to read values into array
			for(int i = 1;i <= 9;i++)
				for(int j = 1;j <= 9;j++)
					if(inputBoardScanner.hasNextInt())
					{
						board[i][j] = new Cell(i,j,inputBoardScanner.nextInt());
					}
			inputBoardScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		SudokuSolver sudokuSolver = new SudokuSolver();
		
		//Check if valid board is given
		if(sudokuSolver.checkValidity(board))
		{
			sudokuSolver.solveSudoku(board);
		}
		else
		{
			System.out.println("Invaild input board, check sudoku rules!");
		}

	}

}
