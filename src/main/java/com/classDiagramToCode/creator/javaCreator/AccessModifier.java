package com.classDiagramToCode.creator.javaCreator;

public enum AccessModifier {
    PUBLIC("public"),
    DEFAULT(""),
    PROTECTED("protected"),
    PRIVATE("private");

    private String text;

    private AccessModifier(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }


}
