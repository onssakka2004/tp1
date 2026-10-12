package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresentationAvecAnnotation {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Activité 1-4 : Spring avec Annotations");
        System.out.println("=================================================");

        // 1. Initialisation du conteneur Spring avec scan automatique des packages
        ApplicationContext context = new AnnotationConfigApplicationContext("dao", "metier");

        // 2. Récupération du bean Métier instancié et injecté par Spring
        IMetier metier = context.getBean(IMetier.class);

        // 3. Appel de la méthode métier
        double resultat = metier.calcul();
        System.out.println("Résultat du calcul = " + resultat + " (Résultat attendu : 21.0)");
    }
}
