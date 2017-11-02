package org.openrtb.dsp.client;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.AvroRemoteException;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.api.Imp;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBExchange;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;
/**
 * This class is used to test the functionality of FiniteStateMachine class with different States of a StatefulBidder class.
 */
public class FiniteStateMachineTest {
	StatefulBidder bidder = new StatefulBidder();
	BidRequest requestError = null;
	BidRequest requestNoMatch = null;
	BidRequest requestExpired = null;
	BidRequest offeredExpired = null;

	@Before
	public void setUpFormatError() {
		requestError = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085));
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		Site site = new Site();
		site.setId("124545sfdghs");

		when(requestError.getImp()).thenReturn(
				Collections.<Imp> singletonList(imp));
		when(requestError.getSite()).thenReturn(site);
		when(requestError.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}

	@Before
	public void setUpNoMatchingBid() {
		requestNoMatch = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085));
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		Site site = new Site();
		site.setId("124545sfdghs");
		when(requestNoMatch.getId()).thenReturn("adfa635656556");
		when(requestNoMatch.getSite()).thenReturn(site);
		when(requestNoMatch.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}

	@Before
	public void setUpRequestExpired() {
		requestExpired = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085));
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		List<Imp> impList = new ArrayList<Imp>();
		for (int i = 0; i < 1000000; i++) {
			imp.setId("10212sdsa1" + i);
			impList.add(imp);
		}
		Site site = new Site();
		site.setId("124545sfdghs");
		when(requestExpired.getId()).thenReturn("adfa635656556");
		when(requestExpired.getImp()).thenReturn(impList);
		when(requestExpired.getSite()).thenReturn(site);
		when(requestExpired.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}

	@Before
	public void setUpOfferedExpired() {
		offeredExpired = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085));
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		List<Imp> impList = new ArrayList<Imp>();
		for (int i = 0; i < 10; i++) {
			imp.setId("10212sdsa1" + i);
			impList.add(imp);
		}
		Site site = new Site();
		site.setId("124545sfdghs");
		when(offeredExpired.getId()).thenReturn("adfa635656556");
		when(offeredExpired.getImp()).thenReturn(impList);
		when(offeredExpired.getSite()).thenReturn(site);
		when(offeredExpired.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}
	/**
	 * This method test the format error state of StatefulBidder class
	 */
	@Test
	public void testFormatError() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(requestError);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "151");
		seats.put("SmallAdExchange", "102");
		RTBAdvertiser adv = new RTBAdvertiser("AdversiderPage", "BigIndia",
				"http://bigbrand-adserver.com", categories, seats);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 100;
		long offerTimeout = 6000;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		assertNull("Format Error but Response is not null", response);
	}
	/**
	 * This method test the  NoMatchingBid state of StatefulBidder class
	 */
	@Test
	public void testNoMatchingBid() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(requestNoMatch);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "151");
		seats.put("SmallAdExchange", "102");
	
		RTBAdvertiser adv = new RTBAdvertiser("AdversiderPage", "BigIndia",
				"http://bigbrand-adserver.com", categories, seats);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 100;
		long offerTimeout = 500;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		assertNull("No matching bid found but Response is not null", response);
	}
	
	/**
	 * This method test the request expired state to expire in a valid time
	 */
	@Test
	public void testRequestExpired() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(requestExpired);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "151");
		seats.put("SmallAdExchange", "102");
		RTBAdvertiser adv = new RTBAdvertiser("AdversiderPage", "BigIndia",
				"http://bigbrand-adserver.com", categories, seats);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 10;
		long offerTimeout = 200;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		//assertNull("Request Expired but Response is not null", response);
	}
	
	/**
	 * This method test the  offer expired state to expire in a valid time.
	 */
	//@Test
	public void testOfferExpired() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(offeredExpired);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "151");
		seats.put("SmallAdExchange", "102");
		RTBAdvertiser adv = new RTBAdvertiser("AdversiderPage", "BigIndia",
				"http://bigbrand-adserver.com", categories, seats);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 200;
		long offerTimeout = 1;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		assertNull("Offer Expired but Response is not null", response);
	}

}