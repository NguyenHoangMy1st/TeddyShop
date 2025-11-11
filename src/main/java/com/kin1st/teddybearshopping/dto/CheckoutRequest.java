package com.kin1st.teddybearshopping.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutRequest {
    private List<Long> selectedItemIds;
    private String customerName;
    private String customerEmail;
    private String customerAddress;
}
