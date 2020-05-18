package com.mycompany;

public class NQueenBoard
{
	private int n;
	private boolean[][] board;

	// queens[x] contains the y-coordinate of a queen placed in column x
	// used as a simple optimization so the board doesn't have to be searched all the time
	private int[] queens;
	
	// queenInRow[y] is true if a queen is in the row
	// used as a simple optimization so the board doesn't have to be searched all the time
	private boolean[] queenInRow;
	
    public static void main(String[] args)
    {
        Boolean success = false;
        for (int i = 0; i < 10; i++)
        {
            NQueenBoard board = new NQueenBoard(i);
            success = board.Solve();

            System.out.println("\n" + i + "x" + i + " Board - " + (success ? "Sucess" : "Failure"));
    		board.Print();
        }
	}
	
	public NQueenBoard(int n)
	{
        this.n = n;
        
        board = new boolean[n][n];		
		queens = new int[n];
		queenInRow = new boolean[n];
	}
	
	public boolean Solve()
	{
		// optimize by solving trying just the first half rows when staring out at column 0
		// this is possible because the board is symetrical
		return Solve((int) Math.ceil(n / 2.0), 0);
	}
	
	// solve board from column x going to the right
	private boolean Solve(int rows, int x)
	{
		// since this is past the last column, the solver succeeded
		if (x == n)
			return true;
		
		// if necessary, try each row in this column
		for (int y = 0; y < rows; y++)
		{
			if (PlaceQueen(x, y))
			{
				if (Solve(n, x + 1))
					return true;
			
				// a queen was place but the rest of the board cannot be solved, undo the placement, and go to the next row
				board[x][y] = false;
				queens[x] = -1;
				queenInRow[y] = false;					
			}
		}
		
		// a queen cannot be put in any of the rows in this column, so the solver failed
		return false;
	}
	
	// try to place a queen in cell x/y
	private boolean PlaceQueen(int x, int y)
	{
		// if row y, NW diagonal containng cell x/y, SW diagonal containing cell x/y, or 3-in-a-line isn't safe, a queen cannot be placed
		if (!SafeRow(y) || !SafeSW(x, y)|| !SafeNW(x, y)  || !SafeLine(x, y))
			return false;
		
		// cell x/y is safe for a queen
		board[x][y] = true;
		queens[x] = y;
		queenInRow[y] = true;
		
		return true;
	}
	
	// determine if a queen is safe at row y
	private boolean SafeRow(int y)
	{
		return !queenInRow[y];
	}
	
	// determine if a queen is safe at cell x/y going up and to the left in the NW direction
	// only need to check columns 0 to x-1 because there's no queen on the right of column x
	private boolean SafeNW(int x, int y)
	{
		// start from the left/top cell before cell x/y and goes in the NW direction and stop when the left/top edge is reached
		for (int i = --x, j = ++y; i >= 0 && j < n; --i, ++j)
			if (board[i][j])
				return false; // a queen is found at column i/j on the NW line
		
		return true;
	}
	
	// determine if a queen is safe at cell x/y going down and to the left in the SW direction
	// only need to check columns 0 to x-1 because there's no queen on the right of column x
	private boolean SafeSW(int x, int y)
	{
		// start from the left/bottom cell before cell x/y and goes in the SW direction and stop when the left/bottom edge is reached
		for (int i = --x, j = --y; i >= 0 && j >= 0; --i, --j)
			if (board[i][j])
				return false; // a queen is found at column i/j on the SW line
		
		return true;
	}
	
	// the next queen is about to be put into cell x/y
	// check to make sure that no 3 queens (so far, and including this one) are in a straight line
	// since previous queens had been checked, only need to check them with this new queen
	private boolean SafeLine(int x, int y)
	{
		// only start checking at column 3
		if (x < 2)
			return true;
        
		for (int i = 0; i < x - 1; i++)
		{
			int iy = queens[i];
			for (int j = i + 1; j < x; j++)
			{
				int jy = queens[j];
				
				// slope of i and j queens
				double m = (double) (jy - iy) / (j - i);

				// check to see if the queen at cell x/y is on the line defined by the i and j queens
				// if it is, then the queen cannot be placed at cell x/y
				// the line going through the i and j queens is: (y - iy) = m (x - i)
				if ((y - iy) == m * (x - i))
					return false;
			}
		}
		
		// no combination of previous 2 queens and this queen is in a straight line, it's safe to place this queen
		return true;
	}
	
	// print the board with cell 0/0 at the bottom left
	public void Print()
	{
		for (int y = n - 1; y >= 0; y--)
		{
			for (int x = 0; x < n; x++)
				System.out.print(board[x][y] ?  "1 " : "0 ");
			
			System.out.println();
		}
	}
}