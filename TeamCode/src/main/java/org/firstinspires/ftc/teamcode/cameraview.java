package org.firstinspires.ftc.teamcode;

import android.util.Size;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;

import java.util.List;

//@Disabled
@TeleOp(name = "Concept: Vision Color-Check with Preview", group = "Concept")
public class cameraview extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Build the color blob locator processor
        ColorBlobLocatorProcessor colorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.BLUE)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.5, 0.5, 0.5, -0.5))
                .setDrawContours(true)  // draw detected blobs / contours on preview
                .setBlurSize(5)
                .build();

        // Build the VisionPortal with camera and optional monitoring (preview) enabled
        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                // .enableCameraMonitoring(true)   // remove or comment out if unsupported
                .build();


        telemetry.setMsTransmissionInterval(50);
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        waitForStart();

        while (opModeIsActive()) {
            List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
            ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);

            telemetry.addLine("Area   Density   Aspect   Center");

            for (ColorBlobLocatorProcessor.Blob b : blobs) {
                RotatedRect box = b.getBoxFit();
                telemetry.addLine(String.format("%5d  %5.2f    %5.2f  (%.1f,%.1f)",
                        b.getContourArea(),
                        b.getDensity(),
                        b.getAspectRatio(),
                        box.center.x,
                        box.center.y));
            }

            if (!blobs.isEmpty()) {
                ColorBlobLocatorProcessor.Blob target = blobs.get(0);
                RotatedRect rect = target.getBoxFit();
                double cx = rect.center.x;
                double cy = rect.center.y;
                telemetry.addData("Target center", "(%.1f, %.1f)", cx, cy);
                // Use cx, cy to drive robot logic
            }

            telemetry.update();

            sleep(50);
        }
    }
}
