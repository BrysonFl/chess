# ♟️ Chess Royale Game
Proyecto desarrollado en Java para la materia de Diseño y Programación Orientada a Objetos

## Class diagram

<img width="6800" height="8800" alt="Chess - Classes Diagram" src="https://github.com/user-attachments/assets/b757a749-ebd8-42c0-90ff-905d1b101593" />


## Pruebas unitarias de GameController

Con JDK 17 o superior y Maven, ejecutar:

```sh
mvn test
```

La primera ejecución necesita acceso a Maven Central. La configuración Maven
complementa la configuración Ant/NetBeans existente y conserva `src` y `test`.

`test/controller/GameControllerTest.java` contiene 18 pruebas JUnit 5:

- Constructor: conserva el tablero recibido.
- `actionPerformed`: selección, cambio de selección, cancelación y clic vacío.
- `validatePieceMovements`: coordenadas resaltadas, ambos turnos y pieza sin movimientos.
- `movePiece`: avance simple/doble, captura, captura al paso de ambos colores,
  cambio de turno, incremento del contador y limpieza de la selección.
- `clearHighlights`: limpieza completa, después de mover y al seleccionar al rival.

Los métodos privados se ejercitan mediante `actionPerformed`. Se usa reflexión
únicamente para observar y restaurar el estado privado; las operaciones Swing
se ejecutan en el hilo de eventos. No ejecutar esta clase de pruebas en paralelo:
el controlador mantiene estado estático compartido.

### Hallazgos de la revisión

1. `whitesMove`, `beforeSquare` y los contadores son `static`: dos partidas comparten
   turno, selección y contadores. Una selección puede incluso apuntar al otro tablero.
   Conviene convertirlos en atributos de instancia.
2. La captura al paso depende de movimientos generados por `Pawn`, que no comprueba
   que la pieza adyacente sea un peón ni que haya avanzado dos casillas en la jugada
   inmediatamente anterior. `movePiece` elimina la pieza adyacente al recibir ese
   destino resaltado. Las pruebas verifican la ejecución en secuencias válidas;
   no certifican la legalidad completa de esta regla.
3. `movementsWithoutCaptures` nunca se actualiza ni se utiliza. No se implementa
   con él ninguna regla de tablas.
4. `Rook.validMovements` se detiene ante cualquier pieza y no incluye capturas.
   La prueba de captura con una pieza distinta de peón usa un doble de torre con
   destinos definidos para probar exclusivamente el controlador.
5. El controlador acepta los destinos que devuelve cada pieza sin comprobar
   si dejan al rey en jaque. Sus resaltados no garantizan movimientos legales
   completos de ajedrez.

Se conservó la lógica de producción. Verificación local: 18/18 pruebas aprobadas
con JUnit Platform y compilación Java 17 usando el JDK 25 instalado. Maven no pudo
resolver dependencias por falta de red; se usaron bibliotecas JUnit ya disponibles
localmente para ejecutar la suite.
