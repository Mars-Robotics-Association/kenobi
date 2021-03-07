package org.firstinspires.ftc.teamcode.MechanicalControl.Curiosity;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class CuriosityPayloadController
{
    ////DEPENDENCIES////
    OpMode opMode;
    //Motors
    private DcMotor[] shooterMotors;
    //Servos
    private Servo intakeServo1;
    private Servo intakeServo2;
    private Servo bootServo;
    private Servo starpathServo;
    private Servo loaderServo;

    //Sensors
    private DistanceSensor intakeDetector;
    private DistanceSensor shooterDetector;

    ////CONFIG VARIABLES////
    public static double bootInPos = 0.7;
    public static double bootOutPos = 0.45;

    public static double starpathDownPos = 0;
    public static double starpathInterval = 0.14;
    public static double starpathUpPos = 0.32;

    public static double loaderClearPos = 0.6;
    public static double loaderLoadPos = 1;

    public static double shooterSpeedMultiplier = -1;
    public static double powerShotSpeedMultiplier = 0.75;

    public static double timeToShoot = 0.8;
    public static double timeToRetract = 0.5;
    public static double timeToMoveToNext = 0.5;

    ////PUBLIC VARIABLES////
    public boolean shooterRunning = false;

    public boolean stopShooterOverride = false;

    ////PRIVATE VARIABLES////
    private double shooterStartTime = 0;
    private double loaderStartTime = 0;
    private boolean loadFromIntakeRunning = false;
    private int starpathPosition = 0; //starts at 0, 3 is to the shooter, 5 is max
    private int discsShot = 3;
    private boolean loaderUsed = false;
    private boolean starpathUsed = false;
    private boolean bootUsed = false;
    public boolean powerShot = false;

    public void Init(OpMode setOpMode, DcMotor[] setShooterMotors, Servo setIntakeServo1, Servo setIntakeServo2, Servo setBootServo, Servo setStarpathServo, Servo setLoaderServo){
        opMode = setOpMode;

        shooterMotors = setShooterMotors;

        intakeServo1 = setIntakeServo1;
        intakeServo2 = setIntakeServo2;
        bootServo = setBootServo;
        starpathServo = setStarpathServo;
        loaderServo = setLoaderServo;


    }

    private void SetShooterPower(double power){
        int i = 0;
        for (DcMotor motor: shooterMotors) {
            motor.setPower(power);
            i++;
        }
    }

    private void BootDisc(){bootServo.setPosition(bootInPos);}
    private void BootMiddle(){bootServo.setPosition((bootOutPos+bootInPos)/2);}
    private void BootReset(){bootServo.setPosition(bootOutPos);}

    public void StarpathToIntake(){
        starpathServo.setPosition(starpathDownPos);
        discsShot = 3;
        starpathPosition = 0;
    }
    private void StarpathMoveInterval(){starpathServo.setPosition(starpathServo.getPosition()+starpathInterval);}
    public void StarPathToShooter(){
        starpathServo.setPosition(starpathUpPos);
        discsShot = 0;
        starpathPosition = 3;
    }

    private void LoaderClear(){loaderServo.setPosition(loaderClearPos);}
    private void LoaderLoad(){loaderServo.setPosition(loaderLoadPos);}

    public void ShooterOn(){
        double modifier = shooterSpeedMultiplier;
        if(powerShot) modifier *= powerShotSpeedMultiplier;
        SetShooterPower(modifier);
    }
    public void ShooterOff(){SetShooterPower(0);}

    public void RotateStarpathToNextPos(){
        //if (intakeDetector.getDistance(DistanceUnit.CM) < 20 && starpathPosition == 5 || shooterDetector.getDistance(DistanceUnit.CM) < 20)
            //return;

        starpathPosition++;

        //if next pos is 6, reset back to 0
        if(starpathPosition == 6){
            StarpathToIntake();
        }
        //if next pos is 3, go to the shooter
        else if(starpathPosition == 3) StarPathToShooter();
        //else, add with the interval
        else StarpathMoveInterval();
    }

    public void Intake(){
        //if starpath not at intake and its shot all discs, return it to intake
        if(starpathPosition > 2 && !loadFromIntakeRunning && discsShot == 3){
            StarpathToIntake();
        }
        discsShot = 0;
        //run intake
        intakeServo1.setPosition(1);
        intakeServo2.setPosition(0);
        if(!loadFromIntakeRunning) BootReset();
    }
    public void StopIntake(){
        intakeServo1.setPosition(0.5);
        intakeServo2.setPosition(0.5);
    }

    public void LoadFromIntake(){
        //start timer
        if(!loadFromIntakeRunning)loaderStartTime = opMode.getRuntime();
        loadFromIntakeRunning = true;

        //boot disc into starpath
        if(!bootUsed) BootDisc();

        //wait
        if(loaderStartTime+1 > opMode.getRuntime()) return;

        if(starpathPosition == 0 || starpathPosition == 1) BootReset();
        else BootMiddle();
        bootUsed = true;

    }
    public void StopLoadFromIntake(){
        //rotate starpath to next pos
        if(starpathPosition < 3) RotateStarpathToNextPos();
        bootUsed = false;
        loadFromIntakeRunning = false;
    }

    public void ShootAsync(){
        //start timer
        if(!shooterRunning)shooterStartTime = opMode.getRuntime();
        shooterRunning = true;

        //start shooter
        ShooterOn();

        //wait
        //if(shooterStartTime+2 > opMode.getRuntime() && discsShot <1) return;

        //load shooter -> it shoots
        if(!loaderUsed){
            LoaderLoad();
            discsShot ++;
            loaderUsed = true;
            shooterStartTime = opMode.getRuntime();
        }

        //wait
        if(shooterStartTime+timeToShoot > opMode.getRuntime()) return;

        //retract loader
        LoaderClear();

        //wait
        if(shooterStartTime+timeToShoot+timeToRetract > opMode.getRuntime()) return;

        //move to next pos
        if(!starpathUsed && discsShot != 3){
            RotateStarpathToNextPos();
            starpathUsed = true;
        }
        else if(!starpathUsed && discsShot == 3){
            StarpathToIntake();
            starpathUsed = true;
        }

        //wait
        if(shooterStartTime+timeToShoot+timeToRetract+timeToMoveToNext > opMode.getRuntime()) return;
        shooterRunning = false;
    }
    public void StopShootAsync(){
        ShooterOff();
        LoaderClear();
        shooterRunning = false;
        loaderUsed = false;
        starpathUsed = false;
    }

    public void ShootThree(){
        ShooterOn(); //Spin up shooter
        stopShooterOverride = false;

        ShootOne();
        ShooterOn();

        ShootOne();
        ShooterOn();

        ShootOne();

        //Reset shooter
        ShooterOff();
    }
    public void StopShootThree(){
        stopShooterOverride = true;
        StopShootAsync();
    }

    public void ShootOne(){
        ShootAsync();
        while(shooterRunning && !stopShooterOverride){
            ShootAsync();
            opMode.telemetry.addLine("Shooting");
            opMode.telemetry.update();
        }
        StopShootAsync();
    }

    public void ModifyForPowerShot(){
        powerShot = true;
    }
    public void StopModifyForPowerShot(){
        powerShot = false;
    }
}
