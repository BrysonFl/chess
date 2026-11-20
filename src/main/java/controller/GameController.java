package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import java.awt.GraphicsEnvironment;

import utils.UtilValidations;
import view.Board;
import view.MovementsPanel;
import view.Square;
import view.pieces.Bishop;
import view.pieces.king.CastleMovement;
import view.pieces.king.King;
import view.pieces.Knight;
import view.pieces.Pawn;
import view.pieces.Queen;
import view.pieces.Rook;
import view.pieces.Piece;

/**
 * Controlador principal encargado de gestionar las reglas, el flujo de turnos
 * y las interacciones del usuario sobre el tablero de ajedrez.
 * <p>
 * Implementa {@link ActionListener} para responder a los clics sobre las casillas
 * ({@link Square}), coordinando la selección de piezas, el resaltado y filtrado de
 * movimientos legales (evitando dejar al propio rey en jaque), la ejecución de
 * jugadas especiales (enroque, captura al paso y coronación de peones), el registro
 * de notación algebraica en {@link MovementsPanel} y la detección de condiciones
 * de fin de partida (jaque mate, ahogado, insuficiencia de material, repetición de
 * posiciones, regla de los 50/75 movimientos, acuerdo de tablas y abandono).
 * </p>
 *
 * @author brysonfl
 * @see Board
 * @see MovementsPanel
 * @see Square
 * @see Piece
 */
public class GameController implements ActionListener {
    
    /**
     * Referencia al tablero gráfico de ajedrez que contiene la cuadrícula de casillas y piezas.
     */
    private final Board board;

    /**
     * Panel lateral encargado de mostrar el historial de jugadas y el estado de la partida.
     */
    private final MovementsPanel movementsPanel;
    
    /**
     * Indica de qué bando es el turno actual: {@code true} para las blancas, {@code false} para las negras.
     */
    private boolean whitesMove = true;

    /**
     * Contador de medios movimientos (<i>plies</i>) consecutivos realizados sin capturas ni avances de peón,
     * utilizado para las reglas de tablas de los 50 y 75 movimientos.
     */
    private int movementsWithoutCaptures;
    
    /**
     * Casilla seleccionada previamente como origen de un posible movimiento.
     */
    private Square beforeSquare;

    /**
     * Indica si la partida ha finalizado ({@code true}) y no admite más movimientos.
     */
    private boolean gameOver;

    /**
     * Mensaje descriptivo con el resultado final de la partida, o una cadena vacía si sigue en curso.
     */
    private String result = "";

    /**
     * Registro de frecuencias de cada posición alcanzada en la partida, indexado por la clave
     * generada en {@link #positionKey()}, para evaluar la triple y quíntuple repetición.
     */
    private final Map<String, Integer> positions = new HashMap<>();

    /**
     * Construye un nuevo controlador de juego asociándolo al tablero y al panel de movimientos.
     *
     * @param board          el tablero de ajedrez ({@link Board}) sobre el que se desarrolla la partida.
     * @param movementsPanel el panel lateral ({@link MovementsPanel}) donde se registran las jugadas
     *                       y se actualiza el estado de la partida (puede ser {@code null} en entornos de prueba).
     */
    public GameController(Board board, MovementsPanel movementsPanel) {
        this.board = board;
        this.movementsPanel = movementsPanel;
    }

    /**
     * Maneja los eventos de acción generados al hacer clic sobre las casillas del tablero.
     * <p>
     * Si la partida aún no ha terminado, evalúa la casilla pulsada ({@link Square}):
     * </p>
     * <ul>
     *   <li>Si existe una casilla de origen seleccionada ({@code beforeSquare}) y la casilla actual
     *       está resaltada como destino válido, ejecuta el movimiento con {@link #movePiece(Square)}.</li>
     *   <li>Si la casilla pulsada contiene una pieza, valida y resalta sus movimientos legales
     *       mediante {@link #validatePieceMovements(Square)} y la almacena como nueva casilla de origen.</li>
     *   <li>Si se pulsa una casilla vacía que no está resaltada, limpia cualquier selección o
     *       resaltado previo con {@link #clearHighlights()}.</li>
     * </ul>
     *
     * @param e el evento de acción ({@link ActionEvent}) cuya fuente ({@link ActionEvent#getSource()})
     *          es la instancia de {@link Square} donde ocurrió el clic.
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
     * Ejecuta el desplazamiento de una pieza desde la casilla de origen ({@code beforeSquare})
     * hasta la casilla de destino indicada.
     * <p>
     * Este método gestiona de forma integral las reglas especiales de movimiento (captura al paso,
     * enroque corto/largo y coronación de peones), actualiza las referencias de las piezas en el
     * tablero, reinicia o incrementa el contador de movimientos sin capturas, alterna el turno al
     * bando contrario, verifica el estado de jaque de los reyes, registra la nueva posición en el
     * historial de repeticiones, evalúa si la partida ha terminado y añade la jugada en notación
     * algebraica al {@link MovementsPanel}.
     * </p>
     *
     * @param currentSquare la casilla de destino ({@link Square}) seleccionada por el jugador
     *                      para completar el movimiento.
     */
    private void movePiece(Square currentSquare) {
        String notation;
        Piece pieceMoved = beforeSquare.getPiece();

        boolean capture = currentSquare.getPiece() != null;
        boolean passant = pieceMoved instanceof Pawn && ((Pawn) pieceMoved).validateIsPassant(beforeSquare, currentSquare, capture);

        CastleMovement castle = pieceMoved instanceof King king
                ? king.castleMovement(beforeSquare, currentSquare, board) : null;

        notation = UtilValidations.generateCoordinate(pieceMoved, currentSquare, capture, passant);

        if (passant) {
            board.getSquares()[beforeSquare.getRow()][currentSquare.getCol()].setPiece(null);
        }

        if (castle != null) {
            Piece rook = castle.rookOrigin().getPiece();
            rook.markMoved();
            castle.rookTarget().setPiece(rook);
            castle.rookOrigin().setPiece(null);
            notation = castle.notation();
        }

        board.setEnPassantSquare(pieceMoved instanceof Pawn && Math.abs(beforeSquare.getRow() - currentSquare.getRow()) == 2 ? currentSquare : null);

        currentSquare.setPiece(pieceMoved);
        beforeSquare.setPiece(null);
        pieceMoved.markMoved();

        if (pieceMoved instanceof Pawn && (currentSquare.getRow() == 0 || currentSquare.getRow() == 7)) {
            Piece promoted = ((Pawn) pieceMoved).choosePromotion(pieceMoved.isWhite(), board);
            promoted.markMoved();
            currentSquare.setPiece(promoted);
            notation += "=" + promoted.getPieceType();
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
     * Recorre todas las casillas del tablero y actualiza el indicador visual de jaque
     * ({@link Square#setCheck(boolean)}) en aquellas casillas ocupadas por un {@link King}
     * que se encuentre amenazado por piezas adversarias.
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
     * Determina si el rey ubicado en la casilla especificada se encuentra bajo ataque
     * directo (en jaque) por alguna pieza del bando contrario.
     * <p>
     * Evalúa los patrones geométricos de ataque de peones, caballos, reyes y piezas
     * deslizantes (alfiles, torres y damas), verificando para estas últimas que no
     * existan piezas intermedias bloqueando la trayectoria.
     * </p>
     *
     * @param kingSquare la casilla ({@link Square}) donde se ubica el rey que se desea evaluar.
     * @return {@code true} si al menos una pieza enemiga amenaza la casilla del rey;
     *         {@code false} en caso contrario.
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

                switch (piece) {
                    case Pawn _ -> {
                        if (rowDistance == (piece.isWhite() ? -1 : 1) && cols == 1) return true;
                    }

                    case Knight _ -> {
                        if ((rows == 2 && cols == 1) || (rows == 1 && cols == 2)) return true;
                    }

                    case King _ -> {
                        if (rows <= 1 && cols <= 1) return true;
                    }

                    default -> {
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
        }

        return false;
    }

    /**
     * Valida y resalta visualmente en el tablero todas las casillas de destino legales
     * disponibles para la pieza situada en la casilla indicada, siempre que corresponda
     * al turno del bando propietario de dicha pieza.
     *
     * @param square la casilla ({@link Square}) que contiene la pieza cuyos movimientos
     *               legales se desean calcular y resaltar.
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
     * Simula temporalmente un movimiento entre una casilla de origen y una de destino
     * para comprobar que dicha jugada no deje ni mantenga al propio rey en estado de jaque.
     * <p>
     * Contempla tanto desplazamientos y capturas ordinarias como la retirada temporal del
     * peón adversario en una captura al paso, restaurando siempre el estado original del
     * tablero en el bloque {@code finally}.
     * </p>
     *
     * @param origin la casilla de origen ({@link Square}) donde se encuentra inicialmente la pieza.
     * @param target la casilla de destino ({@link Square}) hacia la que se pretende mover la pieza.
     * @return {@code true} si tras efectuar el movimiento el rey del bando que mueve queda libre
     *         de jaque; {@code false} si el rey quedaría amenazado.
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
     * Obtiene la lista de coordenadas de todos los movimientos estrictamente legales para la
     * pieza situada en la casilla especificada.
     * <p>
     * Filtra los movimientos candidatos de la pieza descartando aquellos que dejarían al propio
     * rey en jaque ({@link #isSafeMovement(Square, Square)}) e incorpora, en caso de tratarse de
     * un {@link King}, los movimientos de enroque corto y largo que resulten reglamentarios.
     * </p>
     *
     * @param square la casilla ({@link Square}) cuya pieza se desea consultar.
     * @return una lista de arreglos {@code Integer[]} de dos elementos ({@code [fila, columna]})
     *         con las posiciones de destino legales.
     */
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

    /**
     * Verifica si el rey ubicado en la casilla de origen puede realizar legalmente el enroque
     * con la torre situada en la columna especificada.
     * <p>
     * Delega la comprobación estructural en {@link King#canCastle} proporcionando predicados
     * para evaluar si el rey está actualmente en jaque y si las casillas de paso son seguras.
     * </p>
     *
     * @param origin  la casilla ({@link Square}) donde se encuentra el rey.
     * @param rookCol el índice de la columna de la torre con la que se intenta enrocar
     *                ({@code 0} para enroque largo, {@code 7} para enroque corto).
     * @return {@code true} si se cumplen todas las condiciones reglamentarias para enrocar;
     *         {@code false} en caso contrario.
     */
    private boolean canCastle(Square origin, int rookCol) {
        return origin.getPiece() instanceof King king
                && king.canCastle(origin, rookCol, board, this::isKingInCheck,
                        target -> isSafeMovement(origin, target));
    }

    /**
     * Comprueba si el rey del bando especificado se encuentra actualmente en jaque.
     *
     * @param white {@code true} para consultar el estado del rey blanco;
     *              {@code false} para consultar el del rey negro.
     * @return {@code true} si el rey del bando indicado está en jaque;
     *         {@code false} si no está amenazado o no se encuentra en el tablero.
     */
    private boolean inCheck(boolean white) {
        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                if (square.getPiece() instanceof King && square.getPiece().isWhite() == white) return isKingInCheck(square);
            }
        }

        return false;
    }

    /**
     * Evalúa el estado global del tablero tras cada movimiento para determinar si la partida
     * ha concluido automáticamente.
     * <p>
     * Otorga prioridad a la falta de movimientos legales (declarando jaque mate si el jugador
     * al turno está en jaque, o tablas por rey ahogado en caso contrario), y posteriormente
     * verifica las condiciones de tablas automáticas: material insuficiente, quíntuple repetición
     * de posición y regla de los 75 movimientos (150 medios movimientos) sin capturas ni avances
     * de peón.
     * </p>
     */
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

    /**
     * Determina si las piezas restantes en el tablero constituyen material insuficiente
     * para que cualquiera de los dos bandos pueda alcanzar un jaque mate legal.
     * <p>
     * Considera como material insuficiente los finales de:
     * </p>
     * <ul>
     *   <li>Rey contra rey.</li>
     *   <li>Rey y una única pieza menor (alfil o caballo) contra rey.</li>
     *   <li>Reyes y uno o más alfiles cuando todos los alfiles presentes se desplazan
     *       por casillas del mismo color, sin presencia de caballos ni otras piezas.</li>
     * </ul>
     *
     * @return {@code true} si ningún bando dispone de material suficiente para dar jaque mate;
     *         {@code false} en caso contrario.
     */
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

    /**
     * Procesa la solicitud del jugador actual para reclamar tablas reglamentarias por
     * triple repetición de posición o por la regla de los 50 movimientos (100 medios movimientos).
     * <p>
     * Si la condición ya se cumple en la posición presente del tablero, finaliza la partida
     * inmediatamente. Si la condición se alcanzaría mediante una jugada legal del jugador al
     * turno, permite seleccionar y declarar dicha jugada a través de un cuadro de diálogo
     * (en entornos gráficos no <i>headless</i>) para formalizar las tablas.
     * </p>
     */
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

                        if (fiftyMoves || repetition) claims.add(UtilValidations.coordinate(origin) + "-" + UtilValidations.coordinate(target));
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

    /**
     * Presenta un cuadro de diálogo de confirmación para ofrecer tablas de mutuo acuerdo
     * al jugador contrario.
     * <p>
     * Si la partida está en curso, el entorno no es <i>headless</i> y el oponente acepta la
     * propuesta ({@link JOptionPane#YES_OPTION}), la partida concluye en tablas por acuerdo.
     * </p>
     */
    public void offerDraw() {
        if (gameOver || GraphicsEnvironment.isHeadless()) return;

        int answer = JOptionPane.showConfirmDialog(board,
                "Las " + (whitesMove ? "blancas" : "negras") + " proponen tablas. ¿El contrario acepta?",
                "Propuesta de tablas", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) finish("Tablas por acuerdo.");
    }

    /**
     * Finaliza la partida en curso declarando el abandono (rendición) del jugador que
     * tiene el turno actual y otorgando la victoria al bando adversario.
     */
    public void resign() {
        if (!gameOver) finish("Abandono. Ganan las " + (whitesMove ? "negras." : "blancas."));
    }

    /**
     * Marca la partida como terminada, almacena el mensaje con el resultado, limpia
     * los resaltados y selecciones activas del tablero y notifica el estado final
     * al panel de movimientos ({@link MovementsPanel}).
     *
     * @param message el texto descriptivo con el motivo de finalización y el resultado de la partida.
     */
    private void finish(String message) {
        gameOver = true;
        result = message;
        clearHighlights();
        beforeSquare = null;
        if (movementsPanel != null) movementsPanel.setGameStatus(message);
    }

    /**
     * Obtiene el texto descriptivo con el resultado de la partida.
     *
     * @return una cadena con el resultado final si la partida ha concluido (por ejemplo,
     *         {@code "Jaque mate. Ganan las blancas."}), o una cadena vacía ({@code ""})
     *         si la partida aún continúa en juego.
     */
    public String getResult() {
        return result;
    }

    /**
     * Genera una clave única en formato de texto que codifica el estado completo de la
     * posición actual conforme a las reglas de repetición de la FIDE.
     * <p>
     * La clave incluye el bando al turno ({@code "w"} o {@code "b"}), la disposición de
     * todas las piezas en las 64 casillas, los derechos vigentes de enroque para ambos
     * reyes y torres, y la disponibilidad real de una captura al paso estrictamente legal.
     * </p>
     *
     * @return una cadena de texto que identifica unívocamente la posición actual del tablero.
     */
    private String positionKey() {
        StringBuilder key = new StringBuilder(whitesMove ? "w" : "b");

        for (Square[] row : board.getSquares()) {
            for (Square square : row) {
                Piece piece = square.getPiece();
                key.append(piece == null ? "." : piece.isWhite() ? piece.getPieceType() : piece.getPieceType().toLowerCase());
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
                        if (move[1] == passant.getCol()) key.append("ep").append(UtilValidations.coordinate(passant));
                    }
                }
            }
        }

        return key.toString();
    }

    /**
     * Desactiva el resaltado visual de movimiento válido en todas las casillas del tablero,
     * estableciendo su estado {@link Square#setHighlight(boolean)} en {@code false}.
     */
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board.getSquares()[row][col].setHighlight(false);
            }
        }
    }
   
}
