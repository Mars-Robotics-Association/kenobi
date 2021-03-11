package org.firstinspires.ftc.teamcode.MechanicalControl.Kenobi;

import com.qualcomm.robotcore.hardware.CRServo;

public class IntakeController {
    private CRServo[] intakeServos;
    private double intakeSpeed;

    public void Init(CRServo[] setIntakeServos){
        intakeServos = setIntakeServos;
    }

    public void SetIntakePower(double power){
        for (CRServo servo: intakeServos) {
            servo.setPower(power * intakeSpeed);
        }
    }

}
