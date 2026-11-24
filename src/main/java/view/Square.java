package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RadialGradientPaint;
import view.pieces.king.King;
import javax.swing.JButton;

import view.pieces.Piece;

/**
 * Representa una casilla individual dentro de la cuadrícula del tablero de ajedrez.
 * <p>
 * Esta clase extiende {@link JButton} para permitir la interacción del jugador mediante
 * eventos de clic. Gestiona sus coordenadas de fila y columna dentro del tablero, su color
 * de fondo alternado (claro u oscuro), la referencia y el icono gráfico de la pieza
 * ({@link Piece}) que la ocupa, así como los efectos visuales de resaltado para movimientos
 * válidos y de alerta cuando el rey ({@link King}) se encuentra en jaque.
 * </p>
 * 
 * <p>Asignar una pieza actualiza la ocupación y el icono, pero no valida movimientos,
 * turnos ni capturas. Cambiar las coordenadas con los setters no recoloca el botón
 * en el tablero ni recalcula su color de fondo.</p>
 *
 * @author brysonfl
 * @see Board
 * @see Piece
 * @see King
 */
public class Square extends JButton {
    
    /** Pieza de ajedrez ubicada actualmente en esta casilla, o {@code null} si está vacía. */
    private Piece piece;
    
    /** Índice de la fila de la casilla en el tablero (de {@code 0} a {@code 7}). */
    private int row;

    /** Índice de la columna de la casilla en el tablero (de {@code 0} a {@code 7}). */
    private int col;
    
    /** Indica si la casilla debe mostrarse resaltada como destino de un movimiento válido. */
    private boolean highlight = false;

    /** Indica si debe renderizarse el efecto visual de jaque sobre la casilla. */
    private boolean check = false;
    
    /**
     * Construye e inicializa una nueva casilla en la posición especificada del tablero.
     * <p>
     * Configura las propiedades visuales del botón (opacidad, ausencia de bordes y foco,
     * cursor de mano) y asigna automáticamente el color tradicional claro ({@code 0xF0D9B5})
     * u oscuro ({@code 0xB58863}) en función de la paridad de la suma de su fila y columna.
     * </p>
     * 
     * @param row el índice de la fila en la que se ubica la casilla (de {@code 0} a {@code 7}).
     * @param col el índice de la columna en la que se ubica la casilla (de {@code 0} a {@code 7}).
     */
    public Square(int row, int col) {
        setOpaque(true);
        setBorderPainted(false);
        setSelected(false);
        setFocusPainted(false);
        setBackground((row + col) %2 == 0 ? new Color(0xF0D9B5) : new Color(0xB58863));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        this.row = row;
        this.col = col;
    }

    /**
     * Obtiene la pieza de ajedrez ubicada actualmente en esta casilla.
     * 
     * @return la instancia de {@link Piece} presente en la casilla, o {@code null}
     *         si la casilla se encuentra vacía.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Asigna o elimina la pieza ubicada en esta casilla y actualiza su icono gráfico.
     * <p>
     * Si la pieza proporcionada no es {@code null} y dispone de un icono válido, dicho
     * icono se establece en el botón; de lo contrario, se limpia el icono visual.
     * </p>
     * 
     * @param piece la pieza ({@link Piece}) que ocupará la casilla, o {@code null}
     *              para vaciarla.
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
        
        if (piece != null && piece.getIcon() != null) {
            this.setIcon(piece.getIcon());
        } else {
            this.setIcon(null);
        }
    }

    /**
     * Obtiene el índice de la fila a la que pertenece esta casilla dentro del tablero.
     * 
     * @return el índice de fila (entre {@code 0} y {@code 7}).
     */
    public int getRow() {
        return row;
    }

    /**
     * Establece el índice de la fila para esta casilla.
     * 
     * @param row el nuevo índice de fila (entre {@code 0} y {@code 7}).
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Obtiene el índice de la columna a la que pertenece esta casilla dentro del tablero.
     * 
     * @return el índice de columna (entre {@code 0} y {@code 7}).
     */
    public int getCol() {
        return col;
    }

    /**
     * Establece el índice de la columna para esta casilla.
     * 
     * @param col el nuevo índice de columna (entre {@code 0} y {@code 7}).
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Activa o desactiva el indicador visual de movimiento válido en la casilla y
     * solicita su redibujado inmediato.
     * 
     * @param highlight {@code true} para mostrar el círculo de resaltado sobre la casilla;
     *                  {@code false} para ocultarlo.
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }

    /**
     * Activa o desactiva el efecto de destello radial que indica que el rey en esta
     * casilla se encuentra en estado de jaque, forzando el redibujado del componente.
     *
     * @param check {@code true} para activar el destello visual de jaque;
     *              {@code false} para desactivarlo.
     */
    public void setCheck(boolean check) {
        this.check = check;
        repaint();
    }

    /**
     * Indica si la casilla se encuentra actualmente resaltada como destino de un
     * movimiento válido.
     * 
     * @return {@code true} si la casilla está resaltada; {@code false} en caso contrario.
     */
    public boolean isHighlight() {
        return highlight;
    }

    /**
     * Renderiza la casilla junto con los indicadores visuales de estado especiales.
     * <p>
     * Además del renderizado estándar del botón, si el estado {@code check} está activo
     * y la casilla contiene una instancia de {@link King}, dibuja un degradado radial
     * cálido ({@link RadialGradientPaint}) bajo el icono del rey. Si el estado
     * {@code highlight} está activo, dibuja un círculo central semitransparente que
     * señala un movimiento legal disponible.
     * </p>
     * 
     * @param g el contexto gráfico {@link Graphics} utilizado para pintar la casilla.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (check && piece instanceof King && getWidth() > 0 && getHeight() > 0) {
            Graphics2D g2D = (Graphics2D) g.create();
            float radius = Math.min(getWidth(), getHeight()) / 2.0f;

            g2D.setPaint(new RadialGradientPaint(getWidth() / 2.0f, getHeight() / 2.0f, radius,
                    new float[]{ 0.0f, 0.55f, 1.0f },
                    new Color[]{ new Color(255, 220, 40, 180), new Color(255, 165, 0, 160),
                    new Color(255, 140, 0, 0) }));

            g2D.fillRect(0, 0, getWidth(), getHeight());

            if (getIcon() != null) {
                getIcon().paintIcon(this, g2D, (getWidth() - getIcon().getIconWidth()) / 2, (getHeight() - getIcon().getIconHeight()) / 2);
            }

            g2D.dispose();
        }

        if (highlight) {
            Graphics2D g2D = (Graphics2D) g.create();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setColor(new Color(0, 0, 0, 85));
            
            int diameter = Math.min(getWidth(), getHeight() / 4);
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;
            
            g2D.fillOval(x, y, diameter, diameter);
            g2D.dispose();
        }
    }

    /**
     * Devuelve una representación en cadena de texto de esta casilla, incluyendo
     * la pieza que contiene y sus coordenadas de fila y columna.
     * 
     * @return una cadena descriptiva con los valores de {@code piece}, {@code row} y {@code col}.
     */
    @Override
    public String toString() {
        return "Square{" + "piece=" + piece + ", row=" + row + ", col=" + col + '}';
    }
    
}
