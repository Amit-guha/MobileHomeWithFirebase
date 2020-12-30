package com.example.mobilehome.multifileupload;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MultiImage {
    private String mImageURl;
    private String EtRoom;

    public MultiImage() {

    }

    public MultiImage(String mImageURl, String etRoom) {
        this.mImageURl = mImageURl;
        EtRoom = etRoom;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object>map=new HashMap<>();
        map.put("text",EtRoom);
        map.put("link",mImageURl);
        return map;
    }

    public String getmImageURl() {
        return mImageURl;
    }

    public void setmImageURl(String mImageURl) {
        this.mImageURl = mImageURl;
    }

    public String getEtRoom() {
        return EtRoom;
    }

    public void setEtRoom(String etRoom) {
        EtRoom = etRoom;
    }
}
