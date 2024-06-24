package com.framework.controllers;

import com.framework.annotations.GET;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FrontController extends HttpServlet {

    private Map<String, Mapping> urlMappings = new HashMap<>();
    private String controllerPackage;

    @Override
    public void init() throws ServletException {
        controllerPackage = getServletConfig().getInitParameter("controller-package");
        scanControllers();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String requestURL = request.getPathInfo();
        out.println("Request URL: " + requestURL); // Debug: print the request URL

        Mapping mapping = urlMappings.get(requestURL);
        if (mapping == null) {
            out.println("No method associated with URL: " + requestURL);
            out.println("Available mappings: " + urlMappings.keySet()); // Debug: print available mappings
            return;
        }

        try {
            String className = mapping.getClassName();
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String methodName = mapping.getMethodName();
            Method method = clazz.getDeclaredMethod(methodName);

            Object result = method.invoke(instance);

            if (result instanceof String) {
                out.println("Result: " + result);
            } else if (result instanceof ModelView) {
                ModelView modelView = (ModelView) result;
                String url = modelView.getUrl();
                out.println("ModelView URL: " + url); // Debug: print the ModelView URL

                // Ajouter chaque entrée du HashMap en tant que paramètre de la requête
                for (Map.Entry<String, Object> entry : modelView.getData().entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }

                request.getRequestDispatcher(url).forward(request, response);
            } else {
                out.println("Unknown result type: " + result.getClass().getName());
            }
        } catch (ClassNotFoundException e) {
            out.println("Class not found: " + mapping.getClassName());
            e.printStackTrace(out); // Debug: print stack trace
        } catch (NoSuchMethodException e) {
            out.println("Method not found: " + mapping.getMethodName());
            e.printStackTrace(out); // Debug: print stack trace
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            out.println("Error invoking method: " + e.getMessage());
            e.printStackTrace(out); // Debug: print stack trace
        } finally {
            out.close();
        }
    }

    private void scanControllers() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }

            String packagePath = controllerPackage.replace(".", "/");

            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    File packageDirectory = new File(resource.toURI());
                    if (packageDirectory.isDirectory()) {
                        File[] files = packageDirectory.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile() && file.getName().endsWith(".class")) {
                                    String className = file.getName().replace(".class", "");
                                    Class<?> clazz = Class.forName(controllerPackage + "." + className);
                                    for (var method : clazz.getDeclaredMethods()) {
                                        if (method.isAnnotationPresent(GET.class)) {
                                            GET getAnnotation = method.getAnnotation(GET.class);
                                            String url = getAnnotation.value();
                                            urlMappings.put(url, new Mapping(clazz.getName(), method.getName()));
                                            System.out.println("Mapping added: " + url + " -> " + clazz.getName() + "." + method.getName()); // Debug: print the mapping
                                        }
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
