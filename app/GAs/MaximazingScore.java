package GAs;

import fileReaders.DataFileReader;
import fileReaders.TextFileReader;
import fileReaders.WordFileReader;
import item.Service;
import item.Weight;
import org.jgap.*;
import org.jgap.audit.*;
import org.jgap.impl.*;

public class MaximazingScore {
    private static final int MAX_ALLOWED_EVOLUTIONS = 150;

    public static void main(String[] args){
        Weight.c=0.35;
        Weight.r=0.10;
        Weight.p=0.20;
        Weight.a=0.35;
        String fileType = "text", geneType = "Integer";
        if(args.length>=1){
            geneType=args[0];
        }
        if(args.length>=2){
            fileType=args[1];
        }
        DataFileReader fr;
        if(fileType.equals("text")){
            fr=new TextFileReader();
        }
        else {
            fr=new WordFileReader();
        }

        BestCombinationFitnessFunction fitnessFunction =
                new BestCombinationFitnessFunction(fr, geneType);
        try{
            IChromosome bestSolution = doGA(fitnessFunction, geneType);
            outPutResult(bestSolution, fitnessFunction);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Start the Genetic Algorithms
     * @param fitnessFunction       | Injected fitness function
     * @param geneType              | Type of gene
     * @return                      A Chromosome containing best result
     * @throws InvalidConfigurationException
     */
    public static IChromosome doGA(FitnessFunction fitnessFunction, String geneType) {
        try{
            Configuration.reset();
            Configuration conf = new DefaultConfiguration();
            conf.setPreservFittestIndividual(true);
            conf.setFitnessFunction(fitnessFunction);

            Gene[] serviceGene = getServiceGene(geneType, conf);
            IChromosome serviceChromosome = new Chromosome(conf, serviceGene);
            conf.setSampleChromosome(serviceChromosome);

            conf.setPopulationSize(5*3*8);
            Genotype population = Genotype.randomInitialGenotype(conf);
            for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
                //Borrowed from example, just in case
                if (!uniqueChromosomes(population.getPopulation())) {
                    throw new RuntimeException("Invalid state in generation "+i);
                }
                population.evolve();
            }
            IChromosome bestSolutionSoFar = population.getFittestChromosome();
            return bestSolutionSoFar;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Print the result in terminal
     * @param bestSolution
     * @param ff
     */
    public static void outPutResult(IChromosome bestSolution, BestCombinationFitnessFunction ff){
        double score = bestSolution.getFitnessValue();
        Service[] services = ff.serviceExtractor(bestSolution);
        System.out.println("Best Score: " + score);
        System.out.println("SC1: "+ services[0].getName());
        System.out.println("SC2: "+ services[1].getName());
        System.out.println("SC3: "+ services[2].getName());
    }

    /**
     * Get different genes for service based on required geneType
     * @param geneType
     * @param conf
     * @return Gene[]
     */
    private static Gene[] getServiceGene(String geneType, Configuration conf){
        //Integer Gene
        if(!geneType.equals("Boolean")){
            Gene[] serviceGene = new Gene[3];
            try{
                serviceGene[0] = new IntegerGene(conf, 0, 4);
                serviceGene[1] = new IntegerGene(conf, 0, 2);
                serviceGene[2] = new IntegerGene(conf, 0, 7);
                return serviceGene;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        //Boolean Gene
        else {
            Gene[] serviceGene = new Gene[8];
            try{
                for(int i=0;i<8;i++){
                    serviceGene[i] = new BooleanGene(conf);
                }
                return serviceGene;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    //Borrowed from example, not sure if it has ever been used
    public static boolean uniqueChromosomes(Population a_pop) {
        // Check that all chromosomes are unique
        for(int i=0;i<a_pop.size()-1;i++) {
            IChromosome c = a_pop.getChromosome(i);
            for(int j=i+1;j<a_pop.size();j++) {
                IChromosome c2 =a_pop.getChromosome(j);
                if (c == c2) {
                    return false;
                }
            }
        }
        return true;
    }
}
