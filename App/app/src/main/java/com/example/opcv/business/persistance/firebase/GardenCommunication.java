package com.example.opcv.business.persistance.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.opcv.business.persistance.garden.GardenPersistance;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GardenCommunication {

    public interface OnGardenUserFilledListener {
        void onGardenUserFilled(List<ItemGardenHomeList> gardenNames);
    }

    private void fillGardenUser(String ID, OnGardenUserFilledListener listener) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("Gardens");
        String userID = ID;
        Query query = Ref.whereEqualTo("ID_Owner", userID);
        query.whereEqualTo("ID_Owner", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                for(DocumentSnapshot documentSnapshot : value){
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.getString("GardenName");
                        String gardenId = documentSnapshot.getId();
                        GardenPersistance persistance = new GardenPersistance();
                        /*persistance.getGardenPicture(gardenId, this, new GardenPersistance.GetUri() {
                            @Override
                            public void onSuccess(String uri) {
                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                gardenNames.add(newItem);
                            }
                        });*/


                    } else {

                    }
                }
                listener.onGardenUserFilled(gardenNames);
            }
        });
    }
}
