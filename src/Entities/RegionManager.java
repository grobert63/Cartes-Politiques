package Entities;

import Debug.TimeDebug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe qui permet la création et la gestion des régions.
 *
 * @author Théophile
 */
public class RegionManager {
    private final BoundaryManager _bm;
    private final List<Region> _regions = new ArrayList<>();

    /**
     * Créer un manager à partir d'un ensemble de liste de RawPolygon
     *
     * @param rawRegions
     */
    public RegionManager(List<List<RawPolygon>> rawRegions) {
        int nbRegions = rawRegions.size();

        RawPolygon[] rawMainPolygons = new RawPolygon[nbRegions];
        List<RawPolygon>[] displayablePolygons = new ArrayList[nbRegions];
        for (int i = 0; i < nbRegions; i++) {
            List<RawPolygon> polygons = rawRegions.get(i);
            displayablePolygons[i] = polygons;
            rawMainPolygons[i] = Geometry.getMainPolygon(polygons);
        }

        TimeDebug.timeStart(19);
        _bm = new BoundaryManager(rawMainPolygons);
        BoundPolygon[] boundMainPolygons = _bm.getBoundPolygon();
        TimeDebug.timeStop(19);

        for (int i = 0; i < nbRegions; i++) {
            Region r = new Region(displayablePolygons[i], rawMainPolygons[i], boundMainPolygons[i]);
            _regions.add(r);
        }

        TimeDebug.timeStart(20);
        calculateNeighbors(_regions, _bm.getBoundaries());
        TimeDebug.timeStop(20);
    }

    /**
     * Récupère les régions créées
     *
     * @return Les régions
     */
    public List<Region> getRegions() {
        return _regions;
    }

    /**
     * Récupère toutes les frontières toutes régions confondues
     *
     * @return Liste des frontières
     */
    public List<Boundary> getBoundaries() {
        return _bm.getBoundaries();
    }

    /**
     * Calcul et intégre les voisins dans les régions à partir de celle-ci et de l'ensemble des frontières.
     *
     * @param regions       Liste des régions dont on veut calculer les voisins
     * @param allBoundaries Ensemble des frontières de toutes les régions
     */
    private void calculateNeighbors(List<Region> regions, List<Boundary> allBoundaries) {
        List<Region> involvedRegions = new LinkedList<>();
        BoundPolygon currentPoly;

        for (Boundary b : allBoundaries) {
            involvedRegions.clear();

            for (Region r : regions) {
                currentPoly = r.getBoundMainPolygon();
                if (currentPoly.getBoundaries().contains(b)) {
                    involvedRegions.add(r);
                }
            }

            Iterator<Region> it = involvedRegions.iterator();
            switch (involvedRegions.size()) {
                case 1:
                    it.next().addNeighbor(null, b);
                    break;
                case 2:
                    Region r1 = it.next(), r2 = it.next();
                    r1.addNeighbor(r2, b);
                    r2.addNeighbor(r1, b);
                    break;
                default:
                    System.err.println("involvedRegions.size() = " + involvedRegions.size());
                    break;
            }
        }
    }

    /**
     * Indique le nom de la colonne par défaut du DBF qui servira du nom de région
     *
     * @param defaultField Nom de la colonne principal
     */
    public void setRegionsName(String defaultField) {
        for (Region r : _regions) {
            r.setDefaultField(defaultField);
        }
    }
}
