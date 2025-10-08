package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;

@Autonomous(name = "MotifDetection_MoveForward", group = "Sensor")
public class MotifDetectRonak extends LinearOpMode {

    private Limelight3A limelight;

    private DcMotor leftFront, leftBack, rightFront, rightBack, shooter;
    private CRServo geckoLeft, geckoRight, intake;

    // Motif patterns for each tag
    private final String[] tag21 = {"g", "p", "p"};
    private final String[] tag22 = {"p", "g", "p"};
    private final String[] tag23 = {"p", "p", "g"};

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        shooter = hardwareMap.get(DcMotor.class, "shooter");

        geckoLeft = hardwareMap.get(CRServo.class, "geckoLeft");
        geckoRight = hardwareMap.get(CRServo.class, "geckoRight");
        intake = hardwareMap.get(CRServo.class, "intake");

        // Motor directions
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);

        // Initialize Limelight
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5); // ensure correct pipeline
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

                    // Display motif
                    switch (tagID) {
                        case 21:
                            telemetry.addData("Motif Pattern", String.join("-", tag21));
                            break;
                        case 22:
                            telemetry.addData("Motif Pattern", String.join("-", tag22));
                            break;
                        case 23:
                            telemetry.addData("Motif Pattern", String.join("-", tag23));
                            break;
                        default:
                            telemetry.addLine("Unknown Tag ID — no motif assigned.");
                            break;
                    }

                    // Move forward for 1 second when any valid tag is seen
                    moveForward(0.4); // you can adjust the power (0.3–0.6 recommended)
                    sleep(1000);
                    stopMotors();
                }
            } else {
                telemetry.addLine("No valid Limelight data.");
            }

            telemetry.update();
        }

        limelight.stop();
    }

    private void moveForward(double power) {
        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(power);
        rightBack.setPower(power);
    }

    private void stopMotors() {
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }
}
