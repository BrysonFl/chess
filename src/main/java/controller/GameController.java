package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import java.awt.GraphicsEnvironment;

import enums.PieceType;
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
    
    private boolean whitesMove = true;

    private int movementsWithoutCaptures;
    
    private Square beforeSquare;
    private boolean gameOver;
    private String result = "";
    private final Map<String, Integer> positions = new HashMap<>();

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
        if (gameOver) return;
        if (positions.isEmpty()) positions.put(positionKey(), 1);
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
     * @param currentSquare Es el cuadraro al que se le ha dado clic actualmente
     */
    private void movePiece(Square currentSquare) {
        String notation;
        Piece pieceMoved = beforeSquare.getPiece();

        boolean capture = currentSquare.getPiece() != null;
        boolean passant = pieceMoved instanceof Pawn && ((Pawn) pieceMoved).validateIsPassant(beforeSquare, currentSquare, capture);
        boolean castle = pieceMoved instanceof King && Math.abs(beforeSquare.getCol() - currentSquare.getCol()) == 2;

        notation = (pieceMoved instanceof Pawn ? "" : symbol(pieceMoved))
                + coordinate(beforeSquare) + (capture || passant ? "x" : "-") + coordinate(currentSquare);

        if (passant) {
            board.getSquares()[beforeSquare.getRow()][currentSquare.getCol()].setPiece(null);
        }

        if (castle) {
            int rookCol = currentSquare.getCol() == 6 ? 7 : 0;
            int targetCol = currentSquare.getCol() == 6 ? 5 : 3;
            Square rookSquare = board.getSquares()[beforeSquare.getRow()][rookCol];
            rookSquare.getPiece().markMoved();
            board.getSquares()[beforeSquare.getRow()][targetCol].setPiece(rookSquare.getPiece());
            rookSquare.setPiece(null);
            notation = rookCol == 7 ? "O-O" : "O-O-O";
        }

        board.setEnPassantSquare(pieceMoved instanceof Pawn
                && Math.abs(beforeSquare.getRow() - currentSquare.getRow()) == 2 ? currentSquare : null);

        currentSquare.setPiece(pieceMoved);
        beforeSquare.setPiece(null);
        pieceMoved.markMoved();

        if (pieceMoved instanceof Pawn && (currentSquare.getRow() == 0 || currentSquare.getRow() == 7)) {
            Piece promoted = ((Pawn) pieceMoved).choosePromotion(pieceMoved.isWhite(), board);
            promoted.markMoved();
            currentSquare.setPiece(promoted);
            notation += "=" + symbol(promoted);
        }

        movementsWithoutCaptures = pieceMoved instanceof Pawn || capture || passant ? 0 : movementsWithoutCaptures + 1;
        clearHighlights();
        whitesMove = !whitesMove;
        validateKingsCheck();
        positions.merge(positionKey(), 1, Integer::sum);
        updateGameState();

        if (movementsPanel != null) {
            movementsPanel.addMovement(notation + (result.startsWith("Jaque mate") ? "#" : inCheck(whitesMove) ? "+" : ""));
        }

        if (gameOver && !GraphicsEnvironment.isHeadless()) JOptionPane.showMessageDialog(board, result);

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
            List<Integer[]> pieceMovements = legalMovements(square);
        
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

    /** Obtiene los movimientos seguros y añade los enroques permitidos. */
    private List<Integer[]> legalMovements(Square square) {
        List<Integer[]> moves = square.getPiece().validMovements(square.getRow(), square.getCol(), board);
        moves.removeIf(move -> !isSafeMovement(square, board.getSquares()[move[0]][move[1]]));

        if (square.getPiece() instanceof King) {
            for (int col : new int[]{ 0, 7 }) {
                if (canCastle(square, col)) moves.add(new Integer[]{ square.getRow(), col == 7 ? 6 : 2 });
            }
        }

        return moves;
    }

    /** Valida el historial, las casillas libres y los ataques durante el enroque. */
    private boolean canCastle(Square king, int rookCol) {
        if (king.getPiece().hasMoved() || king.getCol() != 4
                || king.getRow() != (king.getPiece().isWhite() ? 7 : 0) || isKingInCheck(king)) return false;

        Piece rook = board.getSquares()[king.getRow()][rookCol].getPiece();

        if (!(rook instanceof Rook) || rook.hasMoved() || rook.isWhite() != king.getPiece().isWhite()) return false;
        int step = rookCol == 7 ? 1 : -1;

        for (int col = 4 + step; col != rookCol; col += step) {
            if (board.getSquares()[king.getRow()][col].getPiece() != null) return false;
        }

        return isSafeMovement(king, board.getSquares()[king.getRow()][4 + step])
                && isSafeMovement(king, board.getSquares()[king.getRow()][4 + 2 * step]);
    }

    /** Comprueba el jaque del bando indicado. */
    private boolean inCheck(boolean white) {
        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                if (square.getPiece() instanceof King && square.getPiece().isWhite() == white) return isKingInCheck(square);
            }
        }

        return false;
    }

    /** Da prioridad al mate antes de aplicar las tablas automáticas. */
    private void updateGameState() {
        boolean canMove = false;

        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                if (square.getPiece() != null && square.getPiece().isWhite() == whitesMove
                        && !legalMovements(square).isEmpty()) canMove = true;
            }
        }

        if (!canMove) finish(inCheck(whitesMove) ? "Jaque mate. Ganan las " + (whitesMove ? "negras." : "blancas.") : "Tablas por ahogado.");
        else if (insufficientMaterial()) finish("Tablas por material insuficiente.");
        else if (positions.getOrDefault(positionKey(), 0) >= 5) finish("Tablas por quíntuple repetición.");
        else if (movementsWithoutCaptures >= 150) finish("Tablas por 75 movimientos sin capturas ni movimientos de peón.");
    }

    /** Detecta finales sin posibilidad de mate con reyes, un menor o alfiles del mismo color. */
    private boolean insufficientMaterial() {
        int minors = 0;
        int knights = 0;
        int bishopColor = -1;
        boolean sameColor = true;

        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                Piece piece = square.getPiece();
                if (piece == null || piece instanceof King) continue;

                if (!(piece instanceof Bishop) && !(piece instanceof Knight)) return false;

                minors++;

                if (piece instanceof Knight) {
                    knights++;
                } else {
                    int color = (square.getRow() + square.getCol()) % 2;

                    if (bishopColor != -1 && bishopColor != color) sameColor = false;

                    bishopColor = color;
                }
            }
        }

        return minors <= 1 || knights == 0 && sameColor;
    }

    /** Reclama tablas cuando la posición actual cumple las condiciones reglamentarias. */
    public void claimDraw() {
        if (gameOver) return;

        if (positions.getOrDefault(positionKey(), 0) >= 3 || movementsWithoutCaptures >= 100) {
            finish("Tablas reclamadas por repetición o por 50 movimientos.");
        }

        if (!gameOver) {
            List<String> claims = new ArrayList<>();

            for (Square[] row : board.getSquares()) {
                for (Square origin : row) {
                    Piece piece = origin.getPiece();
                    if (piece == null || piece.isWhite() != whitesMove || piece instanceof Pawn) continue;

                    for (Integer[] move : legalMovements(origin)) {
                        Square target = board.getSquares()[move[0]][move[1]];

                        if (target.getPiece() != null) continue;
                        boolean fiftyMoves = movementsWithoutCaptures >= 99;
                        boolean repetition = false;

                        if (!(piece instanceof King || piece instanceof Rook) || piece.hasMoved()) {
                            Square passant = board.getEnPassantSquare();
                            origin.setPiece(null);
                            target.setPiece(piece);
                            board.setEnPassantSquare(null);
                            whitesMove = !whitesMove;

                            try {
                                repetition = positions.getOrDefault(positionKey(), 0) >= 2;
                            } finally {
                                whitesMove = !whitesMove;
                                board.setEnPassantSquare(passant);
                                target.setPiece(null);
                                origin.setPiece(piece);
                            }
                        }

                        if (fiftyMoves || repetition) claims.add(coordinate(origin) + "-" + coordinate(target));
                    }
                }
            }

            if (!claims.isEmpty() && !GraphicsEnvironment.isHeadless()) {
                Object intended = JOptionPane.showInputDialog(board,
                        "Selecciona la jugada que declaras para reclamar tablas:", "Reclamar tablas",
                        JOptionPane.QUESTION_MESSAGE, null, claims.toArray(), claims.getFirst());

                if (intended != null) finish("Tablas reclamadas con la jugada declarada " + intended + ".");
            }
        }

        if (!GraphicsEnvironment.isHeadless()) JOptionPane.showMessageDialog(board,
                gameOver ? result : "No se ha realizado una reclamación válida de tablas.");
    }

    /** Permite que el contrario acepte una propuesta de tablas. */
    public void offerDraw() {
        if (gameOver || GraphicsEnvironment.isHeadless()) return;

        int answer = JOptionPane.showConfirmDialog(board,
                "Las " + (whitesMove ? "blancas" : "negras") + " proponen tablas. ¿El contrario acepta?",
                "Propuesta de tablas", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) finish("Tablas por acuerdo.");
    }

    /** Termina la partida por abandono del jugador al turno. */
    public void resign() {
        if (!gameOver) finish("Abandono. Ganan las " + (whitesMove ? "negras." : "blancas."));
    }

    /** Guarda el resultado y bloquea las jugadas posteriores. */
    private void finish(String message) {
        gameOver = true;
        result = message;
        clearHighlights();
        beforeSquare = null;
        if (movementsPanel != null) movementsPanel.setGameStatus(message);
    }

    /** @return El resultado de la partida, o una cadena vacía si continúa. */
    public String getResult() {
        return result;
    }

    private String coordinate(Square square) {
        return "" + (char) ('a' + square.getCol()) + (8 - square.getRow());
    }

    private String symbol(Piece piece) {
        if (piece instanceof King) return PieceType.KING.getType();
        if (piece instanceof Queen) return PieceType.QUEEN.getType();
        if (piece instanceof Rook) return PieceType.ROOK.getType();
        if (piece instanceof Bishop) return PieceType.BISHOP.getType();
        if (piece instanceof Knight) return PieceType.KNIGHT.getType();
        return PieceType.PAWN.getType();
    }

    /** Identifica posición, turno, derechos de enroque y captura al paso legal. */
    private String positionKey() {
        StringBuilder key = new StringBuilder(whitesMove ? "w" : "b");

        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                Piece piece = square.getPiece();
                key.append(piece == null ? "." : piece.isWhite() ? symbol(piece) : symbol(piece).toLowerCase());
            }
        }

        for (int row : new int[]{ 0, 7 }) {
            Piece king = board.getSquares()[row][4].getPiece();

            for (int col : new int[]{ 0, 7 }) {
                Piece rook = board.getSquares()[row][col].getPiece();
                key.append(king instanceof King && !king.hasMoved() && king.isWhite() == (row == 7)
                        && rook instanceof Rook && !rook.hasMoved() && rook.isWhite() == king.isWhite() ? "1" : "0");
            }
        }

        Square passant = board.getEnPassantSquare();

        if (passant != null) {
            for (int col : new int[]{ passant.getCol() - 1, passant.getCol() + 1 }) {
                if (col < 0 || col > 7) continue;
                Square origin = board.getSquares()[passant.getRow()][col];
                if (origin.getPiece() instanceof Pawn && origin.getPiece().isWhite() == whitesMove) {
                    for (Integer[] move : legalMovements(origin)) {
                        if (move[1] == passant.getCol()) key.append("ep").append(coordinate(passant));
                    }
                }
            }
        }

        return key.toString();
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
