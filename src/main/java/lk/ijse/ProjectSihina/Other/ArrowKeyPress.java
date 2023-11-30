package lk.ijse.ProjectSihina.Other;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ArrowKeyPress {
    public static void switchTextFieldOnArrowPressRight(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case RIGHT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressRightToPassword(JFXTextField textField, JFXPasswordField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case RIGHT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressLeftToPassword(JFXPasswordField nextTextField,JFXTextField textField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }

    public static void switchTextFieldOnArrowPressLeft(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }


    public static void switchTextFieldOnArrowPressUP(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }

    public static void switchTextFieldOnArrowPressup(JFXPasswordField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }

    public static void switchTextFieldOnArrowPressUPToPassword(JFXTextField textField, JFXPasswordField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }

    public static void switchTextFieldOnArrowPressDown(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DOWN:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressDOWN(JFXTextField textField, JFXPasswordField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DOWN:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressDownToPassword(JFXPasswordField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DOWN:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }


    public static void switchTextFieldOnArrowPressRIGHT(JFXTextField textField, JFXTextArea nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case RIGHT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressLEFT(JFXTextArea textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case LEFT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
}
