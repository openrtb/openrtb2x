/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
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
 */

package org.openrtb.dsp.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.avro.AvroRemoteException;
import org.openrtb.common.api.Bid;
import org.openrtb.common.api.BidRequest;
import org.openrtb.common.api.BidResponse;
import org.openrtb.common.api.Imp;
import org.openrtb.common.api.OpenRTBAPI;
import org.openrtb.common.api.SeatBid;
import org.openrtb.common.util.StringUtils;
import org.openrtb.common.util.statemachines.FSMCallback;
import org.openrtb.common.util.statemachines.FSMException;
import org.openrtb.common.util.statemachines.FSMTransition;
import org.openrtb.common.util.statemachines.FiniteStateMachine;
import org.openrtb.dsp.intf.model.RTBAdvertiser;
import org.openrtb.dsp.intf.model.RTBRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatefulBidder implements OpenRTBAPI {
	private final String adId = "AD123456789";
	private long numBids;
	private long numResponses;

	// SL4J Logger, ConcurrentMap and ConcurrentHashMap are threadsafe
	private final Logger logger = LoggerFactory.getLogger(StatefulBidder.class);
	private final ConcurrentMap<String, TSMController> transactions = new ConcurrentHashMap<String, TSMController>();

	public StatefulBidder() {
		numBids = 0L;
		numResponses = 0L;
	}

	private synchronized long nextBidNum() {
		this.numBids++;
		return this.numBids;
	}

	private synchronized long nextResponseNum() {
		this.numResponses++;
		return this.numResponses;
	}

	public boolean validateRequest(BidRequest request) {
		if (request == null) {
			logger.error("BidRequest object was null");
			return false;
		} else { 
			if (request.getId() == null) {
					logger.error("BidRequest must have valid Id");
					return false;
			} else if (request.getImp() == null || request.getImp().isEmpty()) {
					logger.error("BidRequest must have one or more impressions");
					return false;
			} else {
					List<Imp> impressionList = request.getImp();
					Iterator<Imp> itr = impressionList.iterator();
					while (itr.hasNext()) {
						Imp imp = itr.next();
						if (imp.getId() == null) {
							logger.error("Imp must have valid Id");
							return false;
						} else if (imp.getBanner() == null && imp.getVideo() == null) {
							logger.error("Impression must have  atleast Banner or Video Object");
							return false;
						} else if (imp.getVideo() != null) {
							if (imp.getVideo().getMimes() == null
									|| imp.getVideo().getMaxduration() == null
									|| imp.getVideo().getMinduration() == null
									|| imp.getVideo().getLinearity() == null
									|| imp.getVideo().getProtocol() == null) {
								logger.error("Video Object must have required fields.");
								return false;
							}
						}
					}
			}
			if ((request.getSite() != null && request.getApp() != null)) {
				logger.error("BidRequest should have either site or app  object not both");
				return false;
			}
		}
		return true;
	}

	public BidResponse selectBids(RTBRequestWrapper wReq, BidResponse response) {

		if (wReq.getImp().size()<=0)
			response = null;
		else {
			response = new BidResponse();
			response.id = wReq.getRequest().getId();
			response.bidid = "simple-bid-tracker-" + nextResponseNum();

			Map<String, String> seats = wReq.getUnblockedSeats(wReq
					.getSSPName());
			for (Imp i : wReq.getRequest().getImp()) {
				for (Map.Entry<String, String> s : seats.entrySet()) {
					RTBAdvertiser a = wReq.getAdvertiser(s.getValue());

					SeatBid seat_bid = new SeatBid();
					seat_bid.seat = s.getKey();
					seat_bid.bid = new ArrayList<Bid>();
					Bid b = new Bid();
					b.id = "StatefulBid #" + nextBidNum();
					b.impid = i.getId();
					b.price = i.getBidfloor() + (float) 0.10; // always bid 10c ents
																// more than the floor
					b.nurl = a.getNurl();
					b.adid = adId; // serves up the same ad to all impressions
					seat_bid.bid.add(b);
					List<SeatBid> seatBidList = new ArrayList<SeatBid>();
					seatBidList.add(seat_bid);
					response.setSeatbid(seatBidList);
				}
			}
		}
		return response;
	}

	@Override
	public BidResponse process(BidRequest request) throws AvroRemoteException {
		// get the transaction wrapper around the request object
		RTBRequestWrapper transaction = (RTBRequestWrapper) request;
		// create a new state machine controller to execute this transaction
		TSMController controller = new TSMController(this, transaction);
		transactions.put(transaction.getSSPName(), controller);
		try {
			controller.exec(TSMStates.TXN_CLOSED);
		} catch (Exception e) {
			logger.error("StatefulBidder: Exception in process method : " + StringUtils.stackTraceToString(e));
			throw new AvroRemoteException(e.getMessage());
		}
		// transaction.setRequestHistory(controller.getHistory());
		return controller.response;
	}

	// use type inference to avoid having to type long generic type names
	// *not* pseudo typedefs
	private static <A extends FSMCallback, B> FSMTransition<A, B> newTransition(
			B event) {
		return new FSMTransition<A, B>(event);
	}

	public static final FSMTransition<TSMStates, String> EV_NEWREQUEST = StatefulBidder
			.newTransition("NewRequest");
	private static final FSMTransition<TSMStates, String> EV_FORMATERROR = StatefulBidder
			.newTransition("FormatError");
	private static final FSMTransition<TSMStates, String> EV_NOTSUPPORTED = StatefulBidder
			.newTransition("NotSupported");
	private static final FSMTransition<TSMStates, String> EV_SELECTBIDS = StatefulBidder
			.newTransition("SelectBids");
	private static final FSMTransition<TSMStates, String> EV_REQUEST_EXPIRED = StatefulBidder
			.newTransition("RequestExpired");
	private static final FSMTransition<TSMStates, String> EV_BIDSOFFERED = StatefulBidder
			.newTransition("BidsOffered");
	private static final FSMTransition<TSMStates, String> EV_NOMATCHINGBIDS = StatefulBidder
			.newTransition("NoMatchingBids");
	private static final FSMTransition<TSMStates, String> EV_OFFER_EXPIRED = StatefulBidder
			.newTransition("OfferExpired");
	private static final FSMTransition<TSMStates, String> EV_WIN_NOTIFICATION = StatefulBidder
	.newTransition("WinNotification");	

	private class TSMController {
		StatefulBidder bidder;
		RTBRequestWrapper request;
		BidResponse response;
		FiniteStateMachine<TSMStates> tsm;
		private final Timer requestTimer = new Timer();
		private final Timer offerTimer = new Timer();
	
		TSMController(StatefulBidder statefulBidder, RTBRequestWrapper wReq) {
			this.bidder = statefulBidder;
			this.request = wReq;
			this.response = null;
			this.tsm = new FiniteStateMachine<TSMStates>();
			tsm.addStates(TSMStates.values());
			tsm.addTransition(TSMStates.TXN_CLOSED, EV_NEWREQUEST, TSMStates.TXN_WAIT_NEW);
			tsm.addTransition(TSMStates.TXN_WAIT_NEW, EV_FORMATERROR, TSMStates.TXN_FORMATERROR);
			tsm.addTransition(TSMStates.TXN_WAIT_NEW, EV_REQUEST_EXPIRED, TSMStates.TXN_REQUESTEXPIRED);
			tsm.addTransition(TSMStates.TXN_WAIT_NEW, EV_SELECTBIDS, TSMStates.TXN_WAIT_OPEN);
			tsm.addTransition(TSMStates.TXN_WAIT_OPEN, EV_FORMATERROR, TSMStates.TXN_FORMATERROR);
			tsm.addTransition(TSMStates.TXN_WAIT_OPEN, EV_REQUEST_EXPIRED, TSMStates.TXN_REQUESTEXPIRED);
			tsm.addTransition(TSMStates.TXN_WAIT_OPEN, EV_NOTSUPPORTED, TSMStates.TXN_NOBID);
			tsm.addTransition(TSMStates.TXN_WAIT_OPEN, EV_NOMATCHINGBIDS, TSMStates.TXN_NOBID);		
			tsm.addTransition(TSMStates.TXN_WAIT_OPEN, EV_BIDSOFFERED, TSMStates.TXN_WAIT_BIDSOFFERED);
			tsm.addTransition(TSMStates.TXN_WAIT_BIDSOFFERED, EV_OFFER_EXPIRED, TSMStates.TXN_OFFEREXPIRED);
			tsm.addTransition(TSMStates.TXN_WAIT_BIDSOFFERED, EV_WIN_NOTIFICATION, TSMStates.TXN_COMPLETE);
			
		}
		public void exec(TSMStates startState) {
			tsm.exec(startState, this);
		}

		private final TimerTask requestTimerTask = new TimerTask() {
			@Override
			public void run() {
				EV_REQUEST_EXPIRED.setState(tsm.getCurrent());
				tsm.followTransition(EV_REQUEST_EXPIRED, this);
			}
		};

		private final TimerTask offerTimerTask = new TimerTask() {
			@Override
			public void run() {
				EV_OFFER_EXPIRED.setState(tsm.getCurrent());
				tsm.followTransition(EV_OFFER_EXPIRED, this);
			}
		};

		public void setRequestTimer() {
			//requestTimer.cancel();
			requestTimer.schedule(requestTimerTask, request.getRequestTO());
		}

		public void cancelRequestTimer() {
			requestTimer.cancel();
		}

		public synchronized void setOfferTimer() {
			if (!request.isOfferTimerActive()) {
				offerTimer.schedule(offerTimerTask, request.getOfferTO());
				request.setOfferTimerActive(true);
			}
		}

		public synchronized void cancelOfferTimer() {
			offerTimer.cancel();
		}
	}

	public enum TSMStates implements FSMCallback {
		TXN_CLOSED {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger.info("New Request");				
				context.setRequestTimer();
				EV_NEWREQUEST.setState(this);
				return EV_NEWREQUEST;
			}
		},

		TXN_WAIT_NEW {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger.info("Validating Request Message Format");
				boolean valid = context.bidder.validateRequest(context.request
						.getRequest());
				if (!valid) {
					EV_FORMATERROR.setState(this);
					return EV_FORMATERROR;
				}
				return EV_SELECTBIDS;
			}
		},

		TXN_FORMATERROR {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger
						.error("Terminating transaction due to Format Error");
				context.cancelRequestTimer();
				context.response = null;
				return null; // this is an end state for this TSM
			}
		},

		TXN_WAIT_OPEN {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger.info("Finding matching Bids");
				context.response = context.bidder.selectBids(context.request,
						context.response);
				if (context.response == null) {
					EV_NOMATCHINGBIDS.setState(this);
					return EV_NOMATCHINGBIDS;
				}
				EV_BIDSOFFERED.setState(this);
				context.cancelRequestTimer();
				return null; //this should be end state
			}
		},

		TXN_NOBID {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger
						.info("No matching bids were found, there will be no response");
				context.cancelRequestTimer();
				return null; // this is an end state for this TSM
			}
		},

		TXN_WAIT_BIDSOFFERED {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger
						.info("Waiting for Notification on Bids for BidRequest id: "
								+ context.request.getRequest().getId()
										.toString());
				context.cancelRequestTimer();
				context.setOfferTimer();
				EV_BIDSOFFERED.setState(this);
				return EV_BIDSOFFERED;
			}
		},

		TXN_REQUESTEXPIRED {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.bidder.logger
						.info("Request timed out. Terminating state machine.");
				context.response = null;
				return null; // this is an end state
			}
		},

		TXN_OFFEREXPIRED {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				StatefulBidder.TSMController context = (StatefulBidder.TSMController) ctx;
				context.bidder.logger
						.info("Offer timed out. Terminating state machine.");
				context.cancelOfferTimer();
				return null; // this is an end state
			}
		},
		
		TXN_COMPLETE {
			public synchronized FSMTransition<TSMStates, String> exec(Object ctx)
					throws FSMException {
				TSMController context = (TSMController) ctx;
				context.response = null;
				return null; // this is an end state
			}
		}
	}
}
