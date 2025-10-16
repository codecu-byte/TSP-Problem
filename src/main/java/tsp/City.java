package tsp;

public class City {
    private int id;
    private double x;
    private double y;

    public City(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Only use for symmetric TSP; for ATSP use distance matrix
    public double distanceTo(City other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return id + " (" + x + "," + y + ")";
    }
}
