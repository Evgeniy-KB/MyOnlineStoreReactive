package task6.MyOnlineStoreReactive.model;

import org.apache.commons.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.ArrayList;

@Table(name = "products")
public class Product {
    @Id
    private Long id;

    @Column("title")
    private String title;

    @Column("picture")
    private byte[] picture;

    @Column("description")
    private String description;

    @Column("price")
    private int price;

    @Transient
    private List<ProductOrder> productOrders = new ArrayList<>();

    public Product(){}

    public Product(Long id, String title, byte[] picture, String description, int price){
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.price = price;
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.getPicture());
    }


    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public byte[] getPicture(){
        return this.picture;
    }

    public void setPicture(byte[] picture){
        this.picture = picture;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getPrice(){
        return this.price;
    }

    public void setPrice(int price){ this.price = price; }



}
