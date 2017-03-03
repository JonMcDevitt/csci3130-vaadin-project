package com.project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RegisteredUserTest {

    private RegisteredUserDatabase testDatabase = RegisteredUserDatabase.getInstance();

    private RegisteredUser testUserPresent = new RegisteredUser("abc", "abc", "abc", "abc", "abc");
    private RegisteredUser testUserAbsent = new RegisteredUser("t2@test.com", "abc", "abc", "abc", "abc");
    private RegisteredUser[] invalidUsers = { new RegisteredUser("", "amc", "abc", "abc", "abc"),
            new RegisteredUser("", "abc", "abc", "abc", "abc"),
            new RegisteredUser("test", "abc", "", "abc", "abc"),
            new RegisteredUser("abc", "abc", "abc", "", "abc"),
            new RegisteredUser("abc", "", "abc", "abc", "abc")};

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
		for (RegisteredUser user : invalidUsers) {
		    assertFalse(user.isValidUser());
		}
		assertTrue(testUserAbsent.isValidUser());
	}

	/** TODO    -   Set up a working isPasswordValid test.
     * */
}
