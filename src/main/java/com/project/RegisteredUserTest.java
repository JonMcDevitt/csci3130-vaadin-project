package com.project;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisteredUserTest {

	@Test
	public void isValidTest() {
		//fail("Not yet implemented");
		RegisteredUser testUserTrue = new RegisteredUser("abc", "abc", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testUserFalse1 = new RegisteredUser("test", "t@test.com", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testUserFalse2 = new RegisteredUser("t", "test@test.com", "abc", "abc", "abc", "abc", "abc");
		RegisteredUserDatabase testDatabase = new RegisteredUserDatabase();
		
		//user is valid
		assertEquals(testUserTrue.isValid(testDatabase), true);
		
		//user is not valid
		assertEquals(testUserFalse1.isValid(testDatabase), false);
		assertEquals(testUserFalse2.isValid(testDatabase), false);
	}
	
	@Test
	public void isValidInputTest() {
		//fail("Not yet implemented");
		RegisteredUser testInputTrue = new RegisteredUser("abc", "abc", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testInputFalse1 = new RegisteredUser("", "t@test.com", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testInputFalse2 = new RegisteredUser("t", "", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testInputFalse3 = new RegisteredUser("t", "test", "abc", "abc", "", "abc", "abc");
		RegisteredUser testInputFalse4 = new RegisteredUser("t", "abc", "abc", "abc", "abc", "", "abc");
		RegisteredUser testInputFalse5 = new RegisteredUser("t", "abc", "", "abc", "abc", "abc", "abc");
		RegisteredUser testInputFalse6 = new RegisteredUser("t", "abc", "abc", "", "abc", "abc", "abc");
		RegisteredUserDatabase testDatabase = new RegisteredUserDatabase();
		
		//user is valid
		assertEquals(testInputTrue.isValidInput(testInputTrue.getUserName(), testInputTrue.getEmail(), testInputTrue.getFirstName(),
				testInputTrue.getLastName(), testInputTrue.getPassword(), testInputTrue.getValidationPassword()), true);
		
		//user is not valid
		assertEquals(testInputFalse1.isValidInput(testInputFalse1.getUserName(), testInputFalse1.getEmail(), testInputFalse1.getFirstName(),
				testInputFalse1.getLastName(), testInputFalse1.getPassword(), testInputFalse1.getValidationPassword()), false);
		assertEquals(testInputFalse2.isValidInput(testInputFalse2.getUserName(), testInputFalse2.getEmail(), testInputFalse2.getFirstName(),
				testInputFalse2.getLastName(), testInputFalse2.getPassword(), testInputFalse2.getValidationPassword()), false);
		assertEquals(testInputFalse3.isValidInput(testInputFalse3.getUserName(), testInputFalse3.getEmail(), testInputFalse3.getFirstName(),
				testInputFalse3.getLastName(), testInputFalse3.getPassword(), testInputFalse3.getValidationPassword()), false);
		assertEquals(testInputFalse4.isValidInput(testInputFalse4.getUserName(), testInputFalse4.getEmail(), testInputFalse4.getFirstName(),
				testInputFalse4.getLastName(), testInputFalse4.getPassword(), testInputFalse4.getValidationPassword()), false);
		assertEquals(testInputFalse5.isValidInput(testInputFalse5.getUserName(), testInputFalse5.getEmail(), testInputFalse5.getFirstName(),
				testInputFalse5.getLastName(), testInputFalse5.getPassword(), testInputFalse5.getValidationPassword()), false);
		assertEquals(testInputFalse6.isValidInput(testInputFalse6.getUserName(), testInputFalse6.getEmail(), testInputFalse6.getFirstName(),
				testInputFalse6.getLastName(), testInputFalse6.getPassword(), testInputFalse6.getValidationPassword()), false);
	}
	
	public void isPasswordValid(){
		RegisteredUser testUserTrue = new RegisteredUser("abc", "abc", "abc", "abc", "abc", "abc", "abc");
		RegisteredUser testUserFalse1 = new RegisteredUser("test", "t@test.com", "ab", "abc", "abc", "abc", "abc");
		RegisteredUser testUserFalse2 = new RegisteredUser("t", "test@test.com", "abc", "ab", "abc", "abc", "abc");
		RegisteredUserDatabase testDatabase = new RegisteredUserDatabase();
		assertEquals(testUserTrue.passwordIsValid(),true);
		assertEquals(testUserTrue.passwordIsValid(),false);
		assertEquals(testUserTrue.passwordIsValid(),false);
		
		
	}
}
