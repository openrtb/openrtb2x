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

/***********************************************
 * This class provides validation result data. *
 **********************************************/

public class ValidationResult {

    private final boolean valid;
    
    private final String result;

    /**
     * Constructs an object containing JSON validation result data.
     * 
     * @param valid
     *            validity status from validation
     * @param result
     *            the result data from validation
     */
    ValidationResult(boolean valid, String result) {
        this.valid = valid;
        this.result = result;
    }

    /**
     * Returns the validity status from validation.
     * 
     * @return the validity status from validation
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the result data from validation.
     * 
     * @return the result data from validation
     */
    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return (new StringBuilder())
                .append("valid=").append(valid)
                .append("result=").append(result)
                .toString();
    }
    
}
