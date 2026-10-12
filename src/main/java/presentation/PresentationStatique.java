package presentation;

import dao.DaoIMP;
import dao.IDao;
import metier.IMetier;
import metier.MetierIMP;

public class PresentationStatique {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Activité 1-1 : Instanciation Statique (new)");
        System.out.println("=================================================");

        // 1. Instanciation statique manuelle
        IDao dao = new DaoIMP();
        IMetier metier = new MetierIMP();

        // 2. Injection de dépendance via le setter
        ((MetierIMP) metier).setDao(dao);

        // 3. Exécution du calcul métier
        double resultat = metier.calcul();
        System.out.println("Résultat du calcul = " + resultat + " (Résultat attendu : 21.0)");
    }
}
