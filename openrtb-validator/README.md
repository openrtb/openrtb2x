# openrtb-validator

This project provides a simple API that can be used to validate JSON bid requests and responses according to OpenRTB specifications. OpenRTB versions 1.0, 2.0, 2.1, 2.2, 2.3 and 2.4 are fully supported.

Provided under the New BSD License. Refer to the file LICENSE file in the root of this project for more information.

## Usage

Add the OpenRTB-validator dependency to your Maven pom.xml:

    <dependency>
        <groupId>org.openrtb</groupId>
        <artifactId>openrtb-validator</artifactId>
        <version>2.3.2</version>
        <type>jar</type>
    </dependency>

To ascertain whether a given JSON String, File, Reader, or Resource is a valid bid request according to OpenRTB v2.4 specifications:

    OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_4);
    boolean valid = validator.isValid(json);

To get a detailed validation report including reasons why the JSON is invalid:

    ValidationResult validationResult = validator.validate(json);
    System.out.println("valid: " + validationResult.isValid() + ", result: " + validationResult.getResult());

## Specification Documents

The specification documents used to create these OpenRTB validation schemas can be found under src/main/resources/specification and at http://www.iab.net/guidelines/rtbproject.
