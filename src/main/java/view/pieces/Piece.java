package view.pieces;

import enums.PieceType;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;

import exceptions.NotFoundException;
import view.Board;

/**
 * Clase abstracta que representa de forma genérica una pieza de ajedrez. 
 * Contiene las propiedades y comportamientos comunes a todas las piezas, 
 * tales como su color, tipo, icono gráfico y la definición del contrato 
 * para calcular sus movimientos válidos.
 * 
 * @author brysonfl
 */
public abstract class Piece {
    
    private ImageIcon icon;
    private boolean moved;
    private final PieceType pieceType;
    
    private final boolean white;
    
    /**
     * Construye una nueva pieza de ajedrez con el color y tipo especificados, 
     * cargando automáticamente su respectivo icono gráfico.
     * 
     * @param isWhite   Define si la pieza es blanca ({@code true}) o negra ({@code false}).
     * @param pieceType El tipo de la pieza (ej. Peón, Torre, Reina, etc.).
     */
    protected Piece(boolean isWhite, PieceType pieceType) {
        this.white = isWhite;
        this.pieceType = pieceType;
        loadIcon(isWhite);
    }
    
    /**
     * Carga y escala la imagen correspondiente al icono de la pieza basándose 
     * en su color y tipo utilizando los recursos del proyecto.
     * 
     * @param isWhite Indica si se debe cargar la versión blanca o negra de la imagen.
     */
    private void loadIcon(boolean isWhite) {
        try {
            String builderRoutePiece = new StringBuilder("/pieces/")
                    .append(isWhite ? "w" : "b")
                    .append(this.pieceType.getType())
                    .append(".png")
                    .toString();

            URL url = getClass().getResource(builderRoutePiece);

            if (url == null) throw new NullPointerException();

            Image image = new ImageIcon(url).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(image);
        } catch (NotFoundException e) {
            System.out.println("ERROR: Lo sentimos, no pudimos encontrar la imagen en la ruta dada.");
        } catch (NullPointerException e) {
            System.out.println("ERROR: El objeto url está null.");
        }
    }

    /**
     * Obtiene el icono gráfico escalado de la pieza.
     * 
     * @return Un objeto {@code ImageIcon} que representa visualmente la pieza.
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /** @return Indica si la pieza ya realizó algún movimiento. */
    public boolean hasMoved() {
        return moved;
    }

    /** Marca la pieza como movida para conservar los derechos de enroque. */
    public void markMoved() {
        moved = true;
    }

    /**
     * Obtiene el valor material de la pieza, independientemente de su color.
     *
     * @return El valor en puntos; el rey devuelve 0 porque no se cuenta como material.
     */
    public int getValue() {
        return pieceType.getValue();
    }

    /**
     * Indica si la pieza pertenece al bando blanco.
     * 
     * @return {@code true} si la pieza es blanca, {@code false} si es negra.
     */
    public boolean isWhite() {
        return white;
    }

    public String getPieceType() {
        return pieceType.getType();
    }
    
    /**
     * Método abstracto para calcular y retornar una lista con todos los movimientos 
     * válidos y legales disponibles para la pieza desde su posición actual en el tablero.
     * 
     * @param row   La fila actual en la que se encuentra la pieza.
     * @param col   La columna actual en la que se encuentra la pieza.
     * @param board El tablero actual del juego, utilizado para verificar restricciones y casillas.
     * @return Una lista de arreglos de enteros donde cada elemento representa las coordenadas 
     *         {@code [fila, columna]} de una casilla válida a la que la pieza puede desplazarse.
     */
    public abstract List<Integer[]> validMovements(int row, int col, Board board);

    /**
     * Comprueba si una pieza contraria puede ser capturada. El rey no se captura.
     *
     * @param piece La pieza que ocupa la casilla de destino.
     * @return {@code true} si pertenece al contrario y no es un rey.
     */
    public boolean canCapture(Piece piece) {
        return piece != null && piece.isWhite() != isWhite() && !(piece instanceof King);
    }

    /**
     * Retorna una representación en cadena de texto del objeto {@code Piece}, 
     * detallando su icono, tipo y color.
     * 
     * @return Una cadena con los atributos principales de la pieza.
     */
    @Override
    public String toString() {
        return "Piece{" + "icon=" + icon + ", pieceType=" + pieceType + ", isWhite=" + white + '}';
    }
    
}
