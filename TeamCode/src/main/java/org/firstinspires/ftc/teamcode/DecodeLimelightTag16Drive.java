package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

@TeleOp(name = "FTC Limelight Tag16 Drive", group = "Sensor")
public class DecodeLimelightTag16Drive extends LinearOpMode {

    private Limelight3A limelight;
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;

    @Override
    public void runOpMode() throws InterruptedException {
        // Map motors
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        // Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5);  // Make sure pipeline 0 is the AprilTag pipeline
        limelight.start();

        telemetry.addLine("Robot Ready. Press Play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                // Loop through fiducials (AprilTags)
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                boolean tag16Detected = false;

                for (LLResultTypes.FiducialResult fr : fiducials) {
                    if (fr.getFiducialId() == 16) {
                        tag16Detected = true;
                        telemetry.addData("AprilTag 16", "Detected! TX: %.2f, TY: %.2f", fr.getTargetXDegrees(), fr.getTargetYDegrees());
                        break;
                    }
                }

                if (tag16Detected) {
                    // Drive forward
                    leftBack.setPower(0.4);
                    rightBack.setPower(0.4);
                    leftFront.setPower(0.4);
                    rightBack.setPower(0.4);
                    telemetry.addLine("Driving forward");
                } else {
                    // Spin slowly until tag 16 is detected
                    leftBack.setPower(0.2);
                    rightBack.setPower(0.2);
                    leftFront.setPower(0.2);
                    rightBack.setPower(0.2);
                    telemetry.addLine("Searching for Tag 16 → Spinning");
                }
            } else {
                // No data from Limelight
                leftBack.setPower(0.2);
                rightBack.setPower(0.2);
                leftFront.setPower(0.2);
                rightBack.setPower(0.2);
                telemetry.addLine("No fiducial data → Spinning");
            }

            telemetry.update();
        }

        // Stop motors
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        limelight.stop();
    }
}
