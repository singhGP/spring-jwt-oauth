package com.example.oAuth;

import java.awt.Robot;
import java.util.Random;

public class RoldeDrive {

	public static final int TEN_SECONDS = 60000;
	public static final int MAX_Y = 10;
	public static final int MAX_X = 10;

	public static void main(String[] args) throws Exception {

		Robot robot = new Robot();
		Random random = new Random();
		while (true) {
			robot.mouseMove(random.nextInt(MAX_X), random.nextInt(MAX_Y));
			Thread.sleep(TEN_SECONDS);
		}

	}


}
