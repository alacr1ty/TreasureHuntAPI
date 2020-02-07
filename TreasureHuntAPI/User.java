package TreasureHuntAPI;

import java.util.*;
import java.sql.*;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;



public class User {
	private int userId;
	private String username;
	private String email;
	private String passwordHash;
	private int coins;
	private int skillPoints;
	private List<TreasureHunt> treasureHunts;
	private TreasureHunt activeTreasureHunt;

	// get existing user from database
	public User (String username) throws Exception {
		int userExists = userExists(username);

		if (userExists == 0) {
			throw new Exception ("User doesn't exist.");			
		} else if (userExists == 1) {
			// otherwise
			Connection con = connectDB();
			Statement stmt = null;
			ResultSet rs = null;

			try {
				stmt = con.createStatement();
			
				// the query will be a SELECT...
				if (stmt.execute("SELECT * FROM Users WHERE (username='" + username + "')")) {
					rs = stmt.getResultSet();
				}

				while (rs.next()) {
					this.userId = rs.getInt("userId");
					this.username = rs.getString("username");
					this.email = rs.getString("email");
					this.passwordHash = rs.getString("passwordHash");
					this.coins = rs.getInt("coins");
					this.skillPoints = rs.getInt("skillPoints");
					// this.treasureHunts = new ArrayList<TreasureHunt>();
					// this.activeTreasureHunt;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (con != null)
				disconnectDB(con);
			}
		} else
			throw new Exception("Database error.");
	}

	// create new user and add to database
	public User (String username, String email, String password, int coins, int skillPoints) throws Exception {
		int userExists = userExists(username);

		// if user exists in Database, throw exception
		if (userExists == 1) {
			throw new Exception ("User already exists.");			
		} else if (userExists == 0) {
			// otherwise
			this.username = username;
			this.email = email;
			this.coins = coins;
			this.skillPoints = skillPoints;
			// this.treasureHunts = new ArrayList<TreasureHunt>();
			// this.activeTreasureHunt;
			
			updateUser();
			this.userId = getUserIdDB();
			setPassword(password);
		} else
			throw new Exception("Database error.");
	}

	public int getUserId () {
		return userId;
	}

	private int getUserIdDB () {
		// search database for user
		Connection con = connectDB();
		Statement stmt = null;
		ResultSet rs = null;
		int res = -1;

		try {
			stmt = con.createStatement();
		
			// the query will be a SELECT...
			if (stmt.execute("SELECT * FROM Users WHERE (username='" + username + "')")) {
				rs = stmt.getResultSet();
			}

			while (rs.next()) {
				res = rs.getInt("userId");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				disconnectDB(con);
			return res;
		}

	}

	// no setUserId() : userId should not be changed.

	public String getUsername () {
		return username;
	}

	public void setUsername (String username) throws Exception {
		int oldUserExists = userExists(this.username);
		int newUserExists = userExists(username);

		if (oldUserExists == 1) {
			if (newUserExists == 0) {
				this.username = username;
				updateUser();
			} else if (newUserExists == 1) {
				throw new Exception("Username taken.");
			}
		} else if (oldUserExists == 0) {
			throw new Exception("User not found.");
		}
	}

		// no setUserId() : userId should not be changed.

	public String getEmail () {
		return email;
	}

	public void setEmail (String email) throws Exception {
		int oldUserExists = userExists(this.username);
		int newUserExists = emailExists(email);

		if (oldUserExists == 1) {
			if (newUserExists == 0) {
      			String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
      			if (email.matches(regex)) {
					this.email = email;
					updateUser();
				}
			} else if (newUserExists == 1) {
				throw new Exception("Email taken.");
			}
		} else if (oldUserExists == 0) {
			throw new Exception("User not found.");
		}
	}

	public String getPasswordHash () {
		return this.passwordHash;
	}

	public void setPassword (String password) {
		int userExists = userExists(username);

		if (userExists == 1) {
			try {
				passwordHash = getSaltedHash(password);
				this.passwordHash = passwordHash;
				updatePassword(passwordHash);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private static String getSaltedHash(String password) throws Exception {
		byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);
		// store the salt with the password
		return Base64.getEncoder().encodeToString(salt) + "$" + hash(password, salt);
	}

	private static String hash(String password, byte[] salt) throws Exception {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException("Empty passwords are not supported.");
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt, 20000, 256));
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public boolean checkPassword(String passwordTry) throws Exception {

		String[] saltAndHash = passwordHash.split("\\$");
		if (saltAndHash.length != 2) {
			throw new IllegalStateException("The stored password must have the form 'salt$hash'");
		}
		String hashOfInput = hash(passwordTry, Base64.getDecoder().decode(saltAndHash[0]));
		return hashOfInput.equals(saltAndHash[1]);
	}

	public int getCoins () {
		return this.coins;
	}

	public void addCoins (int coins) {
		int userExists = userExists(username);
		
		if (userExists == 1) {
			this.coins += coins;
			updateUser();
		}
	}

	public void removeCoins (int coins) {
		int userExists = userExists(username);
		
		if (userExists == 1) {
			this.coins -= coins;
			updateUser();
		}
	}

	public int getSkillPoints () {
		return this.skillPoints;
	}

	public void addSkillPoints (int points) {
		int userExists = userExists(username);
		
		if (userExists == 1) {
			this.skillPoints += points;
			updateUser();
		}
	}

	public void removeSkillPoints (int points) {
		int userExists = userExists(username);
		
		if (userExists == 1) {
			this.skillPoints -= points;
			updateUser();
		}
	}

	// public List<TreasureHunt> getUserHunts () {
	// 	return
	// }

	// public void addTreasureHunt (TreasureHunt treasureHunt) {
	// 	this.treasureHunts.add(treasureHunt);
	// }

	// public void removeTreasureHunt (TreasureHunt treasureHunt) {
	// 	this.treasureHunts.remove(treasureHunt);
	// }

	// public TreasureHunt getActiveTreasureHunt () {
	// 	return this.activeTreasureHunt;
	// }

	// public void setActiveTreasureHunt (TreasureHunt treasureHunt) {
	// 	this.activeTreasureHunt = treasureHunt;
	// }

	private void updateUser () {
		Connection con = connectDB();

		int userExists = userExists(username);
		
		if (userExists == 1)
			updateUserDB(con);
		else
		 	createUserDB(con);

		disconnectDB(con);

		return;
	}

	private void updatePassword (String password) {
		Statement stmt = null;
		ResultSet rs = null;

		Connection con = null;

		try {
			con = connectDB();

			stmt = con.createStatement();
		
			stmt.executeUpdate(
				"UPDATE `Users` SET " +
				"`passwordHash`='" + password +
				"' WHERE username = '" + username + "'"
			);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				disconnectDB(con);
		}

		return;
	}

	private static int userExistsHelper (String username,Connection con) {
		// search database for user
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();

			// the query will be a SELECT...
			if (stmt.execute("SELECT * FROM Users WHERE (username='" + username + "')")) {
				rs = stmt.getResultSet();

				if (rs.next())
					return 1;
				return 0;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return -1;
	}

	public static int userExists (String username) {

		
		Connection con = null;
		int exists = -1;

		try {
			con = connectDB();
			exists = userExistsHelper(username, con);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				disconnectDB(con);
			return exists;			
		}
	
	}

	private static int emailExistsHelper (String email,Connection con) {
		// search database for user
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();

			// the query will be a SELECT...
			if (stmt.execute("SELECT * FROM Users WHERE (email='" + email + "')")) {
				rs = stmt.getResultSet();

				if (rs.next())
					return 1;
				return 0;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return -1;
	}

	public static int emailExists (String email) {

		
		Connection con = null;
		int exists = -1;

		try {
			con = connectDB();
			exists = emailExistsHelper(email, con);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (con != null)
				disconnectDB(con);
			return exists;		
		}
	
	}

	private static Connection connectDB () {
		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();

			DriverManager.setLoginTimeout(5);

			// // Establishing Connection 
			// con = DriverManager.getConnection(
			// 	"jdbc:mysql://classmysql.engr.oregonstate.edu:3306/capstone_2019_treasurehunt?" +
			// 	"useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=PST",
			// 	"capstone_2019_treasurehunt",
			// 	"KiWXM1cStAboVBiF");

			// Establishing Connection 
			con = DriverManager.getConnection(
				"jdbc:mysql://classmysql.engr.oregonstate.edu:3306/capstone_2019_treasurehunt",
				"capstone_2019_treasurehunt",
				"KiWXM1cStAboVBiF");

		} catch (Exception ex) {
			ex.printStackTrace();
			// throw ex;
		}

		return con;
	}

	private void updateUserDB (Connection con) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		
			stmt.executeUpdate(
				"UPDATE `Users` SET " +
				"`userId`='" + userId +
				"',`coins`='" + coins + 
				"',`skillPoints`='" + skillPoints +
				"' WHERE username = '" + username + "'"
			);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return;
	}

	private void createUserDB (Connection con) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
		
			stmt.executeUpdate("INSERT INTO `Users`(`userId`, `username`, `email`, `passwordHash`, `coins`, `skillPoints`) VALUES ('" + userId + "', '" + username + "', '" + email + "', '" + passwordHash + "', '" + coins + "', '" + skillPoints + "')");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return;
	}

	private static void disconnectDB (Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return;
	}

}
