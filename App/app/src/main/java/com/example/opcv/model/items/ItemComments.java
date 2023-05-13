package com.example.opcv.model.items;

public class ItemComments {
    private String comment, nameCommentator, idUSer;

    public ItemComments(String comment, String nameCommentator, String idUSer) {
        this.comment = comment;
        this.nameCommentator = nameCommentator;
        this.idUSer = idUSer;
    }

    public String getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(String idUSer) {
        this.idUSer = idUSer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNameCommentator() {
        return nameCommentator;
    }

    public void setNameCommentator(String nameCommentator) {
        this.nameCommentator = nameCommentator;
    }
}
