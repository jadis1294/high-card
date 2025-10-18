package it.sara.demo.service.database;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.util.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    public boolean save(User user) throws GenericException {
        user.setGuid(java.util.UUID.randomUUID().toString());
        UserUtil.validate(user);
        FakeDatabase.TABLE_USER.add(user);
        return true;
    }

    public Optional<User> getByGuid(String guid) {
        return FakeDatabase.TABLE_USER.stream().filter(u -> u.getGuid().equals(guid)).findFirst();
    }

    public List<User> getAll() {
        return FakeDatabase.TABLE_USER;
    }
}
