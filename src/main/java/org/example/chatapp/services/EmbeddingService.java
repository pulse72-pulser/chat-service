package org.example.chatapp.services;

import dev.langchain4j.data.embedding.Embedding;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;

import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public double[] generateEmbedding(String text) {
        Response<Embedding> response = embeddingModel.embed(text);
        float[] floatVector = response.content().vector();
        return convertFloatArrayToDoubleArray(floatVector);
    }

    private double[] convertFloatArrayToDoubleArray(float[] floatArray) {
        double[] doubleArray = new double[floatArray.length];
        for (int i = 0; i < floatArray.length; i++) {
            doubleArray[i] = floatArray[i];
        }
        return doubleArray;
    }
}
