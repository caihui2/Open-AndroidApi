package com.lewa.chyang.obscure.utils;

public class GaussianBlurFunction {

    public static float[] getWeights(int radius) {

        if (radius <= 0) {
            return new float[]{1.0f};
        }

        int samplerCount = radius;
        float[] weights = new float[samplerCount];
        if (samplerCount == 1) {
            weights[0] = 1;
            return weights;
        }

        float sigma = samplerCount / 3.0f;
        double sumWeights = 0.0;
        for (int i = 0; i < samplerCount; ++i) {
            weights[i] = kernel(i, sigma);
            sumWeights += weights[i];
        }
        sumWeights = 2 * sumWeights - weights[0];

        for (int i = 0; i < samplerCount; ++i) {
            weights[i] /= sumWeights;
        }

        return weights;
    }

    private static float kernel(double distance, double sigma) {
        double sigma2 = sigma * sigma;
        double result = Math.exp(-0.5 * distance * distance / sigma2) / Math.sqrt(2.0 * Math.PI * sigma2);
        return (float) result;
    }
}

