/*
 * Copyright (c) 2025 [Your Name / Team]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;  // or TeleOp if for teleop
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;

import java.util.List;

/**
 * Example OpMode stub for DECODE (2025-2026).
 * Use this as a starting point for artifact detection / gate logic / pattern scoring.
 */
//@Disabled
@Autonomous(name = "Concept: DECODE Vision Locator blobby", group = "Concept")
public class ConceptVisionColorBlob extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // ================ Vision / Detection Setup ================
        // Example: using a color blob locator to detect a particular artifact color or marker
        ColorBlobLocatorProcessor locator = new ColorBlobLocatorProcessor.Builder()
                // TODO: define correct color ranges / thresholds for your artifacts
                .setTargetColorRange(ColorRange.BLUE)
                // use external contours to avoid nested noise
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                // region to search (you may change this)
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.5, 0.5, 0.5, -0.5))
                .setDrawContours(true)
                .setBlurSize(5)
                .build();

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(locator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        telemetry.setMsTransmissionInterval(50);
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        waitForStart();

        while (opModeIsActive()) {
            List<ColorBlobLocatorProcessor.Blob> blobs = locator.getBlobs();

            // Filter out very small blobs (adjust thresholds)
            ColorBlobLocatorProcessor.Util.filterByArea(50, 20000, blobs);

            telemetry.addLine("Idx  Area   Density  Aspect   Center");

            for (int i = 0; i < blobs.size(); i++) {
                ColorBlobLocatorProcessor.Blob b = blobs.get(i);
                RotatedRect box = b.getBoxFit();
                telemetry.addLine(String.format("%2d  %5d  %5.2f  %5.2f  (%.1f,%.1f)",
                        i,
                        b.getContourArea(),
                        b.getDensity(),
                        b.getAspectRatio(),
                        box.center.x, box.center.y));
            }

            // ================ DECODE Logic Placeholders ================
            // For example:
            // - Determine which blob corresponds to the target artifact / AprilTag
            // - Use its center or bounding box to aim or align
            // - Trigger gate mechanism if needed
            // - Decision logic for scoring, overflow, pattern alignment, etc.

            // Example stub:
            if (!blobs.isEmpty()) {
                ColorBlobLocatorProcessor.Blob target = blobs.get(0);
                RotatedRect r = target.getBoxFit();
                double centerX = r.center.x;
                double centerY = r.center.y;

                // Telemetry + placeholder decision
                telemetry.addData("Target center", "(%.1f, %.1f)", centerX, centerY);

                // TODO: convert this into motor/servo commands or state transitions
                // e.g. if centerX < threshold → move left, etc.
            }

            telemetry.update();
            sleep(50);
        }
    }
}
