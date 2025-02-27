package DTOs;

public class Product {
    private String product_id;
    private String product_description;
    private String size;
    private double unit_price;
    private String supplier_id;

    public Product(String id, String description, String size, double price, String supplier_id) {
        this.product_id = id;
        product_description = description;
        this.size = size;
        unit_price = price;
        this.supplier_id = supplier_id;
    }

    public Product() { };

    public String getId() {
        return product_id;
    }

    public void setId(String id) {
        product_id = id;
    }

    public String getDescription() {
        return product_description;
    }

    public void setDescription(String description) {
        product_description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return unit_price;
    }

    public void setPrice(double price) {
        this.unit_price = price;
    }

    public String getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(String id) {
        supplier_id = id;
    }
}
