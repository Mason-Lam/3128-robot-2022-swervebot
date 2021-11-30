package frc.team3128;

import frc.team3128.subsystems.*;
import frc.team3128.subsystems.Shooter.ShooterState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.team3128.commands.*;
import frc.team3128.hardware.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private Mover m_move; // test subsystem
    private NAR_Drivetrain m_drive;
    private Shooter m_shooter;
    private Hopper m_hopper;
    private Intake m_intake;

    private NAR_Joystick m_leftStick;
    private NAR_Joystick m_rightStick;

    private CommandScheduler m_commandScheduler = CommandScheduler.getInstance();
    private Command auto;

    public RobotContainer() {
        m_move = new Mover();
        m_drive = new NAR_Drivetrain();
        m_shooter = new Shooter();
        m_hopper = new Hopper();
        m_intake = new Intake();
        m_shooter.enable();

        m_leftStick = new NAR_Joystick(0);
        m_rightStick = new NAR_Joystick(1);

        m_commandScheduler.registerSubsystem(m_move, m_drive, m_shooter, m_hopper);

        m_commandScheduler.setDefaultCommand(m_drive, new ArcadeDrive(m_drive, m_rightStick::getY, m_rightStick::getX));
        m_commandScheduler.setDefaultCommand(m_hopper, new HopperDefault(m_hopper, m_shooter::atSetpoint));

        configureButtonBindings();
    }   

    private void configureButtonBindings() {

        m_rightStick.getButton(1).whenPressed(new RunCommand(m_intake::runIntake, m_intake))
                                .whenReleased(new RunCommand(m_intake::stopIntake, m_intake));

        m_rightStick.getButton(2).whenPressed(new Shoot(m_shooter, ShooterState.MID_RANGE))
                                .whenReleased(new RunCommand(m_shooter::stopShoot, m_shooter));
    }

    public void stopDrivetrain() {
        m_drive.stop();
    }

    public Command getAutonomousCommand() {
        return auto;
    }
}
