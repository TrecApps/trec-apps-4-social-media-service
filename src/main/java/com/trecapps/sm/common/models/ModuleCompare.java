package com.trecapps.sm.common.models;

import com.trecapps.auth.common.global.Record;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class ModuleCompare {

    String moduleId;
    String status = "Started";
    // ToDo - Record Class

    Set<Record> records = new TreeSet<>();

}
