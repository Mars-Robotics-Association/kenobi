package org.firstinspires.ftc.teamcode.Core.Robots;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Core.PIDController;
import org.firstinspires.ftc.teamcode.MechanicalControl.Belinda.BelindaChassis;
import org.firstinspires.ftc.teamcode.Navigation.OrionNavigator;
import org.firstinspires.ftc.teamcode.Sensors.IMU;

@Config
public class MecanumBaseControl
{
    ////Calibration////
    //Movement Towards Discs
    public static double discMoveCoefficient = -0.0015;
    public static double discTurnCoefficient = -0.0015;
    public static double discMoveSpeed = 0.2;
    //Turn to Vumark
    public static double vumarkTurnCoefficient = -0.05;
    public static int targetVumarkID = 0;

    ////Dependencies////
    //Mechanical Components
    protected BelindaChassis chassis;
    //Core
    protected PIDController pidController; //Look here: https://github.com/tekdemo/MiniPID-Java for how to use it
    protected IMU imu;
    //Orion Navigator
    protected OrionNavigator orion;
    //OpMode
    protected OpMode currentOpMode;
    //Drive Motors
    protected DcMotor FR;
    protected DcMotor FL;
    protected DcMotor RR;
    protected DcMotor RL;

    //Util
    protected double gyroOffset;

    //TODO: ===ROBOT CONFIGURATION===
    protected boolean USE_CHASSIS = true;
    protected boolean USE_PAYLOAD = false;
    protected boolean USE_NAVIGATOR = false;
    public boolean isUSE_CHASSIS(){return USE_CHASSIS;}
    public boolean isUSE_PAYLOAD(){return USE_PAYLOAD;}
    public boolean isUSE_NAVIGATOR(){return USE_NAVIGATOR;}


    //Initializer
    public MecanumBaseControl(OpMode setOpMode, boolean useChassis, boolean usePayload, boolean useNavigator)
    {
        currentOpMode = setOpMode;
        USE_CHASSIS = useChassis;
        USE_PAYLOAD = usePayload;
        USE_NAVIGATOR = useNavigator;
    }

    //TODO: Call this on Init()
    public void InitCoreRobotModules(){
        //TODO: ==INIT CORE MODULES==
        imu = new IMU(currentOpMode);
        pidController = new PIDController(0,0,0);

        if(USE_NAVIGATOR) {
            //TODO: ===INIT ORION===
            orion = new OrionNavigator(currentOpMode, this);
            orion.Init();
        }

        //TODO: ===INIT CHASSIS===
        if(USE_CHASSIS) {
            FR = currentOpMode.hardwareMap.dcMotor.get("FR");
            FL = currentOpMode.hardwareMap.dcMotor.get("FL");
            RR = currentOpMode.hardwareMap.dcMotor.get("RR");
            RL = currentOpMode.hardwareMap.dcMotor.get("RL");
        }
        if(USE_CHASSIS) {
            chassis = new BelindaChassis(imu, FR, FL, RR, RL, currentOpMode.telemetry);//Create chassis instance w/ motors
            chassis.Init();
        }
    }

    //TODO: Call this on Start()
    public void StartCoreRobotModules(){
        imu.Start();
        imu.ResetGyro();
    }

    //TODO: UNIVERSAL PUBLIC METHODS
    public void RawDrive(double angle, double speed, double turnOffset) {
        //Used in continuously in teleop to move robot at any angle using imu and pid controller
        //Enter angle to move, speed, and a turn offset for turning while moving
        chassis.MoveAtAngle(angle, speed, turnOffset);
    }
    public void RawTurn(double speed){
        //Used continuously in teleop to turn the robot
        //Enter speed for turn- positive speed turns left, negative right
        chassis.SpotTurn(speed);
    }
    public void Brake(){
        //Called once to brake the robot
        chassis.Brake();
    }
    public void OdometryDrive(double angle, double speed, double distance) {
        //Used to autonomously drive a certain distance at a certain angle.
        //Enter angle, speed, and distance
    }
    public void SpotTurn(double angle, double speed) {
        //Turns the robot on center of the wheel axis using a ramp turn
        //Enter target angle and turn speed
    }
    public void SweepTurn(double angle, double speed, double turnOffset) {
        //Turns the robot gradually
        //Enter target angle, speed, and turn offset
    }
    public void MoveTowardsClosestDisc(){ orion.MoveTowardsDiscRaw(discMoveSpeed, discMoveCoefficient); }
    public double TurnTowardsClosestDiscSpeed(){return orion.TurnTowardsDiscSpeed(discTurnCoefficient);}

    public void TurnTowardsVuMark(){orion.TurnTowardsVuMark(1, targetVumarkID, vumarkTurnCoefficient, true);}

    //TODO: UNIVERSAL GETTERS
    public OrionNavigator GetOrion(){return orion;}
    public IMU GetImu(){return imu;}
    public BelindaChassis GetChassis(){return chassis;}
    public PIDController GetPID(){return chassis.GetHeadingPID();}
    public OpMode GetOpMode(){return currentOpMode;}

    //TODO: SETTER METHODS
    public void SetDrivePID(double p, double i, double d){
        chassis.SetPIDCoefficients(p,i,d);
    }

    //TODO: PRIVATE METHODS
    private void OffsetGyro(){
        //Offsets the gryo so the current heading can be zero with GetRobotAngle()
        gyroOffset = imu.GetRawAngles().firstAngle;
    }
}
