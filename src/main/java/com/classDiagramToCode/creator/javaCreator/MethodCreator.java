package com.classDiagramToCode.creator.javaCreator;

import com.classDiagramToCode.creator.Creator;
import lombok.Builder;

import java.util.List;
@Builder
public class MethodCreator implements Creator {
    private AccessModifier accessModifier;
    private String returnTypeName;
    private String methodName;
    private List<String> argumentList;

    public static class MethodCreatorBuilder {
        public MethodCreator build(){
            if(accessModifier == null){
                throw new IllegalArgumentException("アクセス修飾子が指定されていません");
            }
            if(methodName == null || methodName.equals("")) {
                throw new IllegalArgumentException("メソッド名が不明です");
            }
            if(returnTypeName == null || returnTypeName.equals("")){
                returnTypeName = "void";
            }

            return new MethodCreator(accessModifier,returnTypeName,methodName,argumentList);
        }
    }

    @Override
    public String create() {
        return create(false);
    }
    public String create(boolean isInterface){
        StringBuilder sb = new StringBuilder();

        sb.append("\t");
        if(accessModifier != AccessModifier.DEFAULT){
            sb.append(accessModifier.getText());
            sb.append(" ");
        }
        sb.append(returnTypeName);
        sb.append(" ");
        sb.append(methodName);
        sb.append("(");
        if(argumentList != null && !argumentList.isEmpty()) sb.append(String.join(", ",argumentList));
        sb.append(")");
        if (isInterface) {
            sb.append(";");
            return sb.toString();
        }
        sb.append(" ");
        sb.append("{");
        sb.append(System.lineSeparator());
        sb.append("\t");
        sb.append("\t");
        if(!returnTypeName.equals("void")){
            sb.append("return");
            sb.append(" ");
            if(PrimitiveType.isMember(returnTypeName)) sb.append(PrimitiveType.getFirstValueByTypeName(returnTypeName));
            else sb.append("null");
            sb.append(";");
        }
        sb.append(System.lineSeparator());
        sb.append("\t");
        sb.append("}");

        return sb.toString();
    }
}
