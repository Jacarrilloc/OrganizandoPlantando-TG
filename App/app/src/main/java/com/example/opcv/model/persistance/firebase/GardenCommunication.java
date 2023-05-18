package com.example.opcv.model.persistance.firebase;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.opcv.R;
import com.example.opcv.business.interfaces.OnCollaboratorIdsObtained;
import com.example.opcv.business.interfaces.firebase.map.GetGardensAddresses;
import com.example.opcv.model.entity.Address;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.model.items.ItemCollaborator;
import com.example.opcv.model.items.ItemCollaboratorsRequest;
import com.example.opcv.model.items.ItemGardenHomeList;
import com.example.opcv.view.base.HomeActivity;
import com.example.opcv.view.gardens.CollaboratorGardensActivity;
import com.example.opcv.view.gardens.CreateGardenActivity;
import com.example.opcv.view.gardens.GardenActivity;
import com.example.opcv.view.gardens.GardenAddressActivity;
import com.example.opcv.view.gardens.GardenEditActivity;
import com.example.opcv.view.gardens.GardenMembersActivity;
import com.example.opcv.view.gardens.GardenRequestsActivity;
import com.example.opcv.view.gardens.GardensAvailableActivity;
import com.example.opcv.view.gardens.GenerateReportsActivity;
import com.example.opcv.view.gardens.OtherGardensActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GardenCommunication {
    int countGardens = 0;
    private static final String TAG = "GardenFiller";

    public void deletePhotoGarden(String gardenId){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(gardenId);
        Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String uri = task.getResult().getString("UriPath");
                    String path = gardenId+".jpg";
                    //if(uri != null){
                        StorageReference storage = FirebaseStorage.getInstance().getReference("gardenMainPhoto/"+path);
                        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("borro");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("no borro");
                            }
                        });
                   // }
                }
            }
        });
    }


    public void addGardenPhoto(byte[] bytes, String gardenID, final GetUriGarden callback) {
        try {
            if (bytes != null) {
                StorageReference storage = FirebaseStorage.getInstance().getReference();
                String imageName = gardenID + ".jpg";
                StorageReference ref = storage.child("gardenMainPhoto/" + imageName);
                UploadTask uploadTask = ref.putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                callback.onSuccess(url);
                            }
                        });
                    }
                });
            }
        }catch (Exception e){
            Log.i("Error: ", e.getMessage());
        }
    }

    public void getGardenPicture(String id, Context context, final GetUri callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(id);
        Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String uri = documentSnapshot.getString("UriPath");
                    if(uri != null){
                        callback.onSuccess(uri);
                    }
                    else{
                        String imageString = "android.resource://" + context.getPackageName() + "/drawable/im_logo_ceres_green";
                        callback.onFailure(imageString);
                    }
                }
            }
        });
    }

    public void addGardenAddress(String id, String address, GeoPoint p){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference ref = database.collection("Gardens").document(id);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   Map<String, Object> garden = new HashMap<>();
                   garden.put("latitude", p.getLatitude());
                   garden.put("longitude", p.getLongitude());
                   garden.put("gardenAddress", address);
                   ref.update(garden);
               }
            }
        });
    }

    public void gardensAddresses(final GetGardensAddresses callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference ref = database.collection("Gardens");

        Query query = ref.whereEqualTo("GardenType", "Public");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    GeoPoint point;
                    String gardenName;
                    Map<Integer, Address> addresses = new HashMap<>();
                    int i = 0;
                    for(QueryDocumentSnapshot docs: task.getResult()) {
                        if (docs.getDouble("latitude") != null && docs.getDouble("longitude") != null) {
                            point = new GeoPoint(docs.getDouble("latitude"), docs.getDouble("longitude"));
                            gardenName = docs.getString("GardenName");
                            Address address = new Address(gardenName, point);
                            addresses.put(i, address);
                            i++;
                        }
                    }
                    callback.onComplete(addresses);
                }else{
                    System.out.println("Error");
                }
            }
        });
    }



    public interface GetUriGarden{
        void onSuccess(String uri);
    }
    public interface GetUri{
        void onSuccess(String uri);

        void onFailure(String imageString);
    }

    public void retrieveCrops(final GetNumber callback){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("Gardens");

        Ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot q : task.getResult()){
                        if(q!=null){
                            Ref.document(q.getId()).collection("Forms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                    if(task1.isSuccessful()){
                                        for(QueryDocumentSnapshot q : task1.getResult()){
                                            String name = q.getString("nameForm");
                                            if(name.equals("Control de Procesos de Siembra")){
                                                countGardens++;
                                                callback.onSuccess(countGardens);
                                                break;
                                            }
                                        }

                                    }
                                }
                            });
                        }
                    }

                }
            }
        });

    }
    public interface GetNumber{
        void onSuccess(int count);
    }

    public void deleteGardensCollections(String idGarden){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(idGarden);


        Ref.collection("Collaborators").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("Collaborators", "Se elimino la coleccion de collaborators");
                }
            }
        });

        Ref.collection("Requests").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("Requests", "Se elimino la coleccion de Requests");
                }
            }
        });

        Ref.collection("Forms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        doc.getReference().delete();
                    }
                    Log.i("Forms", "Se elimino la coleccion de Forms");
                }
            }
        });
    }

    public void fillGardenUser(String userId, HomeActivity activity) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("Gardens");
        Query query = Ref.whereEqualTo("ID_Owner", userId);
        query.whereEqualTo("ID_Owner", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.d(TAG, "Se genero error: ", e);
                        return;
                    }
                    for (DocumentSnapshot documentSnapshot : value) {
                        if (documentSnapshot.exists()) {
                            List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                            for (QueryDocumentSnapshot document : value) {
                                String name = document.getString("GardenName");
                                String gardenId = document.getId();
                                GardenCommunication persistance = new GardenCommunication();
                                if (activity.isOnline()) {
                                    try {
                                        persistance.getGardenPicture(gardenId, activity, new GardenCommunication.GetUri() {
                                            @Override
                                            public void onSuccess(String uri) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                                gardenNames.add(newItem);
                                                activity.fillListGardens(gardenNames);
                                            }

                                            @Override
                                            public void onFailure(String imageString) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, imageString);
                                                gardenNames.add(newItem);
                                                activity.fillListGardens(gardenNames);
                                            }
                                        });
                                    } catch (Exception a) {
                                        Log.i("HOME-ERROR", "Error: " + a.getMessage().toString());
                                    }
                                } else {
                                    ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, null);
                                    gardenNames.add(newItem);
                                    activity.fillListGardens(gardenNames);
                                }
                            }
                            //fillListGardens(gardenNames);
                        } else {
                            Toast.makeText(activity, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                        }
                    }

            }
        });
    }

    public void getImageGarden(String idGarden, ImageView imageView) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String imageName = idGarden + ".jpg";
        StorageReference imageRef = storageRef.child("gardenMainPhoto/" + imageName);
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setImageResource(R.drawable.im_logo_ceres_green);
            }
        });
    }

    public void searchInfoGardenScreen(String gardenID, String idUser, String name, OtherGardensActivity activity) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference ref = firestore.collection("Gardens").document(gardenID);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String infoDoc = null;
                if (documentSnapshot.exists()) {
                    String typeDoc = documentSnapshot.getString("GardenType");
                    infoDoc = documentSnapshot.getString("InfoGarden");
                    String gardenAddress = documentSnapshot.getString("gardenAddress");
                    GardenInfo gardenInfo = new GardenInfo(idUser, name, infoDoc, typeDoc, gardenAddress);
                    activity.fillSreen(gardenInfo);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Se genero error: ", e);
            }
        });
    }

    //este metodo sera para mostrar la infor de huertas publicas
    public void fillGardenAvailable(Context context, GardensAvailableActivity activity) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference ref = database.collection("Gardens");
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        FirebaseUser user = authentication.getCurrentUser();
        String currentUserId;
        if (user != null && !user.isAnonymous()) {
            currentUserId = authentication.getCurrentUser().getUid();
        } else {
            currentUserId = "a";
        }

        Query query = ref.whereEqualTo("GardenType", "Public").whereNotEqualTo("ID_Owner", currentUserId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Se generó un error: ", e);
                    return;
                }

                List<ItemGardenHomeList> gardenNames = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    String name = document.getString("GardenName");
                    String gardenId = document.getId();
                    // Check if user is not a collaborator
                    document.getReference().collection("Collaborators")
                            .whereEqualTo("idCollaborator", currentUserId)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) { // User is not a collaborator
                                        GardenCommunication persistence = new GardenCommunication();
                                        persistence.getGardenPicture(gardenId, context, new GardenCommunication.GetUri() {
                                            @Override
                                            public void onSuccess(String uri) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, uri);
                                                gardenNames.add(newItem);
                                                activity.fillListGardens(gardenNames);
                                            }

                                            @Override
                                            public void onFailure(String imageString) {
                                                ItemGardenHomeList newItem = new ItemGardenHomeList(name, gardenId, imageString);
                                                gardenNames.add(newItem);
                                                activity.fillListGardens(gardenNames);
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "Error al verificar colaboradores: ", task.getException());
                                }
                            });
                }
            }
        });
    }

    //el siguiente metodo sera para mostrar la lista de requests
    public void fillGardenRequests(String gardenId, Context context, GardenRequestsActivity activity) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference ref = database.collection("Gardens").document(gardenId).collection("Requests");
        Query query = ref.whereEqualTo("requestStatus", "SV");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Se generó un error: ", e);
                    return;
                }
                for (DocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.exists()) {
                        List<ItemCollaboratorsRequest> gardenNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            String idUser = document.getString("idUserRequest");
                            database.collection("UserInfo")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                String idSearch;
                                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                                    idSearch = document.getString("ID");
                                                    if (idSearch == null) {
                                                        idSearch = document.getString("id");
                                                    }
                                                    if (idSearch.equals(idUser)) {
                                                        String name = document.getString("Name");
                                                        // Si se necesita más información, usar la clase User
                                                        UserCommunication userCommunication = new UserCommunication();
                                                        userCommunication.getProfilePicture(idUser, new UserCommunication.GetUriUser() {
                                                            @Override
                                                            public void onComplete(String uri) {
                                                                ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(name, idUser, gardenId, uri);
                                                                gardenNames.add(newItem);
                                                                activity.fillListRequests(gardenNames);
                                                            }
                                                        });
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(context, "Error al obtener los documentos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //el siguiente metodo sera para el ver los miembros de la huerta
    public void fillGardenMembers(String idGarden, Context context, GardenMembersActivity activity) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference collaboratorsRef = database.collection("Gardens").document(idGarden).collection("Collaborators");

        getCollaboratorIds(collaboratorsRef, new OnCollaboratorIdsObtained() {
            @Override
            public void onCollaboratorIdsObtained(List<String> collaboratorIds) {
                try {
                    Query query = database.collection("UserInfo").whereIn("ID", collaboratorIds);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    String idSearch;

                                    List<ItemCollaborator> membersNames = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        idSearch = document.getString("ID");
                                        if (idSearch == null) {
                                            idSearch = document.getString("id");
                                        }
                                        if (idSearch != null) {
                                            String name = document.getString("Name");
                                            // Si se necesita más información, usar la clase User
                                            ItemCollaborator newItem = new ItemCollaborator(name, idSearch, idGarden);
                                            membersNames.add(newItem);
                                        }
                                        Map<String, Object> userInfo = document.getData(); // obtener los datos del usuario actual
                                    }
                                    activity.fillListMembers(membersNames);
                                }
                            } else {
                                Log.d(TAG, "Error al obtener los documentos de información del usuario.", task.getException());
                            }
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(context, "Esta huerta no tiene colaboradores", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private List<String> getCollaboratorIds(CollectionReference collaboratorsRef, OnCollaboratorIdsObtained callback) {
        List<String> collaboratorIds = new ArrayList<>();
        collaboratorsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collaboratorIds.add(document.getString("idCollaborator"));
                    }

                    callback.onCollaboratorIdsObtained(collaboratorIds);
                } else {
                    Log.d(TAG, "Error getting collaborator documents.", task.getException());
                }
            }
        });
        return collaboratorIds;
    }

    //los siguientes metodos son para el EditGardenActivity
    public void searchGarden(String idUser, String name, String info, Boolean gardenType, String idGarden, Context context) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Gardens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String userId;
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                userId = document.getData().get("ID_Owner").toString();
                                if(userId.equals(idUser) ){
                                    //String idCollection = document.getId().toString();
                                    final DocumentReference docRef = database.collection("Gardens").document(idGarden);
                                    database.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                            DocumentSnapshot snapshot = transaction.get(docRef);
                                            if(!gardenType){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Public");
                                            }
                                            if(gardenType){
                                                transaction.update(docRef, "GardenName", name, "InfoGarden", info, "GardenType", "Private");
                                            }

                                            return null;
                                        }
                                    });
                                }

                            }
                        }
                        else{
                            Toast.makeText(context, "No tienes permiso para actualizar la huerta", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getImageGardenEdit(String idGarden, ImageView gardenImage, Context context){
        //se supone que con esto no deberia dar StorageException, pero si :(
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference Ref = database.collection("Gardens").document(idGarden);
        Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String uri = task.getResult().getString("UriPath");
                    if(uri != null){
                        Glide.with(context).load(uri).into(gardenImage);
                    }
                    else{
                        gardenImage.setImageResource(R.drawable.im_logo_ceres_green);
                    }

                }
            }
        });
    }

    public void changeGardenPhoto(String idGarden, ImageView gardenImage, Context context){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("gardenMainPhoto/" + idGarden + ".jpg");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final DocumentReference Ref = database.collection("Gardens").document(idGarden);

        gardenImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = gardenImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream stream = new ByteArrayInputStream(baos.toByteArray());
        UploadTask uploadTask = storageRef.putStream(stream);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //taskSnapshot.getUploadSessionUri()
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Ref.update("UriPath", uri);
                        Toast.makeText(context, "Se Cambio la Imagen de la Huerta Exitosamente", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // La carga de la foto ha fallado, manejar el error aquí
            }
        });
    }

    public void deleteGarden(String idGarden, Context context) {
        FirebaseFirestore database2 = FirebaseFirestore.getInstance();
        GardenCommunication persistance = new GardenCommunication();
        persistance.deletePhotoGarden(idGarden);
        persistance.deleteGardensCollections(idGarden);
        database2 = FirebaseFirestore.getInstance();
        FirebaseFirestore finalDatabase = database2;
        database2.collection("Gardens").document(idGarden).collection("Requests")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                finalDatabase.collection("Gardens")
                                        .document(idGarden)
                                        .collection("Requests")
                                        .document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
        FirebaseFirestore finalDatabase1 = database2;
        database2.collection("Gardens")
                .document(idGarden)
                .collection("Forms")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                finalDatabase1.collection("Gardens")
                                        .document(idGarden)
                                        .collection("Forms")
                                        .document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
        database2.collection("Gardens")
                .document(idGarden)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Se borro exitosamente tu huerta", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error al borrar la huerta", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void fillInfoGarden(String idGarden, EditText gardenName, EditText description, Switch switchGardenTypeModified){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference ref = database.collection("Gardens");

        ref.document(idGarden).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String nameGarden, infoGarden;
                    nameGarden = documentSnapshot.getString("GardenName");
                    gardenName.setText(nameGarden);
                    infoGarden = documentSnapshot.getString("InfoGarden");
                    description.setText(infoGarden);
                    String gardenInfoType = documentSnapshot.getString("GardenType");
                    if(gardenInfoType.equals("Public")){
                        switchGardenTypeModified.setChecked(false);
                    } else if (gardenInfoType.equals("Private")) {
                        switchGardenTypeModified.setChecked(true);
                    }
                }
            }
        });
    }

    //los siguientes metodos son para el GardenActivity
    public void SearchInfoGardenScreen(String idUser, String name, String gardenID, GardenActivity activity){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference gardensRef = database.collection("Gardens");
        DocumentReference ref = gardensRef.document(gardenID);

        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String infoDoc=null;
                if (documentSnapshot.exists()) {

                    String typeDoc = documentSnapshot.getString("GardenType");
                    infoDoc = documentSnapshot.getString("InfoGarden");
                    String gardenAddress = documentSnapshot.getString("gardenAddress");
                    GardenInfo gardenInfo = new GardenInfo(idUser,name,infoDoc,typeDoc, gardenAddress);
                    //para conocer el numero de participantes de la huerta
                    ref.collection("Collaborators").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    int participants = task.getResult().size();
                                    activity.fillScreen(gardenInfo, participants);
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Se genero error: ", e);
            }
        });
    }

    public void insertGroupLink(String link, String idGarden, Context context){
        Map<String, Object> gardenLink = new HashMap<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        gardenLink.put("Garden_Chat_Link",link);
        DocumentReference documentRef = database.collection("Gardens").document(idGarden);

        documentRef.update(gardenLink);
        Toast.makeText(context, "Se agregó el link exitosamente", Toast.LENGTH_SHORT).show();
    }

    public void goToLink(String idGarden, Context context){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Gardens").document(idGarden).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String link = (String) task.getResult().get("Garden_Chat_Link");
                            if(link != null){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                context.startActivity(intent);
                            }
                            else{
                                Toast.makeText(context, "Esta huerta no tiene chat grupal", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    public void gardenReportGeneration(Context context, String idGardenFirebase, String id){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Intent requests = new Intent(context, GenerateReportsActivity.class);
        requests.putExtra("idGardenFirebaseDoc",idGardenFirebase);
        requests.putExtra("idUser",id);
        requests.putExtra("garden","true");// con esto se define si, al ejecutar GenerateReportsActivity es solo para la huerta o para todos
        CollectionReference collectionRef2 = database.collection("UserInfo");
        Query query = collectionRef2.whereEqualTo("ID", id);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        String name = document.getData().get("Name").toString();
                        String lastName =document.getData().get("LastName").toString();
                        requests.putExtra("ownerName",name+" "+lastName);
                        context.startActivity(requests);
                    }
                }
            }
        });
    }

    //CollaboratorGardens
    public void fillGardenUser(String userId, Context context, CollaboratorGardensActivity activity){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference Ref = database.collection("UserInfo");

        Query query = Ref.whereEqualTo("ID", userId);
        if(query.equals(null)){
            query = Ref.whereEqualTo("id", userId);
        }
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Se genero error: ", e);
                    return;
                }
                for(DocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.exists()) {
                        for (QueryDocumentSnapshot document : value) {

                            database.collection("UserInfo").document(document.getId()).collection("GardensCollaboration").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {
                                                List<ItemCollaboratorsRequest> gardenNames = new ArrayList<>();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    String idUser = document.getId().toString();

                                                    database.collection("Gardens").document(document.getData().get("idGardenCollab").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            String idOwner, name, info, gardenType, idGarden;
                                                            if(task.isSuccessful()){
                                                                for(QueryDocumentSnapshot documen:value){
                                                                    GardenInfo idSearch;
                                                                    String nameUser = task.getResult().get("GardenName").toString();
                                                                    String idGarde = document.getData().get("idGardenCollab").toString();
                                                                    GardenCommunication persistance = new GardenCommunication();
                                                                    persistance.getGardenPicture(idGarde, context, new GardenCommunication.GetUri() {
                                                                        @Override
                                                                        public void onSuccess(String uri) {
                                                                            ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(nameUser, userId, idGarde, uri);
                                                                            gardenNames.add(newItem);
                                                                            activity.fillListGardens(gardenNames);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(String imageString) {
                                                                            ItemCollaboratorsRequest newItem = new ItemCollaboratorsRequest(nameUser, userId, idGarde, imageString);
                                                                            gardenNames.add(newItem);
                                                                            activity.fillListGardens(gardenNames);
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }
    //metodos para crear huertas
    public void createGarden(EditText nameGarden, EditText infoGarden, Switch gardenType, CreateGardenActivity activity, Context context, ImageView photo){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth autentication = FirebaseAuth.getInstance();
        String name = nameGarden.getText().toString();
        String info = infoGarden.getText().toString();
        GardenInfo newInfo = null;
        Boolean gardenPrivateOrPublic = gardenType.isChecked();
        if(activity.validateField(name,info)){
            FirebaseUser user = autentication.getCurrentUser();
            CollectionReference collectionRef = database.collection("Gardens");

            if(!gardenPrivateOrPublic){
                newInfo = new GardenInfo(user.getUid(),nameGarden.getText().toString(),infoGarden.getText().toString(),"Public", null);
            }
            if(gardenPrivateOrPublic){
                newInfo = new GardenInfo(user.getUid(),nameGarden.getText().toString(),infoGarden.getText().toString(),"Private", null);
            }


            Map<String, Object> gardenInfo = new HashMap<>();
            gardenInfo.put("ID_Owner",newInfo.getID_Owner());
            gardenInfo.put("GardenName",newInfo.getName());
            gardenInfo.put("InfoGarden",newInfo.getInfo());
            gardenInfo.put("GardenType", newInfo.getGardenType());
            gardenInfo.put("UriPath", null);

            collectionRef.add(gardenInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    String idGarden = documentReference.getId();
                    GardenCommunication persistance = new GardenCommunication();
                    Drawable drawable = photo.getDrawable();
                    byte[] bytes;
                    if(drawable != null){
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        bytes = stream.toByteArray();
                        context.startActivity(new Intent(context, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                    }else{
                        bytes = null;
                        context.startActivity(new Intent(context, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                    }
                    persistance.addGardenPhoto(bytes, idGarden, new GardenCommunication.GetUriGarden() {
                        @Override
                        public void onSuccess(String uri) {
                            //descomentar la siguiente linea si se necesita poner la uri en firestore
                            collectionRef.document(idGarden).update("UriPath", uri);
                            context.startActivity(new Intent(context, GardenAddressActivity.class).putExtra("idGarden", documentReference.getId().toString()));
                        }
                    });
                }
            });
        }
    }
}