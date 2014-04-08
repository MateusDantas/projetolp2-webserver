package webserver;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection.Response;
import org.junit.Test;

import ubet.api.Bets;
import ubet.api.Rooms;
import ubet.api.Users;
import ubet.database.BetsDB;
import ubet.sv.BetServer;
import ubet.sv.Templates;
import ubet.sv.UserServer;
import ubet.util.BCrypt;
import ubet.util.StringTemplate;

/**
 */
public class TestBetServer {

	/**
	 * Method test.
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {

		System.out.println(BCrypt.hashpw("12345", BCrypt.gensalt(4)));
		Users.createUser("Mateus", "Dantas", "mateuscd10@gmail.com", "MDantas", "123", 0);
	}

}
