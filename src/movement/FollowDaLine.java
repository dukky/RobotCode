package movement;


import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
//same story as the junction code but this is exactly the same
public class FollowDaLine {

	RegulatedMotor motorRight;
	RegulatedMotor motorLeft;
	public DifferentialPilot chell;
	LightSensor lsLeft = new LightSensor(SensorPort.S1);
	LightSensor lsRight = new LightSensor(SensorPort.S4);

	public FollowDaLine(RegulatedMotor mR, RegulatedMotor mL) {
		motorRight = mR;
		motorLeft = mL;
		chell = new DifferentialPilot(56, 162, motorLeft, motorRight);
		chell.setTravelSpeed(100);
	}

	public void run() {

		chell.forward();
		if ((lsLeft.getLightValue() - lsRight.getLightValue()) > 2)
			chell.arcForward(-20);
		else if ((lsLeft.getLightValue() - lsRight.getLightValue()) < -2)
			chell.arcForward(20);
		Delay.msDelay(20);
	}

	public static void main(String[] args) {
		RegulatedMotor motorRight = Motor.C;
		RegulatedMotor motorLeft = Motor.B;
	//	boolean keepFollowing = true;
		FollowDaLine follow = new FollowDaLine(motorRight, motorLeft);
		while (true) {
			follow.run();
		}
	}

}
