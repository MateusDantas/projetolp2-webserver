package webserver;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection.Response;
import org.junit.Test;

import ubet.api.Bets;
import ubet.api.Rooms;
import ubet.database.BetsDB;
import ubet.sv.BetServer;
import ubet.sv.Templates;
import ubet.sv.UserServer;
import ubet.util.StringTemplate;

public class TestBetServer {

	@Test
	public void test() throws Exception {

		/*HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);
		List<BetsDB> listBets = Bets.betsByUserByGames("1234", 10, 6, 87);
		if (listBets != null) {
			values.put("status", 1);
			String output = (new StringTemplate(
					Templates.GET_BETS_BY_USER_BY_ROOM_TLP)).getString(values);
			for (int i = 0; i < listBets.size(); i++) {
				int gameid = listBets.get(i).getGameId();
				int scoreone = listBets.get(i).getScoreTeamOne();
				int scoretwo = listBets.get(i).getScoreTeamTwo();

				HashMap<String, Object> nowBet = new HashMap<String, Object>();
				nowBet.put("gameid", gameid);
				nowBet.put("scoreone", scoreone);
				nowBet.put("scoretwo", scoretwo);

				output += ((new StringTemplate(
						Templates.GET_BETS_BY_USER_BY_ROOM_LIST_TLP))
						.getString(nowBet));
			}
			System.out.println(output);
		}*/
		int val = Rooms.getRomExtraLim(10);
		System.out.println(val);
	}

}
