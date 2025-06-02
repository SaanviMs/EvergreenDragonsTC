package org.firstinspires.ftc.robotcontroller.internal;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class Test22 extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    @Override
    public void runOpMode() {

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();

        waitForStart();

        frontLeftMotor.setPower(0.7);
        frontRightMotor.setPower(0.7);
        backLeftMotor.setPower(0.7);
        backRightMotor.setPower(0.7);
        sleep(200);
        frontLeftMotor.setPower(-0.7);
        frontRightMotor.setPower(-0.7);
        backLeftMotor.setPower(-0.7);
        backRightMotor.setPower(-0.7);
        sleep(200);
        frontRightMotor.setPower(0.7);
        frontLeftMotor.setPower(0.7);
        backLeftMotor.setPower(-0.7);
        backRightMotor.setPower(-0.7);
        sleep(200);
    }
}