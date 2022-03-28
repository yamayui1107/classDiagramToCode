package com.classDiagramToCode.creator.javaCreator;

public enum ClassType {
    CLASS("class"),
    ABSTRACT_CLASS("abstract class"),
    INTERFACE("interface"),
    ENUM("Enum");

    private String text;

    private ClassType(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
