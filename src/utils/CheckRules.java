package utils;

import enums.PieceType;
import view.Board;
import view.Square;
import view.pieces.Piece;

/** Detección de ataques independiente de los movimientos legales (evita recursión). */
public final class CheckRules {
    private CheckRules() { }

    private static Piece[][] position(Board board) {
        Piece[][] position = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) position[r][c] = board.getSquares()[r][c].getPiece();
        }
        return position;
    }

    public static boolean isInCheck(Board board, boolean white) {
        return isInCheck(position(board), white);
    }

    private static boolean isInCheck(Piece[][] position, boolean white) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = position[r][c];
                if (piece != null && piece.getPieceType() == PieceType.KING && piece.isWhite() == white) {
                    return isAttacked(position, r, c, !white);
                }
            }
        }
        return false;
    }

    private static boolean isAttacked(Piece[][] position, int row, int col, boolean white) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = position[r][c];
                if (piece == null || piece.isWhite() != white) continue;
                int dr = row - r, dc = col - c;
                boolean attacks = switch (piece.getPieceType()) {
                    case PAWN -> dr == (white ? -1 : 1) && Math.abs(dc) == 1;
                    case KNIGHT -> Math.abs(dr) * Math.abs(dc) == 2;
                    case KING -> Math.max(Math.abs(dr), Math.abs(dc)) == 1;
                    case BISHOP -> Math.abs(dr) == Math.abs(dc);
                    case ROOK -> dr == 0 || dc == 0;
                    case QUEEN -> dr == 0 || dc == 0 || Math.abs(dr) == Math.abs(dc);
                };
                if (!attacks) continue;
                if (piece.getPieceType() == PieceType.PAWN || piece.getPieceType() == PieceType.KNIGHT
                        || piece.getPieceType() == PieceType.KING) return true;
                int stepR = Integer.signum(dr), stepC = Integer.signum(dc);
                int scanR = r + stepR, scanC = c + stepC;
                while (scanR != row || scanC != col) {
                    if (position[scanR][scanC] != null) break;
                    scanR += stepR;
                    scanC += stepC;
                }
                if (scanR == row && scanC == col) return true;
            }
        }
        return false;
    }

    /** Simula en una copia para no alterar iconos, selección ni historial. */
    public static boolean leavesKingSafe(Board board, Square from, Square to) {
        Piece[][] position = position(board);
        Piece moved = position[from.getRow()][from.getCol()];
        Piece captured = position[to.getRow()][to.getCol()];
        if (captured != null && captured.getPieceType() == PieceType.KING) return false;
        if (moved.getPieceType() == PieceType.PAWN && from.getCol() != to.getCol() && captured == null) {
            position[from.getRow()][to.getCol()] = null;
        }
        position[to.getRow()][to.getCol()] = moved;
        position[from.getRow()][from.getCol()] = null;
        return !isInCheck(position, moved.isWhite());
    }

    public static void updateIndicators(Board board) {
        boolean whiteCheck = isInCheck(board, true);
        boolean blackCheck = isInCheck(board, false);
        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                Piece piece = square.getPiece();
                square.setInCheck(piece != null && piece.getPieceType() == PieceType.KING
                        && (piece.isWhite() ? whiteCheck : blackCheck));
            }
        }
    }
}
