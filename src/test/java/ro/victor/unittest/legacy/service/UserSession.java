package ro.victor.unittest.legacy.service;

import ro.victor.unittest.legacy.exception.ShouldBeMockedAwayException;
import ro.victor.unittest.legacy.model.User;

public class UserSession {
	private static UserSession INSTANCE;
	
	public static UserSession getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserSession();
		}
		return INSTANCE;
	}

	public User getLoggedUser() {
		throw new ShouldBeMockedAwayException("Depends on infra stuff");
	}
	
	
}
