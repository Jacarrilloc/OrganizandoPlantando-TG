package com.example.opcv.business.interfaces.firebase.map;

import com.example.opcv.model.entity.Address;

import java.util.Map;

public interface GetGardensAddressesInt {
    void onComplete(Map<Integer, Address> addresses);
}
