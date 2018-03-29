package org.openrtb.dsp.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avro.AvroRemoteException;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.App;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.api.Imp;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.intf.model.DSPException;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBExchange;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;

/*
 * This Class is used  to Validate StatefulBidder class functionality
 * and  Validate BidRequest content in a BidRequest Object.
 */
public class StatefulBidderTest {
	StatefulBidder bidder = null;
	BidRequest request = null;
	BidRequest requestSite = null;

	@Before
	public void setUp()  {
		bidder = new StatefulBidder();
		request = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085) );
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		imp.setVideo(video);
		App app = new App();
		app.setId("appTest");
		app.setBundle("com.mygame");
		when(request.getId()).thenReturn(
				"ad1d762d6d9719b6b3c9e09f6433a76d9b593738");
		when(request.getImp()).thenReturn(
				Collections.<Imp> singletonList(imp));
		when(request.getApp()).thenReturn(app);
		when(request.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}

	@Before
	public void setUpSiteTest()  {
		bidder = new StatefulBidder();
		requestSite = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor(new Float(10.085) );
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
		site.setId("siteTest0214");
		site.setPage("com.mygame");
		when(requestSite.getId()).thenReturn(
				"ad1d762d6d9719b6b3c9e09f6433a76d9b593738");
		when(requestSite.getImp()).thenReturn(
				Collections.<Imp> singletonList(imp));
		when(requestSite.getSite()).thenReturn(site);
		when(requestSite.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
	}
	
	/**
	 * This method test the  validateRequest property of SimpleBidder class
	 */
	@Test
	public void validateRequestTest()  {
		boolean test = bidder.validateRequest(request);
		assertTrue("A valid Request ", test == true);
	}
	
	/**
	 * This method test the App Object should be set in BidRequest Object and checks required fields of a App Object
	 */
	@Test
	public void appTest() {
		App app = request.getApp();
		assertNotNull("App should be null in a BidRequest", request.getApp());
		assertTrue("App id should be provided ", app.getId().equals("appTest"));
		assertTrue("Application bundle required ",app.getBundle().equals("com.mygame"));
	}

	/**
	 * This method test the Banner Object Should be set in BidRequest object and checks required fields of a Banner Object.
	 */
	@Test
	public void bannerAdTest() {
		List<Imp> impList = request.getImp();
		Iterator<Imp> itr = impList.iterator();
		while (itr.hasNext()) {
			Imp imp = itr.next();
			Banner b = imp.getBanner();
			assertTrue("Height of the impression must be provided in Banner Object",
					b.getH() == 25);
			assertTrue("Width of the impression must be provided in Banner Object",
					b.getW() == 30);
		}
	}
	
	/**
	 * This method test the Video Object Should be set in BidRequest object and checks required fields of a Video Object.
	 */
	@Test
	public void videoAdTest() {
		List<Imp> impList = request.getImp();
		Iterator<Imp> itr = impList.iterator();
		while (itr.hasNext()) {
			Imp imp = itr.next();
			Video video = imp.getVideo();
			assertNotNull("mimes types must be provided in Video Object",
					video.getMimes());
			assertTrue("linearity types must be provided in Video Object",
					video.getLinearity() == 54);
			assertTrue("min duration must be provided in Video Object",
					video.getMinduration() == 100);
			assertTrue("max duration must be provided in Video Object",
					video.getMaxduration() == 500);
			assertTrue("protocol must be provided in Video Object",
					video.getProtocol() == 200);
		}
	}

	/**
	 * This method test the Site Object Should be set in BidRequest object and checks required fields of a Site Object.
	 */
	@Test
	public void SideTest() {
		Site site = requestSite.getSite();
		assertNotNull("Site should be null in a BidRequest", requestSite.getSite());
		assertTrue("Site id should be provided ", site.getId().equals("siteTest0214"));
		assertTrue("Site page  required ",
				site.getPage().equals("com.mygame"));
	}
	
	/**
	 * This method test the  wseat property in a BidRequest
	 */
	@Test
	public void wseatTest() {
		List<CharSequence> wseat = request.getWseat();
		assertTrue("wseat is empty", wseat.size() == 1);
		assertTrue("Match wseatId", wseat.get(0).equals("012asfdfd25"));
	}
	/**
	 * This method test the  selectBids property in a BidRequest
	 */
	@Test
	public void selectBidsTest() throws DSPException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(request);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "1001");
		RTBAdvertiser adv = new RTBAdvertiser("MyPage", "BigBrandIndia",
				"http://bigbrand-adserver.com/nurl", categories, seats);
		Map<String, RTBAdvertiser> advertisersMap = new HashMap<String, RTBAdvertiser>();
		advertisersMap.put(adv.getLandingPage(), adv);
		long reqTimeout = 2000;
		long offerTimeout = 1000;
		wReq.setContext(exchange, advertisersMap, reqTimeout, offerTimeout);
		BidResponse response = new BidResponse();
		BidResponse res = bidder.selectBids(wReq, response);

		assertNotNull("Response should not be empty ", res);
		assertTrue("Response should atleast one seat Bid object ", res
				.getSeatbid().size() == 1);
	}

	/**
	 * This method test the functionality of process method of StatefulBidder class
	 */
	@Test
	public void processTest() throws AvroRemoteException {
		
		RTBRequestWrapper wReq = new RTBRequestWrapper(request);
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
		long reqTimeout = 5000;
		long offerTimeout = 6000;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		assertNotNull("Response should not be empty ", response);
	}
	
	/**
	 * This method test the valid response comes after the BidRequest processed by the process method 
	 */
	@Test
	public void validateResponseTest() throws AvroRemoteException {
		RTBRequestWrapper wReq = new RTBRequestWrapper(request);
		RTBExchange exchange = new RTBExchange("BigAdExchange",
				"http://bigadex.com/rtb", "application/json");
		List<String> categories = new ArrayList<String>();
		categories.add("cat1");
		categories.add("cat2");
		categories.add("cat3");

		Map<String, String> seats = new HashMap<String, String>();
		seats.put("BigAdExchange", "1001");
		seats.put("SmallAdExchange", "1002");
		RTBAdvertiser adv = new RTBAdvertiser("MyPage", "BigBrandIndia",
				"http://bigbrand-adserver.com/nurl", categories, seats);
		Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
		advertisers.put(adv.getLandingPage(), adv);
		long reqTimeout = 2000;
		long offerTimeout = 1000;
		wReq.setContext(exchange, advertisers, reqTimeout, offerTimeout);
		BidResponse response = bidder.process(wReq);
		assertNotNull("Response should not be empty ", response);
		assertTrue("Response should atleast one seat Bid object ",
				response.getSeatbid().size()>0);
		assertTrue("Response has 1 Bid object ",
				response.getSeatbid().size()==1);
		assertNotNull("Response should have valid Bid ID ", response.getBidid());
		assertTrue("Response should have valid response ID ", response.getId()=="ad1d762d6d9719b6b3c9e09f6433a76d9b593738");
	}

}
