package view.dialogs;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.Collection;

public class AboutDialog extends ADialog<Boolean>{
    @Override
    protected void initDialog() {
        setTitle("About");
        setMessageHeader("Test calculator");
        setMessage("This application create as test.");
    }

    @Override
    protected Collection<ButtonType> getButtonTypes() {
        return Arrays.asList(ButtonType.OK, ButtonType.CANCEL);
    }

    @Override
    protected Node createContent() {
        return null;
    }

    @Override
    protected Callback<ButtonType, Boolean> getResultConverter() {
        return (type) -> true;
    }
}
