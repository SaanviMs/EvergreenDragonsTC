/*
Copyright (c) 2024 Limelight Vision

All rights reserved.
...
*/
package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp(name = "Sensor: Limelight3A", group = "Sensor")
// @Disabled   <-- removed so it shows up on Driver Hub
public class SensorLimelight3A extends LinearOpMode {

    private Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.setMsTransmissionInterval(11);

        limelight.pipelineSwitch(0); // Make sure pipeline 0 is a Color pipeline for Blue

        limelight.start();

        telemetry.addData(">", "Robot Ready. Press Play.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            LLStatus status = limelight.getStatus();
            telemetry.addData("Name", "%s", status.getName());
            telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                    status.getTemp(), status.getCpu(), (int) status.getFps());
            telemetry.addData("Pipeline", "Index: %d, Type: %s",
                    status.getPipelineIndex(), status.getPipelineType());

            LLResult result = limelight.getLatestResult();
            if (result != null) {
                // General info
                Pose3D botpose = result.getBotpose();
                telemetry.addData("LL Latency", result.getCaptureLatency() + result.getTargetingLatency());
                telemetry.addData("Parse Latency", result.getParseLatency());
                telemetry.addData("PythonOutput", java.util.Arrays.toString(result.getPythonOutput()));

                if (result.isValid()) {
                    telemetry.addData("tx", result.getTx());
                    telemetry.addData("txnc", result.getTxNC());
                    telemetry.addData("ty", result.getTy());
                    telemetry.addData("tync", result.getTyNC());
                    telemetry.addData("Botpose", botpose.toString());

                    // Barcodes
                    List<LLResultTypes.BarcodeResult> barcodeResults = result.getBarcodeResults();
                    for (LLResultTypes.BarcodeResult br : barcodeResults) {
                        telemetry.addData("Barcode", "Data: %s", br.getData());
                    }

                    // Classifiers
                    List<LLResultTypes.ClassifierResult> classifierResults = result.getClassifierResults();
                    for (LLResultTypes.ClassifierResult cr : classifierResults) {
                        telemetry.addData("Classifier", "Class: %s, Confidence: %.2f", cr.getClassName(), cr.getConfidence());
                    }

                    // Detectors
                    List<LLResultTypes.DetectorResult> detectorResults = result.getDetectorResults();
                    for (LLResultTypes.DetectorResult dr : detectorResults) {
                        telemetry.addData("Detector", "Class: %s, Area: %.2f", dr.getClassName(), dr.getTargetArea());
                    }

                    // Fiducials
                    List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                    for (LLResultTypes.FiducialResult fr : fiducialResults) {
                        telemetry.addData("Fiducial", "ID: %d, Family: %s, X: %.2f, Y: %.2f",
                                fr.getFiducialId(), fr.getFamily(), fr.getTargetXDegrees(), fr.getTargetYDegrees());
                    }

                    // Colors (Blue detection)
                    List<LLResultTypes.ColorResult> colorResults = result.getColorResults();
                    boolean blueDetected = false;

                    if (colorResults != null && !colorResults.isEmpty()) {
                        for (LLResultTypes.ColorResult cr : colorResults) {
                            double area = cr.getTargetArea();  // % of image covered
                            double x = cr.getTargetXDegrees();
                            double y = cr.getTargetYDegrees();

                            // Show info for blobs found
                            telemetry.addData("Blue Blob", "Area: %.2f, X: %.2f, Y: %.2f", area, x, y);
                            blueDetected = true;
                        }
                    }

                    if (blueDetected) {
                        telemetry.addData("Color Name", "Blue");
                    } else {
                        telemetry.addData("Color Name", "No Blue Detected");
                    }
                }
            } else {
                telemetry.addData("Limelight", "No data available");
            }

            telemetry.update();
        }

        limelight.stop();
    }
}
