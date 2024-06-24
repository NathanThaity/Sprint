# Objectif: Trouver le mapping associe,afficher le chemin URL et le Mapping

# Framework
   # Creation
        -annotation GET
        -classe Mapping  (String className,String methodName)
        -HashMap(String url,Mapping)

   # Modification
        -retirer l'attribut boolean dans FrontController
        -init dans FrontController  
            -scan des controllers
            -methodes anootees GET dans chaque controller
                -is not null = Mapping(controller.name,method.name)
                    HashMap.associer(annotation.value,Mapping)
        -processRequest dans FrontController
            -Mapping associe au chemin de l'URL de la requete
            -si on trouve le Mapping associe = afficher le chemin URL et le Mapping
            -sinon = afficher qu'il n'y a pas de methode associee a ce chemin
