package com.truek;

public class FirebaseTokenManager {

    private static FirebaseTokenManager instance;
    private String firebaseToken;

    private FirebaseTokenManager() {
        // Constructor privado para garantizar que sólo haya una instancia
    }

    public static FirebaseTokenManager getInstance() {
        if (instance == null) {
            instance = new FirebaseTokenManager();
        }
        return instance;
    }

    public void setFirebaseToken(String token) {
        this.firebaseToken = token;
    }

    public String getFirebaseToken() {
        return this.firebaseToken;
    }
}
