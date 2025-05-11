package pl.sankouski;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length!=2) {
            System.out.println("Usage: java -jar application.jar PATH/orders.json PATH/paymentmethods.json");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        List<Order> orders = Arrays.asList(mapper.readValue(new File(args[0]), Order[].class));
        List<PaymentMethod> paymentMethods =
                Arrays.asList(mapper.readValue(new File(args[1]), PaymentMethod[].class));

        PaymentOptimization optimizer = new PaymentOptimization(orders, paymentMethods);

        Result result = optimizer.optimize();

        result.print();
    }
}