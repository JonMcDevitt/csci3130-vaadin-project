package com.project.backend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    private RegisteredUserDatabase testDatabase = RegisteredUserDatabase.getInstance();

    private User testUserPresent = new User("abc", "abc", "abc", "abc", "abc");
    private User testUserAbsent = new User("t2@test.com", "abc", "abc", "abc", "abc");
    private User[] invalidUsers = { new User("", "amc", "abc", "abc", "abc"),
            new User("", "abc", "abc", "abc", "abc"),
            new User("test", "abc", "", "abc", "abc"),
            new User("abc", "abc", "abc", "", "abc"),
            new User("abc", "", "abc", "abc", "abc")};

    @Before
    public void setUp() {
        testDatabase.save(testUserPresent);
    }

    @Test
    public void isValidTest() {
        assertFalse(testUserPresent.isValid(testDatabase));
        assertTrue(testUserAbsent.isValid(testDatabase));
    }

    @Test
	public void isValidUserTest() {
		for (User user : invalidUsers) {
		    assertFalse(user.isValidUser());
		}
		assertTrue(testUserAbsent.isValidUser());
	}

	/** TODO    -   Set up a working isPasswordValid test.
     * */
}
