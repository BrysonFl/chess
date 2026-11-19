import org.junit.jupiter.api.*;
import view.WindowGame;
import view.frames.ResignGameWindow;
import view.buttons.ResignButton;
import view.Board;
import controller.GameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import static org.junit.jupiter.api.Assertions.*;
import static support.SwingTest.*;

/** Pruebas con ventanas reales: ejecutar con -PuiTests=true. */
@Tag("ui")
class WindowIntegrationTest {
    @BeforeEach void needsDisplay() { Assumptions.assumeFalse(GraphicsEnvironment.isHeadless()); }
    @AfterEach void closeWindows() throws Exception {
        onEdt(() -> {for(Window w:Window.getWindows()) w.dispose();});
    }
    @Test void applicationEntryPointCreatesVisibleGame() throws Exception {
        onEdt(() -> {
            GameMain.main(new String[0]);
            assertTrue(java.util.Arrays.stream(Window.getWindows()).anyMatch(w->w instanceof WindowGame && w.isShowing()));
        });
    }
    @Test void customWindowTitle() throws Exception {
        onEdt(() -> {WindowGame w=new WindowGame("Prueba");assertEquals("Prueba",w.getTitle());assertTrue(w.isShowing());});
    }
    private static void answer(Container c,int answer) {
        for(Component child:c.getComponents()) {
            if(child instanceof JOptionPane pane) pane.setValue(answer);
            else if(child instanceof Container container) answer(container,answer);
        }
    }
    private static Timer responder(int choice) {
        Timer timer=new Timer(100,e -> {
            for(Window w:Window.getWindows()) if(w instanceof JDialog && w.isShowing()) {
                if(choice==JOptionPane.CLOSED_OPTION) w.dispatchEvent(new WindowEvent(w,WindowEvent.WINDOW_CLOSING));
                else answer(w,choice);
            }
        }); timer.start();return timer;
    }
    @Test void resignationDialogHandlesYesNoAndClose() throws Exception {
        onEdt(() -> {
            for(int choice:new int[]{JOptionPane.YES_OPTION,JOptionPane.NO_OPTION,JOptionPane.CLOSED_OPTION}) {
                Timer timer=responder(choice);
                try {assertEquals(choice==JOptionPane.YES_OPTION,new ResignGameWindow().confirmResignation(null));}
                finally {timer.stop();}
            }
        });
    }
    @Test void cancellingResignationKeepsGameActive() throws Exception {
        onEdt(() -> {
            GameController controller=new GameController(new Board(null),null);
            ResignButton button=new ResignButton("Abandonar");button.configureAction(controller);
            Timer timer=responder(JOptionPane.NO_OPTION);
            try {button.doClick(0);assertEquals("",controller.getResult());}
            finally {timer.stop();}
        });
    }
}
