package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

import view.pieces.Piece;

/**
 * Clase que representa una casilla individual dentro del tablero de ajedrez. 
 * Extiende de {@code JButton} para permitir la interacción del usuario mediante clics, 
 * gestionando su posición, el color de fondo alternado, la pieza que contiene y 
 * el estado de resaltado visual para los movimientos válidos.
 * 
 * @author brysonfl
 */
public class Square extends JButton {
    
    private Piece piece;
    
    private int row;
    private int col;
    
    private boolean highlight = false;
    
    /**
     * Construye una nueva casilla indicando su posición en el tablero. 
     * Configura el estilo visual inicial del botón, incluyendo el color de las casillas 
     * (claro u oscuro según sus coordenadas), el cursor y los bordes.
     * 
     * @param row La fila en la que se encuentra la casilla (0 a 7).
     * @param col La columna en la que se encuentra la casilla (0 a 7).
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
     * Obtiene la pieza de ajedrez actualmente ubicada en esta casilla.
     * 
     * @return Un objeto {@code Piece} si hay una pieza presente, o {@code null} si la casilla está vacía.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Establece o cambia la pieza ubicada en esta casilla, actualizando 
     * automáticamente su icono gráfico en el botón.
     * 
     * @param piece La pieza que se asignará a la casilla (o {@code null} para dejarla vacía).
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
     * Obtiene el índice de la fila a la que pertenece esta casilla.
     * 
     * @return El número de fila.
     */
    public int getRow() {
        return row;
    }

    /**
     * Establece el índice de la fila para esta casilla.
     * 
     * @param row El nuevo valor de la fila.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Obtiene el índice de la columna a la que pertenece esta casilla.
     * 
     * @return El número de columna.
     */
    public int getCol() {
        return col;
    }

    /**
     * Establece el índice de la columna para esta casilla.
     * 
     * @param col El nuevo valor de la columna.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Activa o desactiva el estado de resaltado de la casilla y fuerza un redibujado visual.
     * 
     * @param highlight {@code true} para resaltar la casilla, {@code false} para quitar el resaltado.
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
        repaint();
    }

    /**
     * Indica si la casilla se encuentra actualmente resaltada (como movimiento válido).
     * 
     * @return {@code true} si está resaltada, {@code false} en caso contrario.
     */
    public boolean isHighlight() {
        return highlight;
    }

    /**
     * Sobrescribe el método de renderizado para pintar un indicador visual 
     * (un círculo semitransparente) sobre la casilla cuando se encuentra resaltada.
     * 
     * @param g El contexto gráfico utilizado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
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
     * Retorna una representación en cadena de texto del objeto {@code Square}, 
     * detallando la pieza contenida, su fila y su columna.
     * 
     * @return Una cadena con los atributos principales de la casilla.
     */
    @Override
    public String toString() {
        return "Square{" + "piece=" + piece + ", row=" + row + ", col=" + col + '}';
    }
    
}
