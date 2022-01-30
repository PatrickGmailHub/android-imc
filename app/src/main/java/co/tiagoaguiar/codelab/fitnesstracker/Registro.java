package co.tiagoaguiar.codelab.fitnesstracker;

import java.util.Date;

public class Registro {

    String type;
    double response;
    String createdDate;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getResponse() {
        return response;
    }

    public void setResponse(double response) {
        this.response = response;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
