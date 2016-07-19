import java.util.ArrayList;

public final class Cell {
	private int x,y;
	ArrayList<Integer> possibleEntries;
	private int value, numberOfPossibilities;
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		if(value < 0 || value > 9)
		{
			System.out.println("Invalid value for a cell. Must be between 1..9 and 0 for a empty cell.");
			System.exit(1);
		}
		this.value = value;
	}
	
	public int getNumberOfPossibilities() {
		return numberOfPossibilities;
	}

	public void setNumberOfPossibilities(int numberOfPossibilities) {
		if(numberOfPossibilities < 0 || numberOfPossibilities > 9)
		{
			System.out.println("Number of possibilities for a cell must be between 0..9");
			System.exit(1);
		}
		this.numberOfPossibilities = numberOfPossibilities;
	}

	Cell(int x,int y,int value)
	{
		this.x = x;
		this.y = y;
		this.value = value;
		this.possibleEntries = null;
	}
	
	private boolean isBetween(int value,int lower,int upper)
	{
		return lower <= value && value < upper;
	}
		
	public void calculatePossibilities(Cell[][] board)
	{
		/*
		 * possibilities array stores all the possibilities. i.e 1-9
		 * existingNumbers array stores all numbers that are in the current cell's row, column and 3*3 grid
		 */
		ArrayList<Integer> possibilities = new ArrayList<Integer>();
		ArrayList<Integer> existingNumbers = new ArrayList<Integer>();
		
		//Eliminate from possibilities all numbers in the row and column
		for(int i = 1;i <= 9;i++)
		{
			//First add all numbers to the possibilities array
			possibilities.add(i);
			
			if(board[this.x][i].value != 0 && !existingNumbers.contains(board[this.x][i].value))
			{
				existingNumbers.add(board[this.x][i].value);
			}
			if(board[i][this.y].value != 0 && !existingNumbers.contains(board[i][this.y].value))
			{
				existingNumbers.add(board[i][this.y].value);
			}
		}
		
		//Find the initial cell of its 3x3 grid, x,y belongs to {1,4,7}
		int gridInitialX = 0,gridInitialY = 0;
		if(isBetween(this.x, 1, 4))
			gridInitialX = 1;
		else if(isBetween(this.x, 4, 7))
			gridInitialX = 4;
		else if(isBetween(this.x,7,10))
			gridInitialX = 7;
		if(isBetween(this.y, 1, 4))
			gridInitialY = 1;
		else if(isBetween(this.y, 4, 7))
			gridInitialY = 4;
		else if(isBetween(this.y,7,10))
			gridInitialY = 7;
		
		//Eliminate from possibilities all values in its 3x3 grid
		for(int i = gridInitialX;i < gridInitialX + 3;i++)
			for(int j = gridInitialY;j < gridInitialY + 3;j++)
				if(board[i][j].value != 0 && !existingNumbers.contains(board[i][j].value))
				{
					existingNumbers.add(board[i][j].value);
				}
		
		//all_possibilities - existing numbers = possibilites 
		possibilities.removeAll(existingNumbers);
		this.possibleEntries = possibilities;
		numberOfPossibilities = this.possibleEntries.size();
	}
}
