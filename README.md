# tp1
TP1 : Inversion de Contrôle (IoC) et Injection de Dépendances (DI)

 Compte-Rendu : TP1 - Inversion de Contrôle & Injection de Dépendances

---

Contexte Universitaire
Établissement :** Faculté des Sciences de Sfax 
* **Matière :** Architecture MicroServices
* **Filière :** Master Professionnel
* **Année Universitaire :2026-2027
* **Enseignantes :** Corinne Amel ZAYANI, Amal ABID
* **Étudiant(e) :Ons Sakka
Objectifs du TP
* Comprendre et appliquer le concept de **couplage faible** en orienté objet à travers les interfaces.
* Comprendre et illustrer les principes fondamentaux d'**Inversion de Contrôle (IoC)** et d'**Injection de Dépendances (DI)**.
* Explorer l'évolution de l'injection de dépendances à travers 4 approches complémentaires :
  1. **Activité 1-1 :** Instanciation statique manuelle (`new`).
  2. **Activité 1-2 :** Instanciation dynamique via fichier de configuration (`config.txt`) et réflexion (`Class.forName`).
  3. **Activité 1-3 :** Automatisation avec le framework Spring et configuration XML (`config.xml`).
  4. **Activité 1-4 :** Configuration moderne avec les annotations Spring (`@Repository`, `@Service`, `@Autowired`).
  Structure du Projet

Pour garantir une séparation stricte des responsabilités (architecture multicouche / MVC), l'application est découpée en 3 packages principaux :
* **`dao` :** Regroupe l'abstraction des données (`IDao`) et son implémentation concrète (`DaoIMP`).
* **`metier` :** Contient la logique applicative (`IMetier`, `MetierIMP`).
* **`presentation` :** Contient les différentes classes de démonstration illustrant chaque étape.
* **`resources` :** Contient les fichiers de configuration externe (`config.xml`, `config.txt`).
<img width="345" height="442" alt="image" src="https://github.com/user-attachments/assets/2be6aee7-2074-46c4-bfbf-207f8c4f5068" />
Activité 1-1 : Couplage Faible et Instanciation Statique

### 1. Principe & Démarche
Dans cette première étape, nous avons relié la classe `MetierIMP` à l'interface `IDao` au lieu d'une classe concrète.  
Dans `PresentationStatique`, nous créons nous-mêmes les instances avec le mot-clé `new`, puis nous lions le métier à son DAO via la méthode `setDao(dao)`.

### 2. Code Source

```java
// Dans presentation.PresentationStatique
package presentation;

import dao.DaoIMP;
import dao.IDao;
import metier.IMetier;
import metier.MetierIMP;

public class PresentationStatique {
    public static void main(String[] args) {
        // Instanciation directe manuelle (opérateur new)
        IDao dao = new DaoIMP();
        IMetier metier = new MetierIMP();

        // Injection de dépendance via le setter
        ((MetierIMP) metier).setDao(dao);

        // Exécution du calcul
        System.out.println(metier.calcul()); // Résultat attendu : 21.0 (3.0 * 7.0)
    }
}
```

### 3. Réponses aux Questions

* **Q9. Expliquez comment cela illustre le couplage faible :**  
  > `MetierIMP` dépend uniquement du contrat défini par l'interface `IDao` (`getValue()`). Elle ne connaît aucune classe concrète (`DaoIMP`). Cela permet d'interchanger l'implémentation de la couche d'accès aux données (base de données, web service, mock de test) sans changer une seule ligne de code dans `MetierIMP`.

* **Q10. Le principe d'Inversion de Contrôle est-il respecté ? Justifier :**  
  > **Non.** C'est la classe principale `PresentationStatique` qui contrôle directement la création des objets avec l'opérateur `new` et effectue manuellement le câblage avec `setDao()`. L'autorité de contrôle reste du côté du développeur ; elle n'a pas été inversée.

* **Q11. Que faudrait-il modifier pour éviter que la classe de présentation contrôle directement la création des objets ?**  
  
<img width="773" height="256" alt="image" src="https://github.com/user-attachments/assets/1743cc7a-428f-4220-a6ac-824d57c78e8d" />

Activité 1-2 : Instanciation Dynamique via Réflexion et Fichier de Configuration

### 1. Principe & Démarche
Afin de ne plus écrire de noms de classes "en dur" dans le code, nous externalisons les classes à charger dans un fichier externe `config.txt` :
```text
dao.DaoIMP
metier.MetierIMP
```
La classe `PresentationDynamique` lit ces chaînes de caractères, charge les classes via `Class.forName()`, crée les instances avec `.getDeclaredConstructor().newInstance()`, puis injecte la dépendance par réflexion (`Method.invoke`).

### 2. Code Source

```java
// Dans presentation.PresentationDynamique
// Lecture depuis config.txt et chargement dynamique
Scanner scanner = new Scanner(new File("config.txt"));
String daoClassName = scanner.nextLine().trim();
String metierClassName = scanner.nextLine().trim();
scanner.close();

// Instanciation dynamique sans constructeur explicite à la compilation
Class<?> cDao = Class.forName(daoClassName);
IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();

Class<?> cMetier = Class.forName(metierClassName);
IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

// Injection par réflexion via la méthode setDao
Method setDao = cMetier.getMethod("setDao", IDao.class);
setDao.invoke(metier, dao);

System.out.println(metier.calcul()); // Affiche 21.0
```

### 3. Réponses aux Questions

* **Q7. Expliquez comment cela illustre l'injection de dépendances par instanciation dynamique :**  
  > Aucun constructeur d'implémentation n'est appelé de façon explicite à la compilation. Tout le processus (découverte, chargement mémoire et liaison) est déporté à l'exécution (*runtime*). La classe de présentation ignore les types concrets au moment de la compilation.

* **Q8. En quoi cette approche se rapproche-t-elle davantage du principe IoC que l'activité 1-1 ?**  
  > On satisfait le principe **Open/Closed** (ouvert à l'extension, fermé à la modification). Si nous concevons un nouveau module DAO (`dao.DaoV2`), il suffit de mettre à jour le fichier texte `config.txt` sans avoir à modifier ni recompiler l'application.

* **Q9. Quels sont les avantages et les limites de cette solution ?**  
  > * **Points forts :** Grande souplesse, extensibilité aisée, couplage très faible.  
  > * **Inconvénients :** Code technique lourd à maintenir, manipulation délicate de l'API Reflection, vulnérabilité aux fautes de frappe (erreurs visibles uniquement au runtime avec `ClassNotFoundException`).

<img width="1868" height="981" alt="image" src="https://github.com/user-attachments/assets/a0f7ccdb-40ac-433f-bed5-225905a8955a" />

 Activité 1-3 : Automatisation via Spring et Configuration XML

### 1. Principe & Démarche
Nous exploitons ici le conteneur léger de Spring. Le câblage des composants est confié au fichier `src/main/resources/config.xml`.  
Le conteneur Spring (`ClassPathXmlApplicationContext`) lit ce fichier, instancie les beans et injecte automatiquement les dépendances par setter. Dans `PresentationAvecSpring`, il suffit d'initialiser le contexte et de récupérer le bean métier prêt à l'emploi.

### 2. Fichier de Configuration XML (`config.xml`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Déclaration du bean DAO -->
    <bean id="dao" class="dao.DaoIMP"/>

    <!-- Déclaration du bean Métier avec injection automatique de dao par setter -->
    <bean id="metier" class="metier.MetierIMP">
        <property name="dao" ref="dao"/>
    </bean>

</beans>
```

### 3. Code Source

```java
// Dans presentation.PresentationAvecSpring
ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
IMetier metier = context.getBean(IMetier.class);
System.out.println(metier.calcul()); // Affiche 21.0
```

### 4. Réponses aux Questions

* **Q6. Comparer cette solution avec l'activité 1-2 en termes de gestion des dépendances :**  
  > Dans l'activité 1-2, le développeur programme manuellement toute la chaîne d'instanciation et de réflexion. Avec Spring XML, l'approche est entièrement déclarative : Spring prend en charge l'instanciation, l'ordre d'injection, la gestion du cycle de vie des beans et les erreurs avec clarté.

* **Q7. Expliquer la différence entre : création manuelle / réflexion (`Class.forName`) / création par le conteneur Spring :**  
  > 1. **Création manuelle (`new`) :** Liaison statique résolue à la compilation, couplage fort, recompilation indispensable pour toute modification.  
  > 2. **Création par réflexion (`Class.forName`) :** Liaison dynamique au runtime, couplage faible, mais code lourd et sans vérification à la compilation.  
  > 3. **Création par le conteneur Spring :** Inversion de contrôle totale. Spring gère le cycle de vie complet (instanciation, injection, scope Singleton/Prototype, destruction) et offre des services d'entreprise avancés (AOP, transactions).

<img width="1910" height="983" alt="image" src="https://github.com/user-attachments/assets/53f30eb8-a667-44ac-a208-d9c06e5ee4d5" />

Activité 1-4 : Injection Moderne avec les Annotations Spring

### 1. Principe & Démarche
Nous nous affranchissons du fichier XML au profit des annotations de stéréotypes :
* `@Repository` sur `DaoIMP` pour identifier le composant de persistance/données.
* `@Service` sur `MetierIMP` pour désigner le service métier.
* `@Autowired` sur la référence `private IDao dao` pour ordonner l'injection automatique.

### 2. Code Source

```java
// dao/DaoIMP.java
@Repository("dao")
public class DaoIMP implements IDao {
    @Override
    public double getValue() {
        return 3.0;
    }
}

// metier/MetierIMP.java
@Service("metier")
public class MetierIMP implements IMetier {
    @Autowired
    private IDao dao;

    @Override
    public double calcul() {
        return dao.getValue() * 7.0;
    }
}

// presentation/PresentationAvecAnnotation.java
ApplicationContext context = new AnnotationConfigApplicationContext("dao", "metier");
IMetier metier = context.getBean(IMetier.class);
System.out.println(metier.calcul()); // Affiche 21.0
```

### 3. Réponses aux Questions

* **Q5. Expliquez comment Spring gère l'injection de dépendances à l'aide des annotations :**  
  > Au démarrage, Spring procède au scan (*component-scan*) des packages cibles (`dao`, `metier`). Lorsqu'il repère les annotations `@Repository` et `@Service`, il instancie les beans associés. En rencontrant `@Autowired`, il recherche un bean de type compatible (`IDao`) dans son conteneur et l'affecte automatiquement.

* **Q6. Pourquoi cette solution est-elle plus maintenable que le XML ? En quoi représente-t-elle une application complète de l'IoC ?**  
  > * **Maintenabilité :** Moins de verbosité, configuration contextuelle au plus près du code, support immédiat de l'autocomplétion et du refactoring par l'IDE (vérification dès la compilation).  
  > * **Aboutissement de l'IoC :** C'est l'illustration parfaite du principe *"Don't call us, we'll call you"*. Le développeur décrit uniquement ses intentions via des annotations, et le framework Spring orchestre l'ensemble de l'architecture et du cycle de vie.

<img width="1908" height="914" alt="image" src="https://github.com/user-attachments/assets/a4d20693-ef90-44e4-b6de-c4d8c6513502" />

Tableau Récapitulatif : Synthèse des 4 Approches

| Étape | Gestionnaire du cycle de vie | Détection des dépendances | Niveau de Couplage | Degré d'IoC |
| :--- | :--- | :--- | :--- | :--- |
| **1-1 Statique** | Développeur (code Java) | Manuelle (`new` + `setDao`) | Fort dans la couche cliente | Nul |
| **1-2 Dynamique** | Développeur (via `config.txt`) | Réflexion (`Method.invoke`) | Faible partout | Partiel |
| **1-3 Spring XML** | Conteneur Spring | Fichier `config.xml` | Faible partout | Total |
| **1-4 Spring Annotations** | Conteneur Spring | Annotations (`@Autowired`) | Faible partout | Total & Moderne |

