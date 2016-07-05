Feature: Validation of Bid Response payload for OpenRtb version 1.0, 2.0, 2.1, 2.2, 2.3 and 2.4

  Scenario Outline: to validate input bid response using openRtb Validator
    Given a bid response json input <bidResponse> to be validated
    When an openRtb validator version <version> runs validation on given bid response
    Then a valid result is returned by the validator

    Examples: 
      | bidResponse                                                             | version |
      | "/v2_4/bid_responses/example1_ad_served_on_win_notice.json"             |     2.4 |
      | "/v2_4/bid_responses/example2_vast_xml_document_returned_inline.json"   |     2.4 |
      | "/v2_4/bid_responses/example3_direct_deal_ad_served_on_win_notice.json" |     2.4 |
      | "/v2_4/bid_responses/example4_native_markup_returned_inline.json"       |     2.4 |

    Examples: 
      | bidResponse                                                             | version |
      | "/v2_3/bid_responses/example1_ad_served_on_win_notice.json"             |     2.3 |
      | "/v2_3/bid_responses/example2_vast_url_returned.json"                   |     2.3 |
      | "/v2_3/bid_responses/example3_direct_deal_ad_served_on_win_notice.json" |     2.3 |
      | "/v2_3/bid_responses/example4_native_markup_returned_inline.json"       |     2.3 |

    Examples: 
      | bidResponse                                                                 | version |
      | "/v2_2/bid_responses/example1_ad_served_on_win_notice.json"                 |     2.2 |
      | "/v2_2/bid_responses/example2_vast_url_returned.json"                       |     2.2 |
      | "/v2_2/bid_responses/fixed/example3_vast_xml_document_returned_inline.json" |     2.2 |
      | "/v2_2/bid_responses/example4_direct_deal_ad_served_on_win_notice.json"     |     2.2 |

    Examples: 
      | bidResponse                                                                 | version |
      | "/v2_1/bid_responses/example1_ad_served_on_win_notice.json"                 |     2.1 |
      | "/v2_1/bid_responses/example2_vast_url_returned.json"                       |     2.1 |
      | "/v2_1/bid_responses/fixed/example3_vast_xml_document_returned_inline.json" |     2.1 |

    Examples: 
      | bidResponse                                                                 | version |
      | "/v2_0/bid_responses/example1_ad_served_on_win_notice.json"                 |     2.0 |
      | "/v2_0/bid_responses/example2_vast_url_returned.json"                       |     2.0 |
      | "/v2_0/bid_responses/fixed/example3_vast_xml_document_returned_inline.json" |     2.0 |

    Examples: 
      | bidResponse                                  | version |
      | "/v1_0/bid_responses/full_bid_response.json" |     1.0 |
