package presentation;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresentationAvecSpring {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Activité 1-3 : Spring Framework avec XML");
        System.out.println("=================================================");

        // 1. Initialisation du conteneur IoC Spring avec le fichier config.xml
        ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");

        // 2. Récupération du bean Métier préconfiguré
        IMetier metier = context.getBean(IMetier.class);

        // 3. Appel de la méthode métier
        double resultat = metier.calcul();
        System.out.println("Résultat du calcul = " + resultat + " (Résultat attendu : 21.0)");
    }
}
