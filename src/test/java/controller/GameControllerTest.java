package controller;

import org.junit.jupiter.api.Test;
import view.*;
import view.pieces.*;
import view.pieces.king.King;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

class GameControllerTest {
    private static GameController controller(Board b) {
        return (GameController)b.getSquares()[0][0].getActionListeners()[0];
    }
    private static void move(Board b,int r,int c,int tr,int tc) {
        b.getSquares()[r][c].doClick(0);
        assertTrue(b.getSquares()[tr][tc].isHighlight(),"Destino legal esperado");
        b.getSquares()[tr][tc].doClick(0);
    }
    @Test void existingRuleRegressions() throws Exception { ChessRulesCheck.main(new String[0]); }
    @Test void turnsAndResignationBlockFurtherPlay() throws Exception {
        onEdt(() -> {
            Board b=new Board(null); GameController c=controller(b);
            b.getSquares()[1][0].doClick(0); assertFalse(b.getSquares()[2][0].isHighlight());
            move(b,6,4,4,4);
            b.getSquares()[6][3].doClick(0); assertFalse(b.getSquares()[5][3].isHighlight());
            c.resign(); String result=c.getResult(); assertTrue(result.contains("blancas"));
            c.resign(); c.claimDraw(); c.offerDraw(); assertEquals(result,c.getResult());
            b.getSquares()[1][0].doClick(0); assertFalse(b.getSquares()[2][0].isHighlight());
        });
    }
    @Test void enPassantRemovesOnlyCapturedPawn() throws Exception {
        onEdt(() -> {
            Board b=new Board(null);
            move(b,6,4,4,4); move(b,1,0,2,0); move(b,4,4,3,4); move(b,1,3,3,3);
            Piece pawn=b.getSquares()[3][4].getPiece(); move(b,3,4,2,3);
            assertSame(pawn,b.getSquares()[2][3].getPiece());
            assertNull(b.getSquares()[3][3].getPiece()); assertNull(b.getEnPassantSquare());
        });
    }
    @Test void enPassantCannotExposeKing() throws Exception {
        onEdt(() -> {
            Board b=emptyBoard();
            b.getSquares()[3][7].setPiece(new King(true)); b.getSquares()[0][7].setPiece(new King(false));
            b.getSquares()[3][0].setPiece(new Rook(false)); b.getSquares()[3][4].setPiece(new Pawn(true));
            Square victim=b.getSquares()[3][3]; Piece pawn=new Pawn(false);victim.setPiece(pawn);
            b.setEnPassantSquare(victim); b.getSquares()[3][4].doClick(0);
            assertFalse(b.getSquares()[2][3].isHighlight());
            assertSame(pawn,victim.getPiece(),"La simulación restaura la pieza");
        });
    }
    @Test void fivefoldRepetitionIsAutomatic() throws Exception {
        onEdt(() -> {
            Board b=new Board(null);
            for(int i=0;i<4;i++) {
                move(b,7,6,5,5);move(b,0,6,2,5);move(b,5,5,7,6);move(b,2,5,0,6);
            }
            assertTrue(controller(b).getResult().contains("quíntuple"));
        });
    }
}
