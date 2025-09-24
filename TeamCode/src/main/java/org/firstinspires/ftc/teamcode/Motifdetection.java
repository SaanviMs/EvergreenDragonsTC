package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Autonomous(name = "MotifDetection", group = "Autonomous")
public class Motifdetection extends LinearOpMode {

    // ⚠️ Change this to match your Limelight IP address
    private static final String LIMELIGHT_IP = "172.29.0.30";

    // Default pipeline before detecting tags
    private static final int DEFAULT_PIPELINE = 0;

    // Drive motors
    private DcMotor rightBack, leftBack, rightFront, leftFront;

    @Override
    public void runOpMode() {
        // Initialize hardware mapping
        rightBack = hardwareMap.get(DcMotor.class, "RightBack");
        leftBack = hardwareMap.get(DcMotor.class, "LeftBack");
        rightFront = hardwareMap.get(DcMotor.class, "RightFront");
        leftFront = hardwareMap.get(DcMotor.class, "LeftFront");

        telemetry.addLine("Motif Detection Ready");
        telemetry.update();

        // Set default pipeline before start
        setPipeline(DEFAULT_PIPELINE);
        telemetry.addData("Pipeline", "Set to %d", DEFAULT_PIPELINE);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            try {
                // Call Limelight’s AprilTag ID endpoint (tid)
                int tagId = (int) getNumberFromLimelight("tid");
                double tx = getNumberFromLimelight("tx");  // horizontal offset
                double ty = getNumberFromLimelight("ty");  // vertical offset
                double tz = getNumberFromLimelight("tz");  // distance (if enabled)

                if (tagId == 21) {
                    telemetry.addData("Motif Tag Detected", "ID: %d", tagId);
                    telemetry.addData("Pose", "tx=%.2f, ty=%.2f, tz=%.2f", tx, ty, tz);

                    // Switch to pipeline 1
                    setPipeline(1);
                    telemetry.addLine("Switched to pipeline 1");

                    setPipeline(1);
                    telemetry.addLine("Switched to pipeline 1");

                } else if (tagId == 22) {
                    telemetry.addData("Motif Tag Detected", "ID: %d", tagId);
                    telemetry.addData("Pose", "tx=%.2f, ty=%.2f, tz=%.2f", tx, ty, tz);

                    // Switch to pipeline 2
                    setPipeline(2);
                    telemetry.addLine("Switched to pipeline 2");

                    setPipeline(1);
                    telemetry.addLine("Switched to pipeline 1");

                } else if (tagId == 23) {
                    telemetry.addData("Motif Tag Detected", "ID: %d", tagId);
                    telemetry.addData("Pose", "tx=%.2f, ty=%.2f, tz=%.2f", tx, ty, tz);

                    // Switch to pipeline 3
                    setPipeline(3);
                    telemetry.addLine("Switched to pipeline 3");

                    setPipeline(1);
                    telemetry.addLine("Switched to pipeline 1");

                } else {
                    telemetry.addLine("No motif tag (21/22/23) detected.");
                }
            } catch (Exception e) {
                telemetry.addLine("Error talking to Limelight: " + e.getMessage());
            }

            telemetry.update();
        }
    }

    /**
     * Helper function to read a single number from Limelight
     */
    private double getNumberFromLimelight(String key) {
        try {
            URL url = new URL("http://" + LIMELIGHT_IP + ":5807/" + key);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100);
            conn.setReadTimeout(100);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();
            reader.close();

            return Double.parseDouble(response.trim());
        } catch (Exception e) {
            return -1; // return -1 if nothing is found
        }
    }

    /**
     * Helper function to set Limelight pipeline
     */
    private void setPipeline(int number) {
        try {
            URL url = new URL("http://" + LIMELIGHT_IP + "/pipeline?p=" + number);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(200);
            conn.setReadTimeout(200);
            conn.getInputStream().close(); // trigger the request
        } catch (Exception e) {
            telemetry.addLine("Error setting pipeline: " + e.getMessage());
        }
    }
}
