package com.kingmartinien.iutnotifyapi.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream serviceAccount =
                    new FileInputStream("iut-notify-api/src/main/resources/iut-notify-2415d-firebase-adminsdk-3vu5x-714eab0b39.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("iut-notify-2415d.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    @Bean
    public Bucket bucket(FirebaseApp firebaseApp) {
        return StorageClient.getInstance(firebaseApp).bucket();
    }

    @Bean
    public Storage storage(FirebaseApp firebaseApp) {
        return this.bucket(firebaseApp).getStorage();
    }

}
