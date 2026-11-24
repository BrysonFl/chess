package view.pieces;

import enums.PieceType;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;

import exceptions.NotFoundException;
import view.Board;
import view.pieces.king.King;

/**
 * Clase abstracta que representa de forma genérica una pieza de ajedrez.
 * <p>
 * Contiene las propiedades y comportamientos comunes a todas las piezas del tablero,
 * tales como su color (bando blanco o negro), tipo ({@link PieceType}), estado de
 * movimiento previo, representación gráfica ({@link ImageIcon}) y la definición del
 * contrato abstracto para calcular sus movimientos pseudolegales sobre el {@link Board}.
 * </p>
 *
 * <p>Las coordenadas usan índices de 0 a 7: (0, 0) representa a8 y (7, 7), h1.
 * Los métodos de movimiento devuelven listas modificables de pares [fila, columna],
 * sin modificar el tablero. No comprueban el turno ni si el rey propio queda en jaque.</p>
 *
 * @author brysonfl
 * @see PieceType
 * @see Board
 */
public abstract class Piece {
    
    /** Icono gráfico escalado que representa visualmente a la pieza en el tablero. */
    private ImageIcon icon;

    /** Indica si la pieza ya ha realizado al menos un movimiento durante la partida. */
    private boolean moved;

    /** Tipo específico de la pieza dentro del catálogo {@link PieceType}. */
    private final PieceType pieceType;
    
    /** Define el bando de la pieza: {@code true} para blancas, {@code false} para negras. */
    private final boolean white;
    
    /**
     * Construye una nueva pieza de ajedrez con el color y tipo especificados,
     * cargando y escalando automáticamente su respectivo icono gráfico.
     *
     * @param isWhite   define si la pieza pertenece al bando blanco ({@code true})
     *                  o negro ({@code false}).
     * @param pieceType el tipo de pieza según la enumeración {@link PieceType}.
     */
    protected Piece(boolean isWhite, PieceType pieceType) {
        this.white = isWhite;
        this.pieceType = pieceType;
        loadIcon(isWhite);
    }
    
    /**
     * Carga y escala la imagen correspondiente al icono de la pieza basándose
     * en su color y tipo, utilizando los recursos empaquetados en {@code /pieces/}.
     * <p>
     * La imagen obtenida se redimensiona a {@code 80x80} píxeles mediante un algoritmo
     * de suavizado ({@link Image#SCALE_SMOOTH}) y se almacena en {@link #icon}.
     * </p>
     *
     * @param isWhite indica si se debe cargar la variante blanca ({@code true}, prefijo {@code "w"})
     *                o negra ({@code false}, prefijo {@code "b"}) de la imagen.
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
     * @return el icono de la pieza, o {@code null} si no se pudo cargar el recurso.
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Verifica si la pieza ya ha efectuado algún movimiento desde el inicio de la partida.
     * <p>
     * Este estado resulta fundamental para validar reglas especiales como el enroque
     * del rey y la torre. El avance doble del peón se determina por su fila.
     * </p>
     *
     * @return {@code true} si la pieza ya se ha movido al menos una vez;
     *         {@code false} si todavía no se ha invocado {@link #markMoved()}.
     */
    public boolean hasMoved() {
        return moved;
    }

    /**
     * Registra que la pieza ha realizado un desplazamiento en el tablero.
     * <p>
     * Establece el indicador interno {@link #moved} en {@code true}, lo cual actualiza
     * condiciones asociadas como la elegibilidad para realizar el enroque.
     * </p>
     */
    public void markMoved() {
        moved = true;
    }

    /**
     * Obtiene el valor material estándar de la pieza, independientemente de su color.
     *
     * @return el valor numérico en puntos asociado al {@link PieceType}; el rey devuelve
     *         {@code 0} dado que no se contabiliza como material capturable.
     * @see PieceType#getValue()
     */
    public int getValue() {
        return pieceType.getValue();
    }

    /**
     * Indica si la pieza pertenece al bando de las blancas.
     *
     * @return {@code true} si la pieza es blanca; {@code false} si es negra.
     */
    public boolean isWhite() {
        return white;
    }

    /**
     * Obtiene el identificador en texto del tipo de pieza.
     *
     * @return una cadena con el nombre o código del tipo de pieza según {@link PieceType#getType()}.
     * @see PieceType#getType()
     */
    public String getPieceType() {
        return pieceType.getType();
    }
    
    /**
     * Calcula y retorna una lista con todos los movimientos válidos para la pieza
     * desde su posición actual en el tablero, según sus reglas geométricas de desplazamiento.
     *
     * @param row   el índice de la fila actual (0 a 7) en la que se encuentra la pieza.
     * @param col   el índice de la columna actual (0 a 7) en la que se encuentra la pieza.
     * @param board la instancia del tablero ({@link Board}) sobre la cual se evalúan
     *              las casillas libres, bloqueos y posibles capturas.
     * @return una lista de arreglos {@code Integer[]} de dos elementos, donde cada par
     *         {@code [fila, columna]} representa una casilla destino alcanzable.
     */
    public abstract List<Integer[]> validMovements(int row, int col, Board board);

    /**
     * Comprueba si una pieza ubicada en una casilla de destino puede ser capturada
     * por esta pieza.
     * <p>
     * Una pieza es capturable únicamente si existe ({@code piece != null}), pertenece
     * al bando contrario y no es una instancia de {@link King} (ya que en ajedrez el rey
     * nunca se captura directamente).
     * </p>
     *
     * @param piece la pieza que ocupa la casilla de destino a evaluar, o {@code null} si está vacía.
     * @return {@code true} si la pieza objetivo pertenece al oponente y no es un rey;
     *         {@code false} en caso contrario.
     * @see King
     */
    public boolean canCapture(Piece piece) {
        return piece != null && piece.isWhite() != isWhite() && !(piece instanceof King);
    }

    /**
     * Retorna una representación en cadena de texto del estado de la pieza,
     * detallando su icono, tipo y color.
     *
     * @return una cadena descriptiva con los atributos principales de la instancia.
     */
    @Override
    public String toString() {
        return "Piece{" + "icon=" + icon + ", pieceType=" + pieceType + ", isWhite=" + white + '}';
    }
    
}
