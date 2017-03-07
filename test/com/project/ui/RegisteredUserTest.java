package com.project.ui;

import static org.junit.Assert.*;

import com.project.backend.RegisteredUser;
import com.project.backend.RegisteredUserDatabase;
import org.junit.Before;
import org.junit.Test;

public class RegisteredUserTest {

    private RegisteredUserDatabase testDatabase = RegisteredUserDatabase.getInstance();

    private RegisteredUser testUserPresent = new RegisteredUser("abc", "abc", "abc", "abc", "abc", "abc");
    private RegisteredUser testUserAbsent = new RegisteredUser("test2", "t2@test.com", "abc", "abc", "abc", "abc");
    private RegisteredUser[] invalidUsers = { new RegisteredUser("", "t@test.com", "abc", "abc", "abc", "abc"),
            new RegisteredUser("t", "", "abc", "abc", "abc", "abc"),
            new RegisteredUser("t", "test", "abc", "", "abc", "abc"),
            new RegisteredUser("t", "abc", "abc", "abc", "", "abc"),
            new RegisteredUser("t", "abc", "", "abc", "abc", "abc")};

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
