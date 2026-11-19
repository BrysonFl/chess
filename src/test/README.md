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

## Límites

Esta suite no implica cobertura total de ramas. Quedan por ampliar los umbrales de 50/75 movimientos, la reclamación por jugada declarada y la elección gráfica de todas las promociones. El camino de confirmación de ResignButton que abre otra partida no se automatiza aún: crea un ejecutor que no cierra y abre ventanas adicionales. Se prueba el diálogo por separado y la cancelación del botón.

Validación realizada: 24 casos sin ventanas correctos con JUnit Platform 6 usando las dependencias locales. Las cuatro pruebas gráficas se han compilado, pero no ejecutado. No se verificó la ejecución mediante el wrapper de Gradle.
