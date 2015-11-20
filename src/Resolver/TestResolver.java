package Resolver;

import Entities.HexGrid;
import Entities.Region;
import java.util.List;

/**
 * Une implémentation simple de HexResolver servant uniquement pour les tests.
 * @author Théophile
 */
public class TestResolver implements HexResolver{

    /**
     * (Test uniquement) Place les régions les unes à la suite des autres sur une grille hexagonale.
     * @param list Liste de régions
     * @return Grille hexagonale
     */
    @Override
    public HexGrid resolve(List<Region> list) {
        int nbLignes = (int)(Math.sqrt((double)(list.size())));
        int nbColonnes = (list.size() / nbLignes) + 1;
        HexGrid grid = new HexGrid(nbColonnes, nbLignes);
        
        int row = 0, col = 0;
        for(Region r : list){
            grid.addRegion(col, row, r);
            
            col++;
            if(col == nbColonnes){
                col = 0;
                row++;
            }
        }
            
        return grid;
    }
}
