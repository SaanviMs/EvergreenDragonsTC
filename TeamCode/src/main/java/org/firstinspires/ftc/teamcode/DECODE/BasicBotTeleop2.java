package org.firstinspires.ftc.teamcode.DECODE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class BasicBotTeleop2 extends OpMode {

    private DcMotor leftBack, leftFront, rightBack, rightFront;
    private DcMotor shooter;
    private CRServo geckoLeft, geckoRight;
    private CRServo intake;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class,"leftFront");
        leftBack = hardwareMap.get(DcMotor.class,"leftBack");
        rightFront = hardwareMap.get(DcMotor.class,"rightFront");
        rightBack = hardwareMap.get(DcMotor.class,"rightBack");

        shooter = hardwareMap.get(DcMotor.class,"shooter");

        geckoLeft = hardwareMap.get(CRServo.class,"geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class,"geckoRight");
        intake = hardwareMap.get(CRServo.class,"intake");

        // Standard motor direction setup
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // Stop all motors/servos initially
        geckoLeft.setPower(0);
        geckoRight.setPower(0);
        intake.setPower(0);
        shooter.setPower(0);
    }

    @Override
    public void loop() {

        // Drive controls
        double x = gamepad2.right_stick_x * 0.3;
        double y = gamepad2.left_stick_y * 1.1;
        double rx = -gamepad2.left_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);


        double shooterPower = gamepad1.left_trigger*0.67;
        shooter.setPower(shooterPower);

        if(gamepad1.left_bumper){
            shooter.setPower(-0.67);
        }

        // Intake + Gecko controls
        if (gamepad1.b) {
            intake.setPower(-1);
            geckoLeft.setPower(1);
            geckoRight.setPower(-1);
        } else if (gamepad1.right_trigger>0.1) {
            geckoRight.setPower(1);
            geckoLeft.setPower(-1);
            intake.setPower(1);
        } else if (gamepad1.a) {
            intake.setPower(0);
            geckoLeft.setPower(-1);
            geckoRight.setPower(1);
        } else if (gamepad1.dpad_up){
            intake.setPower(1);
        } else {
            intake.setPower(0);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);
        }

    }
}
