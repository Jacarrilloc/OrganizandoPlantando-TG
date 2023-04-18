package com.example.opcv.item_list;

public class ItemComments {
    private String comment, nameCommentator;

    public ItemComments(String comment, String nameCommentator) {
        this.comment = comment;
        this.nameCommentator = nameCommentator;
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
