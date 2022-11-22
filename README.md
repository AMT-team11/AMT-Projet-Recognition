# AMT-Projet-Recognition

## Introduction
Dans ce travail, nous avons dû implémenter un client AWS permettant de gérer des opérations sur un bucket spécifié.
L'objectif étant de pouvoir stocker des images sur notre bucket afin d'exécuter ensuite une analyse de labels grâce à 
Amazon Rekognition sur cette même image. L'analyse est ensuite stockée sur le même bucket sous format JSON.

## Installation

### Prérequis
- Java 17
- Maven 3.8.1
- AWS CLI 2.2.37

Notre repository se base sur un projet maven, il est donc nécessaire d'avoir maven d'installé sur votre machine.
Une fois maven installé, il faut lancer la commande suivante pour installer les dépendances du projet :

> TODO votre commande run les tests ce qui n'est pas désirable à cette étape

```bash
mvn install
```

### Configuration
Pour pouvoir utiliser notre client, il faut configurer les identifiants AWS sur votre machine. Si vous souhaitez
utiliser un profil AWS spécifique, il faut que ce dernier apparaisse dans le fichier de configuration des credentials
aws sous la forme :
```
[profile_name]
aws_access_key_id = <access_key_id>
aws_secret_access_key = <secret_access_key>
```

Notez que si vous fournissez un profil AWS n'existant pas, le programme charge le profile *default*.

Les images à ajouter sur le bucket peuvent être placées dans le dossier `src/main/resources/` du projet et doivent
posséder l'extension `.jpg` ou `.png`.

## Utilisation

Afin d'utiliser notre App sur votre machine, il faut lancer la commande suivante :

> TODO on attendait une étape séparée du rest pour run les tests avec la commande pour tout run + la commande pour run un test en particulier.

### Génération d'un jar exécutable
```bash
mvn package
```
Vous trouverez le jar généré dans le dossier `target/` du projet. Notez que vous retrouverez deux fichiers jar, un
contenant les dépendances du projet et un autre ne contenant que le code source.

### Compilation et exécution sur la même machine via maven
```bash
mvn compile exec:java
```
Sachant que les arguments à passer sont à changer dans le fichier pom.xml. Pour modifier les arguments, il faut les
changer dans la partie `<build>` du fichier `pom.xml`. Trouvez le plugin `exec-maven-plugin` et modifiez les arguments
et sous la balise `<configuration>`, vous trouverez les `<arguments>`, c'est à cet endroit qu'il vous faut ajouter 
les arguments sous la forme `<argument>argument</argument>`. Notez que leur ordre est important, vous trouverez des
exemples dans la section d'[exemples](##Exemples d'utilisation).

Les deux premiers arguments sont le profile AWS à utiliser et la région AWS à utiliser. Il faut également spécifier le
nom du bucket sur lequel les opérations sont effectuées. Ils sont donc obligatoires.

Comme notre app permet de faire différentes opérations, le quatrième argument doit être l'opération à effectuer. Les
opérations possibles sont :
- `bucket-content` : permet d'afficher le contenu du bucket
- `create` : permet d'uploader une image sur le bucket
- `delete` : permet de supprimer une image du bucket
- `detect-labels` : permet d'analyser une image et de récupérer les labels de celle-ci
- `download` : permet de télécharger un objet du bucket

Ensuite, en fonction de l'opération choisie, il faut ajouter les arguments nécessaires à l'opération.

### Opération `bucket-content`
Cette opération permet d'afficher le contenu du bucket. Elle ne prend aucun argument supplémentaire. Elle s'avère
utile pour vérifier que les images ont bien été créées/effacées.

### Opération `create`
Cette opération permet d'uploader une image sur le bucket. Pour cela, il faut ajouter l'argument `create` et ajouter
deux arguments supplémentaires :
- le chemin de l'image à ajouter
- le nom de l'image lorsqu'elle sera ajoutée sur le bucket
Par exemple, pour ajouter l'image `image.jpg` sur le bucket avec le nom `image2.jpg`, il faut ajouter les arguments
- `create`
- `<chemin>/image.jpg`
- `image2.jpg`

### Opération `delete`
Cette opération permet de supprimer une image du bucket. Pour cela, il faut ajouter l'argument `delete` et ajouter
un argument supplémentaire :
- le nom de l'image à supprimer (Ce nom étant celui attribué dans le bucket)
Par exemple, pour supprimer l'image `image2.jpg` du bucket, il faut ajouter les arguments
- `delete`
- `image2.jpg`

### Opération `detect-labels`
Cette opération permet d'analyser une image et de récupérer les labels de celle-ci. Pour cela, il faut ajouter l'argument
`detect-labels` et ajouter trois arguments supplémentaires :
- le nom de l'image à analyser (Ce nom étant celui attribué dans le bucket)
- le nombre de labels à récupérer
- la confidence minimale pour un label
Cette opération va créer un fichier JSON contenant les labels de l'image analysée. Ce fichier sera ensuite placé dans le
bucket avec la clé `<image_analysee>RekognitionAnalysis.json`.

Par exemple, pour analyser l'image `image2.jpg` du bucket, il faut ajouter les arguments
- `detect-labels`
- `image2.jpg`
- `10`
- `0.5`
Cette opération créera un fichier `image2.jpgRekognitionAnalysis.json` dans le bucket.

### Opération `download`
Cette opération permet de télécharger un objet du bucket. Pour cela, il faut ajouter l'argument `download` et ajouter
deux arguments supplémentaires :
- le nom de l'objet à télécharger (Ce nom étant celui attribué dans le bucket)
- le chemin où l'objet sera téléchargé
Par exemple, pour télécharger l'image `image2.jpg` du bucket dans un fichier `src/main/resources/dl.jpg`, il faut 
ajouter les arguments :
- `download`
- `image2.jpg`
- `src/main/resources/dl.jpg`

## Exemples d'utilisation

Comme dit précédemment, nous exécutons notre app avec la commande suivante :
```bash
mvn compile exec:java
```
Il vous suffit donc de modifier les arguments dans le fichier `pom.xml` pour lancer les opérations souhaitées.

### Création d'une image
Dans votre fichier ajoutez les arguments comme indiqué plus haut dans le fichier `pom.xml` :
```xml
<arguments>
    <argument>profile_name</argument>
    <argument>region</argument>
    <argument>bucket_name</argument>
    <argument>create</argument>
    <argument>chemin/image/file.jpg</argument>
    <argument>cle</argument>
</arguments>
```

À noter que les regions possibles sont les 
[suivantes](https://docs.aws.amazon.com/AmazonElastiCache/latest/mem-ug/RegionsAndAZs.html)
(Sous la section *Supported regions & endpoints*).

### Suppression d'une image
Dans votre fichier ajoutez les arguments comme indiqué plus haut dans le fichier `pom.xml` :
```xml
<arguments>
    <argument>profile_name</argument>
    <argument>region</argument>
    <argument>bucket_name</argument>
    <argument>delete</argument>
    <argument>cle</argument>
</arguments>
```

### Analyse d'une image
Dans votre fichier ajoutez les arguments comme indiqué plus haut dans le fichier `pom.xml` :
```xml
<arguments>
    <argument>profile_name</argument>
    <argument>region</argument>
    <argument>bucket_name</argument>
    <argument>detect-labels</argument>
    <argument>cle</argument>
    <argument>nombre_labels</argument>
    <argument>confidence_minimale</argument>
</arguments>
```

### Téléchargement d'une image
Dans votre fichier ajoutez les arguments comme indiqué plus haut dans le fichier `pom.xml` :
```xml
<arguments>
    <argument>profile_name</argument>
    <argument>region</argument>
    <argument>bucket_name</argument>
    <argument>download</argument>
    <argument>cle</argument>
    <argument>chemin/destination/file.jpg</argument>
</arguments>
```

### Exécution du fichier jar

L'utilisation de l'exécutable tel quel est semblable aux points cités dessus.
Placez-vous à l'endroit ou vous avez téléchargé le fichier jar et exécutez la commande suivante :
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> <operation> <arguments>
```

Par exemple si vous souhaitez créer une image, l'uploader, l'analyser et télécharger le fichier de résultat, vous pouvez
exécuter les commandes suivantes :
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> create <chemin/image/file.jpg/png> <cle>
```
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> detect-labels <cle> <nombre_labels> <confidence_minimale>
```
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> download <cle> <chemin/destination/file.json>
```

Si vous souhaitez supprimer les objets créés, vous pouvez exécuter la commande suivante :
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> delete <cle>
```

Si vous souhaitez vérifier que vos objets ont bien été créés, vous pouvez exécuter la commande suivante :
```bash
java -jar <nom_du_fichier>.jar <profile_name> <region> <bucket_name> bucket-content
```

## Misc

> TODO vos conventions de nommage ne spécifier rien au sujet du code

> TODO pour les choix technologiques précisez  version de JAVA et du SDK java AWS ainsi que pourquoi vous les avez choisit.

> TODO vos choix technologiques ne parlent pas de la version de Java

> TODO aucun tests sur la partie label detection