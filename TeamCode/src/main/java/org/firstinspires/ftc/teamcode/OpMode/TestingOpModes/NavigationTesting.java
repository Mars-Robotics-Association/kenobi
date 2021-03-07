package org.firstinspires.ftc.teamcode.OpMode.TestingOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robots.BelindaControl;
import org.firstinspires.ftc.teamcode.Navigation.OrionNavigator;

@Config
@Autonomous(name = "NavigationTest")
public class NavigationTesting extends LinearOpMode {
    private BelindaControl control;
    private OrionNavigator orion;
    private FtcDashboard dashboard;

    public static double startX = 0;
    public static double startY = 0;
    public static double startH = 0;

    public static double moveX = 0;
    public static double moveY = 0;
    public static double moveH = 0;

    public static double turnAngle = 0;

    public static boolean isMovementSpline = false;

    @Override
    public void runOpMode() throws InterruptedException {
        control = new BelindaControl(this,true,false,true);
        control.Init();
        orion = control.GetOrion();
        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        waitForStart();
        orion.SetPose(startX, startY, Math.toRadians(startH));

        if(isMovementSpline) orion.MoveSpline(moveX, moveY, moveH, false);
        else orion.MoveLinear(moveX, moveY, moveH);

        /*if(moveX != 0 || moveY != 0) orion.MoveLinear(moveX, moveY, moveH);
        orion.TurnTo(turnAngle);*/
        while (!isStopRequested()){
            orion.PrintVuforiaTelemetry(0);
            orion.PrintTensorflowTelemetry();
            telemetry.update();
        }
    }

    /*@Override
    public void loop(){
        orion.SetTFCoefficients(tfDistCoef, tfXCoef);
        //orion.PrintVuforiaTelemetry(2);
        //orion.GoToDisc();
        orion.PrintTensorflowTelemetry();

        //orion.MoveToVumark(2, 0, 0, 0,5, 5);

        telemetry.update();

        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();
        //packet.put("target X ", control.GetImu().GetAngularVelocity());
        dashboard.sendTelemetryPacket(packet);
    }*/
}
