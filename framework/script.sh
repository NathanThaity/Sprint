#!/bin/bash

# Nom du package contenant les classes Java
class_dir="C:/Users/WINDOWS 10/Documents/ETU002649/S4/Servlet/Sprint/Sprint0-2649/framework/src/test/java"

# Répertoire de travail contenant les fichiers .java
working_dir="C:/Users/WINDOWS 10/Documents/ETU002649/S4/Servlet/Sprint/Sprint0-2649/framework/src/main/java/com/framework/controller"

# Chemin du classpath contenant les bibliothèques nécessaires
classpath="C:/Users/WINDOWS 10/Documents/ETU002649/S4/Servlet/Sprint/Sprint0-2649/framework/lib/*"

# Répertoire de sortie pour le fichier JAR
output_dir="C:/Users/WINDOWS 10/Documents/ETU002649/S4/Servlet/Sprint/Sprint0-2649/framework"

# Création du répertoire de sortie s'il n'existe pas
mkdir -p "$output_dir"

# Compilation des classes Java en bytecode
javac -d "$class_dir" -cp "$classpath" "$working_dir"/*.java

# Vérifier si la compilation a réussi
if [ $? -eq 0 ]; then
    echo "Compilation réussie"
else
    echo "Erreur de compilation"
    exit 1
fi

# Changement du répertoire de travail vers le répertoire de compilation
cd "$class_dir"

# Création du fichier JAR
jar cf "$output_dir/framework.jar" -C "$class_dir" .

# Vérifier si la création du JAR a réussi
if [ $? -eq 0 ]; then
    echo "Fichier JAR créé avec succès"
else
    echo "Erreur lors de la création du fichier JAR"
    exit 1
fi

# Retour au répertoire initial
cd -
