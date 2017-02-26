package com.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Separate Java service class.
 * Backend implementation for the address book application, with "detached entities"
 * simulating real world DAO. Typically these something that the Java EE
 * or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.

public class RegisteredUserDatabase {

    private static final RegisteredUserDatabase instance;
    private List<RegisteredUser> userList;
    
    public List<RegisteredUser> getUserList() {
        return userList;
    }

    static {
        instance = new RegisteredUserDatabase();
    }
    
    private RegisteredUserDatabase() {
    	userList = new ArrayList<>();
    	RegisteredUser testUser = getTestUser();
    	userList.add(testUser);
    }
    
    public static RegisteredUserDatabase getInstance() {
    	return instance;
    }

    public synchronized long count() {
        return userList.size();
    }
  
    public boolean delete(RegisteredUser user) {
        return userList.remove(user);
    }
    
    public synchronized void save(RegisteredUser user) {
        if (user.isValid(this)) {
            userList.add(user);
        }
    }
    
    private static RegisteredUser getTestUser() {
        return new RegisteredUser("test", "test@test.com", "p4ssw0rd", "test", "test", "test");
    }

    public Optional<RegisteredUser> fetchUser(String email) {
        for(RegisteredUser r : userList) {
            if(email.equals(r.getEmail())) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }
}

