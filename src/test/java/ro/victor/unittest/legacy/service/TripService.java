package ro.victor.unittest.legacy.service;
// Original by Sandro Mancuso: https://www.youtube.com/watch?v=_NnElPO5BU0

import ro.victor.unittest.legacy.exception.UserNotLoggedInException;
import ro.victor.unittest.legacy.model.Trip;
import ro.victor.unittest.legacy.model.User;

import java.util.List;

import static java.util.Collections.emptyList;

public class TripService {
    private final TripDAO tripDao;
    private final UserSession userSession;

    public TripService(TripDAO tripDao, UserSession userSession) {
        this.tripDao = tripDao;
        this.userSession = userSession;
    }

    public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
        User loggedUser = userSession.getLoggedUser();
        if (loggedUser == null) {
            throw new UserNotLoggedInException();
        }
		if (user.isFriend(loggedUser)) {
            return tripDao.findTripsByUser(user);
        } else {
			return emptyList();
		}

    }

}
