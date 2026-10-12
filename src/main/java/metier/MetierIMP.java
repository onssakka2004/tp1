package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Couche Métier : Implémentation de la logique métier.
 * Couplage faible : dépendance orientée vers l'interface IDao et non une classe concrète.
 * L'annotation @Service indique à Spring qu'il s'agit d'un composant de la couche métier.
 */
@Service("metier")
public class MetierIMP implements IMetier {

    // Injection de dépendance automatique via Spring (@Autowired)
    @Autowired
    private IDao dao;

    // Constructeur sans argument (nécessaire pour la réflexion et Spring XML)
    public MetierIMP() {
    }

    // Constructeur avec argument (permet l'injection par constructeur)
    public MetierIMP(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        if (dao == null) {
            throw new IllegalStateException("Erreur : la dépendance 'dao' n'a pas été injectée !");
        }
        double val = dao.getValue();
        return val * 7.0; // Règle métier : 3.0 * 7.0 = 21.0
    }

    // Setter utilisé pour l'injection de dépendances (statique, dynamique et Spring XML)
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    public IDao getDao() {
        return dao;
    }
}
