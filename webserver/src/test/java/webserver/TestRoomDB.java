package webserver;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ubet.api.Rooms;
import ubet.database.RoomsDB;
import ubet.database.UserDB;
import ubet.database.UserRoomDB;
import ubet.sv.Templates;
import ubet.util.StringTemplate;

public class TestRoomDB {

	@Test
	public void test() throws SQLException {
		
		List<UserDB> usersInRoom = new ArrayList<UserDB>();
		System.out.println(Rooms.getPointsByUserInRoom("1234", 10));
		
	}

}