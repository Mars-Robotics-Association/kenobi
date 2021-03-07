package org.firstinspires.ftc.teamcode.OpMode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Core.Input.ControllerInput;
import org.firstinspires.ftc.teamcode.Core.Input.ControllerInputListener;
import org.firstinspires.ftc.teamcode.Core.Robots.CuriosityUltimateGoalControl;

@TeleOp(name = "*COMPETITION TELEOP*", group = "Competition")
@Config
public class CompetitionTeleOp extends OpMode implements ControllerInputListener
{
    ////Dependencies////
    private CuriosityUltimateGoalControl control;
    private ControllerInput controllerInput1;
    private ControllerInput controllerInput2;

    ////Variables////
    //Tweaking Vars
    public static double driveSpeed = 1;//used to change how fast robot drives
    public static double turnSpeed = 1;//used to change how fast robot turns

    private double speedMultiplier = 1;

    private boolean busy = false;
    private double turnOffset = 0;

    public static int payloadControllerNumber = 1;

    @Override
    public void init() {
        control = new CuriosityUltimateGoalControl(this, true, false, false);
        control.Init();

        controllerInput1 = new ControllerInput(gamepad1, 1);
        controllerInput1.addListener(this);
        controllerInput2 = new ControllerInput(gamepad2, 2);
        controllerInput2.addListener(this);

        telemetry.addData("Speed Multiplier", speedMultiplier);
        telemetry.update();

        control.StarpathToIntake();

        msStuckDetectLoop = 15000;
    }

    @Override
    public void start(){control.Start();}

    @Override
    public void loop() {
        controllerInput1.Loop();
        controllerInput2.Loop();

        if(!busy) {
            ManageDriving();
        }
        control.GetOrion().PrintVuforiaTelemetry(0);
        telemetry.update();
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
        if(controllerNumber == payloadControllerNumber){
            control.ModifyForPowerShot();
        }
    }

    @Override
    public void XPressed(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber){
            control.RotateStarpathToNextPos();
        }
    }

    @Override
    public void YPressed(double controllerNumber) {
        if(controllerNumber== payloadControllerNumber) control.ShootOne();
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
        if(controllerNumber == payloadControllerNumber) control.StopModifyForPowerShot();
    }

    @Override
    public void XReleased(double controllerNumber) {
    }

    @Override
    public void YReleased(double controllerNumber) {
    }

    @Override
    public void LBPressed(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber) control.AlignAndShoot();
    }

    @Override
    public void RBPressed(double controllerNumber) {

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
        if(controllerNumber == payloadControllerNumber) control.ShooterOn();
    }

    @Override
    public void LTHeld(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber) control.Intake();
    }

    @Override
    public void RTHeld(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber){
            control.LoadStarpath();
        }
    }

    @Override
    public void LBReleased(double controllerNumber) {

    }

    @Override
    public void RBReleased(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber) control.ShooterOff();
    }

    @Override
    public void LTReleased(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber) control.StopIntake();
    }

    @Override
    public void RTReleased(double controllerNumber) {
        if(controllerNumber == payloadControllerNumber){
            control.StopLoadStarpath();
        }
    }
}
