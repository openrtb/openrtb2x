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
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;

/***************************************************
 * This interface provides JSON validation methods.*
 **************************************************/

public interface OpenRtbValidator {

    /**
     * Validates JSON against a given specification
     * 
     * @param json
     *            the JSON as a string
     * @return true if JSON is valid
     */
    public boolean isValid(String json);

    /**
     * Validates JSON against a given specification
     * 
     * @param jsonNode
     *            the JSON object
     * @return true if JSON is valid
     */
    public boolean isValid(JsonNode jsonNode);

    /**
     * Validates JSON against a given specification
     * 
     * @param file
     *            the JSON file
     * @return true if JSON is valid
     */
    public boolean isValid(File file);

    /**
     * Validates JSON against a given specification
     * 
     * @param reader
     *            the JSON reader
     * @return true if JSON is valid
     */
    public boolean isValid(Reader reader);

    /**
     * Validates JSON against a given specification
     * 
     * @param json
     *            the JSON as a string
     * @return a validation result
     * @throws IOException
     *             an exception occurred while parsing or validating JSON
     */
    public ValidationResult validate(String json) throws IOException;

    /**
     * Validates JSON against a given specification
     * 
     * @param jsonNode
     *            the JSON object
     * @return a validation result
     * @throws IOException
     *             an exception occurred while parsing or validating JSON
     */
    public ValidationResult validate(JsonNode jsonNode) throws IOException;

    /**
     * Validates JSON against a given specification
     * 
     * @param file
     *            the JSON file
     * @return a validation result
     * @throws IOException
     *             an exception occurred while parsing or validating JSON
     */
    public ValidationResult validate(File file) throws IOException;

    /**
     * Validates JSON against a given specification
     * 
     * @param reader
     *            the JSON reader
     * @return a validation result
     * @throws IOException
     *             an exception occurred while parsing or validating JSON
     */
    public ValidationResult validate(Reader reader) throws IOException;
	
}
