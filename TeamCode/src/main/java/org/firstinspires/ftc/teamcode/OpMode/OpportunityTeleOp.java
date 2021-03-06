package org.firstinspires.ftc.teamcode.OpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Core.Input.ControllerInput;
import org.firstinspires.ftc.teamcode.Core.Input.ControllerInputListener;
import org.firstinspires.ftc.teamcode.Core.Robots.CuriosityUltimateGoalControl;
import org.firstinspires.ftc.teamcode.Core.Robots.MecanumBaseControl;
import org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi.IntakeController;
import org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi.ShooterController;
import org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi.WobbleGoalController;

import java.util.jar.Attributes;

@TeleOp(name = "Kenobi TeleOp", group = "Competition")
@Config
public class OpportunityTeleOp extends OpMode implements ControllerInputListener
{
    ////Dependencies////
    private MecanumBaseControl control;
    private ControllerInput controllerInput1;
    private ControllerInput controllerInput2;
    private WobbleGoalController wobble;
    private IntakeController intake;
    private ShooterController shooter;
    ////Variables////
    //Tweaking Vars
    public static double driveSpeed = 1;//used to change how fast robot drives
    public static double turnSpeed = 1;//used to change how fast robot turns

    private double speedMultiplier = 1;

    private boolean busy = false;
    private double turnOffset = 0;

    private int payloadController = 2;

    @Override
    public void init() {
        wobble = new WobbleGoalController();
        wobble.Init(
                hardwareMap.crservo.get("wobbleCRServo"),
                hardwareMap.servo.get("leftServo"),
                hardwareMap.servo.get("rightServo")
        );

        control = new MecanumBaseControl(this, true, true, true);
        control.InitCoreRobotModules();

        intake = new IntakeController();
        intake.Init(new CRServo[]{
                hardwareMap.crservo.get("intake_1"),
                hardwareMap.crservo.get("intake_2"),
                hardwareMap.crservo.get("intake_3")
        });

        shooter = new ShooterController();
        shooter.Init(new DcMotor[]{
                hardwareMap.dcMotor.get("shooter_1"),
                hardwareMap.dcMotor.get("shooter_2")
        });

        controllerInput1 = new ControllerInput(gamepad1, 1);
        controllerInput1.addListener(this);
        controllerInput2 = new ControllerInput(gamepad2, 2);
        controllerInput2.addListener(this);

        telemetry.addData("Speed Multiplier", speedMultiplier);
        telemetry.update();
    }

    @Override
    public void start(){control.StartCoreRobotModules();}

    private final double ArmMultiplier = 2;
    private double ArmDirection = 0;
    private boolean LeftBumper = false;
    private boolean RightBumper = false;
    private String item = "Nothing";

    @Override
    public void loop() {
        controllerInput1.Loop();
        controllerInput2.Loop();

        if(!busy) {
            ManageDriving();
        }

        if(LeftBumper){ArmDirection++;}
        if(RightBumper){ArmDirection--;}
        wobble.SetWobbleLiftPower(ArmDirection*ArmMultiplier);



    }

    private void ManageDriving() {
        double moveX = -gamepad1.left_stick_y*driveSpeed*speedMultiplier;
        double moveY = -gamepad1.left_stick_x*driveSpeed*speedMultiplier;
        double turn = gamepad1.right_stick_x*turnSpeed*speedMultiplier + turnOffset;
        control.GetOrion().MoveRaw(moveX, moveY, turn);
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
        if(controllerNumber == 1) {

            if (!item.equals("Nothing")) {
                item = "Nothing";
                wobble.ReleaseArm();
            }

            if (item.equals("Nothing")) {
                item = "Wobble";
                wobble.GrabWobbleGoal();
            }

        }
    }

    @Override
    public void XPressed(double controllerNumber) {
        if(controllerNumber == 2) {
            shooter.SetShooterPower(2);
        }
    }

    @Override
    public void YPressed(double controllerNumber) {
        if(controllerNumber == 1) {

            if (!item.equals("Nothing")) {
                item = "Nothing";
                wobble.ReleaseArm();
            }

            if (item.equals("Nothing")) {
                item = "Ring";
                wobble.GrabRing();
            }

        }
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
        if(controllerNumber == 2) {
            shooter.SetShooterPower(0);
        }
    }

    @Override
    public void YReleased(double controllerNumber) {
    }

    @Override
    public void LBPressed(double controllerNumber) {
        if(controllerNumber == 1) {
            LeftBumper = true;
        }
    }

    @Override
    public void RBPressed(double controllerNumber) {
        if(controllerNumber == 1) {
            RightBumper = true;
        }
    }

    @Override
    public void LTPressed(double controllerNumber) {
        if(controllerNumber == 1){
            wobble.RaiseWobbleLift();
        }
    }

    @Override
    public void RTPressed(double controllerNumber) {
        if(controllerNumber == 1){
            wobble.LowerWobbleLift();
        }
        if(controllerNumber == 2) {
            intake.SetIntakePower(2);
        }
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
        if(controllerNumber == 1) {
            LeftBumper = false;
        }
    }

    @Override
    public void RBReleased(double controllerNumber) {
        if(controllerNumber == 1) {
            RightBumper = false;
        }
    }

    @Override
    public void LTReleased(double controllerNumber) {

    }

    @Override
    public void RTReleased(double controllerNumber) {
        if(controllerNumber == 2) {
            intake.SetIntakePower(0);
        }
    }
}
