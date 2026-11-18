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
import view.pieces.King;
import view.pieces.Knight;
import view.pieces.Pawn;
import view.pieces.Queen;
import view.pieces.Rook;

/**
 * Esta clase es la inicialización del tablero. Contiene la creación de las dimensiones, 
 * la configuración de la cuadrícula, y la colocación inicial de las piezas blancas y oscuras.
 * Extiende de {@code JPanel} para integrarse en la interfaz gráfica del juego.
 * 
 * @author brysonfl
 */
public class Board extends JPanel {
    
    private Square[][] squares;
    private Square enPassantSquare;
    private final MovementsPanel movementsPanel;

    /**
     * Construye un nuevo tablero de ajedrez configurando su tamaño preferido, 
     * inicializando la cuadrícula de casillas con su controlador de eventos 
     * y posicionando las piezas blancas y negras en sus lugares de inicio.
     * 
     * @param movementsPanel Panel donde se registrará el historial de movimientos.
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
     * Este método realiza la inicialización del tablero y de los cuadrados correspondientes. 
     * Adicionalmente, crea el controlador para poder estar observando los clics en cada recuadro.
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
     * Dibuja las coordenadas del tablero fuera de las casillas: números a la
     * izquierda y letras en la parte inferior, con las blancas abajo.
     *
     * @param g El contexto gráfico utilizado para dibujar.
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
     * Este método realiza la inicialización de las piezas blancas en cada recuadro.
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
     * Este método realiza la inicialización de las piezas negras en cada recuadro.
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
     * Obtiene la casilla del peón que acaba de avanzar dos posiciones.
     *
     * @return La casilla del peón vulnerable a captura al paso, o {@code null}.
     */
    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    /**
     * Actualiza el peón vulnerable a captura al paso durante el siguiente turno.
     *
     * @param enPassantSquare La casilla del peón, o {@code null} si no hay ninguno.
     */
    public void setEnPassantSquare(Square enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    /**
     * Obtiene la matriz bidimensional de casillas que conforman el tablero.
     * 
     * @return Una matriz de objetos {@code Square[][]} que representa los recuadros creados.
     */
    public Square[][] getSquares() {
        return squares;
    }
    
}
