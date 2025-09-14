package org.firstinspires.ftc.teamcode.DECODE;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;

@Autonomous(name = "Align and Drive to Tag16", group = "Sensor")
public class LimelightAlignment extends LinearOpMode {

    private Limelight3A limelight;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;

    @Override
    public void runOpMode() throws InterruptedException {

        // Map motors
        leftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        rightBack = hardwareMap.get(DcMotor.class, "RightBack");
        leftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        rightFront = hardwareMap.get(DcMotor.class, "RightFront");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        // Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5); // make sure pipeline 0 is AprilTag
        limelight.start();

        telemetry.addLine("Robot Ready. Press Play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();
            boolean tag16Detected = false;
            double tx = 0; // horizontal offset

            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducials) {
                    if (fr.getFiducialId() == 16) {
                        tag16Detected = true;
                        tx = fr.getTargetXDegrees();



                }
            }

            if (tag16Detected) {
                // Align with tag using TX
                double turnThreshold = 2.0; // degrees tolerance
                double turnPower = 0.4;

                if (Math.abs(tx) > turnThreshold) {
                    // Tag is not centered → turn robot
                    if (tx > 0) {
                        // tag is to the right → turn right
                        leftBack.setPower(turnPower);
                        rightBack.setPower(-turnPower);
                        leftFront.setPower(-turnPower);
                        rightFront.setPower(-turnPower);
                    }
                    if (tx<0){
                        // tag is to the left → turn left
                        leftBack.setPower(-turnPower);
                        rightBack.setPower(turnPower);
                        leftFront.setPower(turnPower);
                        rightFront.setPower(-turnPower);
                    }
                    telemetry.addData("Aligning", "TX = %.2f", tx);
                 if(tx == 0) {
                     // Tag is centered → drive forward
                     leftBack.setPower(turnPower);
                     rightBack.setPower(turnPower);
                     leftFront.setPower(turnPower);
                     rightFront.setPower(turnPower);
                     telemetry.addLine("Driving forward to tag");


                 }
                }
            }
            else {
                // No tag → spin slowly
                leftBack.setPower(-0.3);
                rightBack.setPower(0.3);
                leftFront.setPower(-0.3);
                rightFront.setPower(0.3);
                telemetry.addLine("Searching for Tag 16 → Spinning");
            }

            telemetry.update();

}}}}
