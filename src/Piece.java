import java.util.ArrayList;

public class Piece {

	public static boolean leftWhiteRookMoved = false;
	public static boolean rightWhiteRookMoved = false;
	public static boolean leftBlackRookMoved = false;
	public static boolean rightBlackRookMoved = false;

	public static boolean whiteKingHasMoved = false;
	public static boolean blackKingHasMoved = false;
	
	public static boolean enPassantingOnLeft = false;
	public static boolean enPassantingOnRight = false;

	public static boolean isValidMove(int piece, int old_x, int old_y, int new_x, int new_y, boolean actuallyMoving) {

		// attempting to capture own piece
		if ((piece > 6 && Board.gameState[new_y][new_x] > 6)
				|| (piece < 7 && (Board.gameState[new_y][new_x] < 7) && Board.gameState[new_y][new_x] > 0)) {
			return false;
		}

		if (old_x == new_x && old_y == new_y) {
			return false;
		}

		if (piece == 4 || piece == 10) {
			return isPawnMoveValid(piece, old_x, old_y, new_x, new_y);
		}

		// rook
		if (piece == 6 || piece == 12) {
			return isRookMoveValid(piece, old_x, old_y, new_x, new_y);
		}

		// bishop
		if (piece == 1 || piece == 7) {
			return isBishopMoveValid(piece, old_x, old_y, new_x, new_y);
		}

		// knight
		if (piece == 3 || piece == 9) {
			return isKnightMoveValid(piece, old_x, old_y, new_x, new_y);
		}

		// queen
		if (piece == 5 || piece == 11) {
			return (isRookMoveValid(piece, old_x, old_y, new_x, new_y)
					|| isBishopMoveValid(piece, old_x, old_y, new_x, new_y));
		}

		// king
		if (piece == 2 || piece == 8) {
			return isKingMoveValid(piece, old_x, old_y, new_x, new_y, actuallyMoving);
		}

		return false;
	}

	public static boolean isPawnMoveValid(int piece, int old_x, int old_y, int new_x, int new_y) {
		// black pawn
		if (piece == 4) {
			if (new_x == old_x) {
				if (new_y == (old_y + 1) && Board.gameState[new_y][new_x] == 0) {
					return true;
				}
				if (old_y == 1) {
					if (new_y == 3 && Board.gameState[old_y + 1][old_x] == 0
							&& Board.gameState[old_y + 2][old_x] == 0) {
						return true;
					}
				}
			}
			// move one diagonally, normal capture pattern
			if (new_y == old_y + 1 && (new_x == old_x + 1 || new_x == old_x - 1)) {
				if (Board.gameState[new_y][new_x] > 6) {
					return true;
				}
			}

			// en passant
			if ((old_y == 4 && new_y == 5) && Board.lastPieceMoved == 10 && Board.lastRankMovedTo == 4
					&& Board.lastFileMovedTo == new_x && Board.lastRankMovedFrom == 6) {
				if (Board.gameState[old_y][old_x + 1] == 10 && new_x > old_x) {
//					Board.gameState[old_y][old_x + 1] = 0;

					return true;
				}
				if (Board.gameState[old_y][old_x - 1] == 10 && new_x < old_x) {
//					Board.gameState[old_y][old_x - 1] = 0;

					return true;
				}
			}
		} // end black pawn

		// white pawn
		if (piece == 10) {
			if (new_x == old_x) {
				if (new_y == (old_y - 1) && Board.gameState[new_y][new_x] == 0) {

					return true;
				}
				if (old_y == 6) {
					if (new_y == (old_y - 2) && Board.gameState[old_y - 1][old_x] == 0
							&& Board.gameState[old_y - 2][old_x] == 0) {

						return true;
					}
				}
			}
			// move one diagonally, normal capture pattern
			if (new_y == old_y - 1 && (new_x == old_x + 1 || new_x == old_x - 1)) {
				if (Board.gameState[new_y][new_x] < 7 && Board.gameState[new_y][new_x] > 0) {

					return true;
				}
			}
			// en passant
			if ((old_y == 3 && new_y == 2) && Board.lastPieceMoved == 4 && Board.lastRankMovedTo == 3
					&& Board.lastFileMovedTo == new_x && Board.lastRankMovedFrom == 1) {
				if (Board.gameState[old_y][old_x + 1] == 4 && new_x > old_x) {
//					Board.gameState[old_y][old_x + 1] = 0;

					return true;
				}
				if (Board.gameState[old_y][old_x - 1] == 4 && new_x < old_x) {
//					Board.gameState[old_y][old_x - 1] = 0;

					return true;
				}
			}
		} // end white pawn
		return false;
	}

	public static boolean isRookMoveValid(int piece, int old_x, int old_y, int new_x, int new_y) {
		// horizontal move
		if (new_y == old_y && new_x != old_x) {
			// right move
			if (new_x > old_x) {
				for (int i = old_x + 1; i < new_x; ++i) {
					if (Board.gameState[new_y][i] != 0) {
						return false;
					}
				}

				return true;
			}
			// left move
			else if (new_x < old_x) {
				for (int i = new_x + 1; i < old_x; ++i) {
					if (Board.gameState[new_y][i] != 0) {
						return false;
					}
				}

				return true;
			}
		}

		// vertical move
		if (new_x == old_x && new_y != old_y) {
			// downwards move(white focused)
			if (new_y > old_y) {
				for (int i = old_y + 1; i < new_y; ++i) {
					if (Board.gameState[i][new_x] != 0) {
						return false;
					}
				}

				return true;
			}
			// upwards move(white focused)
			else if (new_y < old_y) {
				for (int i = new_y + 1; i < old_y; ++i) {
					if (Board.gameState[i][new_x] != 0) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	public static boolean isBishopMoveValid(int piece, int old_x, int old_y, int new_x, int new_y) {
		if (Math.abs(old_x - new_x) == Math.abs(new_y - old_y)) {
			// upward right
			if (new_x > old_x && new_y < old_y) {
				for (int i = 1; i < Math.abs(old_x - new_x); ++i) {
					if (Board.gameState[old_y - i][old_x + i] != 0) {
						return false;
					}
				}

				return true;
			}

			// upward left
			if (new_x < old_x && new_y < old_y) {
				for (int i = 1; i < Math.abs(old_x - new_x); ++i) {
					if (Board.gameState[old_y - i][old_x - i] != 0) {
						return false;
					}
				}

				return true;
			}

			// downward right
			if (new_x > old_x && new_y > old_y) {
				for (int i = 1; i < Math.abs(old_x - new_x); ++i) {
					if (Board.gameState[old_y + i][old_x + i] != 0) {
						return false;
					}
				}

				return true;
			}
			// downward left
			if (new_x < old_x && new_y > old_y) {
				for (int i = 1; i < Math.abs(old_x - new_x); ++i) {
					if (Board.gameState[old_y + i][old_x - i] != 0) {
						return false;
					}
				}

				return true;
			}

		}
		return false;
	}

	public static boolean isKnightMoveValid(int piece, int old_x, int old_y, int new_x, int new_y) {
		if (piece == 3 || piece == 9) {
			if (Math.abs(old_x - new_x) == 2 && Math.abs(old_y - new_y) == 1) {

				return true;
			}
			if (Math.abs(old_x - new_x) == 1 && Math.abs(old_y - new_y) == 2) {

				return true;
			}
		}
		return false;
	}

	public static boolean isKingMoveValid(int piece, int old_x, int old_y, int new_x, int new_y, boolean actuallyMoving) {
		if (Math.abs(old_x - new_x) < 2 && Math.abs(old_y - new_y) < 2) {
			if (Board.WhiteToMove) {
				if (actuallyMoving) whiteKingHasMoved = true;
			} else {
				if (actuallyMoving) blackKingHasMoved = true;
			}
			return true;
		}

		return false;
	}

	public static boolean isCastling(int piece, int old_x, int old_y, int new_x, int new_y) {
		// white short side castling
		if (Board.WhiteToMove && new_y == 7 && new_x == 6 && piece == 8 && !rightWhiteRookMoved && !whiteKingHasMoved
				&& Board.gameState[7][5] == 0 && Board.gameState[7][6] == 0) {
			Board.gameState[7][6] = 8;
			Board.gameState[7][5] = 12;
			Board.gameState[7][7] = 0;
			Board.gameState[7][4] = 0;
			return true;
		}
		// white long side castling
		else if (Board.WhiteToMove && new_y == 7 && new_x == 2 && piece == 8 && !leftWhiteRookMoved
				&& !whiteKingHasMoved && Board.gameState[7][1] == 0 && Board.gameState[7][2] == 0
				&& Board.gameState[7][3] == 0) {
			Board.gameState[7][2] = 8;
			Board.gameState[7][3] = 12;
			Board.gameState[7][0] = 0;
			Board.gameState[7][4] = 0;
			return true;
		}
		// black short side castling
		else if (!Board.WhiteToMove && new_y == 0 && new_x == 6 && piece == 2 && !leftBlackRookMoved
				&& !blackKingHasMoved && Board.gameState[0][6] == 0 && Board.gameState[0][5] == 0) {
			Board.gameState[0][6] = 2;
			Board.gameState[0][5] = 6;
			Board.gameState[0][7] = 0;
			Board.gameState[0][4] = 0;
			return true;
		}
		// black long side castling
		else if (!Board.WhiteToMove && new_y == 0 && new_x == 2 && piece == 2 && !rightBlackRookMoved
				&& !blackKingHasMoved && Board.gameState[0][1] == 0 && Board.gameState[0][2] == 0
				&& Board.gameState[0][3] == 0) {
			Board.gameState[0][2] = 2;
			Board.gameState[0][3] = 6;
			Board.gameState[0][0] = 0;
			Board.gameState[0][4] = 0;
			return true;
		}
		return false;
	}

	public static boolean isSquareControlled(boolean WhiteToMove, int y, int x) {
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (WhiteToMove) {
					if (Board.gameState[i][j] < 7 && Board.gameState[i][j] > 0) { // black piece
						boolean isInCheck = isValidMove(Board.gameState[i][j], j, i, x, y, false);
						if (isInCheck)
							return true;
					}
				} else if (!WhiteToMove) {
					if (Board.gameState[i][j] > 6) { // white piece
						boolean isInCheck = isValidMove(Board.gameState[i][j], j, i, x, y, false);
						if (isInCheck)
							return true;
					}
				}
			}
		}
		return false;
	}

	public static ArrayList<Integer> returnMoves(int[][] gameState) {
		ArrayList<Integer> moveList = new ArrayList<Integer>();
		int moveCounter = 0;
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				if (Board.WhiteToMove && gameState[i][j] > 6) {
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							if (gameState[y][x] > 6) {
								continue;
							}
							if (isValidMove(gameState[i][j], j, i, x, y, false)) {
								moveCounter++;

							}

						}
					}

				} else if (!Board.WhiteToMove) {

				}
			}
		}

		return moveList;
	}

	public static int returnNumberOfMoves(int[][] gameState) {
		int moveCounter = 0;
		for (int i = 0; i < 8; ++i) {
			for (int j = 0; j < 8; ++j) {
				
				if (Board.WhiteToMove && gameState[i][j] > 6) {
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							if (gameState[y][x] > 6) {
								continue;
							}
							if (!Board.wouldKingBeInCheck(gameState, Board.WhiteToMove, j, i, x, y)) {
								if (isValidMove(gameState[i][j], j, i, x, y, false)) {
									
									moveCounter++;
	
								}
							}

						}
					}

				} else if (!Board.WhiteToMove && gameState[i][j] < 7 && gameState[i][j] > 0) {
					for (int x = 0; x < 8; x++) {
						for (int y = 0; y < 8; y++) {
							if (gameState[y][x] < 7 && gameState[y][x] > 0) {
								continue;
							}
							if (!Board.wouldKingBeInCheck(gameState, Board.WhiteToMove, j, i, x, y)) {		
								if (isValidMove(gameState[i][j], j, i, x, y, false)) {
									moveCounter++;

								}
							}

						}
					}
				}
			}
		}

		return moveCounter;
	}

}
