package controllers;

import GAs.BestCombinationFitnessFunction;
import GAs.MaximazingScore;
import fileReaders.DataFileReader;
import fileReaders.TextFileReader;
import fileReaders.WordFileReader;
import item.Service;
import item.Weight;
import org.jgap.Gene;
import org.jgap.IChromosome;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.util.HashMap;
import java.util.Map;

public class ApiController extends Controller {

    public Result index() {
        Map<String, Object> result1 = getResultMap("Integer","text", false);
        Map<String, Object> result2 = getResultMap("Integer","text", true);
        return ok(views.html.index.render(result1, result2));
    }

    /**
     * Call Genetics algorithm.
     * @param geneType      | What type of gene to use Integer/Boolean Default to Integer
     * @param fileType      | What type of file to use word/text Default to word
     * @param doubleNormal  | Use divide by 2 for normalization of cost and performance
     * @return
     */
    public Result ga(String geneType, String fileType, boolean doubleNormal){
        Map<String, Object> result = getResultMap(geneType,fileType,doubleNormal);
        if(result==null) return status(204);
        return ok(Json.toJson(result));
    }


    private Map<String, Object> getResultMap(String geneType, String fileType, boolean doubleNormal){
        Weight.setWeights(0.35,0.2,0.35,0.1, doubleNormal?2:1);
        Weight.c=0; Weight.a=0;Weight.r=0;Weight.p=0;
        Weight.nomalization = doubleNormal?2:1;
        DataFileReader fr;
        if(!fileType.equals("word")){
            fr=new TextFileReader();
        }
        else {
            fr=new WordFileReader();
        }
        BestCombinationFitnessFunction fitnessFunction =
                new BestCombinationFitnessFunction(fr, geneType);
        IChromosome bestSolution = MaximazingScore.doGA(fitnessFunction, geneType);
        Service[] services = fitnessFunction.serviceExtractor(bestSolution);
        if(bestSolution==null) return null;
        Map<String, Object> result = new HashMap<>();
        result.put("bestScore", bestSolution.getFitnessValue());
        result.put("SC1", services[0]);
        result.put("SC2", services[1]);
        result.put("SC3", services[2]);
        return result;
    }

}
