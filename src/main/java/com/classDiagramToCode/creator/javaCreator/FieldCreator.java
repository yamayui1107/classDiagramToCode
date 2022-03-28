package com.classDiagramToCode.creator.javaCreator;

import com.classDiagramToCode.creator.Creator;
import lombok.Builder;

@Builder
public class FieldCreator implements Creator {
    private AccessModifier accessModifier;
    private String typeName;
    private String memberName;
    private String implementedTypeName;

    public static class FieldCreatorBuilder {
        public FieldCreator build(){
            if(accessModifier == null){
                throw new IllegalArgumentException("アクセス修飾子が指定されていません");
            }
            if(memberName == null || memberName.equals("")) {
                throw new IllegalArgumentException("変数名が不明です");
            }

            return new FieldCreator(accessModifier,typeName,memberName,implementedTypeName);
        }
    }

    @Override
    public String create() {
        StringBuilder sb = new StringBuilder();

        sb.append("\t");
        if(AccessModifier.DEFAULT != accessModifier){
            sb.append(accessModifier.getText());
            sb.append(" ");
        }
        sb.append(typeName);
        sb.append(" ");
        sb.append(memberName);
        if(implementedTypeName != null){
            sb.append(" ");
            sb.append("=");
            sb.append(" ");
            sb.append(implementedTypeName);
        }
        if (!sb.substring(sb.length() - 1, sb.length()).equals(";")) sb.append(";");
        return sb.toString();
    }
}
