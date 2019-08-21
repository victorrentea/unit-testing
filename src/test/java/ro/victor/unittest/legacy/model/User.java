package ro.victor.unittest.legacy.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	private List<User> friends = new ArrayList<>();
	
	public List<User> getFriends() {
		return friends;
	}
	
	public void addFriend(User friend) {
		this.friends.add(friend);
		friend.friends.add(this);
	}

    public boolean isFriend(User otherUser) {
		return friends.contains(otherUser);
    }
}
