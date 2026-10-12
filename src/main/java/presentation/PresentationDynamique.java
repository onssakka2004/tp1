package presentation;

import dao.IDao;
import metier.IMetier;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;

public class PresentationDynamique {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Activité 1-2 : Instanciation Dynamique (Réflexion)");
        System.out.println("=================================================");

        try {
            // Lecture du fichier de configuration config.txt
            Scanner scanner = null;
            File file = new File("config.txt");
            if (file.exists()) {
                scanner = new Scanner(file);
            } else {
                InputStream is = PresentationDynamique.class.getClassLoader().getResourceAsStream("config.txt");
                if (is != null) {
                    scanner = new Scanner(is);
                } else {
                    File resFile = new File("src/main/resources/config.txt");
                    if (resFile.exists()) {
                        scanner = new Scanner(resFile);
                    } else {
                        throw new RuntimeException("Fichier config.txt non trouvé !");
                    }
                }
            }

            String daoClassName = scanner.nextLine().trim();
            String metierClassName = scanner.nextLine().trim();
            scanner.close();

            System.out.println("Classe DAO chargée depuis config.txt    : " + daoClassName);
            System.out.println("Classe Métier chargée depuis config.txt : " + metierClassName);

            // 1. Chargement dynamique des classes à l'aide de Class.forName
            Class<?> cDao = Class.forName(daoClassName);
            IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();

            Class<?> cMetier = Class.forName(metierClassName);
            IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

            // 2. Injection par réflexion via la méthode setDao
            Method setDao = cMetier.getMethod("setDao", IDao.class);
            setDao.invoke(metier, dao);

            // 3. Exécution du calcul
            double resultat = metier.calcul();
            System.out.println("Résultat du calcul = " + resultat + " (Résultat attendu : 21.0)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
