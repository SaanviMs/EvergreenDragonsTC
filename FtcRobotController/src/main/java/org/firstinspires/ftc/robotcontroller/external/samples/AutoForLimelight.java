package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.List;

@Autonomous(name = "TestBot: Auto with Limelight", group = "TestBot")
public class AutoForLimelight extends LinearOpMode {

    // Motors
    private DcMotor RightBack = null;
    private DcMotor LeftBack = null;
    private DcMotor LeftFront = null;
    private DcMotor RightFront = null;

    // Limelight
    private Limelight3A limelight;

    private ElapsedTime runtime = new ElapsedTime();

    static final double FORWARD_SPEED = 0.3;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize motors
        RightBack = hardwareMap.get(DcMotor.class, "RightBack");
        LeftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        RightFront = hardwareMap.get(DcMotor.class, "RightFront");
        LeftFront = hardwareMap.get(DcMotor.class, "LeftFront");

        // Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(9); // Ensure pipeline 9 is for Blue Color detection
        limelight.start();

        sleep(3000);  // ✅ Give Limelight time to initialize and return valid results


        telemetry.addData("Status", "Initialized - Waiting to Start");
        telemetry.update();

        waitForStart();

        // Get Limelight data
        LLResult result = limelight.getLatestResult();

        boolean blueDetected = false;
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 2.0 && !blueDetected) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                if (colorResults != null && !colorResults.isEmpty()) {
                    for (LLResultTypes.ColorResult cr : colorResults) {
                        double area = cr.getTargetArea();
                        double x = cr.getTargetXDegrees();
                        double y = cr.getTargetYDegrees();

                        telemetry.addData("Blue Blob", "Area: %.2f, X: %.2f, Y: %.2f", area, x, y);

                        if (area > 1.0) {
                            blueDetected = true;
                            break;
                        }
                    }
                }
            }

            telemetry.addData("Blue Detected", blueDetected ? "YES" : "NO");
            telemetry.update();
        }
        telemetry.addData("Blue Detected", blueDetected ? "YES" : "NO");
        telemetry.update();

        if (blueDetected) {
            // Drive forward if blue blob is detected
            LeftBack.setPower(FORWARD_SPEED);
            RightBack.setPower(FORWARD_SPEED);
            LeftFront.setPower(FORWARD_SPEED);
            RightFront.setPower(FORWARD_SPEED);

            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < 3.0)) {
                telemetry.addData("Step", "Driving Forward");
                telemetry.update();
            }

            // Stop
            LeftBack.setPower(0);
            RightBack.setPower(0);
            LeftFront.setPower(0);
            RightFront.setPower(0);
        } else {
            telemetry.addData("Action", "No Blue Detected – Not Moving");
            telemetry.update();
        }

        // Cleanup
        limelight.stop();
        telemetry.addData("Path", "Complete");
        telemetry.update();
    }
}
