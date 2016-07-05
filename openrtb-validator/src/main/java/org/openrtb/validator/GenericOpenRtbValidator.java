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
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * This class provides basic JSON validation against a schema.
 */
public class GenericOpenRtbValidator implements OpenRtbValidator {

	private final JsonSchema schema;
	
    /**
     * Constructs a JSON validator using the given schema read as a resource.
     * 
     * @param schemaResource
     *            the schema resource
     */
    GenericOpenRtbValidator(String schemaResource) {
		try {
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonNode jsonNode = JsonLoader.fromResource(schemaResource);
			schema = factory.getJsonSchema(jsonNode);
		} catch (IOException | ProcessingException e) {
			throw new IllegalStateException("could not initialize validator due to previous exception", e);
		}
	}
	
	@Override
	public final boolean isValid(String json) {
	    try {
            return validate(json).isValid();
        } catch (IOException e) {
            return false;
        }
	}
	
	@Override
	public final boolean isValid(JsonNode jsonNode) {
        try {
            return validate(jsonNode).isValid();
        } catch (IOException e) {
            return false;
        }
	}
	
	@Override
	public final boolean isValid(File file) {
        try {
            return validate(file).isValid();
        } catch (IOException e) {
            return false;
        }
	}
	
	@Override
	public final boolean isValid(Reader reader) {
        try {
            return validate(reader).isValid();
        } catch (IOException e) {
            return false;
        }
	}

	@Override
	public final ValidationResult validate(String json) throws IOException {
		JsonNode jsonNode = JsonLoader.fromString(json);
		return getValidationResult(jsonNode);
	}
	
	@Override
	public final ValidationResult validate(JsonNode jsonNode) throws IOException {
        return getValidationResult(jsonNode);
	}

	@Override
	public final ValidationResult validate(File file) throws IOException {
		JsonNode jsonNode = JsonLoader.fromFile(file);
        return getValidationResult(jsonNode);
	}
	
	@Override
	public final ValidationResult validate(Reader reader) throws IOException {
		JsonNode jsonNode = JsonLoader.fromReader(reader);
        return getValidationResult(jsonNode);
	}
	
	private final ValidationResult getValidationResult(JsonNode jsonNode) throws IOException {
	    try {
            ProcessingReport processingReport = schema.validate(jsonNode);
            if (processingReport != null) {
                return new ValidationResult(processingReport.isSuccess(), processingReport.toString());
            } else {
                return new ValidationResult(false, null);
            }
        } catch (ProcessingException e) {
            throw new IOException(e.getMessage());
        }
	}

}
