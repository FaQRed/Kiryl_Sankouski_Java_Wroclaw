package pl.sankouski;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentOptimization {

    private final List<Order> orders;
    private final List<PaymentMethod> methods;
    private final PaymentMethod pointsMethod;
    private final Map<String, BigDecimal> methodLimits = new HashMap<>();

    public PaymentOptimization(List<Order> orders, List<PaymentMethod> methods) {
        this.orders = orders;
        this.methods = methods;
        this.pointsMethod = methods.stream()
                .filter(m -> m.getId().equals("PUNKTY"))
                .findFirst()
                .orElse(null);
        for (PaymentMethod method : methods) {
            methodLimits.put(method.getId(), method.getLimit());
        }
    }

    private PaymentMethod selectCardMethod(BigDecimal requiredAmount) {
        for (PaymentMethod method : methods) {
            if (!method.getId().equals("PUNKTY") && methodLimits.get(method.getId()).compareTo(requiredAmount) >= 0) {
                return method;
            }
        }
        return null;
    }
    private BigDecimal applyDiscount(BigDecimal value, BigDecimal discountPercent) {
        return value.multiply(BigDecimal.ONE.subtract(discountPercent.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)));
    }

    private static class PaymentOption {
        String methodId;
        BigDecimal effectiveCost;
        BigDecimal amountUsed;

        public PaymentOption(String methodId, BigDecimal effectiveCost, BigDecimal amountUsed) {
            this.methodId = methodId;
            this.effectiveCost = effectiveCost;
            this.amountUsed = amountUsed;
        }
    }



    public Result optimize(){
        Map<String, BigDecimal> results = new HashMap<>();
        Result result = new Result(results);


        for (Order order : orders){
            BigDecimal value = order.getValue();
            String bestMethodId = null;
            BigDecimal minEffectiveCost = null;

            List<PaymentOption> options = new ArrayList<>();


            //pay with full points
            if (pointsMethod != null && methodLimits.get("PUNKTY").compareTo(value) >= 0) {
                BigDecimal discounted = applyDiscount(value, pointsMethod.getDiscount());
                options.add(new PaymentOption("PUNKTY", discounted, value));
            }

            // partial points (10%) + 10% discount
            if (pointsMethod != null) {
                BigDecimal tenPercent = value.multiply(new BigDecimal("0.10"));
                if (methodLimits.get("PUNKTY").compareTo(tenPercent) >= 0) {
                    BigDecimal discounted = applyDiscount(value, new BigDecimal(10));
                    options.add(new PaymentOption("PUNKTY_PARTIAL", discounted, tenPercent));
                }
            }

            //other options
            for (PaymentMethod method : methods) {
                if (method.getId().equals("PUNKTY")) continue;
                if (order.getPromotions() != null && order.getPromotions().contains(method.getId())) {
                    BigDecimal limit = methodLimits.get(method.getId());
                    if (limit.compareTo(value) >= 0) {
                        BigDecimal discounted = applyDiscount(value, method.getDiscount());
                        options.add(new PaymentOption(method.getId(), discounted, value));
                    }
                } else if (order.getPromotions() == null || order.getPromotions().isEmpty()) {
                    if (methodLimits.get(method.getId()).compareTo(value) >= 0) {
                        options.add(new PaymentOption(method.getId(), value, value));
                    }
                }
            }

            // choose the best option
            for (PaymentOption option : options) {
                if (minEffectiveCost == null || option.effectiveCost.compareTo(minEffectiveCost) < 0) {
                    bestMethodId = option.methodId;
                    minEffectiveCost = option.effectiveCost;
                }
            }

            if (bestMethodId != null) {
                switch (bestMethodId) {
                    case "PUNKTY":
                        result.add("PUNKTY", minEffectiveCost);
                        methodLimits.put("PUNKTY", methodLimits.get("PUNKTY").subtract(minEffectiveCost));
                        break;
                    case "PUNKTY_PARTIAL":
                        BigDecimal partialPoints = value.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal discountedTotal = applyDiscount(value, new BigDecimal(10));
                        BigDecimal remainingToPay = discountedTotal.subtract(partialPoints).setScale(2, RoundingMode.HALF_UP);

                        result.add("PUNKTY", partialPoints);
                        methodLimits.put("PUNKTY", methodLimits.get("PUNKTY").subtract(partialPoints));

                        PaymentMethod cardMethod = selectCardMethod(remainingToPay);
                        if (cardMethod != null) {
                            result.add(cardMethod.getId(), remainingToPay);
                            methodLimits.put(cardMethod.getId(), methodLimits.get(cardMethod.getId()).subtract(remainingToPay));
                        }
                        break;
                    default:
                        result.add(bestMethodId, minEffectiveCost);
                        methodLimits.put(bestMethodId, methodLimits.get(bestMethodId).subtract(minEffectiveCost));
                        break;
                }
            }
        }
        return result;
    }



}
