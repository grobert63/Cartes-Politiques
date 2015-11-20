package Resolver;

import Entities.HexGrid;
import Entities.Region;
import java.util.List;

/**
 * Interface devant être implémenter par tous les classes Resolver.
 * Une classe Resolver permette de placer des régions sur une grille hexagonale.
 * Chaque résolveur peut avoir un algorithme différent et être plus ou moins optimisé.
 * @author Théophile
 */
public interface HexResolver {
    public HexGrid resolve(List<Region> list);
}
