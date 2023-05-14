package com.example.opcv.business.maps;

import com.example.opcv.business.interfaces.firebase.map.GetGardensAddresses;
import com.example.opcv.business.interfaces.firebase.map.GetGardensAddressesInt;
import com.example.opcv.model.entity.Address;
import com.example.opcv.model.persistance.firebase.GardenCommunication;

import java.util.Map;

public class GardenMaps {

    GardenCommunication gardenCommunication = new GardenCommunication();

    public void getAddresses(final GetGardensAddressesInt callback){
        gardenCommunication.gardensAddresses(new GetGardensAddresses() {
            @Override
            public void onComplete(Map<Integer, Address> addresses) {
                callback.onComplete(addresses);
            }
        });
    }
}
