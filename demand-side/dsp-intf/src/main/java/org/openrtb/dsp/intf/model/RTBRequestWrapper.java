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

package org.openrtb.dsp.intf.model;

import org.openrtb.common.api.BidRequest;

import java.util.HashMap;
import java.util.Map;

public class RTBRequestWrapper extends BidRequest {

	public RTBRequestWrapper() {
		super();
	}

	BidRequest request;
	RTBExchange exchange;
	final Map<String, RTBAdvertiser> advertisers = new HashMap<String, RTBAdvertiser>();
	long requestTimeoutMs;
	long offerTimeoutMs;
	private boolean isOfferTimerActive;

	public RTBRequestWrapper(BidRequest request) {

        super(request.getId(), request.getImp(), request.getSite(), request
                .getApp(), request.getDevice(), request.getUser(), request.getTest(), request
                .getAt(), request.getTmax(), request.getWseat(), request.getBseat(), request
                .getAllimps(), request.getCur(), request.getWlang(), request.getBcat(), request
                .getBadv(), request.getBapp(), request.getSource(), request.getRegs(), request.getExt());
        this.request = request;
	}

	public void setContext(RTBExchange exchange, Map<String, RTBAdvertiser> advertisers,
							long defaultRequestTO, long defaultOfferTO) {
		this.exchange = new RTBExchange(exchange);
		this.advertisers.clear();
		this.advertisers.putAll(advertisers);
		if (this.request.tmax == null) {
			this.requestTimeoutMs = defaultRequestTO;
		} else {
			this.requestTimeoutMs = this.request.tmax.longValue();
		}
		this.offerTimeoutMs = defaultOfferTO;
		this.isOfferTimerActive = false;
	}

	public BidRequest getRequest() {
		return request;
	}

	/**
	 * Builds a list of advertiser seat Ids that are allowed to bid on this
	 * request
	 *
	 * @param sspName
	 * @return seats
	 */
	public Map<String, String> getUnblockedSeats(String sspName) {
		Map<String, String> seats = new HashMap<String, String>();
		boolean checkWseat = ((this.request.getWseat() != null) && (!this.request
				.getWseat().isEmpty()));
		for (Map.Entry<String, RTBAdvertiser> a : this.advertisers.entrySet()) {
			// check if this is a private deal (checkWseat == true)
			CharSequence seatID = a.getValue().getSeat(sspName);
			// this.request.wseat.contains(seatID));
			/**
			 * if (checkWseat && !request.getWseat().contains(seatID)) {
			 *	 break;
			 * // yes its a private deal, but this adv is not part of it }
			 **/

			if (this.request.getBadv() != null) {
				// now check for blocked advertisers
				for (CharSequence badv : this.request.getBadv()) {

					if (badv.equals(a.getKey())) {
						break; // this advertiser is blocked for this request
					}
				}
			}
			// now check for blocked categories
			for (String acat : a.getValue().getCategories()) {
				if (this.request.getBcat() != null) {
					for (CharSequence bcat : this.request.getBcat()) {
						if (!acat.equals(bcat.toString())) {
							break; // this advertiser belongs to a category that
									// is
									// blocked for this request
						}
					}
				}
			}

			// if all tests pass, add seat as an allowed seat to return
			seats.put(seatID.toString(), a.getValue().getLandingPage());
		}
		return seats;
	}

	public String getSSPName() {
		if (exchange != null) {
			return exchange.getOrgName();
		}
		return null;
	}

	public RTBAdvertiser getAdvertiser(String landingPage) {
		return advertisers.get(landingPage);
	}

	public void setRequestTO(long requestTO) {
		this.requestTimeoutMs = requestTO;
	}

	public long getRequestTO() {
		return this.requestTimeoutMs;
	}

	public long getOfferTO() {
		return this.offerTimeoutMs;
	}

	public void setOfferTimerActive(boolean trueOrFalse) {
		this.isOfferTimerActive = trueOrFalse;
	}

	public boolean isOfferTimerActive() {
		return isOfferTimerActive;
	}
}
