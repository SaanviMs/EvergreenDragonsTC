package org.firstinspires.ftc.teamcode.DECODE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;  // <–– Added

@TeleOp
public class BasicBotTeleop2 extends OpMode {

    private DcMotor leftBack, leftFront, rightBack, rightFront;
    private DcMotor shooter;
    private CRServo geckoLeft, geckoRight;

    private ElapsedTime timer = new ElapsedTime();  // <–– Added

    private boolean shooterStarted = false;         // <–– Added
    private boolean geckosStarted = false;          // <–– Added

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class,"leftFront");
        leftBack = hardwareMap.get(DcMotor.class,"leftBack");
        rightFront = hardwareMap.get(DcMotor.class,"rightFront");
        rightBack = hardwareMap.get(DcMotor.class,"rightBack");

        shooter = hardwareMap.get(DcMotor.class,"shooter");

        geckoLeft = hardwareMap.get(CRServo.class,"geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class,"geckoRight");

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // It looks like these were originally set to power in init; I leave as-is
        geckoLeft.setPower(1);
        geckoRight.setPower(1);
    }

    @Override
    public void loop() {
        double x = -gamepad2.right_stick_x * 0.3; // This makes the robot strafe
        double y = -gamepad2.left_stick_y * 1.1; // This makes the robot turn left and right
        double rx = gamepad2.left_stick_x;        // This makes the robot go forward and backward

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower  = (y + x + rx) / denominator;
        double backLeftPower   = (y + x - rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower  = (y - x + rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);

        if (gamepad1.right_trigger > 0.1) {
            // Trigger pressed

            if (!shooterStarted) {
                // first frame of trigger pressed
                shooter.setPower(0.8);
                timer.reset();  // start counting
                shooterStarted = true;
                geckosStarted = false;  // ensure geckos are reset
            }

            // after 0.5 seconds, start geckos
            if (shooterStarted && !geckosStarted && timer.seconds() >= 0.5) {
                geckoLeft.setPower(-1);
                geckoRight.setPower(1);
                geckosStarted = true;
            }

        } else {
            // trigger not pressed: reset everything
            shooter.setPower(0);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);

            shooterStarted = false;
            geckosStarted = false;
        }
    }
}

