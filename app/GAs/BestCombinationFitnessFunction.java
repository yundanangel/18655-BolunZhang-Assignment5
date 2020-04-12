package GAs;

import fileReaders.DataFileReader;
import item.Service;
import item.Weight;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

public class BestCombinationFitnessFunction extends FitnessFunction {
    private Service[][] services;
    private String geneType;

    /**
     * Intialize the function with injected DataFileReader
     * @param fr            | DataFileReader Text or Doc(TODO)
     * @param geneType      | Type of gene Integer vs Boolean
     */
    public BestCombinationFitnessFunction(DataFileReader fr, String geneType) {
        this.services = fr.readFile();
        this.geneType = geneType;
//        printServices();
    }

    /**
     * Print the services to see if it works as planned
     */
    public void printServices(){
        System.out.println("Printing Services");
        for(Service[] sl: this.services){
            for(Service s:sl){
                System.out.println(s.getName()+": "+ s.getCost() +", "+ s.getReliability()+", "+s.getPerformance()+", "+s.getAvailability());
            }
        }
        System.out.println("Printing Services Done");

    }

    /**
     * Evaluate score
     * @param aServiceSet
     * @return
     */
    public double evaluate(IChromosome aServiceSet) {
        Service[] ss = serviceExtractor(aServiceSet);
        if(ss==null) return 0;
        double totalCost = 0, totalPerformance = 0;
        for(Service s : ss){
            totalCost+= s.getCost();
            totalPerformance+=s.getPerformance();
        }
        Service s1 = ss[0].generateCalculatable(totalCost,totalPerformance);
        Service s2 = ss[1].generateCalculatable(totalCost,totalPerformance);
        Service s3 = ss[2].generateCalculatable(totalCost,totalPerformance);

        Service w1 = calculateSequence(s1, s2);

        Service w2 = calculateParallel(w1, s1);

        Service w3 = calculateSequence(w2, s3);

        return calculateScore(w3);
    }


    /**
     * Extract s1, s2, s3 from chromosome
     * @param aServiceSet
     * @return
     */
    public Service[] serviceExtractor(IChromosome aServiceSet){
        Service[] result;
        if(!this.geneType.equals( "Boolean")){
            result= integerServiceExtractor(aServiceSet);
        }
        else result = booleanServiceExtractor(aServiceSet);
        return result;
    }

    /**
     * Extract services using boolean gene
     * @param aServiceSet
     * @return
     */
    private Service[] booleanServiceExtractor(IChromosome aServiceSet){
        Gene[] genes = aServiceSet.getGenes();
        int c1Index = 0, c2Index = 0, c3Index=0;
        //index for sc1
        c1Index = c1Index + (((Boolean)genes[0].getAllele())?4:0);;
        c1Index = c1Index + (((Boolean)genes[1].getAllele())?2:0);;
        c1Index = c1Index + (((Boolean)genes[2].getAllele())?1:0);

        //index for sc2
        c2Index = c2Index + (((Boolean)genes[3].getAllele())?2:0);;
        c2Index = c2Index + (((Boolean)genes[4].getAllele())?1:0);;

        //index for sc3
        c3Index = c3Index + (((Boolean)genes[5].getAllele())?4:0);;
        c3Index = c3Index + (((Boolean)genes[6].getAllele())?2:0);;
        c3Index = c3Index + (((Boolean)genes[7].getAllele())?1:0);

        if(c1Index>=this.services[0].length|| c2Index>=this.services[1].length||c3Index>=this.services[2].length){
            return null;
        }

        Service[] result = new Service[3];
        result[0] = this.services[0][c1Index];
        result[1] = this.services[1][c2Index];
        result[2] = this.services[2][c3Index];
        return result;
    }

    /**
     * Extract services using integer gene
     * @param aServiceSet
     * @return
     */
    private Service[] integerServiceExtractor(IChromosome aServiceSet){
        Service[] result = new Service[3];
        for(int i=0; i<3; i++){
            Integer order = (Integer)aServiceSet.getGene(i).getAllele();
            result[i] = this.services[i][order];
        }
        return result;
    }

    /**
     * Calculate score of the service
     * @param s
     * @return
     */
    private double calculateScore(Service s){
        return Weight.c*s.getCost()+Weight.p*s.getPerformance()+Weight.a*s.getAvailability()+Weight.r*s.getReliability();
    }

    /**
     * Generate new combined service with sequence relationships
     * @param s1
     * @param s2
     * @return  new Service
     */
    private Service calculateSequence(Service s1, Service s2){
        return new Service(
                s1.getCost()+s2.getCost(),
                Math.min(s1.getReliability(), s2.getReliability()),
                s1.getPerformance()+s2.getPerformance(),
                Math.min(s1.getAvailability(),s2.getAvailability())
        );
    }


    /**
     * Generate new combined service with Parallel relationships
     * @param s1
     * @param s2
     * @return  new Service
     */
    private Service calculateParallel(Service s1, Service s2){
        return new Service(
                s1.getCost()+s2.getCost(),
                s1.getReliability() * s2.getReliability(),
                Math.max(s1.getPerformance(), s2.getPerformance()),
                s1.getAvailability() * s2.getAvailability()
        );
    }
}
