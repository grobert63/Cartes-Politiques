package GUI;

import Entities.HexGrid;
import Entities.Point;
import Entities.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Objet contenant et calculant les coordonnées des hexagones pour faciliter l'affichage
 *
 * @author Théophile
 */
public class HexPolygonContainer {
    // Position des sommets d'un hexagone par rapport au centre de (0;0)
    private final double[] hexEdgesX = {0.5, 0.5, 0.0, -0.5, -0.5, 0.0};
    private final double[] hexEdgesY = {0.25, -0.25, -0.5, -0.25, 0.25, 0.5};

    private List<Region> regions = new ArrayList<>();

    // hexNormalizeCoordCenter : coordonnées des centres normalisés des hexagones.
    // Elles ne seront jamais modifiées ni par un changement de zoom ou de taille du canvas, ni par un décalage.
    private List<Point> hexNormalizeCoordCenter = new ArrayList<>();

    // hexRealCoordCenterWithoutOffset : coordonnées des centres réels des hexagones.
    // Elles sont modifiés à chaque changement de zoom ou de taille du canvas. Elles ne sont pas modifiées par un décalage.
    private List<Point> hexRealCoordCenterWithoutOffset = new ArrayList<>();

    // Taille de la HexGrid
    private double hexGridHeight;
    private double hexGridWidth;

    // Taille d'un hexagone
    private double hexWidth;
    private double hexHeight;

    // HexCanvas saved values
    private double _zoom;
    private double _canvasHeight;
    private double _canvasWidth;


    public HexPolygonContainer(HexGrid hexgrid, double canvasZoom, double canvasWidth, double canvasHeight) {
        _zoom = canvasZoom;
        _canvasHeight = canvasHeight;
        _canvasWidth = canvasWidth;
        notifyHexGridChange(hexgrid);
    }

    /**
     * Notifie l'objet d'un changement de HexGrid
     *
     * @param hexgrid Nouvelle grille hexagonale
     */
    public void notifyHexGridChange(HexGrid hexgrid) {
        recalculateAllNormCenter(hexgrid);
        recalculateAfterNotify();
    }

    /**
     * Notifie l'objet d'un changement de zoom de l'affichage
     *
     * @param newZoom Nouvelle valeur de zoom
     */
    public void notifyZoomChange(double newZoom) {
        this._zoom = newZoom;
        recalculateAfterNotify();
    }

    /**
     * Notifie l'objet du changement de largeur du canvas
     *
     * @param newCanvasWidth Nouvelle largeur du canvas
     */
    public void notifyCanvasWidthChange(double newCanvasWidth) {
        this._canvasWidth = newCanvasWidth;
        recalculateAfterNotify();
    }

    /**
     * Notifie l'objet du changement de hauteur du canvas
     *
     * @param newCanvasHeight Nouvelle hauteur du canvas
     */
    public void notifyCanvasHeightChange(double newCanvasHeight) {
        this._canvasHeight = newCanvasHeight;
        recalculateAfterNotify();
    }

    /**
     * Récupère les composantes en X des coordonnées d'un hexagone à afficher
     *
     * @param index   Indice de l'hexagone à afficher
     * @param offsetX Décalage en X de la grille hexagonale dans le canvas
     * @return Les 6 composantes en X des 6 sommets de l'hexagone à afficher
     */
    public double[] getDrawableHexCoordX(int index, int offsetX) {
        double[] result = new double[6];
        double realCoordX = this.hexRealCoordCenterWithoutOffset.get(index).x;

        for (int i = 0; i < 6; i++) {
            result[i] = realCoordX + (hexEdgesX[i] * hexWidth) + offsetX;
        }
        return result;
    }

    /**
     * Récupère les composantes en Y des coordonnées d'un hexagone à afficher
     *
     * @param index   Indice de l'hexagone à afficher
     * @param offsetY Décalage en Y de la grille hexagonale dans le canvas
     * @return Les 6 composantes en Y des 6 sommets de l'hexagone à afficher
     */
    public double[] getDrawableHexCoordY(int index, int offsetY) {
        double[] result = new double[6];
        double realCoordY = this.hexRealCoordCenterWithoutOffset.get(index).y;

        for (int i = 0; i < 6; i++) {
            result[i] = realCoordY + (hexEdgesY[i] * hexHeight) + offsetY;
        }
        return result;
    }

    /**
     * Récupère la composante en X des coordonnées du texte à afficher pour l'hexagone d'indice "index"
     *
     * @param index   Indice de l'hexagone à afficher
     * @param offsetX Décalage en X de la grille hexagonale dans le canvas
     * @return Composante en X de la position du texte
     */
    public double getTextPositionX(int index, int offsetX) {
        return this.hexRealCoordCenterWithoutOffset.get(index).x - (this.hexWidth / 2) + offsetX;
    }

    /**
     * Récupère la composante en Y des coordonnées du texte à afficher pour l'hexagone d'indice "index"
     *
     * @param index   Indice de l'hexagone à afficher
     * @param offsetY Décalage en Y de la grille hexagonale dans le canvas
     * @return Composante en Y de la position du texte
     */
    public double getTextPositionY(int index, int offsetY) {
        return this.hexRealCoordCenterWithoutOffset.get(index).y + offsetY;
    }

    /**
     * Récupère la région d'indice "index"
     *
     * @param index Indice de la région
     * @return Région correspondante
     */
    public Region getRegion(int index) {
        return regions.get(index);
    }

    /**
     * Récupère le nombre de régions/hexagones contenu dans cet objet
     *
     * @return Nombre d'hexagones
     */
    public int size() {
        return this.hexNormalizeCoordCenter.size();
    }


    /**
     * Recalcule la taille des hexagones et leurs centres.
     * Doit être appelé si la HexGrid, le zoom ou la taille du canvas change.
     */
    private void recalculateAfterNotify() {
        recalculateHexSize(_canvasHeight, _canvasWidth, _zoom);
        recalculateAllRealCenter();
    }

    /**
     * Recalcule tout les centres normalisés des hexagones.
     * Ne doit être appelé que si la HexGrid a changée.
     *
     * @param hexgrid Nouvelle grille hexagonale.
     */
    private void recalculateAllNormCenter(HexGrid hexgrid) {
        hexNormalizeCoordCenter.clear();
        regions.clear();
        hexGridHeight = hexgrid.getHeight();
        hexGridWidth = hexgrid.getWidth();

        for (int row = 0; row < hexGridHeight; row++) {
            for (int col = 0; col < hexGridWidth; col++) {
                if (hexgrid.getRegion(col, row) != null) {
                    this.hexNormalizeCoordCenter.add(this.calculateHexNormCenter(row, col));
                    this.regions.add(hexgrid.getRegion(col, row));
                }
            }
        }
    }

    /**
     * Recalcule tout les centres "rééls" des hexagones sans tenir compte du décalage de la grille dans le canvas.
     * Doit être appelé si la HexGrid, le zoom ou la taille du canvas change.
     */
    private void recalculateAllRealCenter() {
        this.hexRealCoordCenterWithoutOffset.clear();
        this.hexRealCoordCenterWithoutOffset.addAll(this.hexNormalizeCoordCenter.stream().map(this::calculateHexRealCenter).collect(Collectors.toList()));
    }

    /**
     * Recalcule la hauteur et la largeur des hexagones.
     * Doit être appelé si la HexGrid, le zoom ou la taille du canvas change.
     *
     * @param canvasHeight Nouvelle hauteur de canvas
     * @param canvasWidth  Nouvelle largeur de canvas
     * @param zoom         Nouveau zoom
     */
    private void recalculateHexSize(double canvasHeight, double canvasWidth, double zoom) {
        if (canvasHeight / (this.hexGridHeight * 0.75 + 0.25) > canvasWidth / (this.hexGridWidth)) {
            this.hexHeight = canvasHeight / ((3.0 / 4.0) * this.hexGridHeight + 0.25) * zoom;
            this.hexWidth = (Math.sqrt(3.0)) / 2.0 * hexHeight;
        } else {
            this.hexWidth = canvasWidth / (this.hexGridWidth + 0.5) * zoom;
            this.hexHeight = hexWidth * 2.0 / (Math.sqrt(3.0));
        }
    }

    /**
     * Calcule le centre normalisé d'un hexagone selon sa place dans la HexGrid
     *
     * @param row Indice de ligne de l'hexagone dans la HexGrid
     * @param col Indice de colonne de l'hexagone dans la HexGrid
     * @return Centre normalisé de l'hexagone
     */
    private Point calculateHexNormCenter(int row, int col) {
        double decalage = 0.0;
        if (row % 2 == 0) decalage = 0.5;

        double hexPosX = col + 0.5 + decalage;
        double hexPosY = 0.75 * row + 0.5;

        return new Point(hexPosX, hexPosY);
    }

    /**
     * Calcule le centre "réél" d'un hexagone.
     * Cette méthode utilise le centre normalisé de l'hexagone (qui doit avoir été calculé précédemment) et ne pas compte du décalage.
     *
     * @param normCenter Centre normalisé de cet hexagone
     * @return Centre réel de cet hexagone
     */
    private Point calculateHexRealCenter(Point normCenter) {
        double hexPosX = normCenter.x * this.hexWidth + (this.hexGridWidth / 2) * (1 - this._zoom);
        double hexPosY = normCenter.y * this.hexHeight + (this.hexGridHeight / 2) * (1 - this._zoom);

        return new Point(hexPosX, hexPosY);
    }

    public List<Point> getHexNormalizeCoordCenter() {
        return hexNormalizeCoordCenter;
    }

}
