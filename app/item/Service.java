package item;

public class Service {
    private String name;
    private double cost;
    private double reliability;
    private double performance;
    private double availability;

    /**
     * For dynamic generation
     * @param cost
     * @param reliability
     * @param performance
     * @param availability
     */
    public Service(double cost, double reliability, double performance, double availability) {
        this.cost = cost;
        this.reliability = reliability;
        this.performance = performance;
        this.availability = availability;
    }

    /**
     * For initialization and store its name
     * @param name          Name of the Service
     * @param cost
     * @param reliability
     * @param performance
     * @param availability
     */
    public Service(String name, double cost, double reliability, double performance, double availability) {
        this.name = name;
        this.cost = cost;
        this.reliability = reliability;
        this.performance = performance;
        this.availability = availability;
    }

    public double getCost() {
        return cost;
    }

    public double getReliability() {
        return reliability;
    }

    public double getPerformance() {
        return performance;
    }

    public double getAvailability() {
        return availability;
    }

    public String getName(){
        return name;
    }

    /**
     * Generate a calculatable service with correct cost and performance
     * @param cost
     * @param performance
     * @return
     */
    public Service generateCalculatable(double cost, double performance){
        double realCost = (1-(this.cost/cost))/Weight.nomalization;
        double realPerformance = (1-(this.performance/performance))/Weight.nomalization;
        return new Service(realCost, this.reliability, realPerformance, this.availability);
    }
}
