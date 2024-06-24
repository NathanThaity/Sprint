package com.framework.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {

    private boolean controllersScanned = false;
    private List<String> controllerNames = new ArrayList<>();

    // Variable pour stocker le nom du package des contrôleurs
    private String controllerPackage;

    @Override
    public void init() throws ServletException {
        // Récupérer le nom du package des contrôleurs depuis les paramètres d'initialisation
        controllerPackage = getServletConfig().getInitParameter("controller-package");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Vérifier si les contrôleurs ont déjà été analysés
        if (!controllersScanned) {
            // Si non, scanner les contrôleurs et stocker les noms dans la liste
            scanControllers();
            controllersScanned = true;
        }

        // Afficher la liste des noms de contrôleurs
        out.println("Liste des contrôleurs du package " + controllerPackage + " :");
        for (String controllerName : controllerNames) {
            out.println(controllerName + "<br>");
        }
        out.close();
    }

    private void scanControllers() {
        try {
            // Charger le class loader pour accéder aux classes du package
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }

            // Convertir le nom du package en chemin relatif pour le class loader
            String packagePath = controllerPackage.replace(".", "/");

            // Récupérer les ressources (fichiers .class) du package
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File packageDirectory = new File(resource.toURI());
                    if (packageDirectory.isDirectory()) {
                        // Parcourir les fichiers dans le répertoire du package
                        File[] files = packageDirectory.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                // Vérifier si le fichier est une classe .class
                                if (file.isFile() && file.getName().endsWith(".class")) {
                                    // Charger la classe à partir du fichier
                                    String className = file.getName().replace(".class", "");
                                    Class<?> clazz = Class.forName(controllerPackage + "." + className);
                                    // Vérifier si la classe est annotée avec @AnnotationController
                                    if (clazz.isAnnotationPresent(AnnotationController.class)) {
                                        controllerNames.add(className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "FrontController";
    }
}
