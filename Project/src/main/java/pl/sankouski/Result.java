package pl.sankouski;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Result {
    private Map<String, BigDecimal> result = new HashMap<>();

    public Result(Map<String, BigDecimal> result) {
        this.result = result;
    }

    public void add(String paymentMethod, BigDecimal amount){
        // get the current amount - otherwise start from 0
        BigDecimal now = result.getOrDefault(paymentMethod, BigDecimal.ZERO);
        //add new amount to the existing one
        BigDecimal new_amount = now.add(amount);
        //update in the map
        result.put(paymentMethod, new_amount);
    }

    public void print(){
        for (Map.Entry<String, BigDecimal> entrySet : result.entrySet()){

            BigDecimal amount = entrySet.getValue().setScale(2, RoundingMode.HALF_UP);  //round to 2 dec places
            System.out.println(entrySet.getKey() + " " + amount);
        }
    }

    public Map<String, BigDecimal> getResults() {
        return new HashMap<>(result);
    }

}
