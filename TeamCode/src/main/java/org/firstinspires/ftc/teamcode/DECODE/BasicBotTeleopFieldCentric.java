package org.firstinspires.ftc.teamcode.DECODE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "BasicBotTeleopFieldCentric")
public class BasicBotTeleopFieldCentric extends OpMode {

    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private DcMotor shooter;
    private CRServo geckoLeft, geckoRight, intake;
    private IMU imu;

    @Override
    public void init() {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        shooter = hardwareMap.get(DcMotor.class, "shooter");

        geckoLeft = hardwareMap.get(CRServo.class, "geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class, "geckoRight");
        intake = hardwareMap.get(CRServo.class, "intake");

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );
        imu.initialize(parameters);

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        geckoLeft.setPower(0);
        geckoRight.setPower(0);
        intake.setPower(0);
        shooter.setPower(0);

        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Drive inputs
        double y = -gamepad2.left_stick_y;
        double x = gamepad2.left_stick_x;
        double rx = gamepad2.right_stick_x;

        if (gamepad2.options) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX *= 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        leftFront.setPower(frontLeftPower);
        leftBack.setPower(backLeftPower);
        rightFront.setPower(frontRightPower);
        rightBack.setPower(backRightPower);

        // Shooter
        double shooterPower = gamepad1.left_trigger * 0.67;
        shooter.setPower(shooterPower);
        if (gamepad1.left_bumper) {
            shooter.setPower(-0.67);
        }

        // Intake + gecko
        if (gamepad1.b) {
            intake.setPower(-1);
            geckoLeft.setPower(1);
            geckoRight.setPower(-1);
        } else if (gamepad1.right_trigger > 0.1) {
            geckoRight.setPower(1);
            geckoLeft.setPower(-1);
            intake.setPower(1);
        } else if (gamepad1.a) {
            intake.setPower(0);
            geckoLeft.setPower(-1);
            geckoRight.setPower(1);
        } else if (gamepad1.dpad_up) {
            intake.setPower(1);
        } else {
            intake.setPower(0);
            geckoLeft.setPower(0);
            geckoRight.setPower(0);
        }

        telemetry.addData("Heading (deg)", Math.toDegrees(botHeading));
        telemetry.addData("FL", frontLeftPower);
        telemetry.addData("FR", frontRightPower);
        telemetry.addData("BL", backLeftPower);
        telemetry.addData("BR", backRightPower);
        telemetry.update();
    } // end loop

} // end class
