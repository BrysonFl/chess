package view.pieces;

import enums.PieceType;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
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
        String builderRoutePiece = new StringBuilder("/images/pieces/")
            .append(isWhite ? "w" : "b")
            .append(this.pieceType.getType())
            .append(".png")
            .toString();
        
        URL url = getClass().getResource(builderRoutePiece);
        
        if (url != null) {
            Image image = new ImageIcon(url).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(image);
        } else {
            System.out.println("No se encontró la imagen");
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

    /**
     * Indica si la pieza pertenece al bando blanco.
     * 
     * @return {@code true} si la pieza es blanca, {@code false} si es negra.
     */
    public boolean isWhite() {
        return white;
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
