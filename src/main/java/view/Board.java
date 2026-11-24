package view;

import controller.GameController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import java.awt.GridLayout;
import javax.swing.JPanel;
import view.pieces.Bishop;
import view.pieces.king.King;
import view.pieces.Knight;
import view.pieces.Pawn;
import view.pieces.Queen;
import view.pieces.Rook;

/**
 * Representa el tablero gráfico de ajedrez donde se desarrolla la partida.
 * <p>
 * Esta clase extiende {@link JPanel} y gestiona la cuadrícula de {@code 8x8} casillas
 * ({@link Square}), el renderizado de las coordenadas algebraicas en los márgenes
 * (filas {@code 1-8} a la izquierda y columnas {@code a-h} en la parte inferior),
 * la disposición inicial de las piezas blancas y negras, y el seguimiento de la casilla
 * vulnerable a la captura al paso (<i>en passant</i>).
 * </p>
 *
 * <p>La matriz utiliza la orientación de las blancas: fila 0 para la octava línea
 * y columna 0 para la columna a. Conserva tanto la presentación como la ocupación
 * de las casillas; el controlador aplica las reglas de la partida.</p>
 *
 * @author brysonfl
 * @see Square
 * @see MovementsPanel
 * @see GameController
 */
public class Board extends JPanel {
    
    /** Matriz bidimensional de {@code 8x8} que almacena todas las casillas del tablero. */
    private Square[][] squares;

    /**
     * Casilla que contiene el peón que acaba de avanzar dos escaques en el último turno
     * y que puede ser capturado al paso en la jugada inmediata, o {@code null} si no aplica.
     */
    private Square enPassantSquare;

    /** Panel lateral encargado de mostrar el historial de movimientos y el estado de la partida. */
    private final MovementsPanel movementsPanel;

    /**
     * Construye e inicializa un nuevo tablero de ajedrez.
     * <p>
     * Configura las dimensiones preferidas del componente ({@code 1000x1000} píxeles),
     * define el color de fondo y el margen para las coordenadas del tablero, crea la
     * cuadrícula de casillas vinculada a su {@link GameController} y coloca todas las
     * piezas blancas y negras en sus posiciones reglamentarias de inicio.
     * </p>
     * 
     * @param movementsPanel el panel lateral ({@link MovementsPanel}) donde se registrará
     *                       el historial de jugadas y se configurarán los controles de partida;
     *                       puede ser {@code null} para usar el tablero sin panel lateral.
     */
    public Board(MovementsPanel movementsPanel) {
        this.movementsPanel = movementsPanel;
        setPreferredSize(new Dimension(1000, 1000));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 0));
        loadSquaresBoard();
        initializeWhitePieces();
        initializeBlackPieces();
    }
    
    /**
     * Inicializa la cuadrícula de {@code 8x8} casillas del tablero y configura su controlador.
     * <p>
     * Establece un diseño {@link GridLayout} de 8 filas por 8 columnas, instancia el
     * {@link GameController} principal, vincula las acciones del panel de movimientos y
     * registra dicho controlador como oyente de eventos en cada instancia de {@link Square}.
     * </p>
     */
    private void loadSquaresBoard() {
        setLayout(new GridLayout(8, 8));
        squares = new Square[8][8];

        GameController controller = new GameController(this, movementsPanel);

        if (movementsPanel != null) movementsPanel.configureGameActions(controller);
        
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                squares[row][col] = new Square(row, col);
                squares[row][col].addActionListener(controller);
                add(squares[row][col]);
            }
        }
    }
    
    /**
     * Dibuja el componente y renderiza las coordenadas algebraicas fuera de las casillas.
     * <p>
     * Pinta los números de fila ({@code 8} a {@code 1}) centrados verticalmente en el margen
     * izquierdo y las letras de columna ({@code a} a {@code h}) centradas horizontalmente en
     * el margen inferior, asumiendo la orientación estándar con las piezas blancas abajo.
     * </p>
     *
     * @param g el contexto gráfico {@link Graphics} utilizado para dibujar el componente.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g.create();
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2D.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2D.setColor(new Color(0x2B2926));
        FontMetrics metrics = g2D.getFontMetrics();

        for (int index = 0; index < 8; index++) {
            Square rowSquare = squares[index][0];
            String number = String.valueOf(8 - index);
            int numberX = (getInsets().left - metrics.stringWidth(number)) / 2;
            int numberY = rowSquare.getY() + (rowSquare.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
            g2D.drawString(number, numberX, numberY);

            Square colSquare = squares[7][index];
            String letter = String.valueOf((char) ('a' + index));
            int letterX = colSquare.getX() + (colSquare.getWidth() - metrics.stringWidth(letter)) / 2;
            int letterY = getHeight() - getInsets().bottom + (getInsets().bottom - metrics.getHeight()) / 2 + metrics.getAscent();
            g2D.drawString(letter, letterX, letterY);
        }
        g2D.dispose();
    }

    /**
     * Crea y posiciona las piezas blancas en sus casillas iniciales reglamentarias.
     * <p>
     * Coloca los ocho peones blancos ({@link Pawn}) en la fila índice {@code 6} (segunda fila
     * en notación algebraica) y las piezas mayores y menores ({@link Rook}, {@link Knight},
     * {@link Bishop}, {@link Queen} y {@link King}) en la fila índice {@code 7} (primera fila).
     * </p>
     */
    private void initializeWhitePieces() {
        for (int col = 0; col < squares.length; col++) {
            squares[6][col].setPiece(new Pawn(true));
        }
        
        squares[7][0].setPiece(new Rook(true));
        squares[7][7].setPiece(new Rook(true));
        
        squares[7][1].setPiece(new Knight(true));
        squares[7][6].setPiece(new Knight(true));
        
        squares[7][2].setPiece(new Bishop(true));
        squares[7][5].setPiece(new Bishop(true));
        
        squares[7][4].setPiece(new King(true));
        squares[7][3].setPiece(new Queen(true));
    }
    
    /**
     * Crea y posiciona las piezas negras en sus casillas iniciales reglamentarias.
     * <p>
     * Coloca los ocho peones negros ({@link Pawn}) en la fila índice {@code 1} (séptima fila
     * en notación algebraica) y las piezas mayores y menores ({@link Rook}, {@link Knight},
     * {@link Bishop}, {@link Queen} y {@link King}) en la fila índice {@code 0} (octava fila).
     * </p>
     */
    private void initializeBlackPieces() {
        for (int col = 0; col < squares.length; col++) {
            squares[1][col].setPiece(new Pawn(false));
        }
        
        squares[0][0].setPiece(new Rook(false));
        squares[0][7].setPiece(new Rook(false));
        
        squares[0][1].setPiece(new Knight(false));
        squares[0][6].setPiece(new Knight(false));
        
        squares[0][2].setPiece(new Bishop(false));
        squares[0][5].setPiece(new Bishop(false));
        
        squares[0][4].setPiece(new King(false));
        squares[0][3].setPiece(new Queen(false));
    }

    /**
     * Obtiene la casilla donde se encuentra el peón que acaba de avanzar dos posiciones
     * en el turno inmediato anterior.
     *
     * @return la instancia de {@link Square} que contiene el peón vulnerable a captura
     *         al paso (<i>en passant</i>), o {@code null} si ningún peón es elegible.
     */
    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    /**
     * Establece o limpia la casilla del peón vulnerable a una captura al paso durante
     * el siguiente turno.
     *
     * @param enPassantSquare la casilla ({@link Square}) donde se ubica el peón que avanzó
     *                        dos casillas, o {@code null} para limpiar el estado de captura al paso.
     */
    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    /**
     * Obtiene la matriz interna de casillas, sin realizar una copia defensiva.
     * Las modificaciones de sus elementos afectan directamente al tablero.
     * 
     * @return una matriz {@link Square}{@code [8][8]} con todas las casillas del tablero,
     *         indexadas por fila ({@code 0} a {@code 7}) y columna ({@code 0} a {@code 7}).
     */
    public Square[][] getSquares() {
        return squares;
    }
    
}
