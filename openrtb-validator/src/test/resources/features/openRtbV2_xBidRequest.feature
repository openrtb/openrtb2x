Feature: Validation of Bid Request payload for OpenRtb version 1.0, 2.0, 2.1, 2.2, 2.3 and 2.4

  Scenario Outline: to validate input bid request using openRtb Validator
    Given a bid request json input <bidRequest> to be validated
    When an openRtb validator version <version> runs validation on given bid request
    Then a valid result is returned by the validator

    Examples: 
      | bidRequest                                              | version |
      | "/v2_4/bid_requests/example1_simple_banner.json"        |     2.4 |
      | "/v2_4/bid_requests/example2_expandable_creative.json"  |     2.4 |
      | "/v2_4/bid_requests/example3_mobile.json"               |     2.4 |
      | "/v2_4/bid_requests/example4_video.json"                |     2.4 |
      | "/v2_4/bid_requests/example5_pmp_with_direct_deal.json" |     2.4 |
      | "/v2_4/bid_requests/example6_native_ad.json"            |     2.4 |

    Examples: 
      | bidRequest                                              | version |
      | "/v2_3/bid_requests/example1_simple_banner.json"        |     2.3 |
      | "/v2_3/bid_requests/example2_expandable_creative.json"  |     2.3 |
      | "/v2_3/bid_requests/example3_mobile.json"               |     2.3 |
      | "/v2_3/bid_requests/example4_video.json"                |     2.3 |
      | "/v2_3/bid_requests/example5_pmp_with_direct_deal.json" |     2.3 |
      | "/v2_3/bid_requests/example6_native_ad.json"            |     2.3 |

    Examples: 
      | bidRequest                                                    | version |
      | "/v2_2/bid_requests/fixed/example1_simple_banner.json"        |     2.2 |
      | "/v2_2/bid_requests/fixed/example2_expandable_creative.json"  |     2.2 |
      | "/v2_2/bid_requests/fixed/example3_mobile.json"               |     2.2 |
      | "/v2_2/bid_requests/fixed/example4_video.json"                |     2.2 |
      | "/v2_2/bid_requests/fixed/example5_pmp_with_direct_deal.json" |     2.2 |

    Examples: 
      | bidRequest                                                   | version |
      | "/v2_1/bid_requests/fixed/example1_simple_banner.json"       |     2.1 |
      | "/v2_1/bid_requests/fixed/example2_expandable_creative.json" |     2.1 |
      | "/v2_1/bid_requests/fixed/example3_mobile.json"              |     2.1 |
      | "/v2_1/bid_requests/fixed/example4_video.json"               |     2.1 |

    Examples: 
      | bidRequest                                                   | version |
      | "/v2_0/bid_requests/fixed/example2_expandable_creative.json" |     2.0 |
      | "/v2_0/bid_requests/fixed/example3_mobile.json"              |     2.0 |
      | "/v2_0/bid_requests/fixed/example4_video.json"               |     2.0 |

    Examples: 
      | bidRequest                                      | version |
      | "/v1_0/bid_requests/full_bid_request_app.json"  |     1.0 |
      | "/v1_0/bid_requests/full_bid_request_site.json" |     1.0 |
