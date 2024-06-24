package com.framework.controllers;

import java.util.HashMap;

public class ModelView {
    private String url;
    private HashMap<String, Object> data;

    // Constructeur par défaut
    public ModelView() {
        this.data = new HashMap<>();
    }

    // Constructeur avec paramètre url
    public ModelView(String url) {
        this.url = url;
        this.data = new HashMap<>();
    }

    // Constructeur avec paramètres url et data
    public ModelView(String url, HashMap<String, Object> data) {
        this.url = url;
        this.data = data;
    }

    // Getter pour l'URL
    public String getUrl() {
        return url;
    }

    // Setter pour l'URL
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter pour le data
    public HashMap<String, Object> getData() {
        return data;
    }

    // Setter pour le data
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    // Méthode pour ajouter un élément au HashMap
    public void addObject(String key, Object value) {
        this.data.put(key, value);
    }

    // Méthode pour récupérer un élément du HashMap
    public Object getData(String key) {
        return this.data.get(key);
    }

    @Override
    public String toString() {
        return "ModelView{" +
                "url='" + url + '\'' +
                ", data=" + data +
                '}';
    }
}
