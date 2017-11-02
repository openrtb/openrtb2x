package org.openrtb.dsp.intf.model;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.*;

/**
 * This class is used to test the functionality of RTBRequestWrapper class
 */
public class RTBRequestWrapperTest {
	
	private RTBExchange exchanger = null;
	private RTBAdvertiser advertiser = null;	
	private Map<String, RTBAdvertiser> advertisers  = new HashMap<String, RTBAdvertiser>();
	private long requestTime = 2000;
	private long offerTime=2000;
	BidRequest bidRequest;
	
	@Before
	public void setUp()  {		
		List<String>categories = new ArrayList<String>();
		categories.add("categories 1");
		Map<String, String> seat = new HashMap<String, String>();
		seat.put("BigAdExchange", "SeatID001");
		seat.put("SmallAdExchange", "SeatID002");
		advertiser = new RTBAdvertiser("index.htm", "addidas", "http//addidas.com/nurl", categories, seat);		
		advertisers.put("My Advertiser", advertiser);
		exchanger = mock(RTBExchange.class);					
		when(exchanger.getOrgName()).thenReturn("LGExchange");
		when(exchanger.getRtbContentType()).thenReturn("application/json");
		when(exchanger.getRtbServiceUrl()).thenReturn("http://bigadex.com/rtb");	
	}
	
	@Before
	public void setUpBidRequest() 
	{
		bidRequest = mock(BidRequest.class);
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setBidfloor((float) 10.085);
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
		when(bidRequest.getId()).thenReturn(
				"ad1d762d6d9719b6b3c9e09f6433a76d9b593738");
		when(bidRequest.getImp()).thenReturn(
				Collections.<Imp> singletonList(imp));
		when(bidRequest.getApp()).thenReturn(app);
		when(bidRequest.getWseat()).thenReturn(
				Collections.<CharSequence> singletonList("012asfdfd25"));
		
	}
	
	/**
	 * This method test the setContex property of a RTBRequestWrapper class
	 */
	@Test
	public void setContextTest()
	{
		RTBRequestWrapper requestWrapper = new RTBRequestWrapper(bidRequest);	
		requestWrapper.setContext(exchanger, advertisers, requestTime, offerTime);
		assertNotNull("Exchanger should be provided",requestWrapper.exchange);
		assertNotNull("Advertiser should be provided",requestWrapper.advertisers);
		assertTrue("Organisation name not match",requestWrapper.exchange.getOrgName()=="LGExchange");
		assertTrue("request time not match",requestWrapper.offerTimeoutMs==2000);
		assertTrue("request time not match",requestWrapper.requestTimeoutMs==2000);		
	}
	
	/**
	 * This method test the getUnblockedSeats property  of a RTBRequestWrapper class
	 */
	@Test
	public void getUnblockedSeatsTest()
	{
		RTBRequestWrapper requestWrapper = new RTBRequestWrapper(bidRequest);	
		requestWrapper.setContext(exchanger, advertisers, requestTime, offerTime);
		Map<String, String> seats = requestWrapper.getUnblockedSeats("BigAdExchange");
		assertNotNull("seats should not be null",seats);	
		assertTrue("seat value not found",seats.get("SeatID001").equals("index.htm"));
	}
	
	/**
	 * This method test the getSSPName property of a RTBRequestWrapper class
	 */
	@Test
	public void getSSPNameTest()
	{
		RTBRequestWrapper requestWrapper = new RTBRequestWrapper(bidRequest);	
		requestWrapper.setContext(exchanger, advertisers, requestTime, offerTime);	
		assertTrue("Organisation name not match",requestWrapper.getSSPName().equals("LGExchange"));		
	}
	
	/**
	 * This method test the getAdvertiser property of a RTBRequestWrapper class
	 */
	@Test
	public void getAdvertiserTest()
	{
		RTBRequestWrapper requestWrapper = new RTBRequestWrapper(bidRequest);	
		requestWrapper.setContext(exchanger, advertisers, requestTime, offerTime);	
		RTBAdvertiser advertiser=requestWrapper.getAdvertiser("My Advertiser");
		assertTrue("advertiser name not match",advertiser.getName().equals("addidas"));	
		assertTrue("advertiser Landing page not match",advertiser.getLandingPage().equals("index.htm"));
		assertTrue("advertiser NURL not match",advertiser.getNurl().equals("http//addidas.com/nurl"));
		assertNotNull("Seat should not be null",advertiser.getSeats());
		assertNotNull("categories should not be null",advertiser.getCategories());
		
	}
	
}







