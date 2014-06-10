package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {

	private static UserController INSTANCE = null;
	
	private Map<String, User> usersMap = new HashMap<String, User>();
	
	public static UserController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserController();
		}
		return INSTANCE;
	}
	
	private UserController() {}
	
	public void addUser(User user) {
		usersMap.put(user.name, user);
	}
	
	public void deleteUser(User user) {
		usersMap.values().remove(user);
	}
	
	public User getUser(String userName) {
		return usersMap.get(userName);
	}
	
	public List<User> getUsers() {
		return new ArrayList<User>(usersMap.values());
	}
		
}
