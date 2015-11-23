package Resolver;

import Entities.HexGrid;
import Entities.Region;
import java.util.List;

/**
 * Interface devant être implémenté par toutes les classes Resolver.
 * Une classe Resolver permet de placer des régions sur une grille hexagonale.
 * Chaque résolveur peut avoir un algorithme différent et être plus ou moins optimisé.
 * @author Théophile
 */
public interface IResolver {
    HexGrid resolve(List<Region> list);
}
