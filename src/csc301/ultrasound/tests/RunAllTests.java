package csc301.ultrasound.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({csc301.ultrasound.frontend.ui.tests.AnnotationPanelTest.class, 
					 csc301.ultrasound.global.tests.TransmissionTest.class})

public class RunAllTests
{
	// do nothing
}
