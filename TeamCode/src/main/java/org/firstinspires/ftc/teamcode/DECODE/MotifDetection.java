package org.firstinspires.ftc.teamcode.DECODE;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

@Autonomous(name = "MotifDetection", group = "Sensor")
public class MotifDetection extends LinearOpMode {

    private Limelight3A limelight;

    // Motif patterns for each tag
    private final String[] tag21 = {"g", "p", "p"};
    private final String[] tag22 = {"p", "g", "p"};
    private final String[] tag23 = {"p", "p", "g"};

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5); // make sure this is your AprilTag pipeline
        limelight.start();

        telemetry.addLine("Motif Detection Ready — press play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLResult result = limelight.getLatestResult();

            if (result != null && result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

                for (LLResultTypes.FiducialResult fr : fiducials) {
                    int tagID = fr.getFiducialId();
                    telemetry.addData("Detected Tag ID", tagID);

                    if(tagID == 21 ){

                    }
                }
            }
        }
    }
}
