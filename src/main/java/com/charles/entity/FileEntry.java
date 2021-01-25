package com.charles.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class FileEntry {

    private String filePath;

    private File file;
}
