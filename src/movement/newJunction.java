package movement;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.RegulatedMotor;
import lejos.util.Delay;
//junction code, effectively the same as what was submitted before, but just has.
//better calibration code and only just goes to the next junction.
//but there are no comments here..., you can read the comments in the old code submitted for ex3.

public class newJunction {

	RegulatedMotor motorRight;
	RegulatedMotor motorLeft;
	LightSensor lsLeft = new LightSensor(SensorPort.S1);
	LightSensor lsRight = new LightSensor(SensorPort.S4);
	public OpticalDistanceSensor sonar = new OpticalDistanceSensor(SensorPort.S2);
	boolean keepFollowing = true;
	public int leftBlack;
	public int leftWhite;
	public int rightBlack;
	public int rightWhite;
	public FollowDaLine follow;
int count=0;
	public newJunction() {
		RegulatedMotor mR = Motor.C;
		RegulatedMotor mL = Motor.B;
		motorRight = mR;
		motorLeft = mL;
		follow = new FollowDaLine(motorRight, motorLeft);
		System.out.println("Press button to calibrate");
		Button.waitForAnyPress();
		Delay.msDelay(500);
		leftBlack = lsLeft.getLightValue();
		rightBlack = lsRight.getLightValue();
		follow.chell.travel(80);
		leftWhite = lsLeft.getLightValue();
		rightWhite = lsRight.getLightValue();
		System.out.println("Calibration complete");
		//Button.waitForAnyPress();
		follow.chell.setTravelSpeed(160);
		RConsole.openBluetooth(0);
	}

	public void run() {
		while (true) {
			//if (sonar.getDistance() > 30) {
			
				follow.run();
				//RConsole.println(lsLeft.getLightValue() +" "+ lsRight.getLightValue() + " " + count++);
				if (lsLeft.getLightValue() < (leftBlack + leftWhite) / 2
						&& lsRight.getLightValue() < (rightBlack + rightWhite) / 2) {
					Sound.playNote(Sound.XYLOPHONE, 1000, 100);
					follow.chell.travel(80);
					break;
				}
			//}
		}
	}

	public static void main(String[] args) {
		newJunction thisjunc = new newJunction();
		while(true){
		thisjunc.run();
		}
	}

}
