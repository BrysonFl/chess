package view;

import controller.GameController;
import java.awt.Dimension;
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
    
    /**
     * Construye un nuevo tablero de ajedrez configurando su tamaño preferido, 
     * inicializando la cuadrícula de casillas con su controlador de eventos 
     * y posicionando las piezas blancas y negras en sus lugares de inicio.
     */
    public Board() {
        setPreferredSize(new Dimension(1000, 1000));
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
        
        GameController controller = new GameController(this);
        
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                squares[row][col] = new Square(row, col);
                squares[row][col].addActionListener(controller);
                add(squares[row][col]);
            }
        }
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
     * Obtiene la matriz bidimensional de casillas que conforman el tablero.
     * 
     * @return Una matriz de objetos {@code Square[][]} que representa los recuadros creados.
     */
    public Square[][] getSquares() {
        return squares;
    }
    
}
