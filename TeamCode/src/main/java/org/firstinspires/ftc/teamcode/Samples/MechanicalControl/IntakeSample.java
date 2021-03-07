package org.firstinspires.ftc.teamcode.Samples.MechanicalControl;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MechanicalControl.MotorizedIntake;

/*Sample class for MechanicalControl.Intake
Intake goes forwards when right trigger is down and reverses when left trigger is down. Stops when nothing is down.*/
@TeleOp(name = "IntakeSample", group = "Samples")
@Disabled
public class IntakeSample extends OpMode
{
    MotorizedIntake intake;

    @Override
    public void init() {
        intake = new MotorizedIntake();
        intake.Init(new DcMotor[]{hardwareMap.dcMotor.get("motor1")}, new double[]{1});
    }

    @Override
    public void loop() {
        if(gamepad1.right_trigger > 0.1) intake.SetIntakePower(1);
        else if(gamepad1.left_trigger > 0.1) intake.SetIntakePower(-1);
        else intake.SetIntakePower(0);
    }
}
