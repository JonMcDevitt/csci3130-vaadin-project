package com.project.backend;

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

/** TODO:   Remove this once the JPAContainer is set up
 *  */
public class RegisteredUserDatabase {

    private static final RegisteredUserDatabase instance;
    private List<User> userList;
    
    public List<User> getUserList() {
        return userList;
    }

    static {
        instance = new RegisteredUserDatabase();
    }
    
    private RegisteredUserDatabase() {
    	userList = new ArrayList<>();
    	User testUser = getTestUser();
    	userList.add(testUser);
    }
    
    public static RegisteredUserDatabase getInstance() {
    	return instance;
    }

    public synchronized long count() {
        return userList.size();
    }
  
    public boolean delete(User user) {
        return userList.remove(user);
    }
    
    public synchronized void save(User user) {
        if (user.isValid(this)) {
            userList.add(user);
        }
    }
    
    public static User getTestUser() {
        return new User("test@test.com", "p4ssw0rd", "testFirst", "testLast", "test");
    }

    public Optional<User> fetchUser(String email) {
        for(User r : userList) {
            if(email.equals(r.getEmail())) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }
}