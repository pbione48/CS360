package edu.snhu.cs360.inventoryapp;

public class Item {
    private int id;
    private String name;
    private int quantity;
    private String category;
    private String notes;

    public Item(int id, String name, int quantity, String category, String notes) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.notes = notes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public String getNotes() { return notes; }

    // Optional but good practice
    @Override
    public String toString() {
        return "Item{id=" + id + ", name='" + name + "', quantity=" + quantity +
               ", category='" + category + "', notes='" + notes + "'}";
    }
}
