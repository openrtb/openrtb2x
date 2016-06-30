package org.openrtb.validator;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/features/"},
		tags = {"~@unstable"},
		plugin = {
				"pretty",
				"html:target",
		}
		)

public class OpenRtbValidatorV2_xTestRunner {

}
