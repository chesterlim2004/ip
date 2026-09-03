package crystal.gui;

import java.io.InputStream;
import java.util.Objects;

import crystal.Crystal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

/**
 * Controls the main messaging window and forwards user input to Crystal.
 */
public final class MainWindow {
    /** Introductory message displayed when the window opens. */
    private static final String WELCOME_MESSAGE = "Hello! I'm Crystal. "
            + "Send me a command and I'll help you manage your tasks.\n\n"
            + "Try: todo read a book, list, find book, or bye.";

    /** Prefix included in console-oriented Crystal responses. */
    private static final String CRYSTAL_PREFIX = "Crystal: ";

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private VBox messageContainer;

    @FXML
    private TextField commandInput;

    /** Crystal instance that owns the current task session. */
    private Crystal crystal;

    /** Avatar displayed beside Crystal's messages. */
    private Image crystalAvatar;

    /** Avatar displayed beside the user's messages. */
    private Image userAvatar;

    /**
     * Creates the controller instance used by the FXML loader.
     */
    public MainWindow() {
    }

    /**
     * Configures automatic scrolling after the FXML controls are injected.
     */
    @FXML
    private void initialize() {
        messageContainer.heightProperty().addListener((observableHeight, previousHeight, currentHeight) ->
                messageScrollPane.setVvalue(1.0));
    }

    /**
     * Supplies Crystal's command service and displays the initial greeting.
     *
     * @param crystal Crystal instance for this window's session.
     */
    public void setCrystal(Crystal crystal) {
        this.crystal = crystal;
        crystalAvatar = loadImage("/images/crystal-avatar.png");
        userAvatar = loadImage("/images/user-avatar.png");
        messageContainer.getChildren().add(
                DialogBox.createCrystalDialog(WELCOME_MESSAGE, crystalAvatar));
        Platform.runLater(commandInput::requestFocus);
    }

    /**
     * Displays a nonblank command and Crystal's response, then clears the composer.
     */
    @FXML
    private void handleUserInput() {
        String userCommand = commandInput.getText().strip();
        if (userCommand.isEmpty()) {
            return;
        }

        String crystalResponse = formatCrystalResponse(crystal.getResponse(userCommand));
        messageContainer.getChildren().addAll(
                DialogBox.createUserDialog(userCommand, userAvatar),
                DialogBox.createCrystalDialog(crystalResponse, crystalAvatar));
        commandInput.clear();
        commandInput.requestFocus();
    }

    /**
     * Removes console-only prefixes and indentation from a response shown in a named bubble.
     *
     * @param response complete response produced by Crystal.
     * @return response formatted for the graphical message bubble.
     */
    private static String formatCrystalResponse(String response) {
        return response.replace(CRYSTAL_PREFIX, "")
                .replace("\n         ", "\n");
    }

    /**
     * Loads a required image resource from the application classpath.
     *
     * @param resourcePath absolute classpath location of the image.
     * @return decoded image.
     */
    private static Image loadImage(String resourcePath) {
        InputStream imageStream = Objects.requireNonNull(
                MainWindow.class.getResourceAsStream(resourcePath),
                "Missing image resource: " + resourcePath);
        return new Image(imageStream);
    }
}
