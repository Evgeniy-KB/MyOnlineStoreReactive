package task7.dto;

import org.apache.commons.codec.binary.Base64;

public class ProductDto {
    private Long id;
    private String title;
    private byte[] picture;
    private String description;
    private Long quantity = 0L;
    private Long price;

    public ProductDto(){}

    public ProductDto(Long id, String title, byte[] picture, String description, Long quantity, Long price){
        this(id, title, picture, description, price);
        this.quantity = quantity;
    }

    public ProductDto(Long id, String title, byte[] picture, String description, Long price){
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.description = description;
        this.price = price;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.getPicture());
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

    public void setQuantity(Long quantity){
        this.quantity = quantity;
    }

    public Long getQuantity(){
        return this.quantity;
    }

    public Long getPrice(){
        return this.price;
    }

    public void setPrice(Long price){ this.price = price; }

    public Long getTotalPrice(){
        return this.quantity * this.price;
    }

}
