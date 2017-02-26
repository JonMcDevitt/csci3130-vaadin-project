package com.project;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RegisteredUserTest {

    private RegisteredUserDatabase testDatabase = RegisteredUserDatabase.getInstance();

    private RegisteredUser testUserPresent = new RegisteredUser("abc", "abc", "abc", "abc", "abc", "abc");
    private RegisteredUser testUserAbsent = new RegisteredUser("test", "t@test.com", "abc", "abc", "abc", "abc");
    private RegisteredUser[] invalidUsers = { new RegisteredUser("", "t@test.com", "abc", "abc", "abc", "abc"),
            new RegisteredUser("t", "", "abc", "abc", "abc", "abc"),
            new RegisteredUser("t", "test", "abc", "", "abc", "abc"),
            new RegisteredUser("t", "abc", "abc", "abc", "", "abc"),
            new RegisteredUser("t", "abc", "", "abc", "abc", "abc"),
            new RegisteredUser("t", "abc", "abc", "abc", "abc", "abc") };

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
	public void isValidInputTest() {
		for (RegisteredUser user : invalidUsers) {
		    assertFalse(user.isValidUser());
		}
		assertTrue(testUserAbsent.isValidUser());
	}
    //
    // public void isPasswordValid(){
    // RegisteredUser testUserTrue = new RegisteredUser("abc", "abc", "abc",
    // "abc", "abc", "abc", "abc");
    // RegisteredUser testUserFalse1 = new RegisteredUser("test", "t@test.com",
    // "ab", "abc", "abc", "abc", "abc");
    // RegisteredUser testUserFalse2 = new RegisteredUser("t", "test@test.com",
    // "abc", "ab", "abc", "abc", "abc");
    // RegisteredUserDatabase testDatabase = new RegisteredUserDatabase();
    // assertEquals(testUserTrue.passwordIsValid(),true);
    // assertEquals(testUserTrue.passwordIsValid(),false);
    // assertEquals(testUserTrue.passwordIsValid(),false);
    //
    //
    // }
}
