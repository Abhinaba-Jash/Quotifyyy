package com.example.qoute;

public class ModelData {

    String author,timestamp,quote;
    public ModelData(String quote,String author,String timestamp){

        this.quote=quote;
        this.author=author;
        this.timestamp=timestamp;

    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
