package model.database;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import model.data.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserDatabaseFacade {
    private final SimpleObjectProperty<User> currentUser = new SimpleObjectProperty<>(null);

    public User login(String username, String password){
        User user = DAOFactory.getInstance().getUserDAO().getByName(username);
        if(user == null){
            currentUser.set(null);
            return null;
        }
        boolean passwordEquals;
        if(user.getPassword() == null || user.getPassword().isEmpty()){
            passwordEquals = (password == null || password.isEmpty());
        } else {
            passwordEquals = checkPassword(password, user.getPassword());
        }
        if(passwordEquals){
            currentUser.set(user);
            return user;
        } else {
            currentUser.set(null);
            return null;
        }
    }

    public void register(String username, String password){
        User user = new User();
        user.setName(username);
        user.setPassword(getPasswordHash(password));
        DAOFactory.getInstance().getUserDAO().add(user);
    }

    public ObjectBinding<User> createCurrentUserBinding(){
        return Bindings.createObjectBinding(currentUser::get, currentUser);
    }

    private String getPasswordHash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkPassword(String password, String hashed){
        return BCrypt.checkpw(password, hashed);
    }
}
