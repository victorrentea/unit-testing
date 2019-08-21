package ro.victor.unittest.legacy.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ro.victor.unittest.legacy.exception.UserNotLoggedInException;
import ro.victor.unittest.legacy.model.Trip;
import ro.victor.unittest.legacy.model.User;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceShould {
    @Mock
    private TripDAO tripDAO;
    @Mock
    private UserSession userSession;
    @InjectMocks
    private TripService tripService;

    private final User user = new User();
    private User loggedUser = new User();

    public TripServiceShould() {
        System.out.println("Uaaa UaaA!");
    }

    @Before
    public  void initialize() {
        when(tripDAO.findTripsByUser(user)).thenReturn(emptyList());
        when(userSession.getLoggedUser()).thenReturn(loggedUser);
    }

    @Test(expected = UserNotLoggedInException.class)
    public void throwsForNoLoggedInUser() throws UserNotLoggedInException {
        when(userSession.getLoggedUser()).thenReturn(null);
        tripService.getTripsByUser(user);
    }
    @Test
    public void returnsEmptyListWhenUserHasNoFriends() throws UserNotLoggedInException {
        List<Trip> trips = tripService.getTripsByUser(user);
        assertThat(trips).isEmpty();
    }
    @Test
    public void returnsEmptyListWhenUserHasFriendDifferentThanLoggedUser() throws UserNotLoggedInException {
        User anotherUser = new User();
        user.addFriend(anotherUser);
        List<Trip> trips = tripService.getTripsByUser(user);
        assertEquals(emptyList(), trips);
    }
    @Test
    public void returnsTheTripsOfUserWhenShesFriendWithTheLoggedUser() throws UserNotLoggedInException {
        user.addFriend(loggedUser);
        List<Trip> x = asList(new Trip());
        when(tripDAO.findTripsByUser(user)).thenReturn(x);

        List<Trip> trips = tripService.getTripsByUser(user);

        assertEquals(x, trips);
    }
}
