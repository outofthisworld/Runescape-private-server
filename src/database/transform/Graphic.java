package database.transform;

public class Graphic {

    private int graphicId;
    private int height;

    public Graphic(int graphicId, int height) {
        this.graphicId = graphicId;
        this.height = height;
    }

    public Graphic(int graphicId) {
        this(graphicId, 0);
    }

    public int getGraphicId() {
        return graphicId;
    }

    public int getHeight() {
        return height;
    }
}
