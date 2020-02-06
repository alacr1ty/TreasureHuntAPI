import java.util.*;

public class example {

	public static void main (String[] args) {
		
		Scanner darkly = new Scanner(System.in);
		User testUser = null;

		System.out.println("Login or create new user...");

		// get username
		System.out.print("Username: ");
		String username = darkly.nextLine();

		// create new User object
		try {
			// if user exists in the database
			int userExists = User.userExists(username);
			if (userExists == 1) {
				System.out.println("User found.");

				boolean go = true;
				while (go == true) {
					// get user from database
					// new User (username)
					testUser = new User(username);

					// get password
					System.out.print("Password: ");
					String passwordTry = darkly.nextLine();

					// test if password is correct
					if (testUser.checkPassword(passwordTry)) {
						System.out.println("Logged in.");
						// do stuff here
						go = false;
					}
				}
			} else if (userExists == 0) {
				System.out.println("User not found.");

				// get email
				System.out.print("Enter email: ");
				String email = darkly.nextLine();

				// get password
				System.out.print("Set password: ");
				String password = darkly.nextLine();

				// create user and add to database
				// new User (username, email, password, avatarURL, coins, skillPoints)
				testUser = new User(username, email, password, 0, 0);

				System.out.println("User created.");
			} else if (userExists == -1) {
				System.out.println("Error occurred.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (testUser != null) {
				// print out results
				System.out.println("\nYour user ID: " + testUser.getUserId());
				System.out.println("Your username: " + testUser.getUsername());
				System.out.println("Your email: " + testUser.getEmail());
				System.out.println("Your password hash: " + testUser.getPasswordHash());
				System.out.println("Your coins: " + testUser.getCoins());
				System.out.println("Your skill points: " + testUser.getSkillPoints());
			} else {
				System.out.println("Failed to create User.");
			}
		}
	}
}
