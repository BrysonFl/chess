package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import view.Board;
import view.Square;
import view.pieces.Pawn;
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
    
    private static boolean whitesMove = true;
    
    private static int generalMovements;
    private static int movementsWithoutCaptures;
    
    private static Square beforeSquare;
    
    public GameController(Board board) {
        this.board = board;
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
        
        currentSquare.setPiece(beforeSquare.getPiece());
        beforeSquare.setPiece(null);
        clearHighlights();
        generalMovements++;
        whitesMove = !whitesMove;
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

                board.getSquares()[row][col].setHighlight(true);
            }
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
