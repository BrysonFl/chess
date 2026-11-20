# Diagrama de clases
<img width="11780" height="5656" alt="Chess - Classes Diagram drawio" src="https://github.com/user-attachments/assets/119cd892-9274-442d-b1fe-cb7990a62d3e" />

# Pruebas de Chess

La suite usa JUnit Jupiter 6. Los componentes Swing se prueban en el hilo de eventos (EDT).

## Ejecución

- `./gradlew test`: reglas y componentes, sin abrir ventanas.
- `./gradlew test -PuiTests=true`: incluye las pruebas etiquetadas `ui`; requiere una sesión gráfica y abre/cierra ventanas reales.

También se pueden ejecutar las clases JUnit desde IntelliJ. Para las pruebas de reglas configura `-Djava.awt.headless=true`.

## Cobertura funcional

- `PiecesTest`: Piece, Pawn, Knight, Bishop, Rook, Queen, King y CastleMovement. Movimientos, bordes, bloqueos, capturas, recursos, en passant y condiciones del enroque en ambos flancos y colores.
- `GameControllerTest`: turnos, abandono, bloqueo tras terminar, ejecución de en passant, seguridad del rey, repetición automática y la batería previa de ChessRulesCheck (mate, enroque, promoción, repetición reclamada y ahogado).
- `ComponentsTest`: Board, Square, MovementsPanel, ButtonBase, DrawButton, ClaimDrawButton, estilo de ResignButton, UtilValidations, PieceType y NotFoundException.
- `WindowIntegrationTest`: GameMain, WindowGame, ResignGameWindow y cancelación desde ResignButton. Son pruebas de integración gráfica, no pruebas unitarias aisladas.
