package com.example.opcv.business.maps;

import com.example.opcv.business.interfaces.firebase.map.GetGardensAddresses;
import com.example.opcv.business.interfaces.firebase.map.GetGardensAddressesInt;
import com.example.opcv.model.entity.Address;
import com.example.opcv.model.persistance.garden.GardenPersistance;

import java.util.HashMap;
import java.util.Map;

public class GardenMaps {

    GardenPersistance gardenPersistance = new GardenPersistance();

    public void getAddresses(final GetGardensAddressesInt callback){
        gardenPersistance.gardensAddresses(new GetGardensAddresses() {
            @Override
            public void onComplete(Map<Integer, Address> addresses) {
                callback.onComplete(addresses);
            }
        });
    }
}
