package org.openrtb.dsp.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.protobuf.ProtobufDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.thrift.ThriftDatumWriter;
import org.junit.Before;
import org.junit.Test;
import org.openrtb.common.api.App;
import org.openrtb.common.api.Banner;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.Data;
import org.openrtb.common.api.Device;
import org.openrtb.common.api.Geo;
import org.openrtb.common.api.Imp;
import org.openrtb.common.api.Site;
import org.openrtb.common.api.User;
import org.openrtb.common.api.Video;
import org.openrtb.dsp.intf.model.DSPException;

/*
 *This class is used to Test the functionality of a DemandSideServer class .
 */
public class DemandSideServerTest {
	private static EncoderFactory ENCODER_FACTORY = EncoderFactory.get();
	private static final String AVRO_CONTENT_TYPE = "avro/binary";
	private static final String PROTOBUF_CONTENT_TYPE = "application/x-protobuf";
	private static final String THRIFT_CONTENT_TYPE = "application/x-thrift";
	private static final String JSON_CONTENT_TYPE = "application/json";
	BidRequest request = null;
	BidRequest bidRequest = null;
	
	String jsonContent = "{\"id\" :"+"\"ad1d762\" ,\n"+"\"at\" :" +"\"2\" ,\n"
	+ "\"tmax\" :"+150+",\n"+"\"imp\" : [{ \n" +"\"id\" :"+"\"1\" ,"+"\"bidfloor\" :"+12.4 +",\n"+
	"\"tagid\" :" + "\"30027\" ,\n" + "\"banner\" : \n" + "{\n" + "\"w\" :" + 468 +","+ "\"h\" :" + 60 +","+
	"\"battr\" : " + "[\n"+ 9+","+1+","+14014+"], \n"+ "\"api\" :" + "[]} ,\n"+
	"\"video\" :  { \n"+"\"mimes\" : ["+
	"\"video/x-ms-wmv\"," + "\"video/x-flv\" ],"
	+"\"linearity\" :"+1+","+"\"minduration\" :"+100+","+"\"maxduration\" :"+250+","
	+"\"protocol\" :"+414+","+"\"w\" :"+722+","+"\"h\" :"+30+","+"\"startdelay\" :"+200+
	"}}],\n"+"\"app\" : {\n"+"\"id\" :"+"\"\102345\",\n "
	+"\"name\" :"+"\"whatsup\" ,\n"+"\"domain\" :"+"\"whatsup.com\""+"},\n"
	+
	"\"site\" : \n {" + "\"id\" : " + "\"144655\" ,\n" +"\"domain\" :" + "\"discovery.com\"," +"\"cat\" :[\n" +"\"IABI\" ],\n" +
	"\"page\" :" +"\"abc\" ,\n" +"\"publisher\" :" +"{ \n" +"\"id\" :" +"\"0\" },\n" +
	"\"ext\" :"+ "\"myext\"\n },"
	+"\"wseat\" : \n ["+
	"\"w101\" ,"+"\"w102\" ,"+"\"w103\"],"+"\"user\" :{"+"\"id\" :"+"\"edcbabb5691ece183ca4d\" ,\n"+
	"\"ext\" :"+ "\"myext\" }"+"}";
	
	@Before
	public void setUp() {
		request = new BidRequest();
		Banner banner = new Banner();
		banner.setId("102dsd");
		banner.setH(25);
		banner.setW(30);
		banner.setPos(2545);
		banner.setTopframe(24);
		banner.setExt("45dsa");
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setH(25);
		video.setW(30);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		video.setStartdelay(25);
		video.setSequence(125);
		video.setMaxextended(44);
		video.setMinbitrate(55);
		video.setMaxbitrate(55);
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setVideo(video);
		imp.setInstl(55);
		imp.setBidfloor(new Float(2.09));

		imp.setExt("45dsa");
		imp.setDisplaymanager("sdf555");
		imp.setDisplaymanagerver("ss223");
		imp.setTagid("56666");
		imp.setBidfloorcur("dfsd");
		List<Imp> impression = new ArrayList<Imp>();
		impression.add(imp);
		List<CharSequence> wseat = new ArrayList<CharSequence>();
		wseat.add("SeatID001");
		request.setWseat(wseat);
		Site site = new Site();
		site.setId("124545sfdghs");
		List<CharSequence> cat = new ArrayList<CharSequence>();
		cat.add("cat1");
		site.setCat(cat);
		App app = new App();
		app.setId("appTest");
		app.setBundle("sas");
		app.setCat(cat);

		app.setDomain("as");
		app.setExt("ssd");
		app.setKeywords("as");
		app.setName("anroid");
		Device d = new Device();
		d.setDidmd5("adsfda");
		d.setDidsha1("daf");
		d.setDnt(2);
		Geo g = new Geo();
		g.setType(5);
		d.setIp("555656asdaasdd");
		d.setIpv6("ads");
		request.setDevice(d);
		Data data = new Data();
		data.setExt("asd");
		data.setId("df454");
		data.setName("asd");
		User u = new User();
		u.setBuyeruid("a44");
		u.setCustomdata("asd");
		u.setExt("asd");
		u.setGender("male");
		u.setGeo(g);
		request.setUser(u);
		request.setApp(app);
		request.setId("sdafsd252222");
		request.setImp(impression);
		List<CharSequence> badv = new ArrayList<CharSequence>();
		badv.add("abcds");
		request.setBadv(badv);
	}

	@Before
	public void setUpRequiredFieldBidRequest() {
		bidRequest = new BidRequest();
		Banner banner = new Banner();
		banner.setH(25);
		banner.setW(30);
		banner.setId("1203");
		Video video = new Video();
		List<CharSequence> mimes = new ArrayList<CharSequence>();
		mimes.add("video/x-mswmv");
		video.setMimes(mimes);
		video.setLinearity(54);
		video.setMinduration(100);
		video.setMaxduration(500);
		video.setProtocol(200);
		video.setH(25);
		video.setW(30);
		App app = new App();
		app.setId("appTest");
		app.setBundle("bundle");
		Site site = new Site();
		site.setId("124545sfdghs");
		site.setPage("my page");
		Device device = new Device();
		device.setDnt(2);
		device.setIp("555656asdaasdd");
		device.setUa("dfdsa");
		User user = new User();
		user.setBuyeruid("a44");
		user.setId("56sdf");
		Imp imp = new Imp();
		imp.setId("10212sdsa1");
		imp.setBanner(banner);
		imp.setVideo(video);
		imp.setDisplaymanager("sdf555");
		imp.setDisplaymanagerver("ss223");
		imp.setBidfloor(new Float(15.4));
		List<Imp> impression = new ArrayList<Imp>();
		impression.add(imp);

		bidRequest.setId("102335assd55d");
		bidRequest.setImp(impression);
		bidRequest.setApp(app);
		bidRequest.setDevice(device);
		bidRequest.setUser(user);
		bidRequest.setSite(site);
	}

	/**
	 * This method is used to test the authorizeRemoteService property of DemandSideServer class
	 */
	@Test
	public void authorizeRemoteServiceTest() throws DSPException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		boolean authorize = server.authorizeRemoteService("BigAdExchange");
		assertTrue("Not valid Authorize Service", authorize);
	}
	/**
	 * This method is used to test the verifyContentType property of DemandSideServer class
	 */
	@Test
	public void verifyContentTypeTest() throws DSPException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);		
		boolean validate = server.verifyContentType("BigAdExchange",JSON_CONTENT_TYPE);
		assertTrue("Not valid Content Type", validate);
	}
	
	/**
	 * This method is used to test the respond method with json content type 
	 */
	@Test
	public void jsonRespondTest() throws DSPException, IOException {		
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(jsonContent.getBytes());
		byte b[] = server.respond("BigAdExchange", in, JSON_CONTENT_TYPE);	
		assertNotNull("Response should not be null ",b);
	}
	/**
	 * This method is used to test the respond method with avro content type 
	 */
	@Test
	public void avroRespondTest() throws DSPException, IOException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(writeBidRequest(request,
				AVRO_CONTENT_TYPE));
		server.respond("BigAdExchange", in, AVRO_CONTENT_TYPE);
	}
	
	/**
	 * This method is used to test the required parameters in  json BidRequest 
	 */
	@Test
	public void testRequiredParameter() throws DSPException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(jsonContent.getBytes());
		server.respond("BigAdExchange", in, JSON_CONTENT_TYPE);

	}
	/**
	 * This method is used to test the respond method with protobuf content type 
	 */
	//@Test
	public void protobufRespondTest() throws DSPException, IOException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(writeBidRequest(request,
				PROTOBUF_CONTENT_TYPE));
		server.respond("BigAdExchange", in, PROTOBUF_CONTENT_TYPE);
	}
	/**
	 * This method is used to test the respond method with thrift content type 
	 */
	//@Test
	public void thriftRespondTest() throws DSPException, IOException {
		OpenRTBAPIDummyTest bidder = new OpenRTBAPIDummyTest();
		DemandSideDAODummyTest dao = new DemandSideDAODummyTest();
		URL url = this.getClass().getResource("/properties.json");
		dao.loadData(url.getPath());
		DemandSideServer server = new DemandSideServer(bidder, dao);
		InputStream in = new ByteArrayInputStream(writeBidRequest(request,
				THRIFT_CONTENT_TYPE));
		server.respond("BigAdExchange", in, THRIFT_CONTENT_TYPE);
	}

	protected byte[] writeBidRequest(BidRequest bidRequest, String contentType)
			throws DSPException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Encoder encoder = null;
		DatumWriter<BidRequest> writer = null;
		try {// encorder
			encoder = ENCODER_FACTORY.binaryEncoder(os, null);
			// writer
			if (contentType.equals(AVRO_CONTENT_TYPE)) {
				writer = new SpecificDatumWriter<BidRequest>(BidRequest.SCHEMA$);
			} else if (contentType.equals(THRIFT_CONTENT_TYPE)) {
				writer = new ThriftDatumWriter<BidRequest>(BidRequest.class);
			} else if (contentType.equals(PROTOBUF_CONTENT_TYPE)) {
				writer = new ProtobufDatumWriter<BidRequest>(BidRequest.class);
			}
			writer.write(bidRequest, encoder);
			encoder.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for all binary output, the data needs to be serialized
		ByteBuffer serialized = ByteBuffer
				.allocate(os.toByteArray().length);
		serialized.put(os.toByteArray());
		return serialized.array();
	}

}
