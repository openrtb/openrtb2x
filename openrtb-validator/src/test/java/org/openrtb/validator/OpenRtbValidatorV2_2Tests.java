/*
 * ============================================================================
 * Copyright (c) 2015, Millennial Media, Inc.
 * All rights reserved.
 * Provided under BSD License as follows:
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1.  Redistributions of source code must retain the above copyright notice, 
 *     this list of conditions and the following disclaimer.
 * 2.  Redistributions in binary form must reproduce the above copyright 
 *     notice, this list of conditions and the following disclaimer in the 
 *     documentation and/or other materials provided with the distribution.
 * 3.  Neither the name of Millennial Media, Inc. nor the names of its
 *     contributors may be used to endorse or promote products derived from this
 *     software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * ============================================================================
 */

package org.openrtb.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fge.jackson.JsonLoader;

/**
 * Test examples taken from OpenRTB v2.2 specification document. 
 */
public class OpenRtbValidatorV2_2Tests {

	private static final Logger logger = LoggerFactory.getLogger(OpenRtbValidatorV2_2Tests.class);
	
    @Test
    public void testBidRequestExample1SimpleBanner() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);

		// NOTE: IABs example is invalid due to:
    	// 1. "device.ip" is not valid a valid ip address
    	// 2. "site.cat" is not an array
    	// 3. "site.publisher.cat" is not an array
    	String invalidResource = "/v2_2/bid_requests/example1_simple_banner.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

    	String resource = "/v2_2/bid_requests/fixed/example1_simple_banner.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

		logger.info("validation result: " + result);
		assertTrue(resource + " is not valid", result.isValid());
    }
    
    @Test
    public void testBidRequestExample2ExpandableCreative() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);

		// NOTE: IABs example is invalid due to:
    	// 1. "device.ip" is not valid a valid ip address
    	// 2. "site.cat" is not an array
    	// 3. "site.publisher.cat" is not an array
    	// 4. "imp.banner.expandable" should be "imp.banner.expdir"
    	// 5. "imp.banner.iframebuster" should be "imp.iframebuster"
    	String invalidResource = "/v2_2/bid_requests/example2_expandable_creative.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());
    	
		String resource = "/v2_2/bid_requests/fixed/example2_expandable_creative.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testBidRequestExample3Mobile() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);
		
		// NOTE: IABs example is invalid due to:
    	// 1. "app.cat" contains an invalid category
    	// 2. "user.yob" should be an integer
    	String invalidResource = "/v2_2/bid_requests/example3_mobile.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());
    	
    	String resource = "/v2_2/bid_requests/fixed/example3_mobile.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }
    
    @Test
    public void testBidRequestExample4Video() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);

		// NOTE: IABs example is invalid due to:
    	// 1. "device.flashversion" should be "device.flashver"
    	// 2. "site.sitecat" should be "site.cat"
    	// 3. "video.boxingallowed" should be an integer
    	// 4. "user.uid" should be "user.id"
    	// 5. "video.protocol" should be "video.protocols"
    	// 6. "video.companionad.expandable" should be "video.companionad.expdir"
    	// 7. "site.privacypolicy" should be an integer
    	// 8. "site.content.keyword" should be "site.content.keywords"
    	// 9. "site.content.keyword" should be a string
    	// 9. "site.content.season" should be a string
    	String invalidResource = "/v2_2/bid_requests/example4_video.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());
    	
		String resource = "/v2_2/bid_requests/fixed/example4_video.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testBidRequestExample5PmpWithDirectDeal() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);

		// NOTE: IABs example is invalid due to:
    	// 1. "device.ip" is not valid a valid ip address
    	// 2. "site.cat" is not an array
    	// 3. "site.publisher.cat" is not an array
    	String invalidResource = "/v2_2/bid_requests/example5_pmp_with_direct_deal.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

		String resource = "/v2_2/bid_requests/fixed/example5_pmp_with_direct_deal.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testBidResponseExample1AdServedOnWinNotice() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, OpenRtbVersion.V2_2);

		String resource = "/v2_2/bid_responses/example1_ad_served_on_win_notice.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }
    
    @Test
    public void testBidResponseExample2VastUrlReturned() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, OpenRtbVersion.V2_2);

		String resource = "/v2_2/bid_responses/example2_vast_url_returned.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testBidResponseExample3VastXmlDocumentReturnedInline() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, OpenRtbVersion.V2_2);

		// NOTE: IABs example is invalid due to:
    	// 1. "bid.id" is not a string
    	// 2. "bid.impid" is not a string
    	String invalidResource = "/v2_2/bid_responses/example3_vast_xml_document_returned_inline.json";
        ValidationResult invalidResult = validator.validate(JsonLoader.fromResource(invalidResource));

        logger.info("invalid validation result: " + invalidResult);
        assertFalse(invalidResource + " is valid", invalidResult.isValid());

		String resource = "/v2_2/bid_responses/fixed/example3_vast_xml_document_returned_inline.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

    @Test
    public void testBidResponseExample4DirectDealAdServedOnWinNotice() throws IOException {
    	OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_RESPONSE, OpenRtbVersion.V2_2);

		String resource = "/v2_2/bid_responses/example4_direct_deal_ad_served_on_win_notice.json";
        ValidationResult result = validator.validate(JsonLoader.fromResource(resource));

        logger.info("validation result: " + result);
        assertTrue(resource + " is not valid", result.isValid());
    }

}
