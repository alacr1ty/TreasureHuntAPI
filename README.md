# TreasureHunt API - Usage

## Compiling

	// build .jar file
	$ ./make_jar.sh

## User class

### Database

	// check if user exists in the database
	int userExists = User.userExists(String username);
	
	// check if email exists in the database
	int emailExists = User.emailExists(String email);

	// create new user and add to database
	User john = new User (String username, String email, String password, int coins, int skillPoints);

	// get existing user from database
	User john = new User(String username);

### User IDs

	// get user ID
	int userId = john.getUserId();

	// no setUserId() : userId should not be changed.

### Usernames

	// get username
	String username = john.getUsername();

	// set username
	john.setUsername (String username);

### Emails

	// get email
	String email = john.getEmail();

	// set username
	john.setEmail (String email);

### Passwords

	// get password hash
	String passwordHash = john.getPasswordHash();

	// set password
	john.setPassword(String password);

	// check password
	john.checkPassword(String password);

### Coins

	// get coins
	int coins = john.getCoins();

	// add coins
	john.addCoins(int coins);
	
	// remove coins
	john.removeCoins(int coins);

### Skill Points

	// get skill points
	int skillPoints = john.getSkillPoints();

	// add skill points
	john.addSkillPoints(int points);

	// remove skill points
	john.removeSkillPoints(int points);
