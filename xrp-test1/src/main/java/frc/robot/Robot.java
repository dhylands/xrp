// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.xrp.XRPMotor;
import edu.wpi.first.wpilibj.xrp.XRPServo;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import java.sql.JDBCType;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // private final XRPDrivetrain m_drivetrain = new XRPDrivetrain();

  private final Timer timer = new Timer();
  private final XRPMotor leftMotor = new XRPMotor(0);
  private final XRPMotor rightMotor = new XRPMotor(1);
  private final DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);
  private final XRPServo armServo = new XRPServo(4);
  private final Joystick joystick = new Joystick(0);

  private double driveSpeed = 1.0;

  private static final double kDriverSpeedFull = 1.0;
  private static final double kDriveSpeedSlow = 0.6;

  private static final double kArmInside = 1.0;
  private static final double kArmMiddle = 0.5;
  private static final double kArmOutside = 0.0;

  private static final int kAxisLeftX = 0;
  private static final int kAxisLeftY = 1;
  private static final int kAxisRightX = 3;
  private static final int kAxisRightY = 4;

  private static final int kButtontRoundDown = 1;
  private static final int kButtontRoundRight = 2;
  private static final int kButtontRoundUp = 3;
  private static final int kButtontRoundLeft = 4;
  private static final int kButtonLeft1 = 5;
  private static final int kButtonRight1 = 6;
  private static final int kButtonLeft2 = 7;
  private static final int kButtonRight2 = 8;
  private static final int kButtonSelect = 9;
  private static final int kButtonStart = 10;
  private static final int kButtonPS3 = 11;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    leftMotor.setInverted(false);
    rightMotor.setInverted(true);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    // m_drivetrain.resetEncoders();
    timer.start();
    timer.reset();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        drive.tankDrive(0.0, 0.0);
        armServo.setPosition(kArmInside);
        break;
      case kDefaultAuto:
        //leftMotor.set(0.6);
        //rightMotor.set(0.6);
        if (timer.get() < 2.65) {
          drive.tankDrive(0.6, 0.6);
          armServo.setPosition(kArmInside);
        } else if (timer.get() < 3.15) {
          drive.tankDrive(0.7, -0.7);
        } else if (timer.get() < 6.05) {
          drive.tankDrive(-0.5, -0.5);
        } else if (timer.get() < 8.05) {
          drive.tankDrive(0.0, 0.0);
          armServo.setPosition(kArmOutside);
        } else {
          drive.tankDrive(0.0, 0.0);
          armServo.setPosition(kArmInside);
        }
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (joystick.getRawButton(kButtonLeft1)) {
      driveSpeed = kDriveSpeedSlow;
    } else if (joystick.getRawButton(kButtonRight1)) {
      driveSpeed = kDriverSpeedFull;
    }

    // drive.tankDrive(-joystick.getRawAxis(kAxisY_L), -joystick.getRawAxis(kAxisY_R));
    drive.arcadeDrive(-joystick.getRawAxis(kAxisLeftY) * driveSpeed, -joystick.getRawAxis(kAxisRightX) * driveSpeed);

    if (joystick.getRawButton(kButtontRoundUp)) {
      armServo.setPosition(kArmInside);
    } else if (joystick.getRawButton(kButtontRoundRight)) {
      armServo.setPosition(kArmMiddle);
    } else if (joystick.getRawButton(kButtontRoundDown)) {
      armServo.setPosition(kArmOutside);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
