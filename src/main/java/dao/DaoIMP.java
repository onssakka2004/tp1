package dao;

import org.springframework.stereotype.Repository;

/**
 * Couche DAO : Implémentation concrète de l'interface IDao.
 * L'annotation @Repository indique à Spring qu'il s'agit d'un composant de la couche d'accès aux données.
 */
@Repository("dao")
public class DaoIMP implements IDao {

    @Override
    public double getValue() {
        System.out.println(">> [DAO] Récupération des données depuis la source (DaoIMP)...");
        return 3.0; // Simulation d'un accès aux données
    }
}
