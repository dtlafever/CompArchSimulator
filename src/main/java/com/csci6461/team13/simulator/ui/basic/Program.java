package com.csci6461.team13.simulator.ui.basic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
public class Program {


    private Integer initAddr;
    private Map<Integer, Integer> initialData;
    private Map<Integer, List<String>> insts;
    private String description;

    public Program(){
        initialData = new HashMap<>();
        insts = new HashMap<>();
        initAddr = null;
        description = "";
    }

    public Map<Integer, Integer> getInitialData() {
        return initialData;
    }

    public Map<Integer, List<String>> getInsts() {
        return insts;
    }

    public void putInstructionList(Integer key, List<String> list){
        insts.put(key, list);
    }

    public void putInitData(Integer addr, Integer data){
        initialData.put(addr, data);
    }

    public Integer getInitAddr() {
        return initAddr;
    }

    public void setInitAddr(Integer initAddr) {
        this.initAddr = initAddr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
