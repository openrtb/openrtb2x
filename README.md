# Implementation Purpose
This project is focused on supplying a reference implementation,
both for supply-side and demand-side platforms, of the offline
synchronization services specified in the **OpenRTB Specification**.

The goal and design of this work is to minimize the effort required
by any DSP or SSP wishing to leverage the capabilities described
in the spec.

To aid in this effort, the work has been licensed under the New BSD
License.  Please refer to the LICENSE file in the root of this
project for more information, or to the following website for more
information: http://www.opensource.org/licenses/bsd-license.php

## Project Structure
You should be able to build the project by running 'mvn clean
install' from the top-level directory.  The project is structured
in the following manner:

## [common](https://github.com/openrtb/openrtb2x/tree/2.0/common)
This directory is a maven project that produces a single jar file.
This jar file contains all code that is shared between the demand-side
and supply-side implementations (i.e. authentication/validation
work, object models, etc.)

## [demand-side](https://github.com/openrtb/openrtb2x/tree/2.0/demand-side)
This directory contains a series of maven projects responsible for
the DSP behavior of this specification.  The sub-project artifact
responsibilities include:

### [dsp-web](https://github.com/openrtb/openrtb2x/tree/2.0/demand-side/dsp-web)
This project packages up a web archive (war) file, capable of being
deployed in any standard JEE web container based upon the reference
'dsp-client' artifact provided with this implementation.

The contents of this package are purposefully minimized to the 
deployment artifacts necessary to deploy the war file.

### [dsp-core](https://github.com/openrtb/openrtb2x/tree/2.0/demand-side/dsp-core)
This project produces a jar file containing the necessary logic for
sending requests and receiving responses, including all message
handling that is pertinent to the DSP.

This jar file represents standard logic that would be implemented
by any DSP wishing to leverage this implementation.

All DSP specific logic (i.e. persisting the response data, retrieving 
advertiser information, etc.) is delegated to the 'dsp-client' via the 
'dsp-intf'.

### [dsp-intf](https://github.com/openrtb/openrtb2x/tree/2.0/demand-side/dsp-intf)
This package is the contract and set of interfaces between the message 
handling code located in the 'dsp-core' and a DSP's specific internal 
representation of that data.

### [dsp-client](https://github.com/openrtb/openrtb2x/tree/2.0/demand-side/dsp-client)
This package is an example implementation of the 'dsp-intf' to satisfy 
the following:
1) Integration testing between the 'dsp-web' and 'ssp-web' projects.
2) Provide a framework for other DSPs to leverage this specification more 
   quickly by allowing the DSP to focus on the integration aspects of the 
   specification with their own specific platform.

As a result of requirement (1) above, this package must be capable of 
working in concert with the 'ssp-client' described below.

## [supply-side](https://github.com/openrtb/openrtb2x/tree/2.0/supply-side)
Similar to the `demand-side` module, this directory contains a series of 
maven projects responsible for the SSP behavior described in the 
specification.  The sub-module artifact responsibilities include:

### [ssp-web](https://github.com/openrtb/openrtb2x/tree/2.0/supply-side/ssp-web)
This project packages up a web archive (war) file capable of being
deployed in any standard JEE web container.  It is based upon the
reference 'ssp-client' artifact provided with this implementation.

The contents of this package are purposefully minimized to the
deployment configurations necessary to deploy the war file.

### [ssp-core](https://github.com/openrtb/openrtb2x/tree/2.0/supply-side/ssp-core)
This project produces a jar file containing the necessary logic for
sending and receiving both requests and responses, including all
message handling that is pertinent to the SSP.

This jar file represents standard logic that would be implemented
by any SSP wishing to leverage this implementation..

All SSP specific logic (i.e. persisting the advertiser data,
retrieving publisher information, etc.) is delegated to the
'ssp-client' via the 'ssp-intf'.

### [ssp-intf](https://github.com/openrtb/openrtb2x/tree/2.0/supply-side/ssp-intf)
This package is the contract and set of interfaces between the
message handling code located in the 'ssp-core' and an SSP's specific
internal representation of that data.

### [ssp-client](https://github.com/openrtb/openrtb2x/tree/2.0/supply-side/ssp-client)
This package is an example implementation of the 'ssp-intf' to satisfy 
the following:
1. Integration testing between the 'ssp-web' and 'dsp-web' projects.
2. Provide a framework for other SSPs to leverage this specification more 
quickly by allowing the SSP to focus on the integration aspects of the 
specification with their own specific platform.

As a result of requirement (1) above, this package must be capable
of working in concert with the 'dsp-client' described above.

### [openrtb-validator](https://github.com/openrtb/openrtb2x/tree/2.0/openrtb-validator)
This project provides a simple API that can be used to validate
JSON bid requests and responses according to OpenRTB specifications.
OpenRTB versions 1.0, 2.0, 2.1, 2.2, 2.3, 2.4 and 2.5 are fully supported.

### [native-validator](https://github.com/openrtb/openrtb2x/tree/2.0/native-validator)
This project provides a simple API that can be used to validate
Native JSON bid requests and responses according to specifications.
Native version 1.0 is fully supported.
