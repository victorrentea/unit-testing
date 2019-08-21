package ro.victor.unittest.legacy.service;


import ro.victor.unittest.legacy.exception.ShouldBeMockedAwayException;
import ro.victor.unittest.legacy.model.Trip;
import ro.victor.unittest.legacy.model.User;

import java.util.List;

public class TripDAO {

	public static List<Trip> findTripsByUser(User user) {
		throw new ShouldBeMockedAwayException("Depends on infra stuff");
	}

}
