package view.pieces.king;

import view.Square;

/** Datos del enroque; el controlador se encarga de mover las piezas. */
public record CastleMovement(Square rookOrigin, Square rookTarget, String notation) {}
