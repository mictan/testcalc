package view.dialogs;


import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.util.Callback;

import java.util.Collection;
import java.util.Optional;

public abstract class ADialog<R> {
    private Dialog<R> dialog;

    public Optional<R> showSync(){
        createDialog();
        return dialog.showAndWait();
    }

    protected abstract Collection<ButtonType> getButtonTypes();
    protected abstract Node createContent();
    protected abstract Callback<ButtonType, R> getResultConverter();
    protected void initDialog(){
        //DO NOTHING
    }

    protected void setTitle(String title){
        dialog.setTitle(title);
    }
    protected void setMessageHeader(String message){
        dialog.setHeaderText(message);
    }
    protected void setMessage(String message){
        dialog.setContentText(message);
    }

    protected Node lookupButton(ButtonType type){
        return dialog.getDialogPane().lookupButton(type);
    }

    private void createDialog(){
        dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(getButtonTypes());
        dialog.getDialogPane().setContent(createContent());
        dialog.setResultConverter(getResultConverter());
        initDialog();
    }
}
