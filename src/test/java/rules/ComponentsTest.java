package rules;

import org.junit.jupiter.api.Test;
import enums.PieceType;
import exceptions.NotFoundException;
import utils.UtilValidations;
import view.*;
import view.buttons.*;
import view.pieces.*;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

/**
 * Pruebas unitarias para los componentes visuales, utilidades de validación, enumeraciones
 * y excepciones del proyecto de ajedrez.
 * <p>
 * Verifica el estado inicial y actualización de {@link Board} y {@link Square}, el registro
 * de jugadas en {@link MovementsPanel}, el comportamiento y estilo de los botones de acción
 * ({@link ButtonBase} y subclases), la generación de coordenadas en {@link UtilValidations},
 * los valores de {@link PieceType} y la excepción {@link NotFoundException}.
 * </p>
 *
 * @author brysonfl
 * @see Board
 * @see MovementsPanel
 * @see UtilValidations
 */
class ComponentsTest {

    /**
     * Constructor por defecto para el conjunto de pruebas de componentes {@code ComponentsTest}.
     */
    ComponentsTest() {
    }

    /**
     * Busca recursivamente dentro de un contenedor AWT/Swing el primer componente hijo
     * que sea instancia del tipo especificado.
     *
     * @param <T>  el tipo del componente buscado.
     * @param root el contenedor raíz ({@link Container}) desde donde iniciar la búsqueda.
     * @param type la clase ({@link Class}) del componente que se desea localizar.
     * @return la primera instancia encontrada de tipo {@code T}, o {@code null} si no existe ninguna.
     */
    static <T> T find(Container root,Class<T> type) {
        for(Component c:root.getComponents()) {
            if(type.isInstance(c)) return type.cast(c);

            if(c instanceof Container container) {
                T result = find(container,type); if(result!=null) return result;
            }
        }
        return null;
    }

    /**
     * Comprueba que un nuevo {@link Board} inicialice sus 64 casillas ({@link Square}) con
     * sus coordenadas y oyentes de acción, ubique las 32 piezas iniciales y actualice
     * adecuadamente los iconos y el estado de resaltado al modificar una casilla.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void boardInitialStateAndSquareUpdates() throws Exception {
        onEdt(() -> {
            Board b=new Board(null);int count=0;
            for(int r=0;r<8;r++) for(int c=0;c<8;c++) {
                Square s=b.getSquares()[r][c];assertEquals(r,s.getRow());assertEquals(c,s.getCol());
                assertEquals(1,s.getActionListeners().length);
                if(s.getPiece()!=null) count++;
            }
            assertEquals(32,count);assertNull(b.getEnPassantSquare());
            Square s=b.getSquares()[4][4]; Piece p=new Queen(true); s.setPiece(p);
            assertSame(p,s.getPiece());assertSame(p.getIcon(),s.getIcon());
            s.setHighlight(true);assertTrue(s.isHighlight());s.setHighlight(false);assertFalse(s.isHighlight());
            s.setPiece(null);assertNull(s.getIcon());
        });
    }

    /**
     * Verifica que {@link MovementsPanel} agrupe los movimientos de blancas y negras por turno
     * en filas de la tabla ({@link JTable}) y que sus celdas sean de solo lectura.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void historyPairsWhiteAndBlackMovesAndIsReadOnly() throws Exception {
        onEdt(() -> {
            MovementsPanel panel=new MovementsPanel();
            panel.addMovement("e4");panel.addMovement("e5");panel.addMovement("Nf3");
            JTable table=find(panel,JTable.class);assertNotNull(table);
            assertEquals(2,table.getRowCount());assertEquals("1.",table.getValueAt(0,0));
            assertEquals("e4",table.getValueAt(0,1));assertEquals("e5",table.getValueAt(0,2));
            assertEquals("Nf3",table.getValueAt(1,1));assertNull(table.getValueAt(1,2));
            assertFalse(table.isCellEditable(0,1));
        });
    }

    /**
     * Comprueba que los botones {@link DrawButton} y {@link ClaimDrawButton} invoquen las
     * acciones correspondientes en {@link GameController} y que todas las subclases de
     * {@link ButtonBase} hereden el estilo visual y cursor de mano configurados.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void buttonsDispatchControllerActionsAndInheritStyle() throws Exception {
        onEdt(() -> {
            class Spy extends GameController {
                int offers,claims;
                Spy(){super(new Board(null),null);}
                @Override public void offerDraw(){offers++;}
                @Override public void claimDraw(){claims++;}
            }
            Spy spy=new Spy();DrawButton draw=new DrawButton("Tablas");ClaimDrawButton claim=new ClaimDrawButton("Reclamar");
            draw.configureAction(spy);claim.configureAction(spy);draw.doClick(0);claim.doClick(0);
            assertEquals(1,spy.offers);assertEquals(1,spy.claims);
            for(ButtonBase b:new ButtonBase[]{draw,claim,new ResignButton("Abandonar")}) {
                assertEquals(Cursor.HAND_CURSOR,b.getCursor().getType());assertTrue(b.getPreferredSize().height>0);
            }
        });
    }

    /**
     * Verifica la conversión de casillas a notación algebraica, la validación de límites
     * del tablero y la generación de coordenadas de jugadas en {@link UtilValidations}.
     *
     * @throws Exception si ocurre un error al ejecutar la prueba en el EDT.
     */
    @Test void coordinatesAndBounds() throws Exception {
        onEdt(() -> {
            assertEquals("a8",UtilValidations.coordinate(new Square(0,0)));
            assertEquals("h1",UtilValidations.coordinate(new Square(7,7)));
            assertTrue(UtilValidations.validateBoard(0,0));assertTrue(UtilValidations.validateBoard(7,7));
            for(int invalid:new int[]{-1,8}) {
                assertFalse(UtilValidations.validateBoard(invalid,3));assertFalse(UtilValidations.validateBoard(3,invalid));
            }
            assertEquals("e4",UtilValidations.generateCoordinate(new Pawn(true),new Square(4,4),false,false));
            assertEquals("Nf3",UtilValidations.generateCoordinate(new Knight(true),new Square(5,5),false,false));
        });
    }

    /**
     * Comprueba que los tipos de pieza definidos en {@link PieceType} conserven sus abreviaturas
     * y valores numéricos reglamentarios, y que {@link NotFoundException} preserve su mensaje de error.
     */
    @Test void pieceTypesAndExceptionPreserveTheirData() {
        assertEquals(6,PieceType.values().length);
        assertEquals("P",PieceType.PAWN.getType()); assertEquals(1,PieceType.PAWN.getValue());
        assertEquals(5,PieceType.ROOK.getValue());assertEquals(3,PieceType.KNIGHT.getValue());
        assertEquals(3,PieceType.BISHOP.getValue());assertEquals(9,PieceType.QUEEN.getValue());assertEquals(0,PieceType.KING.getValue());
        assertEquals("Imagen ausente",new NotFoundException("Imagen ausente").getMessage());
    }
}
