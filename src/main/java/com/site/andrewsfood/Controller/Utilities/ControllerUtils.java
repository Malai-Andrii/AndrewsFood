package com.site.andrewsfood.Controller.Utilities;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    public static int[] generateCombinations(int[] arr, int M, int N)
    {
        if (arr == null)
        {
            arr = new int[M];
            for (int i = 0; i < M; i++)
                arr[i] = i + 1;
            return arr;
        }
        for (int i = M - 1; i >= 0; i--)
            if (arr[i] < N - M + i + 1)
            {
                arr[i]++;
                for (int j = i; j < M - 1; j++)
                    arr[j + 1] = arr[j] + 1;
                return arr;
            }
        return null;
    }

    public static List<double[]> generateWages(int x) {
        List<double[]> listWages = new LinkedList<>();
        double[] wages = {1.0, 0.8, 0.6, 0.4, 0.2};
        int[] items = new int[x];

        for (int i = 0; i < Math.pow(5, x); i++) {
            int itemPoint = items.length - 1;
            items[itemPoint]++;
            if (items[itemPoint] == 5) {
                items[itemPoint] = 0;
                items[itemPoint-1]++;
                for (int j = itemPoint - 1; j >= 0; j--) {
                    if (items[0] == 5) {
                        items[0] = 0;
                        break;
                    }
                    else if (items[j] == 5) {
                        items[j] = 0;
                        items[j-1]++;
                    }
                    else break;
                }
            }
            double[] currentArray = new double[x];
            for (int k = 0; k < x; k++) {
                currentArray[k] = (wages[items[k]]);
            }

            listWages.add(currentArray);
        }
        return listWages;
    }
}