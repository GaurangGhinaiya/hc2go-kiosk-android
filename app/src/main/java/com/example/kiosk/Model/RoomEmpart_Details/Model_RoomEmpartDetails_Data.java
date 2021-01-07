
package com.example.kiosk.Model.RoomEmpart_Details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model_RoomEmpartDetails_Data {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("room_id")
    @Expose
    private Integer roomId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }


    public Integer is_select_patient = 0;

}
