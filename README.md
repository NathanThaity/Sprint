# Objectif: Afficher la page indiquee par la methode appelee dans l'url

# Framework
    # Modification
        ProcessRequest dans frontController
            _condition si la methode retourne string alors on affiche juste la valeur
            sinon ModelView on envoie vers la page

    # Creation
        -ModelView
            _String url,HashMap data
            _addObject pour rajouter des elements au HashMap
        