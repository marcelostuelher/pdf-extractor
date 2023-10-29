package br.com.mstuelher.extractor.model;

import org.apache.commons.lang.math.NumberUtils;

import java.util.List;

public class CommonParameter {

    private String rightForce;
    private String leftForce;
    private String differenceForce;

    private String rightWork;
    private String leftWork;
    private String differenceWork;

    private String rightStrength;
    private String leftStrength;
    private String differenceStrength;

    public String getRightForce() {
        return rightForce;
    }

    public CommonParameter(List<String> list, TestType type) {
        switch (type) {
            case HIP_ABDUCTORS:
                this.rightForce = list.get(50);
                this.leftForce = list.get(89);
                this.differenceForce = this.numberAdapter(list.get(20));
                this.rightWork = list.get(42);
                this.leftWork = list.get(81);
                this.differenceWork = this.numberAdapter(list.get(18));
                this.rightStrength = list.get(22);
                this.leftStrength = list.get(61);
                this.differenceStrength = this.numberAdapter(list.get(10));
                break;
            case HIP_ADDUCTORS:
                this.rightStrength = list.get(23);
                this.leftStrength = list.get(62);
                this.differenceStrength = this.numberAdapter(list.get(11));
                this.leftWork = list.get(82);
                this.rightWork = list.get(43);
                this.differenceWork = this.numberAdapter(list.get(19));
                this.leftForce = list.get(51);
                this.rightForce = list.get(90);
                this.differenceForce = this.numberAdapter(list.get(21));
                break;
            case KNEE_EXTENDERS:
                this.rightStrength = list.get(126);
                this.leftStrength = list.get(145);
                this.differenceStrength = this.numberAdapter(list.get(114));

                this.leftWork = list.get(185);
                this.rightWork = list.get(146);
                this.differenceWork = this.numberAdapter(list.get(122));

                this.rightForce = list.get(154);
                this.leftForce = list.get(193);
                this.differenceForce = this.numberAdapter(list.get(118));
                break;
            case KNEE_FLEXORS:
                this.rightStrength = list.get(127);
                this.leftStrength = list.get(166);
                this.differenceStrength = this.numberAdapter(list.get(115));

                this.rightWork = list.get(147);
                this.leftWork = list.get(186);
                this.differenceWork = this.numberAdapter(list.get(123));

                this.rightForce = list.get(155);
                this.leftForce = list.get(194);
                this.differenceForce = this.numberAdapter(list.get(125));
                break;
            case ANKLE_EVERSORS:
                this.rightStrength = list.get(228);
                this.leftStrength = list.get(267);
                this.differenceStrength = this.numberAdapter(list.get(216));

                this.rightWork = list.get(248);
                this.leftWork = list.get(287);
                this.differenceWork = this.numberAdapter(list.get(224));

                this.rightForce = list.get(256);
                this.leftForce = list.get(295);
                this.differenceForce = this.numberAdapter(list.get(226));
                break;
            case ANKLE_INVERTERS:
                this.rightStrength = list.get(229);
                this.leftStrength = list.get(268);
                this.differenceStrength = this.numberAdapter(list.get(217));

                this.rightWork = list.get(249);
                this.leftWork = list.get(80);
                this.differenceWork = this.numberAdapter(list.get(225));

                this.rightForce = list.get(257);
                this.leftForce = list.get(296);
                this.differenceForce = this.numberAdapter(list.get(227));
                break;
        }

    }

    public String numberAdapter(String number) {
        return NumberUtils.isNumber(number) && Double.parseDouble(number) < 0 ?
                number : "+".concat(number);
    }


    public void setRightForce(String rightForce) {
        this.rightForce = rightForce;
    }

    public String getLeftForce() {
        return leftForce;
    }

    public void setLeftForce(String leftForce) {
        this.leftForce = leftForce;
    }

    public String getDifferenceForce() {
        return differenceForce;
    }

    public void setDifferenceForce(String differenceForce) {
        this.differenceForce = differenceForce;
    }

    public String getRightWork() {
        return rightWork;
    }

    public void setRightWork(String rightWork) {
        this.rightWork = rightWork;
    }

    public String getLeftWork() {
        return leftWork;
    }

    public void setLeftWork(String leftWork) {
        this.leftWork = leftWork;
    }

    public String getDifferenceWork() {
        return differenceWork;
    }

    public void setDifferenceWork(String differenceWork) {
        this.differenceWork = differenceWork;
    }

    public String getRightStrength() {
        return rightStrength;
    }

    public void setRightStrength(String rightStrength) {
        this.rightStrength = rightStrength;
    }

    public String getLeftStrength() {
        return leftStrength;
    }

    public void setLeftStrength(String leftStrength) {
        this.leftStrength = leftStrength;
    }

    public String getDifferenceStrength() {
        return differenceStrength;
    }

    public void setDifferenceStrength(String differenceStrength) {
        this.differenceStrength = differenceStrength;
    }
}
