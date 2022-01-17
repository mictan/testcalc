package model.database;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.data.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.PersistenceException;

public class UserDatabaseFacade {
    private final SimpleObjectProperty<User> currentUser = new SimpleObjectProperty<>(null);
    private final SimpleLongProperty currentUserId = new SimpleLongProperty(-1);//-1 = no user;

    public User login(String username, String password){
        User user = DAOFactory.getInstance().getUserDAO().getByName(username);
        if(user == null){
            changeCurrentUser(null);
            return null;
        }
        boolean passwordEquals;
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            passwordEquals = (password == null || password.isEmpty());
        } else {
            passwordEquals = checkPassword(password, user.getPassword());
        }
        if(passwordEquals){
            changeCurrentUser(user);
            return user;
        } else {
            changeCurrentUser(null);
            return null;
        }
    }

    public RegisterResult register(String username, String password){
        User user = new User();
        user.setName(username);
        user.setPassword(getPasswordHash(password));
        try {
            DAOFactory.getInstance().getUserDAO().add(user);
            changeCurrentUser(user);
            return RegisterResult.SUCCESS;
        } catch (PersistenceException e){
            changeCurrentUser(null);
            return RegisterResult.DUP_NAME;
        }
    }

    public void logout(){
        changeCurrentUser(null);
    }

    public ObjectBinding<User> createCurrentUserBinding(){
        return Bindings.createObjectBinding(currentUser::get, currentUser);
    }

    public LongBinding createCurrentUserIdBinding(){
        return Bindings.createLongBinding(currentUserId::get, currentUserId);
    }

    private void changeCurrentUser(User newUser){
        if(newUser != null && newUser.getId() != null && newUser.getId() >= 0){
            currentUserId.set(newUser.getId());
        } else {
            currentUserId.set(-1);
        }
        currentUser.set(newUser);
    }

    private String getPasswordHash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPassword(String password, String hashed){
        return BCrypt.checkpw(password, hashed);
    }

    public enum RegisterResult{
        SUCCESS, DUP_NAME
    }
}
