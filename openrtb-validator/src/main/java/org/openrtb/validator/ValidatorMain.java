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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import com.github.fge.jackson.JsonLoader;

/********************************************************************
 * This is the main class to run openRtb validator.                 *
 * A user can enter the input type and version for openRtb in order *
 * to get validator corresponding to entered input type and version.*
 * The validator gives the result after validating a JSon file by   *
 * entering its location. 											*
 *******************************************************************/

public class ValidatorMain {
	private static OpenRtbInputType type = null;
	private static OpenRtbVersion version = null;
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		/** Input Commands for Input Type*/
		System.out.println("Enter Input type for OpenRtbValidator: ");
		System.out.println("1. BID_REQUEST ");
		System.out.println("2. BID_RESPONSE ");

		HashMap<Integer,OpenRtbInputType> m_openRtbInputType = new HashMap<Integer,OpenRtbInputType>();
		m_openRtbInputType.put(1, OpenRtbInputType.BID_REQUEST);
		m_openRtbInputType.put(2, OpenRtbInputType.BID_RESPONSE);
		type = m_openRtbInputType.get(input.nextInt());

		/** Input Commands for Version*/
		System.out.println("Enter version for OpenRtbValidator: ");
		System.out.println("1. V1_0 ");
		System.out.println("2. V2_0 ");
		System.out.println("3. V2_1 ");
		System.out.println("4. V2_2 ");
		System.out.println("5. V2_3 ");
		System.out.println("6. V2_4 ");

		HashMap<Integer, OpenRtbVersion> m_openRtbVersion = new HashMap<Integer, OpenRtbVersion>();
		m_openRtbVersion.put(1, OpenRtbVersion.V1_0);
		m_openRtbVersion.put(2, OpenRtbVersion.V2_0);
		m_openRtbVersion.put(3, OpenRtbVersion.V2_1);
		m_openRtbVersion.put(4, OpenRtbVersion.V2_2);
		m_openRtbVersion.put(5, OpenRtbVersion.V2_3);
		m_openRtbVersion.put(6, OpenRtbVersion.V2_4);
		version = m_openRtbVersion.get(input.nextInt());

		OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(type, version);

		/** Input Commands for JSon File Location*/
		System.out.println("Enter json file location");
		String resource = input.next();
		try {
			File file = new File(resource);
			ValidationResult result = validator.validate(JsonLoader.fromFile(file));
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		input.close();
	}
}