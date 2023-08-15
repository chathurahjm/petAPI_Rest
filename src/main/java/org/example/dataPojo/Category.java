package org.example.dataPojo;

public class Category {
    private long id;
    private String name;

    public Category(int id, String categoryName) {
    }

    public Category() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
