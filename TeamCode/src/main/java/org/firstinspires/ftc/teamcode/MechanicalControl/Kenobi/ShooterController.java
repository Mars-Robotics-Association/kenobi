package org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ShooterController {
    private DcMotor[] Motors;
    private double Power = 2;

    public void Init(DcMotor[] setMotors){
        Motors = setMotors;
    }

    public void SetShooterPower(double power){
        for (DcMotor motor: Motors) {
            motor.setPower(power * Power);
        }
    }
}
