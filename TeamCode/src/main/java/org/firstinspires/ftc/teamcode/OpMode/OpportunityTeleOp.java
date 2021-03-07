package org.firstinspires.ftc.teamcode.OpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Core.Input.ControllerInput;
import org.firstinspires.ftc.teamcode.Core.Input.ControllerInputListener;

//import org.firstinspires.ftc.teamcode.Core.Robots.CuriosityUltimateGoalControl;

import org.firstinspires.ftc.teamcode.Core.Robots.MecanumBaseControl;
import org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi.WobbleGoalController;

@TeleOp(name = "Kenobi TeleOp", group = "Competition")
@Config
public class OpportunityTeleOp extends OpMode implements ControllerInputListener
{
    ////Dependencies////
    private WobbleGoalController wobble;
    private MecanumBaseControl control;
    private ControllerInput controllerInput1;
    private ControllerInput controllerInput2;

    private DcMotor FR;
    private DcMotor FL;
    private DcMotor RR;
    private DcMotor RL;

    private CRServo wobbleCR;
    private Servo leftServo;
    private Servo rightServo;


 //   private Servo liftservo;
  //  private Servo leftarm;
  //  private Servo rightarm;


    ////Variables////
    //Tweaking Vars
    public static double driveSpeed = 1;//used to change how fast robot drives
    public static double turnSpeed = 1;//used to change how fast robot turns

    private double speedMultiplier = 1;

    private boolean busy = false;//used to test if it's ok to drive
    private double turnOffset = 0;

    private int payloadController = 2;

    private double ArmMultiplier = 1;
    private boolean ArmPos = false;
    private boolean ArmNeg = false;

//map wheels FR, FL, RR, RL to the robot configuration
        @Override
        public void init() {
            FR = hardwareMap.dcMotor.get("FR");
            FL = hardwareMap.dcMotor.get("FL");
            RR = hardwareMap.dcMotor.get("RR");
            RL = hardwareMap.dcMotor.get("RL");

            //map the lift and the arms to the robot configuration

            wobbleCR = hardwareMap.crservo.get("wobbleCRServo");
            leftServo = hardwareMap.servo.get("leftServo");
            rightServo = hardwareMap.servo.get("rightServo");


            wobble = new WobbleGoalController();
            wobble.Init(
                  //  hardwareMap.crservo.get("liftservo"),
                  //  hardwareMap.servo.get("leftarm"),
                 //  hardwareMap.servo.get("rightarm")
            );
            control = new MecanumBaseControl(this, true, true, false);
        control.InitCoreRobotModules();

//>>>>>>> Stashed changes

        controllerInput2 = new ControllerInput(gamepad2, 2);
        controllerInput1 = new ControllerInput(gamepad1, 1);
        controllerInput1.addListener(this);
        controllerInput2.addListener(this);

        telemetry.addData("Speed Multiplier", speedMultiplier);
        telemetry.update();
    }

    @Override


   public void start(){
        control.StartCoreRobotModules();
        wobble.start();
    }


    @Override
    public void loop() {


        controllerInput1.Loop();
        controllerInput2.Loop();

        if(!busy) {
            ManageDriving();
        }

       // if(LeftBumper){ArmDirection++;}
       // if(RightBumper){ArmDirection--;}
        //wobble.SetWobbleLiftPower(ArmDirection*ArmMultiplier);

        int dir = 0;

        if(ArmPos){dir++;}
        if(ArmNeg){dir--;}

        wobble.SetWobbleLiftPower(dir*ArmMultiplier);

        wobble.Loop();


    }

    private void ManageDriving() {
        double moveX = -gamepad1.left_stick_y*driveSpeed*speedMultiplier;
        double moveY = -gamepad1.left_stick_x*driveSpeed*speedMultiplier;
        double turn = gamepad1.right_stick_x*turnSpeed*speedMultiplier + turnOffset;




        if(gamepad1.a){
            leftServo.setPosition(0);
            rightServo.setPosition(1);
        }

        if(gamepad1.b){
            leftServo.setPosition(1);
            rightServo.setPosition(0);//reverses the direction of the arm
        }

        if(gamepad1.dpad_up){
            wobbleCR.setPower(1);
        }
        if(gamepad1.dpad_down){
            wobbleCR.setPower(-1);

        }
        //if(gamepad1.dpad_up==false && gamepad1.dpad_down==false){
        //    wobbleCR.setPower(0);

        //}


        if(gamepad1.left_stick_y>0.5)

        FL.setPower(gamepad1.left_stick_y);
        RL.setPower(gamepad1.left_stick_y);


        FR.setPower(gamepad1.right_stick_y);
        RR.setPower(gamepad1.right_stick_y);





       // FL.setPower(gamepad1.left_stick_y);


     //   control.GetOrion().MoveRaw(moveX, moveY, turn);
    }

    @Override
    public void APressed(double controllerNumber) {
        if(controllerNumber == 1) {
            if (speedMultiplier == 1) speedMultiplier = 0.5;
            else if (speedMultiplier == 0.5) speedMultiplier = 0.25;
            else speedMultiplier = 1;
        }
    }

    @Override
    public void BPressed(double controllerNumber) {
        if(controllerNumber == 1){
            wobble.RaiseWobbleLift();
        }
    }

    @Override
    public void XPressed(double controllerNumber) {
        if(controllerNumber == 1){
            wobble.LowerWobbleLift();
        }
    }

    @Override
    public void YPressed(double controllerNumber) {

    }

    @Override
    public void AHeld(double controllerNumber) {

    }

    @Override
    public void BHeld(double controllerNumber) {

    }

    @Override
    public void XHeld(double controllerNumber) {
    }

    @Override
    public void YHeld(double controllerNumber) {
    }

    @Override
    public void AReleased(double controllerNumber) {

    }

    @Override
    public void BReleased(double controllerNumber)  {

    }

    @Override
    public void XReleased(double controllerNumber) {
    }

    @Override
    public void YReleased(double controllerNumber) {
    }

    @Override
    public void LBPressed(double controllerNumber) {
        if(controllerNumber == 1) {ArmPos = true;}
    }

    @Override
    public void RBPressed(double controllerNumber) {
        if(controllerNumber == 1) {ArmPos = true;}
    }

    @Override
    public void LTPressed(double controllerNumber) {

    }

    @Override
    public void RTPressed(double controllerNumber) {

    }

    @Override
    public void LBHeld(double controllerNumber) {

    }

    @Override
    public void RBHeld(double controllerNumber) {
    }

    @Override
    public void LTHeld(double controllerNumber) {

    }

    @Override
    public void RTHeld(double controllerNumber) {

    }

    @Override
    public void LBReleased(double controllerNumber) {
        if(controllerNumber == 1) {ArmPos = false;}
    }

    @Override
    public void RBReleased(double controllerNumber) {
        if(controllerNumber == 1) {ArmNeg = false;}
    }

    @Override
    public void LTReleased(double controllerNumber) {

    }

    @Override
    public void RTReleased(double controllerNumber) {

    }
}
