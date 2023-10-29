package br.com.mstuelher.extractor.model;

public class UserInfo {

    private String name;
    private String dtNasc;
    private String stature;
    private String weight;
    private String gender;

    public UserInfo(String name, String dtNasc, String stature, String weight, String gender) {
        this.name = name;
        this.dtNasc = dtNasc;
        this.stature = stature;
        this.weight = weight;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(String dtNasc) {
        this.dtNasc = dtNasc;
    }

    public String getStature() {
        return stature;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", dtNasc='" + dtNasc + '\'' +
                ", stature='" + stature + '\'' +
                ", weight='" + weight + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
