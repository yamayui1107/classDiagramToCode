package com.classDiagramToCode.creator.javaCreator;

public enum PrimitiveType {
    BOOLEAN("boolean","false"),
    CHAR("char",""),
    SHORT("short","0"),
    INT("int","0"),
    LONG("long","0"),
    FLOAT("float","0.0"),
    DOUBLE("double","0.0");

    private String text;
    private String firstValue;

    private PrimitiveType(String text, String firstValue){
        this.text = text;
        this.firstValue = firstValue;
    }

    public String getText(){
        return text;
    }
    public String getFirstValue(){
        return firstValue;
    }

    public static boolean isMember(String str){
        for(PrimitiveType primitiveType : PrimitiveType.values()){
            if(primitiveType.text.equals(str)) return true;
        }
        return false;
    }

    public static String getFirstValueByTypeName(String typeName){
        for(PrimitiveType primitiveType : PrimitiveType.values()){
            if (primitiveType.text.equals(typeName)) return primitiveType.getFirstValue();
        }
        return null;
    }
}
