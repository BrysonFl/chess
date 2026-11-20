package rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import view.*;
import view.pieces.*;
import view.pieces.king.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

/**
 * Conjunto de pruebas unitarias para validar la lógica de movimiento, captura, estado
 * y recursos gráficos de todas las piezas de ajedrez ({@link Piece} y subclases).
 * <p>
 * Comprueba la cantidad de destinos válidos en un tablero vacío, la detención de piezas
 * deslizantes ({@link Rook}, {@link Bishop}, {@link Queen}) ante piezas aliadas o enemigas,
 * el salto del {@link Knight}, los límites de tablero del {@link King}, las reglas completas
 * del {@link Pawn} y la planificación y validación del enroque ({@link CastleMovement}).
 * </p>
 *
 * @author brysonfl
 * @see Piece
 * @see Pawn
 * @see King
 */
class PiecesTest {

    /**
     * Constructor por defecto para el conjunto de pruebas de piezas {@code PiecesTest}.
     */
    PiecesTest() {
    }

    /**
     * Obtiene el conjunto de coordenadas destino válidas para una pieza en una posición dada,
     * verificando que todos los destinos estén dentro de los límites del tablero y sin duplicados.
     *
     * @param p la pieza ({@link Piece}) cuyos movimientos válidos se van a calcular.
     * @param r la fila actual de la pieza en el tablero.
     * @param c la columna actual de la pieza en el tablero.
     * @param b el tablero ({@link Board}) en el que se evalúan los movimientos.
     * @return un {@link Set} de cadenas con el formato {@code "fila,columna"} que representan
     *         los destinos únicos permitidos.
     */
    static Set<String> moves(Piece p, int r, int c, Board b) {
        Set<String> result = new HashSet<>();

        for (Integer[] m : p.validMovements(r, c, b)) {
            assertTrue(m[0] >= 0 && m[0] < 8 && m[1] >= 0 && m[1] < 8);
            assertTrue(result.add(m[0] + "," + m[1]), "Destino duplicado");
        }

        return result;
    }

    /**
     * Verifica de forma parametrizada la cantidad exacta de casillas destino que puede alcanzar
     * cada pieza ({@link Bishop}, {@link Rook}, {@link Queen}, {@link Knight} y {@link King})
     * desde la casilla central {@code (3, 3)} en un tablero vacío.
     *
     * @param type  el identificador en texto del tipo de pieza a evaluar.
     * @param count el número total de movimientos válidos esperados desde {@code (3, 3)}.
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @ParameterizedTest @CsvSource({"bishop,13", "rook,14", "queen,27", "knight,8", "king,8"})
    void emptyBoardDestinations(String type, int count) throws Exception {
        onEdt(() -> {
            Piece p = switch (type) {
                case "bishop" -> new Bishop(true);
                case "rook" -> new Rook(true);
                case "queen" -> new Queen(true);
                case "knight" -> new Knight(true);
                default -> new King(true);
            };

            Board b = emptyBoard();
            b.getSquares()[3][3].setPiece(p);
            assertEquals(count, moves(p, 3, 3, b).size());
        });
    }

    /**
     * Comprueba que las piezas deslizantes ({@link Rook}, {@link Bishop} y {@link Queen})
     * detengan su trayectoria antes de una pieza aliada y puedan capturar la primera pieza
     * enemiga en su camino sin atravesarla.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void slidersStopAtFriendlyAndEnemyPieces() throws Exception {
        onEdt(() -> {
            for (Piece p : List.of(new Rook(true), new Bishop(true), new Queen(true))) {
                Board b = emptyBoard();
                b.getSquares()[3][3].setPiece(p);

                int dr = p instanceof Rook ? 0 : 1;
                b.getSquares()[3 + dr][4].setPiece(new Pawn(true));
                b.getSquares()[3 - dr][2].setPiece(new Pawn(false));

                Set<String> m = moves(p, 3, 3, b);
                assertFalse(m.contains((3 + dr) + ",4"));
                assertFalse(m.contains((3 + 2 * dr) + ",5"));
                assertTrue(m.contains((3 - dr) + ",2"));
                assertFalse(m.contains((3 - 2 * dr) + ",1"));
            }
        });
    }

    /**
     * Verifica que el {@link Knight} pueda saltar sobre otras piezas en la posición inicial
     * del tablero y que el {@link King} ubicado en una esquina solo genere destinos dentro
     * de los límites del tablero.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void knightJumpsAndKingStaysInsideBoard() throws Exception {
        onEdt(() -> {
            Board b = new Board(null);
            assertEquals(Set.of("5,0", "5,2"), moves(b.getSquares()[7][1].getPiece(), 7, 1, b));

            b = emptyBoard();
            assertEquals(Set.of("0,1", "1,0", "1,1"), moves(new King(true), 0, 0, b));
        });
    }

    /**
     * Comprueba para ambos colores ({@code true} para blancas, {@code false} para negras)
     * el avance simple y doble del {@link Pawn}, el bloqueo frontal, la captura diagonal
     * y las condiciones de activación y validación de la captura al paso (<i>en passant</i>).
     *
     * @param white {@code true} para evaluar un peón blanco; {@code false} para un peón negro.
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @ParameterizedTest @ValueSource(booleans = {true, false})
    void pawnAdvanceCaptureAndEnPassant(boolean white) throws Exception {
        onEdt(() -> {
            Board b = emptyBoard();
            Pawn p = new Pawn(white);

            int r = white ? 6 : 1;
            int d = white ? -1 : 1;

            b.getSquares()[r][3].setPiece(p);
            assertEquals(Set.of((r + d) + ",3", (r + 2 * d) + ",3"), moves(p, r, 3, b));

            b.getSquares()[r + d][3].setPiece(new Pawn(!white));
            assertTrue(moves(p, r, 3, b).isEmpty());

            b.getSquares()[r + d][4].setPiece(new Pawn(!white));
            assertEquals(Set.of((r + d) + ",4"), moves(p, r, 3, b));

            b = emptyBoard();
            r = white ? 3 : 4;
            b.getSquares()[r][3].setPiece(p);

            Square victim = b.getSquares()[r][4];
            victim.setPiece(new Pawn(!white));

            assertFalse(moves(p, r, 3, b).contains((r + d) + ",4"));

            b.setEnPassantSquare(victim);
            assertTrue(moves(p, r, 3, b).contains((r + d) + ",4"));
            assertTrue(p.validateIsPassant(b.getSquares()[r][3], b.getSquares()[r + d][4], false));

            b.setEnPassantSquare(null);
            assertFalse(moves(p, r, 3, b).contains((r + d) + ",4"));
        });
    }

    /**
     * Verifica de forma parametrizada las reglas de validación ({@link King#canCastle}) y la
     * generación del plan de enroque ({@link King#castleMovement}) tanto corto como largo
     * para ambos bandos, incluyendo restricciones por jaque, casillas ocupadas o piezas ya movidas.
     *
     * @param white   {@code true} si se evalúa el rey blanco; {@code false} para el rey negro.
     * @param rookCol la columna de la torre con la que se prueba el enroque ({@code 0} o {@code 7}).
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @ParameterizedTest @CsvSource({"true,0", "true,7", "false,0", "false,7"})
    void castleRulesAndPlan(boolean white, int rookCol) throws Exception {
        onEdt(() -> {
            Board b = emptyBoard();
            int r = white ? 7 : 0;
            King k = new King(white);
            Square origin = b.getSquares()[r][4];
            origin.setPiece(k);
            Rook rook = new Rook(white);
            b.getSquares()[r][rookCol].setPiece(rook);

            assertTrue(k.canCastle(origin, rookCol, b, s -> false, s -> true));
            assertFalse(k.canCastle(origin, rookCol, b, s -> true, s -> true));
            assertFalse(k.canCastle(origin, rookCol, b, s -> false, s -> false));

            int destination = rookCol == 7 ? 6 : 2;
            CastleMovement plan = k.castleMovement(origin, b.getSquares()[r][destination], b);

            assertSame(b.getSquares()[r][rookCol], plan.rookOrigin());
            assertSame(b.getSquares()[r][rookCol == 7 ? 5 : 3], plan.rookTarget());
            assertEquals(rookCol == 7 ? "O-O" : "O-O-O", plan.notation());
            assertSame(rook, plan.rookOrigin().getPiece(), "El plan no ejecuta la jugada");
            assertNull(k.castleMovement(origin, b.getSquares()[r - (white ? 1 : -1)][4], b));

            b.getSquares()[r][rookCol == 7 ? 5 : 3].setPiece(new Knight(white));
            assertFalse(k.canCastle(origin, rookCol, b, s -> false, s -> true));

            b.getSquares()[r][rookCol == 7 ? 5 : 3].setPiece(null);
            rook.markMoved();
            assertFalse(k.canCastle(origin, rookCol, b, s -> false, s -> true));

            b.getSquares()[r][rookCol].setPiece(new Rook(white));
            k.markMoved();
            assertFalse(k.canCastle(origin, rookCol, b, s -> false, s -> true));
        });
    }

    /**
     * Comprueba para todas las piezas de ambos colores su estado inicial de movimiento,
     * la carga y escalado de su icono gráfico, las reglas de captura ({@link Piece#canCapture(Piece)}),
     * el marcado de pieza movida ({@link Piece#markMoved()}) y su representación en texto.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void pieceStateCaptureAndResources() throws Exception {
        onEdt(() -> {
            for (boolean white : new boolean[]{true, false}) {
                for (Piece p : List.of(new Pawn(white), new Rook(white), new Knight(white), new Bishop(white), new Queen(white), new King(white))) {
                    assertEquals(white, p.isWhite());
                    assertFalse(p.hasMoved());
                    assertNotNull(p.getIcon());
                    assertEquals(80, p.getIcon().getIconWidth());
                    assertFalse(p.canCapture(null));
                    assertFalse(p.canCapture(new King(!white)));
                    assertFalse(p.canCapture(new Pawn(white)));
                    assertTrue(p.canCapture(new Pawn(!white)));

                    p.markMoved();
                    assertTrue(p.hasMoved());
                    assertNotNull(p.toString());
                }
            }
        });
    }
}
