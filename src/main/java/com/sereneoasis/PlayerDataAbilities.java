package com.sereneoasis;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class PlayerDataAbilities {

    public HashMap<Integer, String>getAbilities()
    {
        HashMap<Integer,String>abilities = new HashMap<>();
        abilities.put(1,slot1);
        abilities.put(2,slot2);
        abilities.put(3,slot3);
        abilities.put(4,slot4);
        abilities.put(5,slot5);
        abilities.put(6,slot6);
        abilities.put(7,slot7);
        abilities.put(8,slot8);
        abilities.put(9,slot9);
        return abilities;
    }

    public void setAbilities(HashMap<Integer, String> abilities)
    {
        slot1 = abilities.get(1);
        slot2 = abilities.get(2);
        slot3 = abilities.get(3);
        slot4 = abilities.get(4);
        slot5 = abilities.get(5);
        slot6 = abilities.get(6);
        slot7 = abilities.get(7);
        slot8 = abilities.get(8);
        slot9 = abilities.get(9);
    }

    @JsonProperty("slot1")
    private String slot1;

    public String getSlot1() {
        return slot1;
    }

    public void setSlot1(String slot1)
    {
        this.slot1 = slot1;
    }

    @JsonProperty("slot2")
    private String slot2;

    public String getSlot2() {
        return slot2;
    }

    public void setSlot2(String slot2)
    {
        this.slot2 = slot2;
    }
    @JsonProperty("slot3")
    private String slot3;

    public String getSlot3() {
        return slot3;
    }

    public void setSlot3(String slot3)
    {
        this.slot3 = slot3;
    }
    @JsonProperty("slot4")
    private String slot4;

    public String getSlot4() {
        return slot4;
    }

    public void setSlot4(String slot4)
    {
        this.slot4 = slot4;
    }
    @JsonProperty("slot5")
    private String slot5;

    public String getSlot5() {
        return slot5;
    }

    public void setSlot5(String slot5)
    {
        this.slot5 = slot5;
    }
    @JsonProperty("slot6")
    private String slot6;

    public String getSlot6() {
        return slot6;
    }

    public void setSlot6(String slot6)
    {
        this.slot6 = slot6;
    }
    @JsonProperty("slot7")
    private String slot7;

    public String getSlot7() {
        return slot7;
    }

    public void setSlot7(String slot7)
    {
        this.slot7 = slot7;
    }
    @JsonProperty("slot8")
    private String slot8;

    public String getSlot8() {
        return slot8;
    }

    public void setSlot8(String slot8)
    {
        this.slot8 = slot8;
    }
    @JsonProperty("slot9")
    private String slot9;

    public String getSlot9() {
        return slot9;
    }

    public void setSlot9(String slot9)
    {
        this.slot9 = slot9;
    }
}
