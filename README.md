# François Lapierre - 0144461

<br>

## TPFinal - Projet-ScoutFleet - 420-C65-IN - Administration de bases de données - Frédéric Thériault

<br>

## 22/01/2021

<br>

# Index

- db.logentry.createIndex({date:-1}, { unique: true })
- db.logentry.createIndex({planetName:1}, { unique: true })
- db.logentry.createIndex({galaxyName:1}, { unique: true })
- db.logentry.createIndex({planetName:1, galaxyName:1})

<br>

# Justification BD
## MongoDB
    - Mon choix c'est arrêté sur mongoDB, car il me semblait idéal de pouvoir enregistrer les logs sous forme de document JSON. Les données pouvaient ainsi être sauvegarder presque tel quel et puis affiché pour consultation assez facilement. Cependant, le problème fut que cette base de données était moins commode lorsqu'il fallait créer une trajectoire entre deux planètes. Je n'ai pas réussi à trouver la solution à ce problème.