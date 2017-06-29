package com.example.gustavodelgado.proyectofinal.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gustavodelgado on 29/06/17.
 */

public class OperadoresModel {
    private String address,name,phone,imagen,idCity,website,idOperador,email;


    OperadoresModel() {
    }

    public OperadoresModel(String address,String name, String phone,  String website, String imagen, String idCity, String idOperador,String email) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.imagen = imagen;
        this.idCity = idCity;
        this.idOperador = idOperador;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(String idOperador) {
        this.idOperador = idOperador;
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("name", name);
        result.put("phone",phone);
        result.put("website",website);
        result.put("imagen",imagen);
        result.put("idCity",idCity);
        result.put("idOperador",idOperador);
        result.put("email",email);
        return result;
    }
}
