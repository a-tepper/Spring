package org.example.repository;

import org.apache.log4j.Logger;
import org.example.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private final Logger logger = Logger.getLogger(UserRepository.class);
    private final List<User> repo = new ArrayList<>();

    @Override
    public List<User> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(User user) {
        user.setId(user.hashCode());
        repo.add(user);
        logger.info("added new user: " + user);
    }

    @Override
    public boolean removeItemById(Integer userIdToRemove) {
        return repo.removeIf(e -> userIdToRemove.equals(e.getId()));
    }
}
