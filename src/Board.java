
public class Board {

	public static boolean isPieceInHand = false;
	public static int[] previousLocation = new int[2];
	public static int piecePickedUp = 0;
	public static boolean whiteToMove = true;
	
	public static int lastPieceMoved = 0;
	public static int lastRankMovedTo = 0;
	public static int lastRankMovedFrom = 0;
	public static int lastFileMovedTo = 0;
	public static int lastFileMovedFrom = 0;
	
	public static int[][] gameState = 
			 { { 6, 3, 1, 5, 2, 1, 3, 6 }, 
			 { 4, 4, 4, 4, 4, 4, 4, 4 },
			 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
			 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
			 { 0, 0, 0, 0, 0, 0, 0, 0 },
			 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
			 { 10, 10, 10, 10, 10, 10, 10, 10 }, 
			 { 12, 9, 7, 11, 8, 7, 9, 12 } };
//	public static int[][] gameState = 
//		 { { 0, 0, 0, 0, 2, 0, 0, 0 }, 
//		 { 0, 0, 0, 0, 0, 0, 0, 0 },
//		 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
//		 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
//		 { 0, 0, 0, 0, 0, 0, 0, 0 },
//		 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
//		 { 0, 0, 0, 0, 0, 0, 0, 0 }, 
//		 { 12, 9, 7, 11, 8, 7, 9, 12 } };
	// public static int[][] gameState = 
	// 	 { { 11, 0, 0, 0, 0, 0, 0, 0 }, 
	// 	 { 0, 0, 0, 0, 0, 0, 11, 0 },
	// 	 { 0, 0, 0, 0, 11, 0, 0, 0 }, 
	// 	 { 0, 0, 0, 0, 0, 0, 0, 11 }, 
	// 	 { 0, 11, 0, 0, 0, 0, 0, 0 },
	// 	 { 0, 0, 0, 11, 0, 0, 0, 0 }, 
	// 	 { 0, 0, 0, 0, 0, 11, 0, 0 }, 
	// 	 { 0, 0, 11, 0, 0, 0, 0, 0 } };
	

	
	public static void pickPieceUp() {
		
		int index_y = (int) (GameDriver.mouseLocation()[0] / GameDriver.TILE_WIDTH);
		int index_x = (int) (GameDriver.mouseLocation()[1] / GameDriver.TILE_WIDTH);
		int pieceClicked = gameState[index_y][index_x];
		//clicked on a piece
		if (whiteToMove) {
			if (pieceClicked > 6) {
				piecePickedUp = pieceClicked;
				previousLocation[0] = index_y;
				previousLocation[1] = index_x;
				isPieceInHand = true;
			}
		}
		else {
			if (pieceClicked < 7  && pieceClicked > 0) {
				piecePickedUp = pieceClicked;
				previousLocation[0] = index_y;
				previousLocation[1] = index_x;
				isPieceInHand = true;
			}
		}
	}
	
	public static void placePiece () {
		int index_y = (int) (GameDriver.mouseLocation()[0] / GameDriver.TILE_WIDTH);
		int index_x = (int) (GameDriver.mouseLocation()[1] / GameDriver.TILE_WIDTH);
		
		
		boolean isCastling = Piece.isCastling(piecePickedUp, previousLocation[1], previousLocation[0], index_x, index_y);
		if (isCastling) {
			isPieceInHand = false;
			whiteToMove = !whiteToMove;
			return;
		}
		
		boolean validMove = Piece.isValidMove(piecePickedUp, previousLocation[1], previousLocation[0], index_x, index_y, true);
		//move would put king in check
		if (Board.wouldKingBeInCheck(gameState, whiteToMove, previousLocation[1], previousLocation[0], index_x, index_y)) {
			validMove = false;
		}
		
		if (validMove) {	
			lastPieceMoved = piecePickedUp;
			lastRankMovedTo = index_y;
			lastRankMovedFrom = previousLocation[0];
			lastFileMovedFrom = previousLocation[1];
			lastFileMovedTo = index_x;
			
			//empty square you're trying to place it on
			if (gameState[index_y][index_x] == 0) {
				if (index_x != lastFileMovedFrom && (lastPieceMoved == 4 || lastPieceMoved == 10)) {
					//en passant
					if (index_x > lastFileMovedFrom) {
						Board.gameState[lastRankMovedFrom][lastFileMovedFrom + 1] = 0;
					}
					else if (index_x < lastFileMovedFrom) {
						Board.gameState[lastRankMovedFrom][lastFileMovedFrom - 1] = 0;
					}
				}
				// black pawn promotion on empty square
				if (piecePickedUp == 4 && index_y == 7) {
					gameState[index_y][index_x] = 5;
				}
				//white pawn promotion on empty square
				else if (piecePickedUp == 10 && index_y == 0) {
					gameState[index_y][index_x] = 11;
				}
				
				else {
					gameState[index_y][index_x] = piecePickedUp;
				}
			}
			
			//attempting to capture enemy piece
			else if ((piecePickedUp > 6 && (gameState[index_y][index_x] < 7 && gameState[index_y][index_x] > 0)) || 
				(piecePickedUp < 7 && gameState[index_y][index_x] > 6)) {
				// black pawn promotion on empty square
				if (piecePickedUp == 4 && index_y == 7) {
					gameState[index_y][index_x] = 5;
				}
				//white pawn promotion on empty square
				else if (piecePickedUp == 10 && index_y == 0) {
					gameState[index_y][index_x] = 11;
				}
				else {
					gameState[index_y][index_x] = piecePickedUp;
				}
			}
//			System.out.println("piece picked up was " + piecePickedUp + " and previous location was " + previousLocation[0] + previousLocation[1]);
			gameState[previousLocation[0]][previousLocation[1]] = 0;
			isPieceInHand = false;	
			whiteToMove = !whiteToMove;
			
			
			if (lastPieceMoved == 6 && lastFileMovedFrom == 7) {
				Piece.leftBlackRookMoved = true;
			}
			if (lastPieceMoved == 6 && lastFileMovedFrom == 0) {
				Piece.rightBlackRookMoved = true;
			}
			if (lastPieceMoved == 12 && lastFileMovedFrom == 0) {
				Piece.leftWhiteRookMoved = true;
			}
			if (lastPieceMoved == 12 && lastFileMovedFrom == 7) {
				Piece.rightWhiteRookMoved = true;
			}
			
//			check for draw and mate
			checkForMate();
			checkForStalemate();
			
		}
		//invalid move
		else {
			gameState[previousLocation[0]][previousLocation[1]] = piecePickedUp;
			piecePickedUp = 0;
			isPieceInHand = false;
		}
	}
	
	public static int getKingX (int[][] gameState, boolean white) {
		if (white) {
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					if (gameState[i][j] == 8) {
						return j;
					}
				}
			}
		}
		else {
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					if (gameState[i][j] == 2) {
						return j;
					}
				}
			}
		}
		return previousLocation[1];
	}
	
	public static int getKingY (int[][] gameState, boolean white) {
		if (white) {
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					if (gameState[i][j] == 8) {
						return i;
					}
				}
			}
		}
		else {
			for (int i = 0; i < 8; ++i) {
				for (int j = 0; j < 8; ++j) {
					if (gameState[i][j] == 2) {
						return i;
					}
				}
			}
		}
		return previousLocation[0];
	}
	
	public static boolean wouldKingBeInCheck(int[][] gameState, boolean whiteToMove, int oldX, int oldY, int newX, int newY) {
		int originalPiece = gameState[newY][newX];
		int movingPiece = gameState[oldY][oldX];
		// Simulate the move
	    gameState[newY][newX] = movingPiece;
	    gameState[oldY][oldX] = 0;
	    
	    int kingX = getKingX(gameState, whiteToMove);
	    int kingY = getKingY(gameState, whiteToMove);
		boolean isInCheck = Piece.isSquareControlled(whiteToMove, kingY, kingX);
		// Revert the simulated move
	    gameState[newY][newX] = originalPiece;
	    gameState[oldY][oldX] = movingPiece;
	    return isInCheck;
	}
	
	public static void checkForMate() {
		if (Piece.isSquareControlled(whiteToMove, getKingY(gameState, whiteToMove), getKingX(gameState, whiteToMove))) {
			if (Piece.returnNumberOfMoves(gameState) == 0) System.out.println("holy mackarel that's a checkmate");	
		}
	}
	public static void checkForStalemate() {
		if (!Piece.isSquareControlled(whiteToMove, getKingY(gameState, whiteToMove), getKingX(gameState, whiteToMove))) {
			if (Piece.returnNumberOfMoves(gameState) == 0) System.out.println("holy mackarel that's a stalemate");	
		}
	}
}
	

