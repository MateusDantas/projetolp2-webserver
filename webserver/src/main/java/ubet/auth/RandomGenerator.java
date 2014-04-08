package ubet.auth;

import java.util.UUID;

public class RandomGenerator {
	
	public String randomString()  {
		
		return UUID.randomUUID().toString();
	}
}
