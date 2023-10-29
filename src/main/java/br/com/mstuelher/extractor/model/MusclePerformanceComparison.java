package br.com.mstuelher.extractor.model;

public class MusclePerformanceComparison {

    private CommonParameter hipAbductors;
    private CommonParameter hipAdductors;
    private CommonParameter kneeFlexors;
    private CommonParameter kneeExtenders;
    private CommonParameter ankleEversors;
    private CommonParameter ankleInverters;


    public CommonParameter getHipAbductors() {
        return hipAbductors;
    }

    public void setHipAbductors(CommonParameter hipAbductors) {
        this.hipAbductors = hipAbductors;
    }

    public CommonParameter getHipAdductors() {
        return hipAdductors;
    }

    public void setHipAdductors(CommonParameter hipAdductors) {
        this.hipAdductors = hipAdductors;
    }

    public CommonParameter getKneeFlexors() {
        return kneeFlexors;
    }

    public void setKneeFlexors(CommonParameter kneeFlexors) {
        this.kneeFlexors = kneeFlexors;
    }

    public CommonParameter getKneeExtenders() {
        return kneeExtenders;
    }

    public void setKneeExtenders(CommonParameter kneeExtenders) {
        this.kneeExtenders = kneeExtenders;
    }

    public CommonParameter getAnkleEversors() {
        return ankleEversors;
    }

    public void setAnkleEversors(CommonParameter ankleEversors) {
        this.ankleEversors = ankleEversors;
    }

    public CommonParameter getAnkleInverters() {
        return ankleInverters;
    }

    public void setAnkleInverters(CommonParameter ankleInverters) {
        this.ankleInverters = ankleInverters;
    }

    @Override
    public String toString() {
        return "MusclePerformanceComparison{" +
                "hipAbductors=" + hipAbductors +
                ", hipAdductors=" + hipAdductors +
                ", kneeFlexors=" + kneeFlexors +
                ", kneeExtenders=" + kneeExtenders +
                ", ankleEversors=" + ankleEversors +
                ", ankleInverters=" + ankleInverters +
                '}';
    }
}
