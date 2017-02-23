package com.project;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

/** Separate Java service class.
 * Backend implementation for the address book application, with "detached entities"
 * simulating real world DAO. Typically these something that the Java EE
 * or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class RegisteredUserDatabase {

    private static RegisteredUserDatabase instance;

    private ArrayList<RegisteredUser> userDatabase;
    
    
    public RegisteredUserDatabase() {
    	userDatabase = new ArrayList<>();
    	RegisteredUser testUser = new RegisteredUser("test", "test@test.com", "p4ssw0rd", "p4ssw0rd", "test", "test", "test");
    	userDatabase.add(testUser);
    }
    
    public ArrayList<RegisteredUser> getDatabase(){
    	return userDatabase;
    }

    public synchronized long count() {
        return userDatabase.size();
    }
    
    
    public boolean delete(RegisteredUser user) {
        for (RegisteredUser r : userDatabase){
        	if(r.getUserName().equals(user.getUserName())){
        		userDatabase.remove(r);
        		return true;
        	}
        }
        return false;
    }

    public synchronized void save(RegisteredUser user) {
        userDatabase.add(user);
    }
}

