package org.firstinspires.ftc.teamcode.OpMode.Belinda;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Core.Robots.CuriosityUltimateGoalControl;
import org.firstinspires.ftc.teamcode.Navigation.OrionNavigator;

@Config
@Autonomous(name = "*WOBBLE GOAL*", group = "Competition")
public class WobbleGoalAutonomous extends LinearOpMode {
    private CuriosityUltimateGoalControl control;
    private OrionNavigator orion;
    private FtcDashboard dashboard;

    public static double tfDistCoef = 6666;
    public static double tfXCoef = 0.001;

    public static double robotX = 0;
    public static double robotY = -4;
    public static double robotH = 180;

    public static double powerShotStartAngle = -21;
    public static double powerShotIncrament = -3.5;

    @Override
    public void runOpMode() throws InterruptedException {
        control = new CuriosityUltimateGoalControl(this,true,true,true);
        control.Init();
        orion = control.GetOrion();
        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        waitForStart();
        orion.SetPose(robotX, robotY, Math.toRadians(robotH));//robot starts on blue left line

        control.StarpathToShooter();

        //Move to where it can see discs
        orion.MoveLinear(10, 0, 0);

        sleep(500);//wait for tensorflow to detect discs
        int numberOfDiscs = orion.GetNumberOfDiscs();//figure out where to go

        //Go to square A
        orion.MoveSpline(70, 12, 0, true);

        if(numberOfDiscs == 0){ //A
            //deposit wobble goal and go to shoot position
            orion.MoveLinear(60, 12, 0);
            orion.MoveLinear(60, -36, 0);
        }
        if(numberOfDiscs > 0 && numberOfDiscs < 3){ //B
            //spline to B, deposit, and go to shoot position
            orion.MoveSpline(94, -12, 0, true);
            orion.MoveLinear(84, -12, 0);
            orion.MoveLinear(60, -36, 0);
        }
        else { //C
            //keep going forwards, deposit, and go to shoot position
            orion.MoveLinear(112, 12, 0);
            orion.MoveLinear(102, 12, 0);
            orion.MoveLinear(60, -36, 0);
        }


    }

    private void PowerShotRoutine(){
        //Start at (x,y)
        control.ModifyForPowerShot();
        control.ShooterOn();

        //turn to first shot
        orion.Turn(powerShotStartAngle);
        control.ShootOne();
        control.ShooterOn();

        control.ModifyForPowerShot();
        //turn to second shot
        orion.Turn(powerShotIncrament);
        control.ShootOne();
        control.ShooterOn();

        control.ModifyForPowerShot();
        //turn to third shot
        orion.Turn(powerShotIncrament);
        control.ShootOne();

        //Reset shooter
        control.ShooterOff();
        control.StopModifyForPowerShot();
    }
    /*private void Shoot(){
        control.ShootAsync();
        while(control.IsShooterRunning()){
            control.ShootAsync();
            telemetry.addLine("Shooting");
            telemetry.update();
        }
        control.StopShootAsync();
        control.ShooterOn();
    }*/
}

/*if(numberOfDiscs == 0){ //go to A
        telemetry.addLine("close target");
        orion.MoveSpline(30, 12, 0);//drop off wobble goal 1
        orion.MoveLinear(-62, -36, 0);//go to second wobble goal
        orion.MoveLinear(0, -24, 0);
        orion.MoveSpline(62, 60, 0);//places it
        }

        else if(numberOfDiscs > 0 && numberOfDiscs < 3){ //go to B
        telemetry.addLine("middle target");
        orion.MoveSpline(54, -2, 0);//drop off wobble goal 1
            *//*orion.MoveLinear(-10, -30, 0);
            orion.MoveLinear(-72, 0, 0);*//*
        orion.MoveLinear(-44,-50, 0);//heads back
        orion.MoveLinear(-40,0, 0);
        orion.MoveLinear(0,20,0);//lines up for second wobble goal
        orion.MoveLinear(30,-3,0);//places it
        orion.MoveSpline(56,10,0);
        orion.MoveLinear(-15,0,0);//goes back to line

        }

        else{ //go to C
        telemetry.addLine("far target");
        orion.MoveSpline(78, 12, 0);//drop off wobble goal 1
        orion.MoveLinear(-68,-52, 0);//heads back
        orion.MoveLinear(-42,0, 0);
        orion.MoveLinear(0,16,0);//lines up for second wobble goal
        orion.MoveLinear(30,0,0);//places it
        orion.MoveSpline(80,34,0);
        }*/
