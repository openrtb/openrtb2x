package org.openrtb.validator.steps;
import static org.junit.Assert.assertTrue;

import org.openrtb.validator.OpenRtbInputType;
import org.openrtb.validator.OpenRtbValidator;
import org.openrtb.validator.OpenRtbValidatorFactory;
import org.openrtb.validator.OpenRtbVersion;
import org.openrtb.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fge.jackson.JsonLoader;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OpenRtb2_4BidRequestResponseSteps {

	private static final Logger logger = LoggerFactory.getLogger(OpenRtb2_4BidRequestResponseSteps.class);
	OpenRtbValidator validator = null;
	String resource = null;
	ValidationResult result = null;
	OpenRtbVersion openRtbVersion = null;
	
	@Given("^a bid request json input \"([^\"]*)\" to be validated$")
	public void bidRequestExampleToBeValidated(String bidRequest) throws Throwable {
		resource = bidRequest;
	}

	@When("^an openRtb validator version \"([^\"]*)\" runs validation on given bid request$")
	public void isValidBidRequest(String version) throws Throwable {
		getVersion(version);
		System.out.println("version"+version);
		System.out.println(openRtbVersion);
		validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, openRtbVersion);
		result = validator.validate(JsonLoader.fromResource(resource));
		logger.info("validation result: " + result);
	}
	
	@Given("^a bid response json input \"([^\"]*)\" to be validated$")
	public void bidResponseExampleToBeValidated(String bidResponse) throws Throwable {
		resource = bidResponse;
	}

	@When("^an openRtb validator version \"([^\"]*)\" runs validation on given bid response$")
	public void isValidBidResponse(String version) throws Throwable {
		getVersion(version);
		validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, openRtbVersion);
		result = validator.validate(JsonLoader.fromResource(resource));
		logger.info("validation result: " + result);
	}

	@Then("^a valid result is returned by the validator$")
	public void validationOutput() throws Throwable {
		assertTrue(resource + " is not valid", result.isValid());
	}

	private void getVersion(String version)
	{
		switch(version)
		{
		case "2.4":
			openRtbVersion = OpenRtbVersion.V2_4;
			break;
		case "2.3":
			openRtbVersion =  OpenRtbVersion.V2_3;
			break;
		case "2.2":
			openRtbVersion = OpenRtbVersion.V2_2;
			break;
		case "2.1":
			openRtbVersion = OpenRtbVersion.V2_1;
			break;
		case "2.0":
			openRtbVersion = OpenRtbVersion.V2_0;
			break;
		case "1.0":
			openRtbVersion = OpenRtbVersion.V1_0;
			break;
		}
	}
}
