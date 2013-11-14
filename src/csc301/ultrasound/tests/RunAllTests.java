package csc301.ultrasound.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({/*csc301.ultrasound.frontend.ui.tests.AnnotationPanelTest.class,*/
					 csc301.ultrasound.global.tests.TransmissionTest.class,
					 csc301.ultrasound.global.tests.AuthenticationTest.class,
					 csc301.ultrasound.model.tests.UserTest.class})

public class RunAllTests
{
	// do nothing
	public static void main(String[] args) throws Exception 
	{
		JUnitCore.main("csc301.ultrasound.tests.RunAllTests");            
	}
}
