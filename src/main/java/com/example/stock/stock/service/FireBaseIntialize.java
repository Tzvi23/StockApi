package com.example.stock.stock.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
public class FireBaseIntialize {
    @PostConstruct
    public void initialize() {
        //To provide the serviceaccount.json in other builds created 2 approaches
        try {
            InputStream serviceAccount2 = null;
            FileInputStream serviceAccount = null;
            try{
                 serviceAccount =
                    new FileInputStream("./serviceaccount.json");
            }
            catch (FileNotFoundException e){
                System.out.println("Working Directory = " + System.getProperty("user.dir"));
                serviceAccount2 =
                        new ClassPathResource("serviceaccount.json").getInputStream();
            }

        // --------------------------------------------------------
            FirebaseOptions options;
            if (serviceAccount == null){
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount2))
                        .setDatabaseUrl("https://spring-boot-stock-api-default-rtdb.firebaseio.com/")
                        .build();
            }
            else{
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://spring-boot-stock-api-default-rtdb.firebaseio.com/")
                        .build();
            }
            FirebaseApp.initializeApp(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
