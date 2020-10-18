package com.quy.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "datetime")
    private String dateTime;

    @ColumnInfo(name = "subtitle")
    private String subTitle;

    @ColumnInfo(name = "note_text")
    private String noteText;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "web_link")
    private String webLink;

    public Note(int id, String title, String dateTime, String subTitle, String noteText, String imagePath, String color, String webLink) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.subTitle = subTitle;
        this.noteText = noteText;
        this.imagePath = imagePath;
        this.color = color;
        this.webLink = webLink;
    }

    public Note(String title, String dateTime, String subTitle, String noteText, String imagePath, String color, String webLink) {
        this.title = title;
        this.dateTime = dateTime;
        this.subTitle = subTitle;
        this.noteText = noteText;
        this.imagePath = imagePath;
        this.color = color;
        this.webLink = webLink;
    }

    public Note(String title, String dateTime, String subTitle, String noteText) {
        this.title = title;
        this.dateTime = dateTime;
        this.subTitle = subTitle;
        this.noteText = noteText;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
