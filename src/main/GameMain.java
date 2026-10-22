package main;


import view.WindowGame;

/**
 * Clase principal e inicializadora del juego de ajedrez (Chess Royale). 
 * Contiene el punto de entrada de la aplicación para ejecutar y levantar 
 * la interfaz gráfica.
 * 
 * @author brysonfl
 */
public class GameMain {
    
    /**
     * Punto de entrada principal de la aplicación. 
     * Se encarga de instanciar y hacer visible la ventana principal del juego.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        
        WindowGame windowGame = new WindowGame();
        
    }
}
