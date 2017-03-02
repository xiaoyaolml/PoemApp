package com.leo.poemapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Leo on 2017-03-02.
 */

@Entity
public class Poem {
    @Id
    private Long id;
    private String first;
    private String second;
    private int count;
    private int type;
    @Generated(hash = 1309984557)
    public Poem(Long id, String first, String second, int count, int type) {
        this.id = id;
        this.first = first;
        this.second = second;
        this.count = count;
        this.type = type;
    }
    @Generated(hash = 1852989059)
    public Poem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirst() {
        return this.first;
    }
    public void setFirst(String first) {
        this.first = first;
    }
    public String getSecond() {
        return this.second;
    }
    public void setSecond(String second) {
        this.second = second;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
