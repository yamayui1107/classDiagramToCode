package com.classDiagramToCode.parser;

import com.classDiagramToCode.creator.Creator;

import java.util.List;

public interface Parser {
    List<? extends Creator> parse();
}
