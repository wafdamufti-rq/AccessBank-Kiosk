package com.kiosk.accessbank;


// Changes made to the model interface
// This interface was renamed from Classifier to SimilarityClassifier
public interface SimilarityClassifier {

    // added method for registering samples into the dataset
    void register(String name, Recognition recognition);
    public class Recognition {

        // now we use distance instead of confidence (lower is better, eg should be zero for the same input)
        private final Float distance;

        // extra field to store any data (we are going to use it to store the embeedings)
        private Object extra;

        public Recognition(Float distance, Object extra) {
            this.distance = distance;
            this.extra = extra;
        }
    }

}


