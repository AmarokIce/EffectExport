package cn.mcmod.effect_export;

import javax.annotation.Nullable;

public class Data {
    String registerName;
    String name;
    String englishname;
    String smallIcon;
    String largeIcon;

    public Data(String registerName, String name, String englishname, String smallIcon, String largeIcon) {
        this.registerName = registerName;
        this.name = name;
        this.englishname = englishname;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
    }
}
