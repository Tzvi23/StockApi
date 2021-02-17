package com.example.stock.stock.service;

import com.example.stock.stock.model.ItemDB;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ItemDBService {
    public static final String COL_NAME = "items";

    //Local function to get all the inventory codes
    //This functions used to make sure that the inventory codes are unique
    private List<String> getInventoryCodes () throws ExecutionException, InterruptedException {
        List<String> inventoryCodes = new ArrayList<>();
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
        for(DocumentReference counterItem : documentReference){
            ApiFuture<DocumentSnapshot> future = counterItem.get();
            DocumentSnapshot document = future.get();
            ItemDB currentItem = document.toObject(ItemDB.class);
            if(currentItem != null) inventoryCodes.add(currentItem.getInventoryCode());
        }
        return inventoryCodes;
    }

    public String addItemDB(ItemDB item) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
        //Check if inventory code unique
        if(getInventoryCodes().contains(item.getInventoryCode())) return "Inventory code not unique!";
        //Using the list documents iterator takes the last item, and uses its item no
        //to decide what the item counter should be
        long itemCounter = 1;
        ItemDB lastItem = null;
        for(DocumentReference counterItem : documentReference){
            ApiFuture<DocumentSnapshot> future = counterItem.get();
            DocumentSnapshot document = future.get();
            lastItem = document.toObject(ItemDB.class);
            itemCounter = lastItem.getItemNo() + 1;
        }
        item.setItemNo(itemCounter);
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(String.valueOf(itemCounter)).set(item);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public ItemDB getItemDB(long itemDBNo) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(String.valueOf(itemDBNo));
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        ItemDB item = null;
        if(document.exists()) {
            item = document.toObject(ItemDB.class);
            return item;
        }else {
            return null;
        }
    }

    public List<ItemDB> getInventoryDB() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Iterable<DocumentReference> documentReference = dbFirestore.collection(COL_NAME).listDocuments();
        List<ItemDB> items = new ArrayList<>();
        for(DocumentReference item : documentReference){
            ApiFuture<DocumentSnapshot> future = item.get();
            DocumentSnapshot document = future.get();
            items.add(document.toObject(ItemDB.class));
        }
        return items;

    }

    public String RemoveItemDB(long itemNo){
        String itemNoCode = String.valueOf(itemNo);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(COL_NAME).document(itemNoCode).delete();
        return "Item with item No "+itemNoCode+" has been removed";
    }

    public ItemDB depositDB (long itemNo, long amount) throws ExecutionException, InterruptedException {
        String itemNoCode = String.valueOf(itemNo);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(itemNoCode);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        ItemDB item = document.toObject(ItemDB.class);
        if (item != null){
            if (amount > 0){
            item.setAmount(item.getAmount() + amount);
            }
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(itemNoCode).set(item);
        }

        return item;
    }

    public ItemDB withdrawDB (long itemNo, long amount) throws ExecutionException, InterruptedException {
        String itemNoCode = String.valueOf(itemNo);
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(COL_NAME).document(itemNoCode);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        ItemDB item = document.toObject(ItemDB.class);
        if (item != null){
            if (amount > 0){
                long newAmount = item.getAmount() - amount;
                if (newAmount >= 0){
                    item.setAmount(newAmount);
                }
            }
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COL_NAME).document(itemNoCode).set(item);
        }

        return item;
    }
}
