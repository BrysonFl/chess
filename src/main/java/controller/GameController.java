package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import view.Board;
import view.MovementsPanel;
import view.Square;
import view.pieces.Bishop;
import view.pieces.King;
import view.pieces.Knight;
import view.pieces.Pawn;
import view.pieces.Queen;
import view.pieces.Rook;
import view.pieces.Piece;

/**
 * Esta clase nos permite hacer la validación de la lógica del juego, 
 * controlando las acciones de los jugadores, los turnos, el movimiento de 
 * las piezas y el estado visual del tablero de ajedrez.
 * 
 * @author brysonfl
 */
public class GameController implements ActionListener {
    
    private final Board board;
    private final MovementsPanel movementsPanel;
    
    private static boolean whitesMove = true;
    
    private static int generalMovements;
    private static int movementsWithoutCaptures;
    
    private static Square beforeSquare;
    
    public GameController(Board board) {
        this(board, null);
    }

    public GameController(Board board, MovementsPanel movementsPanel) {
        this.board = board;
        this.movementsPanel = movementsPanel;
    }

    /**
     * Maneja los eventos de acción generados al interactuar con las casillas del tablero. 
     * Controla la selección de piezas, la validación de sus movimientos permitidos, 
     * la ejecución de dichos movimientos cuando se selecciona una casilla resaltada, 
     * y la limpieza de la selección si se hace clic en una casilla vacía no válida.
     * 
     * @param e El evento de acción que contiene la fuente (casilla) donde ocurrió el clic.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Square currentSquare = (Square) e.getSource();
        
        if (beforeSquare != null && currentSquare.isHighlight()) {
            movePiece(currentSquare);
            beforeSquare = null;
            return;
        }
        
        if (currentSquare.getPiece() != null) {
            validatePieceMovements(currentSquare);
            beforeSquare = currentSquare;
        }
        
        if (currentSquare.getPiece() == null && !currentSquare.isHighlight()) {
            clearHighlights();
            beforeSquare = null;
        }
    }
    
    /**
     * Mueve una pieza desde la casilla de origen (beforeSquare) hasta la casilla de destino 
     * especificada, gestionando reglas especiales del juego (como la captura al paso en los peones),
     * actualizando el estado del tablero, incrementando el contador de movimientos y cambiando el turno.
     * 
     * @param currentSquare 
     */
    private void movePiece(Square currentSquare) {
        Piece pieceMoved = beforeSquare.getPiece();
        
        if (pieceMoved instanceof Pawn) {
            if (beforeSquare.getCol() != currentSquare.getCol() && currentSquare.getPiece() == null) {
                board.getSquares()[beforeSquare.getRow()][currentSquare.getCol()].setPiece(null);
            }
        }
        
        if (movementsPanel != null) {
            movementsPanel.addMovement(beforeSquare, currentSquare);
        }
        
        board.setEnPassantSquare(pieceMoved instanceof Pawn
                && Math.abs(beforeSquare.getRow() - currentSquare.getRow()) == 2 ? currentSquare : null);
        currentSquare.setPiece(beforeSquare.getPiece());
        beforeSquare.setPiece(null);
        clearHighlights();
        generalMovements++;
        whitesMove = !whitesMove;
        validateKingsCheck();
    }
    
    /**
     * Actualiza el destello de los reyes según los ataques de las piezas contrarias.
     */
    private void validateKingsCheck() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board.getSquares()[row][col];
                square.setCheck(square.getPiece() instanceof King && isKingInCheck(square));
            }
        }
    }

    /**
     * Comprueba si alguna pieza contraria amenaza la casilla del rey.
     *
     * @param kingSquare La casilla donde se encuentra el rey.
     * @return {@code true} si el rey está en jaque.
     */
    private boolean isKingInCheck(Square kingSquare) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getSquares()[row][col].getPiece();
                if (piece == null || piece.isWhite() == kingSquare.getPiece().isWhite()) continue;

                int rowDistance = kingSquare.getRow() - row;
                int colDistance = kingSquare.getCol() - col;
                int rows = Math.abs(rowDistance);
                int cols = Math.abs(colDistance);

                if (piece instanceof Pawn) {
                    if (rowDistance == (piece.isWhite() ? -1 : 1) && cols == 1) return true;
                } else if (piece instanceof Knight) {
                    if ((rows == 2 && cols == 1) || (rows == 1 && cols == 2)) return true;
                } else if (piece instanceof King) {
                    if (rows <= 1 && cols <= 1) return true;
                } else {
                    boolean diagonal = rows == cols;
                    boolean straight = rowDistance == 0 || colDistance == 0;
                    boolean canAttack = (piece instanceof Bishop && diagonal)
                            || (piece instanceof Rook && straight)
                            || (piece instanceof Queen && (diagonal || straight));

                    if (!canAttack) continue;

                    int rowStep = Integer.signum(rowDistance);
                    int colStep = Integer.signum(colDistance);
                    int targetRow = row + rowStep;
                    int targetCol = col + colStep;

                    while (targetRow != kingSquare.getRow() || targetCol != kingSquare.getCol()) {
                        if (board.getSquares()[targetRow][targetCol].getPiece() != null) break;
                        targetRow += rowStep;
                        targetCol += colStep;
                    }

                    if (targetRow == kingSquare.getRow() && targetCol == kingSquare.getCol()) return true;
                }
            }
        }
        return false;
    }

    /**
     * Valida y resalta en el tablero los movimientos legales disponibles para la pieza 
     * ubicada en la casilla especificada, siempre y cuando sea el turno del jugador 
     * dueño de dicha pieza.
     * 
     * @param square La casilla que contiene la pieza cuyos movimientos se van a validar y resaltar.
     */
    private void validatePieceMovements(Square square) {
        clearHighlights();
        
        boolean rightTurn = (whitesMove && square.getPiece().isWhite()) || (!whitesMove && !square.getPiece().isWhite());
        
        if (rightTurn) {
            List<Integer[]> pieceMovements = square.getPiece()
                .validMovements(square.getRow(), square.getCol(), this.board);
        
            for (Integer[] move : pieceMovements) {
                int row = move[0];
                int col = move[1];

                if (isSafeMovement(square, board.getSquares()[row][col])) {
                    board.getSquares()[row][col].setHighlight(true);
                }
            }
        }
        
    }
    
    /**
     * Simula el movimiento para evitar dejar al propio rey en jaque.
     * Restaura las piezas después de comprobar la posición, incluida la captura al paso.
     *
     * @param origin La casilla de origen.
     * @param target La casilla de destino.
     * @return {@code true} si el movimiento no deja al rey amenazado.
     */
    private boolean isSafeMovement(Square origin, Square target) {
        Piece moved = origin.getPiece();
        Piece captured = target.getPiece();
        Square passant = null;
        Piece passantPiece = null;
        if (moved instanceof Pawn && origin.getCol() != target.getCol() && captured == null) {
            passant = board.getSquares()[origin.getRow()][target.getCol()];
            passantPiece = passant.getPiece();
            passant.setPiece(null);
        }
        origin.setPiece(null);
        target.setPiece(moved);
        try {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square square = board.getSquares()[row][col];
                    if (square.getPiece() instanceof King && square.getPiece().isWhite() == moved.isWhite()
                            && isKingInCheck(square)) return false;
                }
            }
            return true;
        } finally {
            origin.setPiece(moved);
            target.setPiece(captured);
            if (passant != null) passant.setPiece(passantPiece);
        }
    }

    /**
     * Limpia y desactiva el resaltado de todas las casillas del tablero, 
     * estableciendo su propiedad de highlight en falso.
     */
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getSquares()[row][col].setHighlight(false);
            }
        }
    }
   
}
